package com.example.danceschool.repository;

import com.example.danceschool.dao.HiphopEnrollmentDAO;
import com.example.danceschool.model.HiphopEnrollment;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Hip-hop enrollments using DAO pattern.
 */
@Repository
public class HiphopEnrollmentRepository {
    
    private final HiphopEnrollmentDAO hiphopDAO;
    
    public HiphopEnrollmentRepository(HiphopEnrollmentDAO hiphopDAO) {
        this.hiphopDAO = hiphopDAO;
    }
    
    public List<HiphopEnrollment> findAll() {
        try {
            return hiphopDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching hip-hop enrollments", e);
        }
    }
    
    public Optional<HiphopEnrollment> findById(Long id) {
        try {
            return hiphopDAO.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching hip-hop enrollment by id: " + id, e);
        }
    }
    
    public List<HiphopEnrollment> findByKorisnikId(Long korisnikId) {
        try {
            return hiphopDAO.findByKorisnikId(korisnikId);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching hip-hop enrollments by user id: " + korisnikId, e);
        }
    }
    
    public boolean existsByKorisnikId(Long korisnikId) {
        return !findByKorisnikId(korisnikId).isEmpty();
    }
    
    public List<HiphopEnrollment> findByInstruktorId(Long instruktorId) {
        // Not implemented in DAO yet, return empty list
        return List.of();
    }
}
