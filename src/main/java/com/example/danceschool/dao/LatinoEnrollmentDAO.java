package com.example.danceschool.dao;

import com.example.danceschool.dao.mapper.LatinoEnrollmentRowMapper;
import com.example.danceschool.model.LatinoEnrollment;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

/**
 * DAO for Latino enrollments.
 */
@Repository
public class LatinoEnrollmentDAO {
    
    private final QueryRunner queryRunner;
    private final DataSource dataSource;
    
    public LatinoEnrollmentDAO(QueryRunner queryRunner, DataSource dataSource) {
        this.queryRunner = queryRunner;
        this.dataSource = dataSource;
    }
    
    public List<LatinoEnrollment> findAll() throws SQLException {
        String sql = "SELECT * FROM latino ORDER BY datum_prijave DESC";
        return queryRunner.query(sql, new LatinoEnrollmentRowMapper());
    }
    
    public Optional<LatinoEnrollment> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM latino WHERE id = ?";
        LatinoEnrollment enrollment = queryRunner.query(sql, new LatinoEnrollmentRowMapper.SingleRowMapper(), id);
        return Optional.ofNullable(enrollment);
    }
    
    public List<LatinoEnrollment> findByKorisnikId(Long korisnikId) throws SQLException {
        String sql = "SELECT * FROM latino WHERE korisnik_id = ? ORDER BY datum_prijave DESC";
        return queryRunner.query(sql, new LatinoEnrollmentRowMapper(), korisnikId);
    }
    
    public String prijaviNaLatino(Long korisnikId, Long instruktorId, String napomena) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement cs = conn.prepareCall("{CALL sp_prijavi_na_cas(?, ?, ?, ?, ?)}");
            
            cs.setLong(1, korisnikId);
            cs.setString(2, "latino");
            cs.setLong(3, instruktorId);
            cs.registerOutParameter(4, Types.BOOLEAN);
            cs.registerOutParameter(5, Types.VARCHAR);
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(4);
            String poruka = cs.getString(5);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
    
    public String odjaviSaLatino(Long korisnikId) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement cs = conn.prepareCall("{CALL sp_odjavi_sa_casa(?, ?, ?, ?)}");
            
            cs.setLong(1, korisnikId);
            cs.setString(2, "latino");
            cs.registerOutParameter(3, Types.BOOLEAN);
            cs.registerOutParameter(4, Types.VARCHAR);
            
            cs.execute();
            
            boolean uspeh = cs.getBoolean(3);
            String poruka = cs.getString(4);
            
            return uspeh ? "USPEH: " + poruka : "GRESKA: " + poruka;
        }
    }
}
