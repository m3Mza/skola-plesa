package com.example.danceschool.repository;

import com.example.danceschool.dao.KorisnikDAO;
import com.example.danceschool.model.Korisnik;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Korisnik repozitorijum, apstrakcija nad DAO slojem
 */
@Repository
public class KorisnikRepozitorijum {
    
    private final KorisnikDAO korisnik_dao;
    
    public KorisnikRepozitorijum(KorisnikDAO korisnik_dao) {
        this.korisnik_dao = korisnik_dao;
    }
    
    /**
     * Pronalazi sve korisnike.
     */
    public List<Korisnik> pronadji_sve() {
        try {
            return korisnik_dao.pronadji_sve();
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju svih korisnika", e);
        }
    }
    
    /**
     * Pronalazi korisnika po ID-u.
     */
    public Optional<Korisnik> pronadji_po_id(Long id) {
        try {
            return korisnik_dao.pronadji_po_id(id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju korisnika sa id: " + id, e);
        }
    }
    
    /**
     * Pronalazi korisnika po email adresi.
     */
    public Optional<Korisnik> pronadji_po_emailu(String email) {
        try {
            return korisnik_dao.pronadji_po_emailu(email);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju korisnika sa email: " + email, e);
        }
    }
    
    /**
     * Čuva korisnika (insert ili update).
     */
    public Korisnik sacuvaj(Korisnik korisnik) {
        try {
            return korisnik_dao.sacuvaj(korisnik);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri čuvanju korisnika", e);
        }
    }
    
    /**
     * Briše korisnika po ID-u.
     */
    public void obrisi_po_id(Long id) {
        try {
            korisnik_dao.obrisi(id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri brisanju korisnika sa id: " + id, e);
        }
    }
    
    /**
     * Proverava da li korisnik sa datim email-om postoji.
     */
    public boolean postoji_po_emailu(String email) {
        return pronadji_po_emailu(email).isPresent();
    }
}
