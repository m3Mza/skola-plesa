package com.example.danceschool.repository;

import com.example.danceschool.dao.RasporedDAO;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Repository for Schedule stored procedures using DAO pattern.
 * Wraps DAO layer with cleaner interface for service layer.
 */
@Repository
public class RasporedProcedureRepository {

    private final RasporedDAO rasporedDAO;

    public RasporedProcedureRepository(RasporedDAO rasporedDAO) {
        this.rasporedDAO = rasporedDAO;
    }

    /**
     * Call stored procedure to create schedule entry
     */
    public RasporedResult createSchedule(String tipCasa, Integer instruktorId, LocalDateTime datumVreme,
                                        Integer trajanjeMin, String lokacija, Integer maksimalnoPolaznika,
                                        String opis, Integer keiraoId) {
        try {
            String result = rasporedDAO.dodajURaspored(
                tipCasa, 
                instruktorId.longValue(), 
                datumVreme, 
                trajanjeMin, 
                lokacija,
                maksimalnoPolaznika,
                opis, 
                keiraoId.longValue()
            );
            
            // Parse result message to determine success
            boolean uspeh = result != null && result.contains("uspešno");
            return new RasporedResult(uspeh, result, null);
        } catch (SQLException e) {
            return new RasporedResult(false, "Greška: " + e.getMessage(), null);
        }
    }

    /**
     * Call stored procedure to delete schedule entry
     */
    public RasporedResult deleteSchedule(Integer rasporedId) {
        try {
            String result = rasporedDAO.obrisiRaspored(rasporedId.longValue());
            boolean uspeh = result != null && result.contains("uspešno");
            return new RasporedResult(uspeh, result, null);
        } catch (SQLException e) {
            return new RasporedResult(false, "Greška: " + e.getMessage(), null);
        }
    }

    /**
     * Result class for schedule operations
     */
    public static class RasporedResult {
        private final Boolean success;
        private final String message;
        private final Integer newId;

        public RasporedResult(Boolean success, String message, Integer newId) {
            this.success = success;
            this.message = message;
            this.newId = newId;
        }

        public Boolean isSuccess() {
            return success != null && success;
        }

        public String getMessage() {
            return message;
        }

        public Integer getNewId() {
            return newId;
        }
    }
}
