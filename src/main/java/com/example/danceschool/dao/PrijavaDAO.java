package com.example.danceschool.dao;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * DAO klasa za rad sa prijavama učenika na časove.
 * Koristi stored procedures za prijavu, odjavu i proveru prijave.
 */
@Repository
public class PrijavaDAO {
    
    private final DataSource data_source;
    
    public PrijavaDAO(DataSource data_source) {
        this.data_source = data_source;
    }
    
    /**
     * Prijavljuje učenika na čas uz proveru kapaciteta.
     * 
     * @param raspored_id ID termina iz rasporeda
     * @param korisnik_id ID učenika
     * @return Poruka o rezultatu operacije (USPEH: ... ili GRESKA: ...)
     * @throws SQLException u slučaju greške u bazi
     */
    public String prijavi_se(Long raspored_id, Long korisnik_id) throws SQLException {
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_prijavi_se_na_cas(?, ?, ?, ?)}")) {
            
            cs.setLong(1, raspored_id);
            cs.setLong(2, korisnik_id);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.VARCHAR);
            
            cs.execute();
            
            int uspeh = cs.getInt(3);
            String poruka = cs.getString(4);
            
            return uspeh == 1 ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
    
    /**
     * Odjavljuje učenika sa časa.
     * 
     * @param raspored_id ID termina iz rasporeda
     * @param korisnik_id ID učenika
     * @return Poruka o rezultatu operacije (USPEH: ... ili GRESKA: ...)
     * @throws SQLException u slučaju greške u bazi
     */
    public String odjavi_se(Long raspored_id, Long korisnik_id) throws SQLException {
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_odjavi_se_sa_casa(?, ?, ?, ?)}")) {
            
            cs.setLong(1, raspored_id);
            cs.setLong(2, korisnik_id);
            cs.registerOutParameter(3, Types.INTEGER);
            cs.registerOutParameter(4, Types.VARCHAR);
            
            cs.execute();
            
            int uspeh = cs.getInt(3);
            String poruka = cs.getString(4);
            
            return uspeh == 1 ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
    
    /**
     * Proverava da li je učenik prijavljen na određeni čas.
     * 
     * @param raspored_id ID termina iz rasporeda
     * @param korisnik_id ID učenika
     * @return true ako je učenik prijavljen, false ako nije
     * @throws SQLException u slučaju greške u bazi
     */
    public boolean proveri_prijavu(Long raspored_id, Long korisnik_id) throws SQLException {
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_proveri_prijavu(?, ?)}")) {
            
            cs.setLong(1, raspored_id);
            cs.setLong(2, korisnik_id);
            
            boolean ima_rezultat = cs.execute();
            
            if (ima_rezultat) {
                try (ResultSet rs = cs.getResultSet()) {
                    return rs.next(); // Ako postoji red, učenik je prijavljen
                }
            }
            
            return false;
        }
    }
}
