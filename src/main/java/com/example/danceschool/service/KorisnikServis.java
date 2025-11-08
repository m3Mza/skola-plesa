package com.example.danceschool.service;

import com.example.danceschool.model.Korisnik;
import com.example.danceschool.repository.KorisnikRepozitorijum;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servis za upravljanje korisnicima.
 * Sadrži poslovnu logiku za operacije sa korisnicima.
 */
@Service
public class KorisnikServis {
    
    private final KorisnikRepozitorijum korisnik_repozitorijum;
    
    public KorisnikServis(KorisnikRepozitorijum korisnik_repozitorijum) {
        this.korisnik_repozitorijum = korisnik_repozitorijum;
    }
    
    /**
     * Pronalazi sve korisnike.
     */
    public List<Korisnik> pronadji_sve_korisnike() {
        return korisnik_repozitorijum.pronadji_sve();
    }
    
    /**
     * Pronalazi korisnika po ID-u.
     */
    public Optional<Korisnik> pronadji_korisnika_po_id(Long id) {
        return korisnik_repozitorijum.pronadji_po_id(id);
    }
    
    /**
     * Pronalazi korisnika po email adresi.
     */
    public Optional<Korisnik> pronadji_korisnika_po_emailu(String email) {
        return korisnik_repozitorijum.pronadji_po_emailu(email);
    }
    
    /**
     * Registruje novog korisnika.
     * Čuva lozinku kao plain text.
     */
    public Korisnik registruj_korisnika(Korisnik korisnik) {
        // Proveri da li korisnik sa tim email-om već postoji
        if (korisnik_repozitorijum.postoji_po_emailu(korisnik.getEmail())) {
            throw new RuntimeException("Korisnik sa email adresom " + korisnik.getEmail() + " već postoji");
        }
        
        // Čuvaj korisnika (lozinka ostaje plain text)
        return korisnik_repozitorijum.sacuvaj(korisnik);
    }
    
    /**
     * Ažurira postojećeg korisnika.
     */
    public Korisnik azuriraj_korisnika(Korisnik korisnik) {
        // Proveri da li korisnik postoji
        Optional<Korisnik> postojeci = korisnik_repozitorijum.pronadji_po_id(korisnik.getId());
        if (!postojeci.isPresent()) {
            throw new RuntimeException("Korisnik sa ID " + korisnik.getId() + " ne postoji");
        }
        
        return korisnik_repozitorijum.sacuvaj(korisnik);
    }
    
    /**
     * Briše korisnika po ID-u.
     */
    public void obrisi_korisnika(Long id) {
        korisnik_repozitorijum.obrisi_po_id(id);
    }
    
    /**
     * Proverava da li se lozinka poklapa (plain text poređenje).
     */
    public boolean proveri_lozinku(Korisnik korisnik, String sirova_lozinka) {
        return korisnik.getLozinka().equals(sirova_lozinka);
    }
    
    /**
     * Autentifikuje korisnika po email-u i lozinci.
     */
    public Optional<Korisnik> autentifikuj(String email, String lozinka) {
        Optional<Korisnik> korisnik_opt = pronadji_korisnika_po_emailu(email);
        
        if (korisnik_opt.isPresent()) {
            Korisnik korisnik = korisnik_opt.get();
            if (proveri_lozinku(korisnik, lozinka)) {
                return Optional.of(korisnik);
            }
        }
        
        return Optional.empty();
    }
}
