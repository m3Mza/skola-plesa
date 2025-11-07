package com.example.danceschool.repository;

import com.example.danceschool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Pronađi korisnika po email-u
    Optional<User> findByEmail(String email);
    
    // Proveri da li email postoji
    boolean existsByEmail(String email);
}
