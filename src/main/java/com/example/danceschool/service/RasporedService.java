package com.example.danceschool.service;

import com.example.danceschool.model.Raspored;
import com.example.danceschool.model.User;
import com.example.danceschool.repository.RasporedRepository;
import com.example.danceschool.repository.RasporedProcedureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RasporedService {

    @Autowired
    private RasporedRepository rasporedRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private RasporedProcedureRepository rasporedProcedureRepository;

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

        List<Raspored> allSchedules = rasporedRepository.findAll();

        // Instructors see everything
        if (user.isInstruktor()) {
            return allSchedules;
        }

        // Learners see only their enrolled classes
        if (user.isUcenik()) {
            return allSchedules.stream()
                    .filter(raspored -> enrollmentService.isEnrolled(user, raspored.getTipCasa()))
                    .collect(Collectors.toList());
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
        RasporedProcedureRepository.RasporedResult result = 
            rasporedProcedureRepository.createSchedule(
                tipCasa.toLowerCase(),
                Math.toIntExact(instruktorId != null ? instruktorId : user.getId()),
                datumVreme,
                trajanjeMin,
                lokacija, // lokacija
                opis != null ? opis.trim() : "",
                Math.toIntExact(user.getId())
            );
        
        if (!result.isSuccess()) {
            throw new IllegalArgumentException(result.getMessage());
        }
        
        // Fetch and return the created schedule
        if (result.getNewId() != null) {
            return rasporedRepository.findById(result.getNewId().longValue()).orElse(null);
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
        RasporedProcedureRepository.RasporedResult result = 
            rasporedProcedureRepository.deleteSchedule(
                Math.toIntExact(scheduleId),
                Math.toIntExact(user.getId())
            );
        
        if (!result.isSuccess()) {
            throw new IllegalArgumentException(result.getMessage());
        }
    }

    /**
     * Get all schedule entries (admin/instructor view)
     */
    public List<Raspored> getAllSchedules() {
        return rasporedRepository.findAll();
    }
}
