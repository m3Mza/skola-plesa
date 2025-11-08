package com.example.danceschool.repository;

import com.example.danceschool.dao.UpisBaletDAO;
import com.example.danceschool.dao.UpisHiphopDAO;
import com.example.danceschool.dao.UpisLatinoDAO;
import com.example.danceschool.model.UpisBalet;
import com.example.danceschool.model.UpisHiphop;
import com.example.danceschool.model.UpisLatino;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Repozitorijum za rad sa upisima korisnika na časove.
 * Objedinjuje DAO objekte za sve tipove upisa.
 */
@Repository
public class UpisRepozitorijum {
    
    private final UpisBaletDAO upis_balet_dao;
    private final UpisHiphopDAO upis_hiphop_dao;
    private final UpisLatinoDAO upis_latino_dao;
    
    public UpisRepozitorijum(UpisBaletDAO upis_balet_dao,
                             UpisHiphopDAO upis_hiphop_dao,
                             UpisLatinoDAO upis_latino_dao) {
        this.upis_balet_dao = upis_balet_dao;
        this.upis_hiphop_dao = upis_hiphop_dao;
        this.upis_latino_dao = upis_latino_dao;
    }
    
    /**
     * Pronalazi sve balet upise za korisnika.
     */
    public List<UpisBalet> pronadji_balet_po_korisniku(Long korisnik_id) {
        try {
            return upis_balet_dao.pronadji_po_korisniku(korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju balet upisa", e);
        }
    }
    
    /**
     * Pronalazi sve hiphop upise za korisnika.
     */
    public List<UpisHiphop> pronadji_hiphop_po_korisniku(Long korisnik_id) {
        try {
            return upis_hiphop_dao.pronadji_po_korisniku(korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju hiphop upisa", e);
        }
    }
    
    /**
     * Pronalazi sve latino upise za korisnika.
     */
    public List<UpisLatino> pronadji_latino_po_korisniku(Long korisnik_id) {
        try {
            return upis_latino_dao.pronadji_po_korisniku(korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri učitavanju latino upisa", e);
        }
    }
    
    /**
     * Prijavljuje korisnika na balet koristeći stored procedure.
     */
    public String prijavi_na_balet(Long korisnik_id, Long instruktor_id) {
        try {
            return upis_balet_dao.prijavi_na_balet(korisnik_id, instruktor_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri prijavi na balet", e);
        }
    }
    
    /**
     * Prijavljuje korisnika na hiphop koristeći stored procedure.
     */
    public String prijavi_na_hiphop(Long korisnik_id, Long instruktor_id) {
        try {
            return upis_hiphop_dao.prijavi_na_hiphop(korisnik_id, instruktor_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri prijavi na hiphop", e);
        }
    }
    
    /**
     * Prijavljuje korisnika na latino koristeći stored procedure.
     */
    public String prijavi_na_latino(Long korisnik_id, Long instruktor_id) {
        try {
            return upis_latino_dao.prijavi_na_latino(korisnik_id, instruktor_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri prijavi na latino", e);
        }
    }
    
    /**
     * Odjavljuje korisnika sa baleta koristeći stored procedure.
     */
    public String odjavi_sa_baleta(Long korisnik_id) {
        try {
            return upis_balet_dao.odjavi_sa_baleta(korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri odjavi sa baleta", e);
        }
    }
    
    /**
     * Odjavljuje korisnika sa hiphopa koristeći stored procedure.
     */
    public String odjavi_sa_hiphopa(Long korisnik_id) {
        try {
            return upis_hiphop_dao.odjavi_sa_hiphopa(korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri odjavi sa hiphopa", e);
        }
    }
    
    /**
     * Odjavljuje korisnika sa latina koristeći stored procedure.
     */
    public String odjavi_sa_latina(Long korisnik_id) {
        try {
            return upis_latino_dao.odjavi_sa_latina(korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri odjavi sa latina", e);
        }
    }
}
