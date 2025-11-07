package com.example.danceschool.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public class RasporedProcedureRepository {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Call stored procedure to create schedule entry
     */
    @Transactional
    public RasporedResult createSchedule(String tipCasa, Integer instruktorId, LocalDateTime datumVreme,
                                        Integer trajanjeMin, String lokacija, String opis, Integer keiraoId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_kreiraj_raspored");
        
        query.registerStoredProcedureParameter("p_tip_casa", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_instruktor_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_datum_vreme", LocalDateTime.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_trajanje_min", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_lokacija", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_opis", String.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_kreirao_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_uspeh", Boolean.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_poruka", String.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_novi_id", Integer.class, ParameterMode.OUT);
        
        query.setParameter("p_tip_casa", tipCasa);
        query.setParameter("p_instruktor_id", instruktorId);
        query.setParameter("p_datum_vreme", datumVreme);
        query.setParameter("p_trajanje_min", trajanjeMin);
        query.setParameter("p_lokacija", lokacija);
        query.setParameter("p_opis", opis);
        query.setParameter("p_kreirao_id", keiraoId);
        
        query.execute();
        
        Boolean uspeh = (Boolean) query.getOutputParameterValue("p_uspeh");
        String poruka = (String) query.getOutputParameterValue("p_poruka");
        Integer noviId = (Integer) query.getOutputParameterValue("p_novi_id");
        
        return new RasporedResult(uspeh, poruka, noviId);
    }

    /**
     * Call stored procedure to delete schedule entry
     */
    @Transactional
    public RasporedResult deleteSchedule(Integer rasporedId, Integer korisnikId) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("sp_obrisi_raspored");
        
        query.registerStoredProcedureParameter("p_raspored_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_korisnik_id", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("p_uspeh", Boolean.class, ParameterMode.OUT);
        query.registerStoredProcedureParameter("p_poruka", String.class, ParameterMode.OUT);
        
        query.setParameter("p_raspored_id", rasporedId);
        query.setParameter("p_korisnik_id", korisnikId);
        
        query.execute();
        
        Boolean uspeh = (Boolean) query.getOutputParameterValue("p_uspeh");
        String poruka = (String) query.getOutputParameterValue("p_poruka");
        
        return new RasporedResult(uspeh, poruka, null);
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
