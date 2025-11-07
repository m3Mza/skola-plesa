package com.example.danceschool.repository;

import com.example.danceschool.dao.BaletEnrollmentDAO;
import com.example.danceschool.dao.HiphopEnrollmentDAO;
import com.example.danceschool.dao.LatinoEnrollmentDAO;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

/**
 * Repository for enrollment stored procedures using DAO pattern.
 */
@Repository
public class EnrollmentProcedureRepository {

    private final BaletEnrollmentDAO baletDAO;
    private final HiphopEnrollmentDAO hiphopDAO;
    private final LatinoEnrollmentDAO latinoDAO;

    public EnrollmentProcedureRepository(BaletEnrollmentDAO baletDAO, 
                                        HiphopEnrollmentDAO hiphopDAO,
                                        LatinoEnrollmentDAO latinoDAO) {
        this.baletDAO = baletDAO;
        this.hiphopDAO = hiphopDAO;
        this.latinoDAO = latinoDAO;
    }

    /**
     * Call stored procedure to enroll user in a class
     */
    public EnrollmentResult enrollInClass(Integer korisnikId, String tipCasa, Integer instruktorId) {
        try {
            String result;
            switch (tipCasa.toLowerCase()) {
                case "balet":
                    result = baletDAO.prijaviNaBalet(korisnikId.longValue(), instruktorId.longValue(), null);
                    break;
                case "hiphop":
                    result = hiphopDAO.prijaviNaHiphop(korisnikId.longValue(), instruktorId.longValue(), null);
                    break;
                case "latino":
                    result = latinoDAO.prijaviNaLatino(korisnikId.longValue(), instruktorId.longValue(), null);
                    break;
                default:
                    return new EnrollmentResult(false, "Nepoznat tip časa: " + tipCasa);
            }
            
            boolean uspeh = result != null && result.contains("uspešno");
            return new EnrollmentResult(uspeh, result);
        } catch (SQLException e) {
            return new EnrollmentResult(false, "Greška: " + e.getMessage());
        }
    }

    /**
     * Call stored procedure to unenroll user from a class
     * Note: Not implemented in DAO layer yet
     */
    public EnrollmentResult unenrollFromClass(Integer korisnikId, String tipCasa) {
        return new EnrollmentResult(false, "Odjava nije još implementirana");
    }

    /**
     * Result class for enrollment operations
     */
    public static class EnrollmentResult {
        private final Boolean success;
        private final String message;

        public EnrollmentResult(Boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public Boolean isSuccess() {
            return success != null && success;
        }

        public String getMessage() {
            return message;
        }
    }
}
