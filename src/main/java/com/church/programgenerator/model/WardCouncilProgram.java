package com.church.programgenerator.model;

import java.time.LocalDate;

public class WardCouncilProgram {
    private String wardName = "Pasay 3rd";
    private LocalDate meetingDate;
    private String openingPrayer;
    private String handbookReading;
    private String auxiliary;
    private String agenda;
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
    
    public String getAgenda() {
        return agenda;
    }
    
    public void setAgenda(String agenda) {
        this.agenda = agenda;
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