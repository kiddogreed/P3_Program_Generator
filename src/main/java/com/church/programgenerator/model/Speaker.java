package com.church.programgenerator.model;

public class Speaker {
    private int order;
    private String name;
    private String title;
    private String topic;
    
    // Constructors
    public Speaker() {}
    
    public Speaker(int order, String name, String title) {
        this.order = order;
        this.name = name;
        this.title = title;
    }
    
    public Speaker(int order, String name, String title, String topic) {
        this.order = order;
        this.name = name;
        this.title = title;
        this.topic = topic;
    }
    
    // Getters and setters
    public int getOrder() { return order; }
    public void setOrder(int order) { this.order = order; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
}