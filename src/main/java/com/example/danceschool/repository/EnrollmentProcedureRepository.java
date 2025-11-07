package com.example.danceschool.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class EnrollmentProcedureRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Call stored procedure to enroll user in a class
     */
    @Transactional
    public EnrollmentResult enrollInClass(Integer korisnikId, String tipCasa, Integer instruktorId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_prijavi_na_cas");
        
        query.registerStoredProcedureParameter("p_korisnik_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_tip_casa", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_instruktor_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_uspeh", Boolean.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_poruka", String.class, ParameterMode.OUT);
        
        query.setParameter("p_korisnik_id", korisnikId);
        query.setParameter("p_tip_casa", tipCasa);
        query.setParameter("p_instruktor_id", instruktorId);
        
        query.execute();
        
        Boolean uspeh = (Boolean) query.getOutputParameterValue("p_uspeh");
        String poruka = (String) query.getOutputParameterValue("p_poruka");
        
        return new EnrollmentResult(uspeh, poruka);
    }

    /**
     * Call stored procedure to unenroll user from a class
     */
    @Transactional
    public EnrollmentResult unenrollFromClass(Integer korisnikId, String tipCasa) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_odjavi_sa_casa");
        
        query.registerStoredProcedureParameter("p_korisnik_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_tip_casa", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_uspeh", Boolean.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_poruka", String.class, ParameterMode.OUT);
        
        query.setParameter("p_korisnik_id", korisnikId);
        query.setParameter("p_tip_casa", tipCasa);
        
        query.execute();
        
        Boolean uspeh = (Boolean) query.getOutputParameterValue("p_uspeh");
        String poruka = (String) query.getOutputParameterValue("p_poruka");
        
        return new EnrollmentResult(uspeh, poruka);
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
