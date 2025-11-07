package com.example.danceschool.repository;

import com.example.danceschool.dao.UserDAO;
import com.example.danceschool.model.User;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Repository implementation for User entity using DAO pattern.
 * This follows the Repository pattern - providing a higher-level abstraction
 * over the DAO layer, hiding SQL exceptions and database-specific logic.
 */
@Repository
public class UserRepository {
    
    private final UserDAO userDAO;
    
    public UserRepository(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    public List<User> findAll() {
        try {
            return userDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all users", e);
        }
    }
    
    public Optional<User> findById(Long id) {
        try {
            return userDAO.findById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by id: " + id, e);
        }
    }
    
    public Optional<User> findByEmail(String email) {
        try {
            return userDAO.findByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by email: " + email, e);
        }
    }
    
    public User save(User user) {
        try {
            return userDAO.save(user);
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user", e);
        }
    }
    
    public void deleteById(Long id) {
        try {
            userDAO.deleteById(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user with id: " + id, e);
        }
    }
    
    public List<User> findByUloga(String uloga) {
        try {
            return userDAO.findByUloga(uloga);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users by role: " + uloga, e);
        }
    }
    
    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
