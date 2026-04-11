package com.church.programgenerator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Singleton configuration row (id = 1) that stores scheduling rules
 * and organization defaults for all program types.
 */
@Entity
@Table(name = "ward_config")
public class WardConfig {

    @Id
    private Long id = 1L;

    // ── Organization ─────────────────────────────────────────────────────────
    @Column(name = "stake_name", length = 200)
    private String stakeName = "Pasay Philippine Stake";

    @Column(name = "ward_name", length = 200)
    private String wardName = "Pasay 3rd Ward";

    @Column(name = "acknowledgement_template", columnDefinition = "text")
    private String acknowledgementTemplate =
            "Acknowledge {OTHER_CONDUCTORS}, Bro. Adrian Matro (wrd Clrk), " +
            "Johanne Perlas (Asst. Clrk. rec). Bro. Norman Oliva (Asst. Clrk. fin), " +
            "John Russelle Domingo (wrd exc. Secr.), Genesis Ferareza (wrd exc. Asst. Secr.). " +
            "{BISHOPRIC_OTHERS} To all Visitors and Stake Leaders (Welcome).";

    // ── Sacrament Meeting ─────────────────────────────────────────────────────
    @Column(name = "sacrament_time", length = 10)
    private String sacramentTime = "09:00";

    // ── Bishopric Meeting ─────────────────────────────────────────────────────
    /** "Thursday" (priority 1) or "Sunday" (priority 2 fallback) */
    @Column(name = "bishopric_preferred_day", length = 20)
    private String bishopricPreferredDay = "Thursday";

    @Column(name = "bishopric_thursday_time", length = 10)
    private String bishopricThursdayTime = "19:00";

    @Column(name = "bishopric_sunday_time", length = 10)
    private String bishopricSundayTime = "12:00";

    // ── Ward Council ──────────────────────────────────────────────────────────
    /** Comma-separated occurrence numbers, e.g. "1,3" = 1st and 3rd Sundays */
    @Column(name = "ward_council_occurrences", length = 20)
    private String wardCouncilOccurrences = "1,3";

    @Column(name = "ward_council_time", length = 10)
    private String wardCouncilTime = "11:00";

    // ── Speaker Rotation ──────────────────────────────────────────────────────
    /**
     * Year-month string (e.g. "2026-01") that is the start of cycle 1.
     * Cycle number = ((monthsSinceBase % 3) + 3) % 3 + 1  → 1, 2, or 3.
     */
    @Column(name = "speaker_cycle_base_month", length = 10)
    private String speakerCycleBaseMonth = "2026-01";

    // ── Conductor Round-Robin Tracking ────────────────────────────────────────
    @Column(name = "last_sacrament_conductor_id")
    private Long lastSacramentConductorId;

    @Column(name = "last_bishopric_conductor_id")
    private Long lastBishopricConductorId;

    // ── Prayer / Handbook Rotation Indices ────────────────────────────────────
    /** Last-used index in the auxiliaries list for ward council opening prayer (null/-1 = none yet) */
    @Column(name = "wc_opening_prayer_idx")
    private Integer wcOpeningPrayerIdx;

    @Column(name = "wc_closing_prayer_idx")
    private Integer wcClosingPrayerIdx;

    @Column(name = "wc_handbook_idx")
    private Integer wcHandbookIdx;

    /** Last-used index in the bishopric conductors list for opening prayer (null/-1 = none yet) */
    @Column(name = "bp_opening_prayer_idx")
    private Integer bpOpeningPrayerIdx;

    @Column(name = "bp_closing_prayer_idx")
    private Integer bpClosingPrayerIdx;

    @Column(name = "bp_handbook_idx")
    private Integer bpHandbookIdx;

    // ── Getters / Setters ─────────────────────────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStakeName() { return stakeName; }
    public void setStakeName(String stakeName) { this.stakeName = stakeName; }

    public String getWardName() { return wardName; }
    public void setWardName(String wardName) { this.wardName = wardName; }

    public String getAcknowledgementTemplate() { return acknowledgementTemplate; }
    public void setAcknowledgementTemplate(String acknowledgementTemplate) {
        this.acknowledgementTemplate = acknowledgementTemplate;
    }

    public String getSacramentTime() { return sacramentTime; }
    public void setSacramentTime(String sacramentTime) { this.sacramentTime = sacramentTime; }

    public String getBishopricPreferredDay() { return bishopricPreferredDay; }
    public void setBishopricPreferredDay(String bishopricPreferredDay) {
        this.bishopricPreferredDay = bishopricPreferredDay;
    }

    public String getBishopricThursdayTime() { return bishopricThursdayTime; }
    public void setBishopricThursdayTime(String bishopricThursdayTime) {
        this.bishopricThursdayTime = bishopricThursdayTime;
    }

    public String getBishopricSundayTime() { return bishopricSundayTime; }
    public void setBishopricSundayTime(String bishopricSundayTime) {
        this.bishopricSundayTime = bishopricSundayTime;
    }

    public String getWardCouncilOccurrences() { return wardCouncilOccurrences; }
    public void setWardCouncilOccurrences(String wardCouncilOccurrences) {
        this.wardCouncilOccurrences = wardCouncilOccurrences;
    }

    public String getWardCouncilTime() { return wardCouncilTime; }
    public void setWardCouncilTime(String wardCouncilTime) { this.wardCouncilTime = wardCouncilTime; }

    public String getSpeakerCycleBaseMonth() { return speakerCycleBaseMonth; }
    public void setSpeakerCycleBaseMonth(String speakerCycleBaseMonth) {
        this.speakerCycleBaseMonth = speakerCycleBaseMonth;
    }

    public Long getLastSacramentConductorId() { return lastSacramentConductorId; }
    public void setLastSacramentConductorId(Long lastSacramentConductorId) {
        this.lastSacramentConductorId = lastSacramentConductorId;
    }

    public Long getLastBishopricConductorId() { return lastBishopricConductorId; }
    public void setLastBishopricConductorId(Long lastBishopricConductorId) {
        this.lastBishopricConductorId = lastBishopricConductorId;
    }

    public int getWcOpeningPrayerIdx() { return wcOpeningPrayerIdx != null ? wcOpeningPrayerIdx : -1; }
    public void setWcOpeningPrayerIdx(int wcOpeningPrayerIdx) { this.wcOpeningPrayerIdx = wcOpeningPrayerIdx; }

    public int getWcClosingPrayerIdx() { return wcClosingPrayerIdx != null ? wcClosingPrayerIdx : -1; }
    public void setWcClosingPrayerIdx(int wcClosingPrayerIdx) { this.wcClosingPrayerIdx = wcClosingPrayerIdx; }

    public int getWcHandbookIdx() { return wcHandbookIdx != null ? wcHandbookIdx : -1; }
    public void setWcHandbookIdx(int wcHandbookIdx) { this.wcHandbookIdx = wcHandbookIdx; }

    public int getBpOpeningPrayerIdx() { return bpOpeningPrayerIdx != null ? bpOpeningPrayerIdx : -1; }
    public void setBpOpeningPrayerIdx(int bpOpeningPrayerIdx) { this.bpOpeningPrayerIdx = bpOpeningPrayerIdx; }

    public int getBpClosingPrayerIdx() { return bpClosingPrayerIdx != null ? bpClosingPrayerIdx : -1; }
    public void setBpClosingPrayerIdx(int bpClosingPrayerIdx) { this.bpClosingPrayerIdx = bpClosingPrayerIdx; }

    public int getBpHandbookIdx() { return bpHandbookIdx != null ? bpHandbookIdx : -1; }
    public void setBpHandbookIdx(int bpHandbookIdx) { this.bpHandbookIdx = bpHandbookIdx; }
}
