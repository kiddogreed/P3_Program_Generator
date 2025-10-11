package com.church.programgenerator.model;

import java.time.LocalDate;
import java.util.List;

public class BishopricProgram {
    private String wardName = "3rd Ward";
    private LocalDate meetingDate;
    private String presiding;
    private String conducting;
    private String openingPrayer;
    private String handbookSpiritual;
    private List<String> agendaItems;
    private String callingsAndReleases;
    private String closingPrayer;
    
    // Default constructor
    public BishopricProgram() {
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
    
    public String getHandbookSpiritual() {
        return handbookSpiritual;
    }
    
    public void setHandbookSpiritual(String handbookSpiritual) {
        this.handbookSpiritual = handbookSpiritual;
    }
    
    public List<String> getAgendaItems() {
        return agendaItems;
    }
    
    public void setAgendaItems(List<String> agendaItems) {
        this.agendaItems = agendaItems;
    }
    
    public String getCallingsAndReleases() {
        return callingsAndReleases;
    }
    
    public void setCallingsAndReleases(String callingsAndReleases) {
        this.callingsAndReleases = callingsAndReleases;
    }
    
    public String getClosingPrayer() {
        return closingPrayer;
    }
    
    public void setClosingPrayer(String closingPrayer) {
        this.closingPrayer = closingPrayer;
    }
}