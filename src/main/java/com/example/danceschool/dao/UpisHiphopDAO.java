package com.example.danceschool.dao;

import com.example.danceschool.dao.mapper.MaperRedovaUpisaHiphop;
import com.example.danceschool.model.UpisHiphop;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * DAO objekat za pristup podacima upisa na hiphop časove.
 */
@Repository
public class UpisHiphopDAO {
    
    private final QueryRunner query_runner;
    private final DataSource data_source;
    
    public UpisHiphopDAO(QueryRunner query_runner, DataSource data_source) {
        this.query_runner = query_runner;
        this.data_source = data_source;
    }
    
    /**
     * Pronalazi sve hiphop upise.
     */
    public List<UpisHiphop> pronadji_sve() throws SQLException {
        String sql = "SELECT * FROM hiphop";
        return query_runner.query(sql, new MaperRedovaUpisaHiphop());
    }
    
    /**
     * Pronalazi hiphop upise za određenog korisnika.
     */
    public List<UpisHiphop> pronadji_po_korisniku(Long korisnik_id) throws SQLException {
        String sql = "SELECT * FROM hiphop WHERE korisnik_id = ?";
        return query_runner.query(sql, new MaperRedovaUpisaHiphop(), korisnik_id);
    }
    
    /**
     * Prijavljuje korisnika na hiphop časove koristeći stored procedure.
     */
    public String prijavi_na_hiphop(Long korisnik_id, Long instruktor_id) throws SQLException {
        System.out.println("DEBUG: Pozivam prijaviNaHiphop sa korisnik_id=" + korisnik_id + ", instruktor_id=" + instruktor_id);
        
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_prijavi_na_cas(?, ?, ?, ?, ?)}")) {
            
            cs.setLong(1, korisnik_id);
            cs.setString(2, "hiphop");
            cs.setLong(3, instruktor_id);
            cs.registerOutParameter(4, Types.BOOLEAN);  // p_uspeh
            cs.registerOutParameter(5, Types.VARCHAR);  // p_poruka
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(4);
            String poruka = cs.getString(5);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
    
    /**
     * Odjavljuje korisnika sa hiphop časova koristeći stored procedure.
     */
    public String odjavi_sa_hiphopa(Long korisnik_id) throws SQLException {
        System.out.println("DEBUG: Pozivam odjaviSaHiphopa sa korisnik_id=" + korisnik_id);
        
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_odjavi_sa_casa(?, ?, ?, ?)}")) {
            
            cs.setLong(1, korisnik_id);
            cs.setString(2, "hiphop");
            cs.registerOutParameter(3, Types.BOOLEAN);  // p_uspeh
            cs.registerOutParameter(4, Types.VARCHAR);  // p_poruka
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(3);
            String poruka = cs.getString(4);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
}
