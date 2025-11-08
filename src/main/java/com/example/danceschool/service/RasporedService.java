package com.example.danceschool.service;

import com.example.danceschool.model.Korisnik;
import com.example.danceschool.model.Raspored;
import com.example.danceschool.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Service wrapper for Raspored operations using new model classes
 */
@Service
public class RasporedService {
    
    private final RasporedServis rasporedServis;
    private final KorisnikServis korisnikServis;
    
    public RasporedService(RasporedServis rasporedServis, KorisnikServis korisnikServis) {
        this.rasporedServis = rasporedServis;
        this.korisnikServis = korisnikServis;
    }
    
    /**
     * Get schedule for a user
     */
    public List<Raspored> getScheduleForUser(User user) {
        Korisnik korisnik = korisnikServis.pronadji_korisnika_po_emailu(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        return rasporedServis.dobavi_raspored_za_korisnika(korisnik);
    }
    
    /**
     * Add new schedule
     */
    public void addSchedule(User user, String tipCasa, LocalDateTime datumVreme, Integer trajanjeMin,
                           String lokacija, String opis, Long kreiraoId) {
        Korisnik korisnik = korisnikServis.pronadji_korisnika_po_emailu(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        
        rasporedServis.dodaj_raspored(korisnik, tipCasa, datumVreme, trajanjeMin, lokacija, 15, opis);
    }
    
    /**
     * Delete schedule
     */
    public void deleteSchedule(User user, Long rasporedId) {
        Korisnik korisnik = korisnikServis.pronadji_korisnika_po_emailu(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        
        String result = rasporedServis.obrisi_raspored(korisnik, rasporedId);
        if (!result.startsWith("USPEH")) {
            throw new RuntimeException(result);
        }
    }
    
    /**
     * Update schedule using stored procedure sp_izmeni_cas
     */
    public void updateSchedule(User user, Long rasporedId, String tipCasa, String datumVremeStr,
                              Integer trajanjeMin, String lokacija, Integer maksimalnoPolaznika, String opis) {
        Korisnik korisnik = korisnikServis.pronadji_korisnika_po_emailu(user.getEmail())
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen"));
        
        // Parse datetime string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime datumVreme = LocalDateTime.parse(datumVremeStr, formatter);
        
        Raspored updatedRaspored = rasporedServis.azuriraj_raspored(
                korisnik.getId(), rasporedId, tipCasa, datumVreme, 
                trajanjeMin, lokacija, maksimalnoPolaznika, opis);
        
        if (updatedRaspored == null) {
            throw new RuntimeException("Greška pri ažuriranju rasporeda");
        }
    }
}
