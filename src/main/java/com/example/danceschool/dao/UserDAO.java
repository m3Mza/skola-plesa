package com.example.danceschool.dao;

import com.example.danceschool.dao.mapper.UserRowMapper;
import com.example.danceschool.model.User;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for User entity.
 * Separates database technology (SQL, DBUtils) from business logic.
 */
@Repository
public class UserDAO {
    
    private final QueryRunner queryRunner;
    
    public UserDAO(QueryRunner queryRunner) {
        this.queryRunner = queryRunner;
    }
    
    public List<User> findAll() throws SQLException {
        String sql = "SELECT * FROM korisnici";
        return queryRunner.query(sql, new UserRowMapper());
    }
    
    public Optional<User> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM korisnici WHERE id = ?";
        User user = queryRunner.query(sql, new UserRowMapper.SingleRowMapper(), id);
        return Optional.ofNullable(user);
    }
    
    public Optional<User> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM korisnici WHERE email = ?";
        User user = queryRunner.query(sql, new UserRowMapper.SingleRowMapper(), email);
        return Optional.ofNullable(user);
    }
    
    public User save(User user) throws SQLException {
        if (user.getId() == null) {
            return insert(user);
        } else {
            return update(user);
        }
    }
    
    private User insert(User user) throws SQLException {
        String sql = "INSERT INTO korisnici (ime, prezime, email, lozinka, telefon, datum_registracije, uloga, aktivan) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        queryRunner.update(sql, 
            user.getIme(),
            user.getPrezime(), 
            user.getEmail(),
            user.getLozinka(),
            user.getTelefon(),
            user.getDatumRegistracije(),
            user.getUloga(),
            user.getAktivan()
        );
        
        // Get the generated ID
        String lastIdSql = "SELECT LAST_INSERT_ID()";
        Long id = queryRunner.query(lastIdSql, rs -> {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return null;
        });
        
        user.setId(id);
        return user;
    }
    
    private User update(User user) throws SQLException {
        String sql = "UPDATE korisnici SET ime = ?, prezime = ?, email = ?, lozinka = ?, " +
                     "telefon = ?, uloga = ?, aktivan = ? WHERE id = ?";
        
        queryRunner.update(sql,
            user.getIme(),
            user.getPrezime(),
            user.getEmail(),
            user.getLozinka(),
            user.getTelefon(),
            user.getUloga(),
            user.getAktivan(),
            user.getId()
        );
        
        return user;
    }
    
    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM korisnici WHERE id = ?";
        queryRunner.update(sql, id);
    }
    
    public List<User> findByUloga(String uloga) throws SQLException {
        String sql = "SELECT * FROM korisnici WHERE uloga = ?";
        return queryRunner.query(sql, new UserRowMapper(), uloga);
    }
}
