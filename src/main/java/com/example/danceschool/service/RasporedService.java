package com.example.danceschool.service;

import com.example.danceschool.dao.RasporedDAO;
import com.example.danceschool.model.Raspored;
import com.example.danceschool.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RasporedService {

    @Autowired
    private RasporedDAO rasporedDAO;

    @Autowired
    private EnrollmentService enrollmentService;

    /**
     * Get schedule for a specific user based on their role
     * Business rules:
     * - Instructors see all schedule entries
     * - Learners see only entries for classes they are enrolled in
     */
    public List<Raspored> getScheduleForUser(User user) {
        if (user == null) {
            return List.of();
        }

        List<Raspored> allSchedules;
        try {
            allSchedules = rasporedDAO.findAll();
            System.out.println("DEBUG: Fetched " + allSchedules.size() + " schedules from database");
            for (Raspored r : allSchedules) {
                System.out.println("  - Schedule: id=" + r.getId() + ", tipCasa=" + r.getTipCasa() + 
                                   ", datum=" + r.getDatumVreme() + ", lokacija=" + r.getLokacija());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error fetching schedules", e);
        }

        // Instructors see everything
        if (user.isInstruktor()) {
            System.out.println("DEBUG: User is instructor, returning all " + allSchedules.size() + " schedules");
            return allSchedules;
        }

        // Learners see only their enrolled classes
        if (user.isUcenik()) {
            List<Raspored> filtered = allSchedules.stream()
                    .filter(raspored -> {
                        boolean enrolled = enrollmentService.isEnrolled(user, raspored.getTipCasa());
                        System.out.println("DEBUG: Checking " + raspored.getTipCasa() + " enrollment: " + enrolled);
                        return enrolled;
                    })
                    .collect(Collectors.toList());
            System.out.println("DEBUG: User is learner, returning " + filtered.size() + " enrolled schedules");
            return filtered;
        }

        return List.of();
    }

    /**
     * Add a new schedule entry using stored procedure
     * Business rules:
     * - Only instructors can add schedule entries
     * - All required fields must be provided
     * - Start time must be in the future
     * - No overlapping schedule for the same class type at the same time
     */
    public Raspored addSchedule(User user, String tipCasa, LocalDateTime datumVreme, Integer trajanjeMin, 
                                 String lokacija, String opis, Long instruktorId) 
            throws IllegalArgumentException {
        
        // Validate user permissions
        if (user == null || !user.isInstruktor()) {
            throw new IllegalArgumentException("Only instructors can add schedule entries");
        }

        // Validate required fields
        if (tipCasa == null || tipCasa.trim().isEmpty()) {
            throw new IllegalArgumentException("Class type is required");
        }
        if (!tipCasa.equalsIgnoreCase("balet") && 
            !tipCasa.equalsIgnoreCase("hiphop") && 
            !tipCasa.equalsIgnoreCase("latino")) {
            throw new IllegalArgumentException("Invalid class type. Must be balet, hiphop, or latino");
        }
        if (datumVreme == null) {
            throw new IllegalArgumentException("Date and time is required");
        }
        if (trajanjeMin == null || trajanjeMin <= 0) {
            trajanjeMin = 60; // Default to 60 minutes
        }

        // Call stored procedure
        String result;
        try {
            result = rasporedDAO.dodajURaspored(
                tipCasa.toLowerCase(),
                instruktorId != null ? instruktorId : user.getId(),
                datumVreme,
                trajanjeMin,
                lokacija,
                15, // maksimalnoPolaznika - default value
                opis != null ? opis.trim() : "",
                user.getId()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database error adding schedule: " + e.getMessage(), e);
        }
        
        if (result == null || !result.startsWith("USPEH")) {
            throw new IllegalArgumentException("Failed to add schedule: " + (result != null ? result : "Unknown error"));
        }
        
        // Extract ID from success message (format: "USPEH: Dodat termin sa ID 123")
        try {
            String[] parts = result.split(" ID ");
            if (parts.length > 1) {
                Long newId = Long.parseLong(parts[1].trim());
                return rasporedDAO.findById(newId).orElse(null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving created schedule: " + e.getMessage(), e);
        } catch (Exception e) {
            // If we can't parse the ID, log it but don't fail
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Delete a schedule entry using stored procedure
     * Business rules:
     * - Only instructors can delete schedule entries
     * - Schedule entry must exist
     */
    public void deleteSchedule(User user, Long scheduleId) throws IllegalArgumentException {
        if (user == null || !user.isInstruktor()) {
            throw new IllegalArgumentException("Only instructors can delete schedule entries");
        }

        if (scheduleId == null) {
            throw new IllegalArgumentException("Schedule ID is required");
        }

        // Call stored procedure
        String result;
        try {
            result = rasporedDAO.obrisiRaspored(scheduleId);
        } catch (SQLException e) {
            throw new RuntimeException("Database error deleting schedule", e);
        }
        
        if (!result.startsWith("USPEH")) {
            throw new IllegalArgumentException(result);
        }
    }

    /**
     * Get all schedule entries (admin/instructor view)
     */
    public List<Raspored> getAllSchedules() {
        try {
            return rasporedDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException("Database error fetching schedules", e);
        }
    }
}
