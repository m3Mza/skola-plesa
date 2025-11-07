package com.example.danceschool.repository;

import com.example.danceschool.dao.LatinoEnrollmentDAO;
import com.example.danceschool.model.LatinoEnrollment;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Latino enrollments using DAO pattern.
 */
@Repository
public class LatinoEnrollmentRepository {
    
    private final LatinoEnrollmentDAO latinoDAO;
    
    public LatinoEnrollmentRepository(LatinoEnrollmentDAO latinoDAO) {
        this.latinoDAO = latinoDAO;
    }
    
    public List<LatinoEnrollment> findAll() {
        try {
            return latinoDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching latino enrollments", e);
        }
    }
    
    public Optional<LatinoEnrollment> findById(Long id) {
        try {
            return latinoDAO.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching latino enrollment by id: " + id, e);
        }
    }
    
    public List<LatinoEnrollment> findByKorisnikId(Long korisnikId) {
        try {
            return latinoDAO.findByKorisnikId(korisnikId);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching latino enrollments by user id: " + korisnikId, e);
        }
    }
    
    public boolean existsByKorisnikId(Long korisnikId) {
        return !findByKorisnikId(korisnikId).isEmpty();
    }
    
    public List<LatinoEnrollment> findByInstruktorId(Long instruktorId) {
        // Not implemented in DAO yet, return empty list
        return List.of();
    }
}
