package com.example.danceschool.controller;

import com.example.danceschool.model.Korisnik;
import com.example.danceschool.service.KorisnikServis;
import com.example.danceschool.service.UpisServis;
import com.example.danceschool.viewmodel.CasoviViewModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;
import java.util.Optional;

/**
 * Kontroler za prijavu i odjavu sa casova (Korisnik)
 */
@Controller
public class CasoviKontroler {
    
    private final KorisnikServis korisnik_servis;
    private final UpisServis upis_servis;
    
    public CasoviKontroler(KorisnikServis korisnik_servis, UpisServis upis_servis) {
        this.korisnik_servis = korisnik_servis;
        this.upis_servis = upis_servis;
    }
    
    /**
     * Prikazuje stranicu sa časovima.
     */
    @GetMapping("/classes")
    public String prikaziCasove(Model model) {
        CasoviViewModel view_model = new CasoviViewModel();
        
        // Preuzmi trenutno prijavljenog korisnika
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.isAuthenticated() && 
            !auth.getName().equals("anonymousUser")) {
            
            String email = auth.getName();
            Optional<Korisnik> korisnik_opt = korisnik_servis.pronadji_korisnika_po_emailu(email);
            
            if (korisnik_opt.isPresent()) {
                Korisnik korisnik = korisnik_opt.get();
                view_model.setIme_korisnika(korisnik.getPuno_ime());
                
                // Dobavlja upise za sve casove
                Map<String, Boolean> status_upisa = upis_servis.dobavi_status_upisa(korisnik);
                view_model.setUpisan_balet(status_upisa.getOrDefault("balet", false));
                view_model.setUpisan_hiphop(status_upisa.getOrDefault("hiphop", false));
                view_model.setUpisan_latino(status_upisa.getOrDefault("latino", false));
                
                System.out.println("DEBUG: Status upisa - Balet: " + view_model.isUpisan_balet() +
                                 ", Hiphop: " + view_model.isUpisan_hiphop() +
                                 ", Latino: " + view_model.isUpisan_latino());
            }
        }
        
        model.addAttribute("classesViewModel", view_model);
        return "classes";
    }
    
    /**
     * Obrađuje prijavu korisnika na čas.
     */
    @PostMapping("/classes/enroll")
    public String prijaviNaCas(@RequestParam("tip_casa") String tip_casa,
                               RedirectAttributes redirect_attributes) {
        try {
            // Preuzmi trenutno prijavljenog korisnika
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Optional<Korisnik> korisnik_opt = korisnik_servis.pronadji_korisnika_po_emailu(email);
            
            if (!korisnik_opt.isPresent()) {
                redirect_attributes.addFlashAttribute("greska", "Korisnik nije pronađen");
                return "redirect:/classes";
            }
            
            Korisnik korisnik = korisnik_opt.get();
            
            // Pozovi servis za prijavu
            String rezultat = upis_servis.prijavi_korisnika(korisnik, tip_casa);
            
            if (rezultat.startsWith("USPEH")) {
                redirect_attributes.addFlashAttribute("uspeh", 
                    "Uspešno ste se prijavili na " + tip_casa + " čas!");
            } else {
                redirect_attributes.addFlashAttribute("greska", rezultat);
            }
            
        } catch (Exception e) {
            System.err.println("GREŠKA pri prijavi na čas: " + e.getMessage());
            e.printStackTrace();
            redirect_attributes.addFlashAttribute("greska", 
                "Došlo je do greške prilikom prijave");
        }
        
        return "redirect:/classes";
    }
    
    /**
     * Obrađuje odjavu korisnika sa časa.
     */
    @PostMapping("/classes/unenroll")
    public String odjaviSaCasa(@RequestParam("tip_casa") String tip_casa,
                               RedirectAttributes redirect_attributes) {
        try {
            // Preuzmi trenutno prijavljenog korisnika
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Optional<Korisnik> korisnik_opt = korisnik_servis.pronadji_korisnika_po_emailu(email);
            
            if (!korisnik_opt.isPresent()) {
                redirect_attributes.addFlashAttribute("greska", "Korisnik nije pronađen");
                return "redirect:/classes";
            }
            
            Korisnik korisnik = korisnik_opt.get();
            
            // Pozovi servis za odjavu
            String rezultat = upis_servis.odjavi_korisnika(korisnik, tip_casa);
            
            if (rezultat.startsWith("USPEH")) {
                redirect_attributes.addFlashAttribute("uspeh", 
                    "Uspešno ste se odjavili sa " + tip_casa + " časa!");
            } else {
                redirect_attributes.addFlashAttribute("greska", rezultat);
            }
            
        } catch (Exception e) {
            System.err.println("GREŠKA pri odjavi sa časa: " + e.getMessage());
            e.printStackTrace();
            redirect_attributes.addFlashAttribute("greska", 
                "Došlo je do greške prilikom odjave");
        }
        
        return "redirect:/classes";
    }
}
