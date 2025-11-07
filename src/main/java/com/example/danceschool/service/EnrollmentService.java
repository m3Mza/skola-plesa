package com.example.danceschool.service;

import com.example.danceschool.model.*;
import com.example.danceschool.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    @Autowired
    private BaletEnrollmentRepository baletRepository;

    @Autowired
    private HiphopEnrollmentRepository hiphopRepository;

    @Autowired
    private LatinoEnrollmentRepository latinoRepository;

    @Autowired
    private EnrollmentProcedureRepository enrollmentProcedureRepository;

    // Default instructor ID
    private static final Integer DEFAULT_INSTRUCTOR_ID = 2;

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

        // Call stored procedure
        EnrollmentProcedureRepository.EnrollmentResult result = 
            enrollmentProcedureRepository.enrollInClass(
                Math.toIntExact(user.getId()), 
                classType.toLowerCase(), 
                DEFAULT_INSTRUCTOR_ID
            );
        
        if (!result.isSuccess()) {
            throw new IllegalArgumentException(result.getMessage());
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

        // Call stored procedure
        EnrollmentProcedureRepository.EnrollmentResult result = 
            enrollmentProcedureRepository.unenrollFromClass(
                Math.toIntExact(user.getId()), 
                classType.toLowerCase()
            );
        
        if (!result.isSuccess()) {
            throw new IllegalArgumentException(result.getMessage());
        }
    }

    /**
     * Check if user is enrolled in a specific class type
     */
    public boolean isEnrolled(User user, String classType) {
        if (user == null || !user.isUcenik()) {
            return false;
        }

        switch (classType.toLowerCase()) {
            case "balet":
                return baletRepository.existsByKorisnikId(user.getId());
            case "hiphop":
                return hiphopRepository.existsByKorisnikId(user.getId());
            case "latino":
                return latinoRepository.existsByKorisnikId(user.getId());
            default:
                return false;
        }
    }

    /**
     * Get enrollment status for all class types
     */
    public EnrollmentStatus getEnrollmentStatus(User user) {
        EnrollmentStatus status = new EnrollmentStatus();
        if (user != null && user.isUcenik()) {
            status.setEnrolledBalet(baletRepository.existsByKorisnikId(user.getId()));
            status.setEnrolledHiphop(hiphopRepository.existsByKorisnikId(user.getId()));
            status.setEnrolledLatino(latinoRepository.existsByKorisnikId(user.getId()));
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
