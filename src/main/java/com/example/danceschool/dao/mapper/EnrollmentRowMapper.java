package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.Enrollment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Base mapper for enrollment classes.
 * Handles common enrollment fields.
 */
public abstract class EnrollmentRowMapper {
    
    protected <T extends Enrollment> T mapCommonFields(ResultSet rs, T enrollment) throws SQLException {
        enrollment.setId(rs.getLong("id"));
        enrollment.setKorisnikId(rs.getLong("korisnik_id"));
        enrollment.setInstruktorId(rs.getLong("instruktor_id"));
        
        Timestamp datumPrijave = rs.getTimestamp("datum_prijave");
        System.out.println("DEBUG EnrollmentMapper: datum_prijave from DB = " + datumPrijave);
        if (datumPrijave != null) {
            enrollment.setDatumPrijave(datumPrijave.toLocalDateTime());
            System.out.println("DEBUG EnrollmentMapper: Set datumPrijave to = " + enrollment.getDatumPrijave());
        } else {
            System.out.println("DEBUG EnrollmentMapper: datum_prijave is NULL in database!");
        }
        
        enrollment.setNapomena(rs.getString("napomena"));
        
        return enrollment;
    }
}
