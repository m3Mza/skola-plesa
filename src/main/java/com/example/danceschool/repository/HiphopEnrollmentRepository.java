package com.example.danceschool.repository;

import com.example.danceschool.model.HiphopEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HiphopEnrollmentRepository extends JpaRepository<HiphopEnrollment, Long> {
    
    List<HiphopEnrollment> findByKorisnikId(Long korisnikId);
    
    boolean existsByKorisnikId(Long korisnikId);
    
    List<HiphopEnrollment> findByInstruktorId(Long instruktorId);
}
