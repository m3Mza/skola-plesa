package com.example.danceschool.model;

import java.io.Serializable;

/**
 * User model wrapping Korisnik for session management
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String ime;
    private String prezime;
    private String email;
    private String telefon;
    private String uloga;
    private boolean instruktor;
    
    public User() {
    }
    
    public User(Korisnik korisnik) {
        this.id = korisnik.getId();
        this.ime = korisnik.getIme();
        this.prezime = korisnik.getPrezime();
        this.email = korisnik.getEmail();
        this.telefon = korisnik.getTelefon();
        this.uloga = korisnik.getUloga();
        this.instruktor = korisnik.je_instruktor();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIme() {
        return ime;
    }
    
    public void setIme(String ime) {
        this.ime = ime;
    }
    
    public String getPrezime() {
        return prezime;
    }
    
    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefon() {
        return telefon;
    }
    
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    
    public String getUloga() {
        return uloga;
    }
    
    public void setUloga(String uloga) {
        this.uloga = uloga;
    }
    
    public boolean isInstruktor() {
        return instruktor;
    }
    
    public void setInstruktor(boolean instruktor) {
        this.instruktor = instruktor;
    }
    
    public String getFullName() {
        return ime + " " + prezime;
    }
}
