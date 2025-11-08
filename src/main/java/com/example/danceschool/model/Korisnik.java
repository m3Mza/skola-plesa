package com.example.danceschool.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Model korisnika - Čist POJO bez anotacija za bazu podataka.
 * Validacione anotacije su zadržane jer su deo poslovne logike, a ne tehnologije baze podataka.
 */
public class Korisnik {
    
    private Long id;
    
    @NotBlank(message = "Ime je obavezno")
    @Size(max = 100)
    private String ime;
    
    @NotBlank(message = "Prezime je obavezno")
    @Size(max = 100)
    private String prezime;
    
    @NotBlank(message = "Email je obavezan")
    @Email(message = "Unesite validnu email adresu")
    @Size(max = 255)
    private String email;
    
    @NotBlank(message = "Lozinka je obavezna")
    @Size(min = 6, message = "Lozinka mora imati minimum 6 karaktera")
    private String lozinka;
    
    @Size(max = 20)
    private String telefon;
    
    private LocalDateTime datum_registracije;
    private String uloga = "ucenik"; // 'ucenik' ili 'instruktor'
    private Boolean aktivan = true;
    
    // Konstruktori
    public Korisnik() {
        this.datum_registracije = LocalDateTime.now();
        this.uloga = "ucenik";
    }
    
    public Korisnik(String ime, String prezime, String email, String lozinka) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.lozinka = lozinka;
        this.datum_registracije = LocalDateTime.now();
    }
    
    // Getteri i Setteri
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
    
    public String getLozinka() {
        return lozinka;
    }
    
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
    
    public String getTelefon() {
        return telefon;
    }
    
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }
    
    public LocalDateTime getDatum_registracije() {
        return datum_registracije;
    }
    
    public void setDatum_registracije(LocalDateTime datum_registracije) {
        this.datum_registracije = datum_registracije;
    }
    
    public Boolean getAktivan() {
        return aktivan;
    }
    
    public void setAktivan(Boolean aktivan) {
        this.aktivan = aktivan;
    }
    
    public String getUloga() {
        return uloga;
    }
    
    public void setUloga(String uloga) {
        this.uloga = uloga;
    }
    
    // Pomoćne metode
    public boolean je_instruktor() {
        return "instruktor".equals(uloga);
    }
    
    public boolean je_ucenik() {
        return "ucenik".equals(uloga);
    }
    
    public String getPuno_ime() {
        return ime + " " + prezime;
    }
}
