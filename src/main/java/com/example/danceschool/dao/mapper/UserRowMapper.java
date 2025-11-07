package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.User;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom row mapper for User to handle database column name to Java field mapping.
 */
public class UserRowMapper implements ResultSetHandler<List<User>> {
    
    @Override
    public List<User> handle(ResultSet rs) throws SQLException {
        List<User> results = new ArrayList<>();
        while (rs.next()) {
            results.add(mapRow(rs));
        }
        return results;
    }
    
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setIme(rs.getString("ime"));
        user.setPrezime(rs.getString("prezime"));
        user.setEmail(rs.getString("email"));
        user.setLozinka(rs.getString("lozinka"));
        user.setTelefon(rs.getString("telefon"));
        
        Timestamp datumRegistracije = rs.getTimestamp("datum_registracije");
        if (datumRegistracije != null) {
            user.setDatumRegistracije(datumRegistracije.toLocalDateTime());
        }
        
        user.setUloga(rs.getString("uloga"));
        user.setAktivan(rs.getBoolean("aktivan"));
        
        return user;
    }
    
    public static class SingleRowMapper implements ResultSetHandler<User> {
        @Override
        public User handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setIme(rs.getString("ime"));
                user.setPrezime(rs.getString("prezime"));
                user.setEmail(rs.getString("email"));
                user.setLozinka(rs.getString("lozinka"));
                user.setTelefon(rs.getString("telefon"));
                
                Timestamp datumRegistracije = rs.getTimestamp("datum_registracije");
                if (datumRegistracije != null) {
                    user.setDatumRegistracije(datumRegistracije.toLocalDateTime());
                }
                
                user.setUloga(rs.getString("uloga"));
                user.setAktivan(rs.getBoolean("aktivan"));
                
                return user;
            }
            return null;
        }
    }
}
