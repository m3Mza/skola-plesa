package com.example.danceschool.service;

import com.example.danceschool.model.Korisnik;
import com.example.danceschool.repository.UpisRepozitorijum;
import org.springframework.stereotype.Service;

/**
 * Servis za upravljanje prijava i odjava za casove
 */
@Service
public class UpisServis {
    
    private static final Long PODRAZUMEVANI_INSTRUKTOR_ID = 2L;
    
    private final UpisRepozitorijum upis_repozitorijum;
    
    public UpisServis(UpisRepozitorijum upis_repozitorijum) {
        this.upis_repozitorijum = upis_repozitorijum;
    }
    
    /**
     * Prijavljuje korisnika na određeni tip časa.
     */
    public String prijavi_korisnika(Korisnik korisnik, String tip_casa) {
        System.out.println("DEBUG: Prijavljujem korisnika " + korisnik.getId() + " na " + tip_casa);
        
        try {
            String rezultat;
            
            switch (tip_casa.toLowerCase()) {
                case "balet":
                    System.out.println("DEBUG: Pozivam prijavi_na_balet");
                    rezultat = upis_repozitorijum.prijavi_na_balet(korisnik.getId(), PODRAZUMEVANI_INSTRUKTOR_ID);
                    break;
                    
                case "hiphop":
                    System.out.println("DEBUG: Pozivam prijavi_na_hiphop");
                    rezultat = upis_repozitorijum.prijavi_na_hiphop(korisnik.getId(), PODRAZUMEVANI_INSTRUKTOR_ID);
                    break;
                    
                case "latino":
                    System.out.println("DEBUG: Pozivam prijavi_na_latino");
                    rezultat = upis_repozitorijum.prijavi_na_latino(korisnik.getId(), PODRAZUMEVANI_INSTRUKTOR_ID);
                    break;
                    
                default:
                    return "GRESKA: Nepoznat tip časa: " + tip_casa;
            }
            
            System.out.println("DEBUG: Rezultat prijave: " + rezultat);
            return rezultat;
            
        } catch (Exception e) {
            System.err.println("GRESKA prilikom prijave: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom prijave na čas", e);
        }
    }
    
    /**
     * Odjavljuje korisnika sa određenog tipa časa.
     */
    public String odjavi_korisnika(Korisnik korisnik, String tip_casa) {
        System.out.println("DEBUG: Odjavljujem korisnika " + korisnik.getId() + " sa " + tip_casa);
        
        try {
            String rezultat;
            
            switch (tip_casa.toLowerCase()) {
                case "balet":
                    System.out.println("DEBUG: Pozivam odjavi_sa_baleta");
                    rezultat = upis_repozitorijum.odjavi_sa_baleta(korisnik.getId());
                    break;
                    
                case "hiphop":
                    System.out.println("DEBUG: Pozivam odjavi_sa_hiphopa");
                    rezultat = upis_repozitorijum.odjavi_sa_hiphopa(korisnik.getId());
                    break;
                    
                case "latino":
                    System.out.println("DEBUG: Pozivam odjavi_sa_latina");
                    rezultat = upis_repozitorijum.odjavi_sa_latina(korisnik.getId());
                    break;
                    
                default:
                    return "GRESKA: Nepoznat tip časa: " + tip_casa;
            }
            
            System.out.println("DEBUG: Rezultat odjave: " + rezultat);
            return rezultat;
            
        } catch (Exception e) {
            System.err.println("GRESKA prilikom odjave: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Greška prilikom odjave sa časa", e);
        }
    }
    
    /**
     * Proverava da li je korisnik upisan na određeni tip časa.
     */
    public boolean je_upisan(Korisnik korisnik, String tip_casa) {
        try {
            switch (tip_casa.toLowerCase()) {
                case "balet":
                    return !upis_repozitorijum.pronadji_balet_po_korisniku(korisnik.getId()).isEmpty();
                case "hiphop":
                    return !upis_repozitorijum.pronadji_hiphop_po_korisniku(korisnik.getId()).isEmpty();
                case "latino":
                    return !upis_repozitorijum.pronadji_latino_po_korisniku(korisnik.getId()).isEmpty();
                default:
                    return false;
            }
        } catch (Exception e) {
            System.err.println("GRESKA pri proveri upisa: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Vraća status upisa za korisnika (da li je upisan na svaki tip časa).
     */
    public java.util.Map<String, Boolean> dobavi_status_upisa(Korisnik korisnik) {
        System.out.println("DEBUG: Dobavljam status upisa za korisnika " + korisnik.getId());
        
        java.util.Map<String, Boolean> status = new java.util.HashMap<>();
        
        try {
            java.util.List<?> balet_lista = upis_repozitorijum.pronadji_balet_po_korisniku(korisnik.getId());
            System.out.println("DEBUG: Balet upisi: " + balet_lista.size());
            status.put("balet", !balet_lista.isEmpty());
            
            java.util.List<?> hiphop_lista = upis_repozitorijum.pronadji_hiphop_po_korisniku(korisnik.getId());
            System.out.println("DEBUG: Hiphop upisi: " + hiphop_lista.size());
            status.put("hiphop", !hiphop_lista.isEmpty());
            
            java.util.List<?> latino_lista = upis_repozitorijum.pronadji_latino_po_korisniku(korisnik.getId());
            System.out.println("DEBUG: Latino upisi: " + latino_lista.size());
            status.put("latino", !latino_lista.isEmpty());
            
            System.out.println("DEBUG: Status upisa - Balet: " + status.get("balet") + 
                             ", Hiphop: " + status.get("hiphop") + 
                             ", Latino: " + status.get("latino"));
            
        } catch (Exception e) {
            System.err.println("GRESKA pri dobavljanju statusa upisa: " + e.getMessage());
            e.printStackTrace();
            status.put("balet", false);
            status.put("hiphop", false);
            status.put("latino", false);
        }
        
        return status;
    }
}
