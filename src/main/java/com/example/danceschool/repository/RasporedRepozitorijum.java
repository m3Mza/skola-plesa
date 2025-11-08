package com.example.danceschool.repository;

import com.example.danceschool.dao.RasporedDAO;
import com.example.danceschool.model.Raspored;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repozitorijum implementacija za Raspored entitet.
 * Pruža apstrakciju nad DAO slojem za operacije sa rasporedom.
 */
@Repository
public class RasporedRepozitorijum {
    
    private final RasporedDAO raspored_dao;
    
    public RasporedRepozitorijum(RasporedDAO raspored_dao) {
        this.raspored_dao = raspored_dao;
    }
    
    /**
     * Pronalazi sve rasporede.
     */
    public List<Raspored> pronadji_sve() {
        try {
            return raspored_dao.pronadji_sve();
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju svih rasporeda", e);
        }
    }
    
    /**
     * Pronalazi raspored po ID-u.
     */
    public Optional<Raspored> pronadji_po_id(Long id) {
        try {
            return raspored_dao.pronadji_po_id(id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju rasporeda sa id: " + id, e);
        }
    }
    
    /**
     * Pronalazi rasporede po tipu časa.
     */
    public List<Raspored> pronadji_po_tipu_casa(String tip_casa) {
        try {
            return raspored_dao.pronadji_po_tipu_casa(tip_casa);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju rasporeda za tip: " + tip_casa, e);
        }
    }
    
    /**
     * Pronalazi rasporede po instruktoru.
     */
    public List<Raspored> pronadji_po_instruktoru(Long instruktor_id) {
        try {
            return raspored_dao.pronadji_po_instruktoru(instruktor_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju rasporeda za instruktora: " + instruktor_id, e);
        }
    }
    
    /**
     * Dodaje raspored u bazu koristeći stored procedure.
     */
    public String dodaj_raspored(Raspored raspored) {
        try {
            return raspored_dao.dodaj_u_raspored(raspored);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri dodavanju rasporeda", e);
        }
    }
    
    /**
     * Briše raspored iz baze koristeći stored procedure.
     */
    public String obrisi_raspored(Long raspored_id, Long korisnik_id) {
        try {
            return raspored_dao.obrisi_iz_rasporeda(raspored_id, korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri brisanju rasporeda sa id: " + raspored_id, e);
        }
    }
    
    /**
     * Ažurira raspored u bazi koristeći stored procedure.
     */
    public String azuriraj_raspored(Raspored raspored, Long korisnik_id) {
        try {
            return raspored_dao.azuriraj(raspored, korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri ažuriranju rasporeda sa id: " + raspored.getId(), e);
        }
    }
}

