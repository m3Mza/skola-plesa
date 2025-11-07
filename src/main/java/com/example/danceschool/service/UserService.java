package com.example.danceschool.service;

import com.example.danceschool.dao.UserDAO;
import com.example.danceschool.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    /**
     * Authenticate user with email and password
     * Business rules:
     * - Email and password must not be null or empty
     * - User must exist in database
     * - Password must match (plain text comparison - should use BCrypt in production)
     */
    public User authenticate(String email, String password) throws IllegalArgumentException {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }

        User user;
        try {
            user = userDAO.findByEmail(email).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException("Database error during authentication", e);
        }
        
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Note: In production, use BCrypt password encoding
        if (!user.getLozinka().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user;
    }

    /**
     * Register a new user
     * Business rules:
     * - All required fields must be provided
     * - Email must be unique
     * - Email must be valid format
     * - Password must be at least 6 characters
     * - Default role is 'ucenik'
     */
    public User registerUser(String ime, String prezime, String email, String password, String telefon) 
            throws IllegalArgumentException {
        
        // Validation
        if (ime == null || ime.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }
        if (prezime == null || prezime.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name is required");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        if (telefon == null || telefon.trim().isEmpty()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        // Check if email already exists
        try {
            if (userDAO.findByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email already registered");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error during registration", e);
        }

        // Create new user
        User user = new User();
        user.setIme(ime.trim());
        user.setPrezime(prezime.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setLozinka(password); // Note: Should hash with BCrypt in production
        user.setTelefon(telefon.trim());
        user.setUloga("ucenik"); // Default role
        user.setDatumRegistracije(LocalDateTime.now());
        user.setAktivan(true);

        try {
            return userDAO.save(user);
        } catch (SQLException e) {
            throw new RuntimeException("Database error during registration", e);
        }
    }

    /**
     * Find user by email
     */
    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        try {
            return userDAO.findByEmail(email.trim().toLowerCase()).orElse(null);
        } catch (SQLException e) {
            throw new RuntimeException("Database error", e);
        }
    }

    /**
     * Check if user is an instructor
     */
    public boolean isInstructor(User user) {
        return user != null && user.isInstruktor();
    }

    /**
     * Check if user is a learner
     */
    public boolean isLearner(User user) {
        return user != null && user.isUcenik();
    }
}
