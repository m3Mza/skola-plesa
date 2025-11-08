package com.example.danceschool.dao;

import com.example.danceschool.dao.mapper.MaperRedovaRasporeda;
import com.example.danceschool.model.Raspored;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class RasporedDAO {
    
    private final QueryRunner query_runner;
    private final DataSource data_source;
    
    public RasporedDAO(QueryRunner query_runner, DataSource data_source) {
        this.query_runner = query_runner;
        this.data_source = data_source;
    }
    
    public List<Raspored> pronadji_sve() throws SQLException {
        String sql = "SELECT * FROM raspored ORDER BY datum_vreme";
        return query_runner.query(sql, new MaperRedovaRasporeda());
    }
    
    public Optional<Raspored> pronadji_po_id(Long id) throws SQLException {
        String sql = "SELECT * FROM raspored WHERE id = ?";
        Raspored raspored = query_runner.query(sql, new MaperRedovaRasporeda.JedanRed(), id);
        return Optional.ofNullable(raspored);
    }
    
    public List<Raspored> pronadji_po_instruktoru(Long instruktor_id) throws SQLException {
        String sql = "SELECT * FROM raspored WHERE instruktor_id = ? ORDER BY datum_vreme";
        return query_runner.query(sql, new MaperRedovaRasporeda(), instruktor_id);
    }
    
    public List<Raspored> pronadji_po_tipu_casa(String tip_casa) throws SQLException {
        String sql = "SELECT * FROM raspored WHERE tip_casa = ? ORDER BY datum_vreme";
        return query_runner.query(sql, new MaperRedovaRasporeda(), tip_casa);
    }
    
    public String dodaj_u_raspored(Raspored raspored) throws SQLException {
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_kreiraj_raspored(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
            
            cs.setString(1, raspored.getTip_casa());
            cs.setLong(2, raspored.getInstruktor_id());
            cs.setTimestamp(3, java.sql.Timestamp.valueOf(raspored.getDatum_vreme()));
            cs.setInt(4, raspored.getTrajanje_min());
            cs.setString(5, raspored.getLokacija());
            cs.setInt(6, raspored.getMaksimalno_polaznika());
            cs.setString(7, raspored.getOpis());
            cs.setLong(8, raspored.getKreirao_id());
            cs.registerOutParameter(9, Types.BOOLEAN);
            cs.registerOutParameter(10, Types.VARCHAR);
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(9);
            String poruka = cs.getString(10);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
    
    public String obrisi_iz_rasporeda(Long raspored_id, Long korisnik_id) throws SQLException {
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_obrisi_raspored(?, ?, ?, ?)}")) {
            
            cs.setLong(1, raspored_id);
            cs.setLong(2, korisnik_id);
            cs.registerOutParameter(3, Types.BOOLEAN);
            cs.registerOutParameter(4, Types.VARCHAR);
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(3);
            String poruka = cs.getString(4);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
    
    /**
     * Ažurira postojeći raspored u bazi koristeći stored procedure.
     */
    public String azuriraj(Raspored raspored, Long korisnik_id) throws SQLException {
        try (Connection conn = data_source.getConnection();
             CallableStatement cs = conn.prepareCall("{CALL sp_izmeni_cas(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}")) {
            
            cs.setLong(1, raspored.getId());
            cs.setString(2, raspored.getTip_casa());
            cs.setTimestamp(3, java.sql.Timestamp.valueOf(raspored.getDatum_vreme()));
            cs.setInt(4, raspored.getTrajanje_min());
            cs.setString(5, raspored.getLokacija());
            cs.setInt(6, raspored.getMaksimalno_polaznika());
            cs.setString(7, raspored.getOpis());
            cs.setLong(8, korisnik_id);
            cs.registerOutParameter(9, Types.BOOLEAN);
            cs.registerOutParameter(10, Types.VARCHAR);
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(9);
            String poruka = cs.getString(10);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
}

