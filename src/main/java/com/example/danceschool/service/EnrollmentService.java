package com.example.danceschool.service;

import com.example.danceschool.dao.*;
import com.example.danceschool.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class EnrollmentService {

    @Autowired
    private BaletEnrollmentDAO baletDAO;

    @Autowired
    private HiphopEnrollmentDAO hiphopDAO;

    @Autowired
    private LatinoEnrollmentDAO latinoDAO;

    // Default instructor ID
    private static final Long DEFAULT_INSTRUCTOR_ID = 2L;

    /**
     * Enroll a user in a specific class type using stored procedure
     * Business rules:
     * - User must be a learner (ucenik)
     * - Cannot enroll twice in the same class
     */
    public void enrollUser(User user, String classType) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (!user.isUcenik()) {
            throw new IllegalArgumentException("Only learners can enroll in classes");
        }

        System.out.println("DEBUG: Enrolling user " + user.getId() + " in " + classType);

        // Call stored procedure
        String result;
        try {
            switch (classType.toLowerCase()) {
                case "balet":
                    System.out.println("DEBUG: Calling prijaviNaBalet");
                    result = baletDAO.prijaviNaBalet(user.getId(), DEFAULT_INSTRUCTOR_ID, null);
                    break;
                case "hiphop":
                    System.out.println("DEBUG: Calling prijaviNaHiphop");
                    result = hiphopDAO.prijaviNaHiphop(user.getId(), DEFAULT_INSTRUCTOR_ID, null);
                    break;
                case "latino":
                    System.out.println("DEBUG: Calling prijaviNaLatino");
                    result = latinoDAO.prijaviNaLatino(user.getId(), DEFAULT_INSTRUCTOR_ID, null);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid class type");
            }
            System.out.println("DEBUG: Enrollment result: " + result);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error during enrollment: " + e.getMessage(), e);
        }
        
        if (result == null || !result.startsWith("USPEH")) {
            throw new IllegalArgumentException("Failed to enroll: " + (result != null ? result : "Unknown error"));
        }
    }

    /**
     * Unenroll a user from a specific class type using stored procedure
     * Business rules:
     * - User must be a learner (ucenik)
     * - User must be enrolled in the class
     */
    public void unenrollUser(User user, String classType) throws IllegalArgumentException {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        if (!user.isUcenik()) {
            throw new IllegalArgumentException("Only learners can unenroll from classes");
        }

        System.out.println("DEBUG: Unenrolling user " + user.getId() + " from " + classType);

        // Call stored procedure
        String result;
        try {
            switch (classType.toLowerCase()) {
                case "balet":
                    System.out.println("DEBUG: Calling odjaviSaBalet");
                    result = baletDAO.odjaviSaBalet(user.getId());
                    break;
                case "hiphop":
                    System.out.println("DEBUG: Calling odjaviSaHiphop");
                    result = hiphopDAO.odjaviSaHiphop(user.getId());
                    break;
                case "latino":
                    System.out.println("DEBUG: Calling odjaviSaLatino");
                    result = latinoDAO.odjaviSaLatino(user.getId());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid class type");
            }
            System.out.println("DEBUG: Unenrollment result: " + result);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error during unenrollment: " + e.getMessage(), e);
        }
        
        if (result == null || !result.startsWith("USPEH")) {
            throw new IllegalArgumentException("Failed to unenroll: " + (result != null ? result : "Unknown error"));
        }
    }

    /**
     * Check if user is enrolled in a specific class type
     */
    public boolean isEnrolled(User user, String classType) {
        if (user == null || !user.isUcenik() || classType == null) {
            return false;
        }

        try {
            switch (classType.toLowerCase()) {
                case "balet":
                    return !baletDAO.findByKorisnikId(user.getId()).isEmpty();
                case "hiphop":
                    return !hiphopDAO.findByKorisnikId(user.getId()).isEmpty();
                case "latino":
                    return !latinoDAO.findByKorisnikId(user.getId()).isEmpty();
                default:
                    return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error checking enrollment", e);
        }
    }

    /**
     * Get enrollment status for all class types
     */
    public EnrollmentStatus getEnrollmentStatus(User user) {
        EnrollmentStatus status = new EnrollmentStatus();
        if (user != null && user.isUcenik()) {
            try {
                System.out.println("DEBUG: Getting enrollment status for user " + user.getId());
                
                java.util.List<?> baletList = baletDAO.findByKorisnikId(user.getId());
                System.out.println("DEBUG: Balet enrollments: " + baletList.size());
                status.setEnrolledBalet(!baletList.isEmpty());
                
                java.util.List<?> hiphopList = hiphopDAO.findByKorisnikId(user.getId());
                System.out.println("DEBUG: Hiphop enrollments: " + hiphopList.size());
                status.setEnrolledHiphop(!hiphopList.isEmpty());
                
                java.util.List<?> latinoList = latinoDAO.findByKorisnikId(user.getId());
                System.out.println("DEBUG: Latino enrollments: " + latinoList.size());
                status.setEnrolledLatino(!latinoList.isEmpty());
                
                System.out.println("DEBUG: Enrollment status - Balet: " + status.isEnrolledBalet() + 
                                   ", Hiphop: " + status.isEnrolledHiphop() + 
                                   ", Latino: " + status.isEnrolledLatino());
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Database error getting enrollment status", e);
            }
        }
        return status;
    }

    /**
     * Inner class to hold enrollment status for all class types
     */
    public static class EnrollmentStatus {
        private boolean enrolledBalet;
        private boolean enrolledHiphop;
        private boolean enrolledLatino;

        public boolean isEnrolledBalet() {
            return enrolledBalet;
        }

        public void setEnrolledBalet(boolean enrolledBalet) {
            this.enrolledBalet = enrolledBalet;
        }

        public boolean isEnrolledHiphop() {
            return enrolledHiphop;
        }

        public void setEnrolledHiphop(boolean enrolledHiphop) {
            this.enrolledHiphop = enrolledHiphop;
        }

        public boolean isEnrolledLatino() {
            return enrolledLatino;
        }

        public void setEnrolledLatino(boolean enrolledLatino) {
            this.enrolledLatino = enrolledLatino;
        }
    }
}
