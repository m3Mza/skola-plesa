package com.example.danceschool.model;

import java.time.LocalDateTime;

/**
 * Raspored (Schedule) model - Pure POJO without database annotations.
 * Represents a dance class schedule entry.
 */
public class Raspored {
    
    private Long id;
    private String tipCasa; // 'balet', 'hiphop', 'latino'
    private Long instruktorId;
    private LocalDateTime datumVreme;
    private Integer trajanjeMin = 60;
    private String lokacija;
    private Integer maksimalnoPolaznika = 15;
    private String opis;
    private Long keiraoId;
    private LocalDateTime datumKreiranja;
    
    public Raspored() {
        this.datumKreiranja = LocalDateTime.now();
    }
    
    // Getteri i Setteri
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTipCasa() {
        return tipCasa;
    }
    
    public void setTipCasa(String tipCasa) {
        this.tipCasa = tipCasa;
    }
    
    public Long getInstruktorId() {
        return instruktorId;
    }
    
    public void setInstruktorId(Long instruktorId) {
        this.instruktorId = instruktorId;
    }
    
    public LocalDateTime getDatumVreme() {
        return datumVreme;
    }
    
    public void setDatumVreme(LocalDateTime datumVreme) {
        this.datumVreme = datumVreme;
    }
    
    public Integer getTrajanjeMin() {
        return trajanjeMin;
    }
    
    public void setTrajanjeMin(Integer trajanjeMin) {
        this.trajanjeMin = trajanjeMin;
    }
    
    public String getLokacija() {
        return lokacija;
    }
    
    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }
    
    public Integer getMaksimalnoPolaznika() {
        return maksimalnoPolaznika;
    }
    
    public void setMaksimalnoPolaznika(Integer maksimalnoPolaznika) {
        this.maksimalnoPolaznika = maksimalnoPolaznika;
    }
    
    public String getOpis() {
        return opis;
    }
    
    public void setOpis(String opis) {
        this.opis = opis;
    }
    
    public Long getKeiraoId() {
        return keiraoId;
    }
    
    public void setKeiraoId(Long keiraoId) {
        this.keiraoId = keiraoId;
    }
    
    public LocalDateTime getDatumKreiranja() {
        return datumKreiranja;
    }
    
    public void setDatumKreiranja(LocalDateTime datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }
}
