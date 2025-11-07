package com.example.danceschool.dao;

import com.example.danceschool.dao.mapper.RasporedRowMapper;
import com.example.danceschool.model.Raspored;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 * Data Access Object for Raspored (Schedule) entity.
 * Handles all database operations for schedules using DBUtils and stored procedures.
 */
@Repository
public class RasporedDAO {
    
    private final QueryRunner queryRunner;
    private final DataSource dataSource;
    
    public RasporedDAO(QueryRunner queryRunner, DataSource dataSource) {
        this.queryRunner = queryRunner;
        this.dataSource = dataSource;
    }
    
    public List<Raspored> findAll() throws SQLException {
        String sql = "SELECT * FROM raspored ORDER BY datum_vreme";
        return queryRunner.query(sql, new RasporedRowMapper());
    }
    
    public Optional<Raspored> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM raspored WHERE id = ?";
        Raspored raspored = queryRunner.query(sql, new RasporedRowMapper.SingleRowMapper(), id);
        return Optional.ofNullable(raspored);
    }
    
    public List<Raspored> findByTipCasa(String tipCasa) throws SQLException {
        String sql = "SELECT * FROM raspored WHERE tip_casa = ? ORDER BY datum_vreme";
        return queryRunner.query(sql, new RasporedRowMapper(), tipCasa);
    }
    
    public List<Raspored> findByInstruktorId(Long instruktorId) throws SQLException {
        String sql = "SELECT * FROM raspored WHERE instruktor_id = ? ORDER BY datum_vreme";
        return queryRunner.query(sql, new RasporedRowMapper(), instruktorId);
    }
    
    public List<Raspored> findByDatumVremeAfter(LocalDateTime datum) throws SQLException {
        String sql = "SELECT * FROM raspored WHERE datum_vreme > ? ORDER BY datum_vreme";
        return queryRunner.query(sql, new RasporedRowMapper(), datum);
    }
    
    /**
     * Adds a schedule entry using stored procedure.
     */
    public String dodajURaspored(String tipCasa, Long instruktorId, LocalDateTime datumVreme, 
                                  Integer trajanjeMin, String lokacija, Integer maksimalnoPolaznika,
                                  String opis, Long kreiraoId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement cs = conn.prepareCall("{CALL sp_kreiraj_raspored(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            
            cs.setString(1, tipCasa);
            cs.setLong(2, instruktorId);
            cs.setObject(3, datumVreme);
            cs.setInt(4, trajanjeMin);
            cs.setString(5, lokacija);
            cs.setString(6, opis);
            cs.setLong(7, kreiraoId);
            cs.registerOutParameter(8, Types.BOOLEAN);
            cs.registerOutParameter(9, Types.VARCHAR);
            cs.registerOutParameter(10, Types.INTEGER);
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(8);
            String poruka = cs.getString(9);
            Integer noviId = cs.getInt(10);
            
            if (uspeh && noviId != null) {
                return "USPEH: Dodat termin sa ID " + noviId;
            } else {
                return "GRESKA: " + poruka;
            }
        }
    }
    
    /**
     * Deletes a schedule entry using stored procedure.
     */
    public String obrisiRaspored(Long id) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement cs = conn.prepareCall("{CALL sp_obrisi_raspored(?, ?, ?, ?)}");
            
            cs.setLong(1, id);
            cs.setLong(2, 2); // Assuming current user ID - this should be passed as parameter
            cs.registerOutParameter(3, Types.BOOLEAN);
            cs.registerOutParameter(4, Types.VARCHAR);
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(3);
            String poruka = cs.getString(4);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
}
