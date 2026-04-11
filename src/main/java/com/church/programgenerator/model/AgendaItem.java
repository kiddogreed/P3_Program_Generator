package com.church.programgenerator.model;

import java.util.ArrayList;
import java.util.List;

public class AgendaItem {

    private String title;
    private List<String> details;

    public AgendaItem() {
        this.details = new ArrayList<>();
    }

    public AgendaItem(String title) {
        this.title = title;
        this.details = new ArrayList<>();
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details != null ? details : new ArrayList<>(); }
}
