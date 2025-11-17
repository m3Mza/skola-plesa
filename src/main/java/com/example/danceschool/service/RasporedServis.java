package com.example.danceschool.service;

import com.example.danceschool.model.Korisnik;
import com.example.danceschool.model.Raspored;
import com.example.danceschool.repository.RasporedRepozitorijum;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servis za upravljanje rasporedom časova.
 * Sadrži poslovnu logiku za kreiranje, brisanje i pregled rasporeda.
 */
@Service
public class RasporedServis {
    
    private final RasporedRepozitorijum raspored_repozitorijum;
    private final UpisServis upis_servis;
    
    public RasporedServis(RasporedRepozitorijum raspored_repozitorijum,
                          UpisServis upis_servis) {
        this.raspored_repozitorijum = raspored_repozitorijum;
        this.upis_servis = upis_servis;
    }
    
    /**
     * Vraća raspored za određenog korisnika.
     * Instruktori vide sve rasporede, učenici samo rasporede za časove na koje su upisani.
     */
    public List<Raspored> dobavi_raspored_za_korisnika(Korisnik korisnik) {
        List<Raspored> svi_rasporedi = raspored_repozitorijum.pronadji_sve();
        
        System.out.println("=== DEBUG RASPORED SERVIS ===");
        System.out.println("Ukupno učitano iz baze: " + svi_rasporedi.size() + " rasporeda");
        for (Raspored r : svi_rasporedi) {
            System.out.println("  - ID: " + r.getId() + ", Tip: " + r.getTip_casa() +
                             ", Datum: " + r.getDatum_vreme() + ", Instruktor ID: " + r.getInstruktor_id());
        }
        
        System.out.println("DEBUG: Uloga korisnika = '" + korisnik.getUloga() + "'");
        System.out.println("DEBUG: je_instruktor() = " + korisnik.je_instruktor());
        System.out.println("DEBUG: je_ucenik() = " + korisnik.je_ucenik());
        
        if (korisnik.je_instruktor()) {
            System.out.println("Korisnik JE instruktor, vraćam SVE rasporede: " + svi_rasporedi.size());
            System.out.println("=============================");
            return svi_rasporedi;
        }
        
        if (korisnik.je_ucenik()) {
            List<Raspored> filtrirani_rasporedi = svi_rasporedi.stream()
                .filter(raspored -> {
                    try {
                        boolean upisan = upis_servis.je_upisan(korisnik, raspored.getTip_casa());
                        System.out.println("  Proveravam " + raspored.getTip_casa() + " upis: " + upisan);
                        return upisan;
                    } catch (Exception e) {
                        System.err.println("GRESKA pri proveri upisa: " + e.getMessage());
                        return false;
                    }
                })
                .collect(Collectors.toList());
                
            System.out.println("Korisnik JE učenik, vraćam filtrirane rasporede: " + filtrirani_rasporedi.size());
            System.out.println("=============================");
            return filtrirani_rasporedi;
        }
        
        System.out.println("Korisnik NIJE ni instruktor ni učenik, vraćam sve: " + svi_rasporedi.size());
        System.out.println("=============================");
        return svi_rasporedi;
    }
    
    /**
     * Dodaje novi raspored.
     */
    public Raspored dodaj_raspored(Korisnik korisnik, String tip_casa, LocalDateTime datum_vreme,
                                   Integer trajanje_min, String lokacija, Integer maksimalno_polaznika, String opis) {
        if (!korisnik.je_instruktor()) {
            throw new RuntimeException("Samo instruktori mogu kreirati raspored");
        }
        
        if (trajanje_min == null || trajanje_min <= 0) {
            trajanje_min = 60;
        }
        
        if (maksimalno_polaznika == null || maksimalno_polaznika <= 0) {
            maksimalno_polaznika = 15;
        }
        
        Raspored raspored = new Raspored();
        raspored.setTip_casa(tip_casa);
        raspored.setInstruktor_id(korisnik.getId());
        raspored.setDatum_vreme(datum_vreme);
        raspored.setTrajanje_min(trajanje_min);
        raspored.setLokacija((lokacija != null) ? lokacija.trim() : "");
        raspored.setMaksimalno_polaznika(maksimalno_polaznika);
        raspored.setOpis((opis != null) ? opis.trim() : "");
        raspored.setKreirao_id(korisnik.getId());
        
        String rezultat = raspored_repozitorijum.dodaj_raspored(raspored);
        
        System.out.println("DEBUG: Rezultat dodavanja rasporeda: " + rezultat);
        
        if (rezultat.startsWith("USPEH")) {
            try {
                String id_str = rezultat.substring(rezultat.indexOf("ID:") + 3).trim();
                Long novi_id = Long.parseLong(id_str);
                return raspored_repozitorijum.pronadji_po_id(novi_id).orElse(null);
            } catch (Exception e) {
                System.err.println("GRESKA pri parsiranju ID-a: " + e.getMessage());
            }
        }
        
        return null;
    }
    
    /**
     * Briše raspored.
     */
    public String obrisi_raspored(Korisnik korisnik, Long raspored_id) {
        if (!korisnik.je_instruktor()) {
            return "GRESKA: Samo instruktori mogu brisati raspored";
        }
        
        String rezultat = raspored_repozitorijum.obrisi_raspored(raspored_id, korisnik.getId());
        
        System.out.println("DEBUG: Rezultat brisanja rasporeda: " + rezultat);
        
        return rezultat;
    }
    
    /**
     * Ažurira postojeći raspored.
     */
    public Raspored azuriraj_raspored(Long korisnik_id, Long raspored_id, String tip_casa, LocalDateTime datum_vreme,
                                      Integer trajanje_min, String lokacija, Integer maksimalno_polaznika, String opis) {
        if (trajanje_min == null || trajanje_min <= 0) {
            trajanje_min = 60;
        }
        
        if (maksimalno_polaznika == null || maksimalno_polaznika <= 0) {
            maksimalno_polaznika = 15;
        }
        
        // Pronađi postojeći raspored
        Raspored raspored = raspored_repozitorijum.pronadji_po_id(raspored_id)
            .orElseThrow(() -> new RuntimeException("Raspored nije pronađen"));
        
        // Ažuriraj vrednosti
        raspored.setTip_casa(tip_casa);
        raspored.setDatum_vreme(datum_vreme);
        raspored.setTrajanje_min(trajanje_min);
        raspored.setLokacija((lokacija != null) ? lokacija.trim() : "");
        raspored.setMaksimalno_polaznika(maksimalno_polaznika);
        raspored.setOpis((opis != null) ? opis.trim() : "");
        
        // Sačuvaj izmene pomoću stored procedure
        String rezultat = raspored_repozitorijum.azuriraj_raspored(raspored, korisnik_id);
        
        System.out.println("DEBUG: Rezultat ažuriranja rasporeda: " + rezultat);
        
        if (rezultat.startsWith("USPEH")) {
            return raspored_repozitorijum.pronadji_po_id(raspored_id).orElse(null);
        } else {
            throw new RuntimeException(rezultat.replace("GRESKA: ", ""));
        }
    }

    
    /**
     * Vraća sve rasporede.
     */
    public List<Raspored> dobavi_sve_rasporede() {
        return raspored_repozitorijum.pronadji_sve();
    }
}
