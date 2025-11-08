package com.example.danceschool.controller;

import com.example.danceschool.model.*;
import com.example.danceschool.service.RasporedService;
import com.example.danceschool.service.KorisnikServis;
import com.example.danceschool.viewmodel.RasporedViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/raspored")
public class RasporedController {
    
    @Autowired
    private RasporedService rasporedService;
    
    @Autowired
    private KorisnikServis korisnikServis;
    
    @Value("${app.naziv:Škola plesa}")
    private String nazivSkole;
    
    @GetMapping
    public String showRaspored(Model model, RedirectAttributes redirectAttributes) {
        
        // Get authenticated user from Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth == null || !auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            redirectAttributes.addFlashAttribute("error", "Morate biti prijavljeni da biste pristupili rasporedu.");
            return "redirect:/login";
        }
        
        String email = auth.getName();
        Optional<Korisnik> korisnikOpt = korisnikServis.pronadji_korisnika_po_emailu(email);
        
        if (!korisnikOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Korisnik nije pronađen.");
            return "redirect:/login";
        }
        
        Korisnik korisnik = korisnikOpt.get();
        User loggedInUser = new User(korisnik);
        
        // Get schedule data
        List<Raspored> casovi = rasporedService.getScheduleForUser(loggedInUser);
        
        // Build RasporedViewModel
        RasporedViewModel viewModel = new RasporedViewModel(
            loggedInUser.getFullName(), 
            korisnik.getUloga(), 
            true, 
            korisnik.getId(), 
            casovi
        );
        
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("user", loggedInUser);
        
        return "raspored";
    }
    
    // Kreiranje novog časa (samo za instruktore)
    @PostMapping("/add")
    public String addRaspored(@ModelAttribute Raspored raspored,
                             RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Nemate dozvolu za ovu akciju.");
            return "redirect:/login";
        }
        
        Optional<Korisnik> korisnikOpt = korisnikServis.pronadji_korisnika_po_emailu(auth.getName());
        if (!korisnikOpt.isPresent() || !korisnikOpt.get().je_instruktor()) {
            redirectAttributes.addFlashAttribute("error", "Nemate dozvolu za ovu akciju.");
            return "redirect:/raspored";
        }
        
        User loggedInUser = new User(korisnikOpt.get());
        
        try {
            rasporedService.addSchedule(loggedInUser, raspored.getTip_casa(), 
                                       raspored.getDatum_vreme(), raspored.getTrajanje_min(),
                                       raspored.getLokacija(), raspored.getOpis(), loggedInUser.getId());
            redirectAttributes.addFlashAttribute("success", "Čas uspešno dodat u raspored!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri dodavanju časa.");
            e.printStackTrace();
        }
        
        return "redirect:/raspored";
    }
    
    // Brisanje časa (samo za instruktore)
    @PostMapping("/delete/{id}")
    public String deleteRaspored(@PathVariable Long id,
                                RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Nemate dozvolu za ovu akciju.");
            return "redirect:/login";
        }
        
        Optional<Korisnik> korisnikOpt = korisnikServis.pronadji_korisnika_po_emailu(auth.getName());
        if (!korisnikOpt.isPresent() || !korisnikOpt.get().je_instruktor()) {
            redirectAttributes.addFlashAttribute("error", "Nemate dozvolu za ovu akciju.");
            return "redirect:/raspored";
        }
        
        User loggedInUser = new User(korisnikOpt.get());
        
        try {
            rasporedService.deleteSchedule(loggedInUser, id);
            redirectAttributes.addFlashAttribute("success", "Čas uspešno obrisan iz rasporeda!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri brisanju časa.");
            e.printStackTrace();
        }
        
        return "redirect:/raspored";
    }
    
    // Izmena časa (samo za instruktore)
    @PostMapping("/edit")
    public String editRaspored(@RequestParam("rasporedId") Long rasporedId,
                              @RequestParam("tip_casa") String tip_casa,
                              @RequestParam("datum_vreme") String datum_vreme,
                              @RequestParam("trajanje_min") Integer trajanje_min,
                              @RequestParam("lokacija") String lokacija,
                              @RequestParam("maksimalno_polaznika") Integer maksimalno_polaznika,
                              @RequestParam("opis") String opis,
                              RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Nemate dozvolu za ovu akciju.");
            return "redirect:/login";
        }
        
        Optional<Korisnik> korisnikOpt = korisnikServis.pronadji_korisnika_po_emailu(auth.getName());
        if (!korisnikOpt.isPresent() || !korisnikOpt.get().je_instruktor()) {
            redirectAttributes.addFlashAttribute("error", "Nemate dozvolu za ovu akciju.");
            return "redirect:/raspored";
        }
        
        User loggedInUser = new User(korisnikOpt.get());
        
        try {
            rasporedService.updateSchedule(loggedInUser, rasporedId, tip_casa, datum_vreme, 
                                          trajanje_min, lokacija, maksimalno_polaznika, opis);
            redirectAttributes.addFlashAttribute("success", "Čas uspešno ažuriran!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri ažuriranju časa.");
            e.printStackTrace();
        }
        
        return "redirect:/raspored";
    }
}
