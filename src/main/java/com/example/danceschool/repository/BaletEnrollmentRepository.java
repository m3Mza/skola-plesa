package com.example.danceschool.repository;

import com.example.danceschool.dao.BaletEnrollmentDAO;
import com.example.danceschool.model.BaletEnrollment;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Ballet enrollments using DAO pattern.
 */
@Repository
public class BaletEnrollmentRepository {
    
    private final BaletEnrollmentDAO baletDAO;
    
    public BaletEnrollmentRepository(BaletEnrollmentDAO baletDAO) {
        this.baletDAO = baletDAO;
    }
    
    public List<BaletEnrollment> findAll() {
        try {
            return baletDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching ballet enrollments", e);
        }
    }
    
    public Optional<BaletEnrollment> findById(Long id) {
        try {
            return baletDAO.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching ballet enrollment by id: " + id, e);
        }
    }
    
    public List<BaletEnrollment> findByKorisnikId(Long korisnikId) {
        try {
            return baletDAO.findByKorisnikId(korisnikId);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching ballet enrollments by user id: " + korisnikId, e);
        }
    }
    
    public boolean existsByKorisnikId(Long korisnikId) {
        return !findByKorisnikId(korisnikId).isEmpty();
    }
    
    public List<BaletEnrollment> findByInstruktorId(Long instruktorId) {
        // Not implemented in DAO yet, return empty list
        return List.of();
    }
}
