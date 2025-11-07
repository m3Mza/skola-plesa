package com.example.danceschool.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "raspored")
public class Raspored {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tip_casa", length = 20)
    private String tipCasa; // 'balet', 'hiphop', 'latino'
    
    @Column(name = "instruktor_id")
    private Long instruktorId;
    
    @Column(name = "datum_vreme")
    private LocalDateTime datumVreme;
    
    @Column(name = "trajanje_min")
    private Integer trajanjeMin = 60;
    
    private String lokacija;
    
    @Column(name = "maksimalno_polaznika")
    private Integer maksimalnoPolaznika = 15;
    
    @Column(columnDefinition = "TEXT")
    private String opis;
    
    @Column(name = "kreirao_id")
    private Long keiraoId;
    
    @Column(name = "datum_kreiranja")
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
