package com.church.programgenerator.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SacramentProgram {
    private String stakeName;
    private String wardName;
    private LocalDate date;
    private String presiding;
    private String conducting;
    private String acknowledgement;
    private List<String> announcements;
    
    // Music
    private String chorister;
    private String pianist;
    private String openingHymn;
    private String sacramentHymn;
    private String closingHymn;
    
    // Program sections
    private String invocation;
    private String wardBusiness;
    private String stakeBusiness;
    private List<Speaker> speakers;
    private String speakersAuxiliary;
    private String benediction;
    
    // Constructors
    public SacramentProgram() {
        this.announcements = new ArrayList<>();
        this.speakers = new ArrayList<>();
    }
    
    // Getters and setters
    public String getStakeName() { return stakeName; }
    public void setStakeName(String stakeName) { this.stakeName = stakeName; }
    
    public String getWardName() { return wardName; }
    public void setWardName(String wardName) { this.wardName = wardName; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public String getPresiding() { return presiding; }
    public void setPresiding(String presiding) { this.presiding = presiding; }
    
    public String getConducting() { return conducting; }
    public void setConducting(String conducting) { this.conducting = conducting; }
    
    public String getAcknowledgement() { return acknowledgement; }
    public void setAcknowledgement(String acknowledgement) { this.acknowledgement = acknowledgement; }
    
    public List<String> getAnnouncements() { return announcements; }
    public void setAnnouncements(List<String> announcements) { this.announcements = announcements; }
    
    public String getChorister() { return chorister; }
    public void setChorister(String chorister) { this.chorister = chorister; }
    
    public String getPianist() { return pianist; }
    public void setPianist(String pianist) { this.pianist = pianist; }
    
    public String getOpeningHymn() { return openingHymn; }
    public void setOpeningHymn(String openingHymn) { this.openingHymn = openingHymn; }
    
    public String getSacramentHymn() { return sacramentHymn; }
    public void setSacramentHymn(String sacramentHymn) { this.sacramentHymn = sacramentHymn; }
    
    public String getClosingHymn() { return closingHymn; }
    public void setClosingHymn(String closingHymn) { this.closingHymn = closingHymn; }
    
    public String getInvocation() { return invocation; }
    public void setInvocation(String invocation) { this.invocation = invocation; }
    
    public String getWardBusiness() { return wardBusiness; }
    public void setWardBusiness(String wardBusiness) { this.wardBusiness = wardBusiness; }
    
    public String getStakeBusiness() { return stakeBusiness; }
    public void setStakeBusiness(String stakeBusiness) { this.stakeBusiness = stakeBusiness; }
    
    public List<Speaker> getSpeakers() { return speakers; }
    public void setSpeakers(List<Speaker> speakers) { this.speakers = speakers; }
    
    public String getSpeakersAuxiliary() { return speakersAuxiliary; }
    public void setSpeakersAuxiliary(String speakersAuxiliary) { this.speakersAuxiliary = speakersAuxiliary; }
    
    public String getBenediction() { return benediction; }
    public void setBenediction(String benediction) { this.benediction = benediction; }
    
    // Helper method to add speakers
    public void addSpeaker(Speaker speaker) {
        this.speakers.add(speaker);
    }
    
    // Helper method to add announcements
    public void addAnnouncement(String announcement) {
        this.announcements.add(announcement);
    }
}