package com.example.danceschool.model;

import java.time.LocalDateTime;

/**
 * Bazna klasa za sve tipove upisa.
 * Čist POJO bez anotacija za bazu podataka - razdvaja semantiku od tehnologije.
 */
public abstract class Upis {
    
    private Long id;
    private Long korisnik_id;
    private Long instruktor_id;
    private LocalDateTime datum_prijave;
    private String napomena;
    
    public Upis() {
        this.datum_prijave = LocalDateTime.now();
    }
    
    // Getteri i Setteri
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getKorisnik_id() {
        return korisnik_id;
    }
    
    public void setKorisnik_id(Long korisnik_id) {
        this.korisnik_id = korisnik_id;
    }
    
    public Long getInstruktor_id() {
        return instruktor_id;
    }
    
    public void setInstruktor_id(Long instruktor_id) {
        this.instruktor_id = instruktor_id;
    }
    
    public LocalDateTime getDatum_prijave() {
        return datum_prijave;
    }
    
    public void setDatum_prijave(LocalDateTime datum_prijave) {
        this.datum_prijave = datum_prijave;
    }
    
    public String getNapomena() {
        return napomena;
    }
    
    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }
}
