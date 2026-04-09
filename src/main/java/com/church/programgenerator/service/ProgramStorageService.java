package com.church.programgenerator.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.church.programgenerator.model.BishopricProgram;
import com.church.programgenerator.model.SacramentProgram;
import com.church.programgenerator.model.SavedProgram;
import com.church.programgenerator.model.WardCouncilProgram;
import com.church.programgenerator.repository.SavedProgramRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class ProgramStorageService {

    private final SavedProgramRepository repository;
    private final ObjectMapper objectMapper;

    public ProgramStorageService(SavedProgramRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    // ── Save methods ──────────────────────────────────────────────────────

    @CacheEvict(value = "programHistory", allEntries = true)
    public SavedProgram saveSacramentProgram(SacramentProgram program) {
        try {
            String json = objectMapper.writeValueAsString(program);
            SavedProgram saved = new SavedProgram();
            saved.setMeetingType("SACRAMENT");
            saved.setMeetingDate(program.getDate());
            saved.setDescription(buildDescription("Sacrament Meeting",
                    program.getWardName(), program.getDate()));
            saved.setProgramData(json);
            return repository.save(saved);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save sacrament program: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "programHistory", allEntries = true)
    public SavedProgram saveBishopricProgram(BishopricProgram program) {
        try {
            String json = objectMapper.writeValueAsString(program);
            SavedProgram saved = new SavedProgram();
            saved.setMeetingType("BISHOPRIC");
            saved.setMeetingDate(program.getMeetingDate());
            saved.setDescription(buildDescription("Bishopric Meeting",
                    program.getWardName(), program.getMeetingDate()));
            saved.setProgramData(json);
            return repository.save(saved);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save bishopric program: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "programHistory", allEntries = true)
    public SavedProgram saveWardCouncilProgram(WardCouncilProgram program) {
        try {
            String json = objectMapper.writeValueAsString(program);
            SavedProgram saved = new SavedProgram();
            saved.setMeetingType("WARD_COUNCIL");
            saved.setMeetingDate(program.getMeetingDate());
            saved.setDescription(buildDescription("Ward Council",
                    program.getWardName(), program.getMeetingDate()));
            saved.setProgramData(json);
            return repository.save(saved);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save ward council program: " + e.getMessage(), e);
        }
    }

    // ── Load methods ──────────────────────────────────────────────────────

    @Cacheable("programHistory")
    public List<SavedProgram> getAllPrograms() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Cacheable(value = "programHistory", key = "#meetingType")
    public List<SavedProgram> getProgramsByType(String meetingType) {
        return repository.findByMeetingTypeOrderByCreatedAtDesc(meetingType);
    }

    public Page<SavedProgram> getAllPrograms(int page, int size) {
        return repository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
    }

    public Page<SavedProgram> getProgramsByType(String meetingType, int page, int size) {
        return repository.findByMeetingTypeOrderByCreatedAtDesc(meetingType.toUpperCase(), PageRequest.of(page, size));
    }

    public SacramentProgram loadSacramentProgram(Long id) {
        SavedProgram saved = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Program not found: " + id));
        try {
            return objectMapper.readValue(saved.getProgramData(), SacramentProgram.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load sacrament program: " + e.getMessage(), e);
        }
    }

    public BishopricProgram loadBishopricProgram(Long id) {
        SavedProgram saved = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Program not found: " + id));
        try {
            return objectMapper.readValue(saved.getProgramData(), BishopricProgram.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load bishopric program: " + e.getMessage(), e);
        }
    }

    public WardCouncilProgram loadWardCouncilProgram(Long id) {
        SavedProgram saved = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Program not found: " + id));
        try {
            return objectMapper.readValue(saved.getProgramData(), WardCouncilProgram.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load ward council program: " + e.getMessage(), e);
        }
    }

    @CacheEvict(value = "programHistory", allEntries = true)
    public void deleteProgram(Long id) {
        repository.deleteById(id);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private String buildDescription(String type, String wardName, LocalDate date) {
        String dateStr = date != null
                ? date.format(DateTimeFormatter.ofPattern("MMM d, yyyy")) : "No date";
        String ward = (wardName != null && !wardName.isBlank()) ? wardName : "Unknown Ward";
        return type + " – " + ward + " – " + dateStr;
    }
}
