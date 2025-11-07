package com.example.danceschool.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "korisnik_id")
    private Long korisnikId;
    
    @Column(name = "instruktor_id")
    private Long instruktorId;
    
    @Column(name = "datum_prijave")
    private LocalDateTime datumPrijave;
    
    @Column(columnDefinition = "TEXT")
    private String napomena;
    
    public Enrollment() {
        this.datumPrijave = LocalDateTime.now();
    }
    
    // Getteri i Setteri
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getKorisnikId() {
        return korisnikId;
    }
    
    public void setKorisnikId(Long korisnikId) {
        this.korisnikId = korisnikId;
    }
    
    public Long getInstruktorId() {
        return instruktorId;
    }
    
    public void setInstruktorId(Long instruktorId) {
        this.instruktorId = instruktorId;
    }
    
    public LocalDateTime getDatumPrijave() {
        return datumPrijave;
    }
    
    public void setDatumPrijave(LocalDateTime datumPrijave) {
        this.datumPrijave = datumPrijave;
    }
    
    public String getNapomena() {
        return napomena;
    }
    
    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
