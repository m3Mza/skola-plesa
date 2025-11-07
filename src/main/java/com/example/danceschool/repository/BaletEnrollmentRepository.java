package com.example.danceschool.repository;

import com.example.danceschool.model.BaletEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BaletEnrollmentRepository extends JpaRepository<BaletEnrollment, Long> {
    
    List<BaletEnrollment> findByKorisnikId(Long korisnikId);
    
    boolean existsByKorisnikId(Long korisnikId);
    
    List<BaletEnrollment> findByInstruktorId(Long instruktorId);
}
