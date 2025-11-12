package com.example.danceschool.dao;

import com.example.danceschool.dao.mapper.MaperRedovaUpisaLatino;
import com.example.danceschool.model.UpisLatino;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * DAO objekat za pristup podacima upisa na latino časove.
 */
@Repository
public class UpisLatinoDAO {
    
    private final QueryRunner query_runner;
    private final DataSource data_source;
    
    public UpisLatinoDAO(QueryRunner query_runner, DataSource data_source) {
        this.query_runner = query_runner;
        this.data_source = data_source;
    }
    
    /**
     * Pronalazi sve latino upise.
     */
    public List<UpisLatino> pronadji_sve() throws SQLException {
        String sql = "SELECT * FROM latino";
        return query_runner.query(sql, new MaperRedovaUpisaLatino());
    }
    
    /**
     * Pronalazi latino upise za određenog korisnika.
     */
    public List<UpisLatino> pronadji_po_korisniku(Long korisnik_id) throws SQLException {
        String sql = "SELECT * FROM latino WHERE korisnik_id = ?";
        return query_runner.query(sql, new MaperRedovaUpisaLatino(), korisnik_id);
    }
    
    /**
     * Prijavljuje korisnika na latino časove koristeći stored procedure.
     */
    public String prijavi_na_latino(Long korisnik_id, Long instruktor_id) throws SQLException {
        System.out.println("DEBUG: Pozivam prijaviNaLatino sa korisnik_id=" + korisnik_id + ", instruktor_id=" + instruktor_id);
        
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_prijavi_na_cas(?, ?, ?, ?, ?)}")) {
            
            cs.setLong(1, korisnik_id);
            cs.setString(2, "latino");
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
     * Odjavljuje korisnika sa latino časova koristeći stored procedure.
     */
    public String odjavi_sa_latina(Long korisnik_id) throws SQLException {
        System.out.println("DEBUG: Pozivam odjaviSaLatina sa korisnik_id=" + korisnik_id);
        
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_odjavi_sa_casa(?, ?, ?, ?)}")) {
            
            cs.setLong(1, korisnik_id);
            cs.setString(2, "latino");
            cs.registerOutParameter(3, Types.BOOLEAN);  // p_uspeh
            cs.registerOutParameter(4, Types.VARCHAR);  // p_poruka
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(3);
            String poruka = cs.getString(4);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
}
