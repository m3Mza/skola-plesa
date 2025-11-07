package com.example.danceschool.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "korisnici")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Lozinka je obavezna")
    @Size(min = 6, message = "Lozinka mora imati minimum 6 karaktera")
    private String lozinka;
    
    @Size(max = 20)
    private String telefon;
    
    @Column(name = "datum_registracije")
    private LocalDateTime datumRegistracije;
    
    @Column(length = 20)
    private String uloga = "ucenik"; // 'ucenik' ili 'instruktor'
    
    private Boolean aktivan = true;
    
    // Konstruktori
    public User() {
        this.datumRegistracije = LocalDateTime.now();
        this.uloga = "ucenik";
    }
    
    public User(String ime, String prezime, String email, String lozinka) {
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.lozinka = lozinka;
        this.datumRegistracije = LocalDateTime.now();
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
    
    public LocalDateTime getDatumRegistracije() {
        return datumRegistracije;
    }
    
    public void setDatumRegistracije(LocalDateTime datumRegistracije) {
        this.datumRegistracije = datumRegistracije;
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
    
    public boolean isInstruktor() {
        return "instruktor".equals(uloga);
    }
    
    public boolean isUcenik() {
        return "ucenik".equals(uloga);
    }
    
    public String getPunoIme() {
        return ime + " " + prezime;
    }
}
