package com.example.danceschool.dao;

import com.example.danceschool.dao.mapper.MaperRedovaUpisaBalet;
import com.example.danceschool.model.UpisBalet;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * DAO objekat za pristup podacima upisa na balet časove.
 */
@Repository
public class UpisBaletDAO {
    
    private final QueryRunner query_runner;
    private final DataSource data_source;
    
    public UpisBaletDAO(QueryRunner query_runner, DataSource data_source) {
        this.query_runner = query_runner;
        this.data_source = data_source;
    }
    
    /**
     * Pronalazi sve balet upise.
     */
    public List<UpisBalet> pronadji_sve() throws SQLException {
        String sql = "SELECT * FROM balet";
        return query_runner.query(sql, new MaperRedovaUpisaBalet());
    }
    
    /**
     * Pronalazi balet upise za određenog korisnika.
     */
    public List<UpisBalet> pronadji_po_korisniku(Long korisnik_id) throws SQLException {
        String sql = "SELECT * FROM balet WHERE korisnik_id = ?";
        return query_runner.query(sql, new MaperRedovaUpisaBalet(), korisnik_id);
    }
    
    /**
     * Prijavljuje korisnika na balet časove koristeći stored procedure.
     */
    public String prijavi_na_balet(Long korisnik_id, Long instruktor_id) throws SQLException {
        System.out.println("DEBUG: Pozivam prijaviNaBalet sa korisnik_id=" + korisnik_id + ", instruktor_id=" + instruktor_id);
        
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_prijavi_na_cas(?, ?, ?, ?, ?)}")) {
            
            cs.setLong(1, korisnik_id);
            cs.setString(2, "balet");
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
     * Odjavljuje korisnika sa balet časova koristeći stored procedure.
     */
    public String odjavi_sa_baleta(Long korisnik_id) throws SQLException {
        System.out.println("DEBUG: Pozivam odjaviSaBaleta sa korisnik_id=" + korisnik_id);
        
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_odjavi_sa_casa(?, ?, ?, ?)}")) {
            
            cs.setLong(1, korisnik_id);
            cs.setString(2, "balet");
            cs.registerOutParameter(3, Types.BOOLEAN);  // p_uspeh
            cs.registerOutParameter(4, Types.VARCHAR);  // p_poruka
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(3);
            String poruka = cs.getString(4);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
}
