package com.church.programgenerator.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "saved_programs")
public class SavedProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String meetingType;   // SACRAMENT, WARD_COUNCIL, BISHOPRIC

    @Column(nullable = false, length = 200)
    private String description;   // e.g. "Sacrament – Pasay 3rd Ward – 2026-04-13"

    @Column(nullable = false)
    private LocalDate meetingDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String programData;   // JSON-serialized program object

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ── Getters & Setters ─────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMeetingType() { return meetingType; }
    public void setMeetingType(String meetingType) { this.meetingType = meetingType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getMeetingDate() { return meetingDate; }
    public void setMeetingDate(LocalDate meetingDate) { this.meetingDate = meetingDate; }

    public String getProgramData() { return programData; }
    public void setProgramData(String programData) { this.programData = programData; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
