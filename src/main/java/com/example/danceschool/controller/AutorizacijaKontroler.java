package com.example.danceschool.controller;

import com.example.danceschool.model.Korisnik;
import com.example.danceschool.service.KorisnikServis;
import com.example.danceschool.viewmodel.PrijavaViewModel;
import com.example.danceschool.viewmodel.RegistracijaViewModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Kontroler za autorizaciju korisnika (prijava i registracija).
 */
@Controller
public class AutorizacijaKontroler {
    
    private final KorisnikServis korisnik_servis;
    
    public AutorizacijaKontroler(KorisnikServis korisnik_servis) {
        this.korisnik_servis = korisnik_servis;
    }
    
    /**
     * Prikazuje formu za prijavu.
     */
    @GetMapping("/login")
    public String prikaziPrijavu(Model model) {
        model.addAttribute("prijavaViewModel", new PrijavaViewModel());
        return "login";
    }
    
    /**
     * Prikazuje formu za registraciju.
     */
    @GetMapping("/register")
    public String prikaziRegistraciju(Model model) {
        model.addAttribute("registracijaViewModel", new RegistracijaViewModel());
        return "register";
    }
    
    /**
     * Obrađuje registraciju novog korisnika.
     */
    @PostMapping("/register")
    public String obradiRegistraciju(@ModelAttribute RegistracijaViewModel registracija_vm,
                                     RedirectAttributes redirect_attributes) {
        try {
            // Validacija ulaznih podataka
            if (registracija_vm.getIme() == null || registracija_vm.getIme().trim().isEmpty()) {
                redirect_attributes.addFlashAttribute("greska", "Ime je obavezno");
                return "redirect:/register";
            }
            
            if (registracija_vm.getPrezime() == null || registracija_vm.getPrezime().trim().isEmpty()) {
                redirect_attributes.addFlashAttribute("greska", "Prezime je obavezno");
                return "redirect:/register";
            }
            
            if (registracija_vm.getEmail() == null || registracija_vm.getEmail().trim().isEmpty()) {
                redirect_attributes.addFlashAttribute("greska", "Email je obavezan");
                return "redirect:/register";
            }
            
            if (registracija_vm.getLozinka() == null || registracija_vm.getLozinka().length() < 6) {
                redirect_attributes.addFlashAttribute("greska", "Lozinka mora imati najmanje 6 karaktera");
                return "redirect:/register";
            }
            
            // Kreiranje novog korisnika
            Korisnik novi_korisnik = new Korisnik();
            novi_korisnik.setIme(registracija_vm.getIme().trim());
            novi_korisnik.setPrezime(registracija_vm.getPrezime().trim());
            novi_korisnik.setEmail(registracija_vm.getEmail().trim().toLowerCase());
            novi_korisnik.setLozinka(registracija_vm.getLozinka());
            novi_korisnik.setTelefon(registracija_vm.getTelefon() != null ? 
                                    registracija_vm.getTelefon().trim() : "");
            novi_korisnik.setUloga("UCENIK");
            
            // Registracija kroz servis
            Korisnik registrovani = korisnik_servis.registruj_korisnika(novi_korisnik);
            
            if (registrovani != null) {
                redirect_attributes.addFlashAttribute("uspeh", 
                    "Uspešno ste se registrovali! Molimo prijavite se.");
                return "redirect:/login";
            } else {
                redirect_attributes.addFlashAttribute("greska", 
                    "Email adresa je već u upotrebi");
                return "redirect:/register";
            }
            
        } catch (Exception e) {
            System.err.println("GREŠKA pri registraciji: " + e.getMessage());
            e.printStackTrace();
            redirect_attributes.addFlashAttribute("greska", 
                "Došlo je do greške prilikom registracije");
            return "redirect:/register";
        }
    }
    
    /**
     * Odjavljuje korisnika i preusmerava na stranicu za prijavu.
     */
    @GetMapping("/logout")
    public String odjava() {
        SecurityContextHolder.clearContext();
        return "redirect:/login?logout";
    }
}
