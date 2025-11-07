package com.example.danceschool.repository;

import com.example.danceschool.model.LatinoEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LatinoEnrollmentRepository extends JpaRepository<LatinoEnrollment, Long> {
    
    List<LatinoEnrollment> findByKorisnikId(Long korisnikId);
    
    boolean existsByKorisnikId(Long korisnikId);
    
    List<LatinoEnrollment> findByInstruktorId(Long instruktorId);
}
