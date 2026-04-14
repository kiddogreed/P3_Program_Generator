package com.church.programgenerator.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "musicians")
public class Musician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    /** "chorister" or "pianist" */
    @Column(name = "musician_type", nullable = false, length = 20)
    private String musicianType = "chorister";

    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    public Musician() {}

    public Musician(String name, String musicianType) {
        this.name = name;
        this.musicianType = musicianType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMusicianType() { return musicianType; }
    public void setMusicianType(String musicianType) { this.musicianType = musicianType; }

    public int getDisplayOrder() { return displayOrder; }
    public void setDisplayOrder(int displayOrder) { this.displayOrder = displayOrder; }
}
