package com.example.danceschool.service;

import com.example.danceschool.repository.PrijavaRepozitorijum;
import org.springframework.stereotype.Service;

/**
 * Servis za upravljanje prijavama učenika na časove.
 * Sadrži poslovnu logiku za prijavu, odjavu i proveru prijave.
 */
@Service
public class PrijavaServis {
    
    private final PrijavaRepozitorijum prijava_repozitorijum;
    
    public PrijavaServis(PrijavaRepozitorijum prijava_repozitorijum) {
        this.prijava_repozitorijum = prijava_repozitorijum;
    }
    
    /**
     * Prijavljuje učenika na čas uz proveru kapaciteta.
     * 
     * @param raspored_id ID termina
     * @param korisnik_id ID učenika
     * @return Rezultat sa statusom i porukom
     */
    public RezultatPrijave prijavi_se(Long raspored_id, Long korisnik_id) {
        String odgovor = prijava_repozitorijum.prijavi_se(raspored_id, korisnik_id);
        
        boolean uspeh = odgovor.startsWith("USPEH:");
        String poruka = odgovor.substring(odgovor.indexOf(":") + 2); // Ukloni "USPEH: " ili "GRESKA: "
        
        return new RezultatPrijave(uspeh, poruka);
    }
    
    /**
     * Odjavljuje učenika sa časa.
     * 
     * @param raspored_id ID termina
     * @param korisnik_id ID učenika
     * @return Rezultat sa statusom i porukom
     */
    public RezultatPrijave odjavi_se(Long raspored_id, Long korisnik_id) {
        String odgovor = prijava_repozitorijum.odjavi_se(raspored_id, korisnik_id);
        
        boolean uspeh = odgovor.startsWith("USPEH:");
        String poruka = odgovor.substring(odgovor.indexOf(":") + 2);
        
        return new RezultatPrijave(uspeh, poruka);
    }
    
    /**
     * Proverava da li je učenik prijavljen na čas.
     * 
     * @param raspored_id ID termina
     * @param korisnik_id ID učenika
     * @return true ako je prijavljen, false ako nije
     */
    public boolean je_prijavljen(Long raspored_id, Long korisnik_id) {
        return prijava_repozitorijum.proveri_prijavu(raspored_id, korisnik_id);
    }
    
    /**
     * Pomoćna klasa za rezultat prijave/odjave.
     */
    public static class RezultatPrijave {
        private final boolean uspeh;
        private final String poruka;
        
        public RezultatPrijave(boolean uspeh, String poruka) {
            this.uspeh = uspeh;
            this.poruka = poruka;
        }
        
        public boolean isUspeh() {
            return uspeh;
        }
        
        public String getPoruka() {
            return poruka;
        }
    }
}
