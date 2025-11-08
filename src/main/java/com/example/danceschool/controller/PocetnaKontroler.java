package com.example.danceschool.controller;

import com.example.danceschool.model.Korisnik;
import com.example.danceschool.service.KorisnikServis;
import com.example.danceschool.viewmodel.PocetnaViewModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

/**
 * Kontroler za početnu stranicu.
 */
@Controller
public class PocetnaKontroler {
    
    private final KorisnikServis korisnik_servis;
    
    public PocetnaKontroler(KorisnikServis korisnik_servis) {
        this.korisnik_servis = korisnik_servis;
    }
    
    /**
     * Prikazuje početnu stranicu.
     */
    @GetMapping({"/", "/index"})
    public String prikaziPocetnu(Model model) {
        PocetnaViewModel view_model = new PocetnaViewModel();
        
        // Preuzmi trenutno prijavljenog korisnika
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && 
            !auth.getName().equals("anonymousUser")) {
            
            String email = auth.getName();
            Optional<Korisnik> korisnik_opt = korisnik_servis.pronadji_korisnika_po_emailu(email);
            
            if (korisnik_opt.isPresent()) {
                Korisnik korisnik = korisnik_opt.get();
                view_model.setIme_korisnika(korisnik.getPuno_ime());
                view_model.setPrijavljen(true);
                view_model.setInstruktor(korisnik.je_instruktor());
                
                System.out.println("DEBUG: Korisnik prijavljen - " + korisnik.getPuno_ime() +
                                 " (uloga: " + korisnik.getUloga() + ")");
            }
        } else {
            view_model.setPrijavljen(false);
            System.out.println("DEBUG: Korisnik nije prijavljen");
        }
        
        model.addAttribute("homeViewModel", view_model);
        return "index";
    }
}
