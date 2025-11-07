package com.example.danceschool.repository;

import com.example.danceschool.dao.RasporedDAO;
import com.example.danceschool.model.Raspored;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository implementation for Raspored (Schedule) entity.
 * Wraps DAO layer with Repository pattern for cleaner service layer.
 */
@Repository
public class RasporedRepository {
    
    private final RasporedDAO rasporedDAO;
    
    public RasporedRepository(RasporedDAO rasporedDAO) {
        this.rasporedDAO = rasporedDAO;
    }
    
    public List<Raspored> findAll() {
        try {
            return rasporedDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all schedules", e);
        }
    }
    
    public Optional<Raspored> findById(Long id) {
        try {
            return rasporedDAO.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching schedule by id: " + id, e);
        }
    }
    
    public List<Raspored> findByTipCasa(String tipCasa) {
        try {
            return rasporedDAO.findByTipCasa(tipCasa);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching schedules by class type: " + tipCasa, e);
        }
    }
    
    public List<Raspored> findByInstruktorId(Long instruktorId) {
        try {
            return rasporedDAO.findByInstruktorId(instruktorId);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching schedules by instructor id: " + instruktorId, e);
        }
    }
    
    public List<Raspored> findByDatumVremeAfter(LocalDateTime datum) {
        try {
            return rasporedDAO.findByDatumVremeAfter(datum);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching schedules after date: " + datum, e);
        }
    }
    
    public List<Raspored> findByDatumVremeBetween(LocalDateTime start, LocalDateTime end) {
        try {
            return rasporedDAO.findAll().stream()
                .filter(r -> r.getDatumVreme().isAfter(start) && r.getDatumVreme().isBefore(end))
                .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching schedules between dates", e);
        }
    }
    
    public List<Raspored> findByDatumVremeAfterOrderByDatumVremeAsc(LocalDateTime datum) {
        return findByDatumVremeAfter(datum); // Already ordered in DAO
    }
}
