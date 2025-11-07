package com.example.danceschool.repository;

import com.example.danceschool.model.Raspored;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RasporedRepository extends JpaRepository<Raspored, Long> {
    
    // Pronađi raspored za određeni tip časa
    List<Raspored> findByTipCasa(String tipCasa);
    
    // Pronađi raspored za instruktora
    List<Raspored> findByInstruktorId(Long instruktorId);
    
    // Pronađi časove nakon određenog datuma
    List<Raspored> findByDatumVremeAfter(LocalDateTime datum);
    
    // Pronađi časove između dva datuma
    List<Raspored> findByDatumVremeBetween(LocalDateTime start, LocalDateTime end);
    
    // Pronađi sve buduće časove sortarane po datumu
    List<Raspored> findByDatumVremeAfterOrderByDatumVremeAsc(LocalDateTime datum);
}
