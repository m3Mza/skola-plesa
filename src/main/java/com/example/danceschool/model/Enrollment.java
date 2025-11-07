package com.example.danceschool.model;

import java.time.LocalDateTime;

/**
 * Base class for all enrollment types.
 * Pure POJO with no database annotations - separates semantics from technology.
 */
public abstract class Enrollment {
    
    private Long id;
    private Long korisnikId;
    private Long instruktorId;
    private LocalDateTime datumPrijave;
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
