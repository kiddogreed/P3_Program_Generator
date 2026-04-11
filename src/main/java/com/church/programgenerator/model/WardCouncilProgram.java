package com.church.programgenerator.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WardCouncilProgram {
    @NotBlank(message = "Ward name is required")
    private String wardName = "Pasay 3rd";
    @NotNull(message = "Meeting date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate meetingDate;
    private String presiding;
    private String conducting;
    private String openingPrayer;
    private String handbookReading;
    private String auxiliary;
    private List<AgendaItem> agendaItems;
    private String welfare;
    private String closingPrayer;
    
    // Default constructor
    public WardCouncilProgram() {
        this.meetingDate = LocalDate.now();
    }
    
    // Getters and setters
    public String getWardName() {
        return wardName;
    }
    
    public void setWardName(String wardName) {
        this.wardName = wardName;
    }
    
    public LocalDate getMeetingDate() {
        return meetingDate;
    }
    
    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }
    

    public String getPresiding() {
        return presiding;
    }
    public void setPresiding(String presiding) {
        this.presiding = presiding;
    }

    public String getConducting() {
        return conducting;
    }
    public void setConducting(String conducting) {
        this.conducting = conducting;
    }

    public String getOpeningPrayer() {
        return openingPrayer;
    }
    public void setOpeningPrayer(String openingPrayer) {
        this.openingPrayer = openingPrayer;
    }
    
    public String getHandbookReading() {
        return handbookReading;
    }
    
    public void setHandbookReading(String handbookReading) {
        this.handbookReading = handbookReading;
    }
    
    public String getAuxiliary() {
        return auxiliary;
    }

    public void setAuxiliary(String auxiliary) {
        this.auxiliary = auxiliary;
    }

    public List<AgendaItem> getAgendaItems() {
        return agendaItems;
    }

    public void setAgendaItems(List<AgendaItem> agendaItems) {
        this.agendaItems = agendaItems;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }

    public String getClosingPrayer() {
        return closingPrayer;
    }

    public void setClosingPrayer(String closingPrayer) {
        this.closingPrayer = closingPrayer;
    }
}