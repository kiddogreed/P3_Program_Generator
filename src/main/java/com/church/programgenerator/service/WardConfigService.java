package com.church.programgenerator.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.church.programgenerator.model.Conductor;
import com.church.programgenerator.model.WardConfig;
import com.church.programgenerator.repository.WardConfigRepository;

@Service
public class WardConfigService {

    private final WardConfigRepository repository;

    public WardConfigService(WardConfigRepository repository) {
        this.repository = repository;
    }

    // ── Config CRUD ───────────────────────────────────────────────────────────

    /** Returns the singleton config, creating defaults if the row doesn't exist yet. */
    public WardConfig getConfig() {
        return repository.findById(1L).orElseGet(() -> repository.save(new WardConfig()));
    }

    public WardConfig save(WardConfig config) {
        config.setId(1L);
        return repository.save(config);
    }

    /** Update last-used conductor for the given program type. */
    public void markConductorUsed(String programType, Long conductorId) {
        WardConfig cfg = getConfig();
        if ("sacrament".equalsIgnoreCase(programType)) {
            cfg.setLastSacramentConductorId(conductorId);
        } else if ("bishopric".equalsIgnoreCase(programType)) {
            cfg.setLastBishopricConductorId(conductorId);
        }
        repository.save(cfg);
    }

    // ── Date Helpers ──────────────────────────────────────────────────────────

    /**
     * Returns the nearest upcoming Sunday (today if today is Sunday,
     * else next Sunday).
     */
    public LocalDate nextSacramentDate() {
        LocalDate today = LocalDate.now();
        int dayValue = today.getDayOfWeek().getValue(); // Mon=1 … Sun=7
        int daysToSunday = dayValue == 7 ? 0 : 7 - dayValue;
        return today.plusDays(daysToSunday);
    }

    /**
     * Returns the next occurrence of the bishopric preferred day
     * (Thursday by default, or Sunday as fallback).
     * Always returns a future date (never today).
     */
    public LocalDate nextBishopricDate() {
        WardConfig cfg = getConfig();
        DayOfWeek preferred = "Sunday".equalsIgnoreCase(cfg.getBishopricPreferredDay())
                ? DayOfWeek.SUNDAY : DayOfWeek.THURSDAY;
        LocalDate today = LocalDate.now();
        int dayValue = today.getDayOfWeek().getValue();
        int targetValue = preferred.getValue();
        int daysUntil = (targetValue - dayValue + 7) % 7;
        if (daysUntil == 0) daysUntil = 7; // skip today even if it matches
        return today.plusDays(daysUntil);
    }

    /**
     * Returns the next Sunday that falls on one of the configured ward-council
     * occurrence numbers (1=1st Sunday, 3=3rd Sunday, …).
     */
    public LocalDate nextWardCouncilDate() {
        WardConfig cfg = getConfig();
        List<Integer> occurrences = parseOccurrences(cfg.getWardCouncilOccurrences());
        LocalDate candidate = nextSacramentDate();
        // Scan up to 8 Sundays to find the next matching occurrence
        for (int i = 0; i < 8; i++) {
            if (occurrences.contains(getSundayOccurrence(candidate))) {
                return candidate;
            }
            candidate = candidate.plusWeeks(1);
        }
        return candidate; // fallback
    }

    // ── Sunday Classification ─────────────────────────────────────────────────

    /**
     * Which occurrence (1st, 2nd, … 5th) is this Sunday within its month?
     */
    public int getSundayOccurrence(LocalDate date) {
        return (date.getDayOfMonth() - 1) / 7 + 1;
    }

    /**
     * Speaker rotation cycle (1, 2, or 3) for the month of the given date.
     * Determined by counting months elapsed since the configured base month.
     */
    public int getSpeakerCycleNumber(LocalDate date) {
        WardConfig cfg = getConfig();
        YearMonth base = YearMonth.parse(cfg.getSpeakerCycleBaseMonth(),
                DateTimeFormatter.ofPattern("yyyy-MM"));
        YearMonth current = YearMonth.from(date);
        long elapsed = base.until(current, ChronoUnit.MONTHS);
        return (int) (((elapsed % 3) + 3) % 3) + 1;
    }

    /**
     * Human-readable speaker type label for a given Sunday.
     * e.g. "Relief Society", "Fast &amp; Testimony", "Stake Assignment"
     */
    public String getSpeakerTypeLabel(LocalDate sunday) {
        int occurrence = getSundayOccurrence(sunday);
        switch (occurrence) {
            case 1: return "Fast & Testimony";
            case 3: return "Stake Assignment";
            case 5: return "Bishopric Special";
            case 2: {
                int cycle = getSpeakerCycleNumber(sunday);
                if (cycle == 1) return "Relief Society";
                if (cycle == 2) return "Elders Quorum";
                return "Ward Mission & Family History";
            }
            default: { // 4th Sunday
                int cycle = getSpeakerCycleNumber(sunday);
                if (cycle == 1) return "Sunday School";
                if (cycle == 2) return "Primary";
                return "Youth";
            }
        }
    }

    /** Preview of the next N consecutive Sundays with their labels. */
    public List<SundayPreview> getUpcomingSundayPreviews(int count) {
        List<SundayPreview> result = new ArrayList<>();
        LocalDate sunday = nextSacramentDate();
        for (int i = 0; i < count; i++) {
            result.add(new SundayPreview(sunday, getSundayOccurrence(sunday),
                    getSpeakerTypeLabel(sunday)));
            sunday = sunday.plusWeeks(1);
        }
        return result;
    }

    // ── Conductor Round-Robin ─────────────────────────────────────────────────

    /**
     * Returns the suggested next conductor from the ordered list using
     * round-robin rotation after the last-used conductor.
     */
    public Conductor getSuggestedConductor(List<Conductor> conductors, Long lastUsedId) {
        if (conductors == null || conductors.isEmpty()) return null;
        if (lastUsedId == null) return conductors.get(0);
        int lastIndex = -1;
        for (int i = 0; i < conductors.size(); i++) {
            if (conductors.get(i).getId().equals(lastUsedId)) {
                lastIndex = i;
                break;
            }
        }
        int nextIndex = (lastIndex + 1) % conductors.size();
        return conductors.get(nextIndex);
    }

    // ── Acknowledgement Template ──────────────────────────────────────────────

    /**
     * Resolves the acknowledgement template by substituting:
     * <ul>
     *   <li>{OTHER_CONDUCTORS} – all sacrament conductors except the conducting person</li>
     *   <li>{BISHOPRIC_OTHERS} – all bishopric conductors except the bishop (first in list)
     *       and the conducting counselor</li>
     * </ul>
     */
    public String buildAcknowledgement(String conducting,
                                       List<Conductor> sacramentConductors,
                                       List<Conductor> bishopricConductors) {
        WardConfig cfg = getConfig();
        String template = cfg.getAcknowledgementTemplate();
        if (template == null || template.isBlank()) return "";

        // {OTHER_CONDUCTORS}: sacrament conductors excluding the one conducting
        String otherConductors = sacramentConductors.stream()
                .filter(c -> !c.getName().equalsIgnoreCase(conducting))
                .map(Conductor::getName)
                .collect(Collectors.joining(", "));
        template = template.replace("{OTHER_CONDUCTORS}", otherConductors);

        // {BISHOPRIC_OTHERS}: bishopric members except the bishop (first in list)
        // and the conducting person (if in the list)
        String bishopricOthers = "";
        if (bishopricConductors != null && bishopricConductors.size() > 1) {
            // Skip index 0 (Bishop) and skip whoever is presiding
            bishopricOthers = bishopricConductors.stream()
                    .skip(1)
                    .filter(c -> !c.getName().equalsIgnoreCase(conducting))
                    .map(Conductor::getName)
                    .collect(Collectors.joining(", "));
        }
        template = template.replace("{BISHOPRIC_OTHERS}",
                bishopricOthers.isBlank() ? "" : bishopricOthers + ". ");

        return template;
    }

    // ── Internal Helpers ──────────────────────────────────────────────────────

    private List<Integer> parseOccurrences(String raw) {
        List<Integer> result = new ArrayList<>();
        if (raw == null || raw.isBlank()) {
            result.add(1);
            result.add(3);
            return result;
        }
        for (String part : raw.split(",")) {
            try { result.add(Integer.parseInt(part.trim())); } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    // ── Prayer / Handbook Rotation ────────────────────────────────────────────

    /**
     * Returns the next item from the list after lastIdx (wraps around).
     * If the list is empty returns null. Index -1 means start from 0.
     */
    public <T> T getNextItem(List<T> items, int lastIdx) {
        if (items == null || items.isEmpty()) return null;
        int next = (lastIdx + 1) % items.size();
        return items.get(next);
    }

    public int nextIndex(List<?> items, int lastIdx) {
        if (items == null || items.isEmpty()) return -1;
        return (lastIdx + 1) % items.size();
    }

    /**
     * Suggests the next 3 assignments from a list, cycling from lastIdx,
     * ensuring opening/closing/handbook don't all land on the same entry
     * when the list is large enough.
     */
    public int[] nextThreeIndices(List<?> items, int lastIdx) {
        int size = items == null ? 0 : items.size();
        if (size == 0) return new int[]{-1, -1, -1};
        int i0 = (lastIdx + 1) % size;
        int i1 = (lastIdx + 2) % size;
        int i2 = (lastIdx + 3) % size;
        // Collapse indices if list is too small
        if (size == 1) return new int[]{0, 0, 0};
        if (size == 2) return new int[]{i0, i1, i0};
        return new int[]{i0, i1, i2};
    }

    /** Update ward-council prayer/handbook rotation counters. */
    public void markWardCouncilAssignments(int openingIdx, int closingIdx, int handbookIdx) {
        WardConfig cfg = getConfig();
        cfg.setWcOpeningPrayerIdx(openingIdx);
        cfg.setWcClosingPrayerIdx(closingIdx);
        cfg.setWcHandbookIdx(handbookIdx);
        repository.save(cfg);
    }

    /** Update bishopric prayer/handbook rotation counters. */
    public void markBishopricAssignments(int openingIdx, int closingIdx, int handbookIdx) {
        WardConfig cfg = getConfig();
        cfg.setBpOpeningPrayerIdx(openingIdx);
        cfg.setBpClosingPrayerIdx(closingIdx);
        cfg.setBpHandbookIdx(handbookIdx);
        repository.save(cfg);
    }

    // ── Inner DTO ─────────────────────────────────────────────────────────────

    public static class SundayPreview {
        private final LocalDate date;
        private final int occurrence;
        private final String speakerType;

        public SundayPreview(LocalDate date, int occurrence, String speakerType) {
            this.date = date;
            this.occurrence = occurrence;
            this.speakerType = speakerType;
        }

        public LocalDate getDate() { return date; }
        public int getOccurrence() { return occurrence; }
        public String getSpeakerType() { return speakerType; }

        private static final String[] ORDINALS = {"", "1st", "2nd", "3rd", "4th", "5th"};
        public String getOccurrenceLabel() {
            return occurrence >= 1 && occurrence <= 5 ? ORDINALS[occurrence] : String.valueOf(occurrence);
        }

        public String getFormattedDate() {
            return date.format(DateTimeFormatter.ofPattern("EEE, MMM d, yyyy"));
        }
    }
}
