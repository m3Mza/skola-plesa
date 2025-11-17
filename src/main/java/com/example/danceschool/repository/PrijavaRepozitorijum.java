package com.example.danceschool.repository;

import com.example.danceschool.dao.PrijavaDAO;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * Repozitorijum za prijave učenika na časove.
 * Pruža apstrakciju nad DAO slojem.
 */
@Repository
public class PrijavaRepozitorijum {
    
    private final PrijavaDAO prijava_dao;
    
    public PrijavaRepozitorijum(PrijavaDAO prijava_dao) {
        this.prijava_dao = prijava_dao;
    }
    
    /**
     * Prijavljuje učenika na čas.
     * 
     * @param raspored_id ID termina
     * @param korisnik_id ID učenika
     * @return Poruka o rezultatu
     */
    public String prijavi_se(Long raspored_id, Long korisnik_id) {
        try {
            return prijava_dao.prijavi_se(raspored_id, korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri prijavi na čas", e);
        }
    }
    
    /**
     * Odjavljuje učenika sa časa.
     * 
     * @param raspored_id ID termina
     * @param korisnik_id ID učenika
     * @return Poruka o rezultatu
     */
    public String odjavi_se(Long raspored_id, Long korisnik_id) {
        try {
            return prijava_dao.odjavi_se(raspored_id, korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri odjavi sa časa", e);
        }
    }
    
    /**
     * Proverava da li je učenik prijavljen.
     * 
     * @param raspored_id ID termina
     * @param korisnik_id ID učenika
     * @return true ako je prijavljen
     */
    public boolean proveri_prijavu(Long raspored_id, Long korisnik_id) {
        try {
            return prijava_dao.proveri_prijavu(raspored_id, korisnik_id);
        } catch (SQLException e) {
            throw new RuntimeException("Greška pri proveri prijave", e);
        }
    }
}
