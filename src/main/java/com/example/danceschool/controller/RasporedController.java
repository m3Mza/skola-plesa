package com.example.danceschool.controller;

import com.example.danceschool.model.*;
import com.example.danceschool.service.RasporedServis;
import com.example.danceschool.service.KorisnikServis;
import com.example.danceschool.service.PrijavaServis;
import com.example.danceschool.viewmodel.RasporedViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/raspored")
public class RasporedController {
    
    @Autowired
    private RasporedServis rasporedServis;
    
    @Autowired
    private KorisnikServis korisnikServis;
    
    @Autowired
    private PrijavaServis prijavaServis;
    
    @Value("${app.naziv:Škola plesa}")
    private String nazivSkole;
    
    @GetMapping
    public String showRaspored(Model model, RedirectAttributes redirectAttributes) {
        
        // autentikacija (da li je prijavljen)
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
        
        System.out.println("DEBUG: Korisnik prijavljen - " + korisnik.getIme() + " " + korisnik.getPrezime() + " (uloga: " + korisnik.getUloga() + ")");
        
        // Prikaz rasporeda
        List<Raspored> casovi = rasporedServis.dobavi_raspored_za_korisnika(korisnik);
        
        // Build RasporedViewModel
        RasporedViewModel viewModel = new RasporedViewModel(
            korisnik.getIme() + " " + korisnik.getPrezime(), 
            korisnik.getUloga(), 
            true, 
            korisnik.getId(), 
            casovi
        );
        
        model.addAttribute("viewModel", viewModel);
        
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
        
        Korisnik korisnik = korisnikOpt.get();
        
        try {
            rasporedServis.dodaj_raspored(korisnik, raspored.getTip_casa(), 
                                       raspored.getDatum_vreme(), raspored.getTrajanje_min(),
                                       raspored.getLokacija(), raspored.getMaksimalno_polaznika(), raspored.getOpis());
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
        
        Korisnik korisnik = korisnikOpt.get();
        
        try {
            rasporedServis.obrisi_raspored(korisnik, id);
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
        
        Korisnik korisnik = korisnikOpt.get();
        
        try {
            // Parse datum_vreme string to LocalDateTime
            LocalDateTime datumVremeObj = LocalDateTime.parse(datum_vreme, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            
            rasporedServis.azuriraj_raspored(korisnik.getId(), rasporedId, tip_casa, datumVremeObj, 
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
    
    // ========================================================================
    // REST API ENDPOINTS ZA PRIJAVU/ODJAVU NA ČAS
    // ========================================================================
    
    /**
     * REST endpoint za prijavu učenika na čas.
     * Proverava kapacitet i vraća JSON odgovor.
     */
    @PostMapping("/api/{id}/prijavi")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> prijaviNaCas(@PathVariable Long id) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        
        if (auth == null || !auth.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "Niste prijavljeni.");
            return ResponseEntity.status(401).body(response);
        }
        
        Optional<Korisnik> korisnikOpt = korisnikServis.pronadji_korisnika_po_emailu(auth.getName());
        if (!korisnikOpt.isPresent()) {
            response.put("success", false);
            response.put("message", "Korisnik nije pronađen.");
            return ResponseEntity.status(404).body(response);
        }
        
        Korisnik korisnik = korisnikOpt.get();
        
        try {
            PrijavaServis.RezultatPrijave rezultat = prijavaServis.prijavi_se(id, korisnik.getId());
            
            response.put("success", rezultat.isUspeh());
            response.put("message", rezultat.getPoruka());
            
            return rezultat.isUspeh() 
                ? ResponseEntity.ok(response) 
                : ResponseEntity.badRequest().body(response);
                
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Greška pri prijavi na čas: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * REST endpoint za odjavu učenika sa časa.
     */
    @PostMapping("/api/{id}/odjavi")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> odjaviSaCasa(@PathVariable Long id) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        
        if (auth == null || !auth.isAuthenticated()) {
            response.put("success", false);
            response.put("message", "Niste prijavljeni.");
            return ResponseEntity.status(401).body(response);
        }
        
        Optional<Korisnik> korisnikOpt = korisnikServis.pronadji_korisnika_po_emailu(auth.getName());
        if (!korisnikOpt.isPresent()) {
            response.put("success", false);
            response.put("message", "Korisnik nije pronađen.");
            return ResponseEntity.status(404).body(response);
        }
        
        Korisnik korisnik = korisnikOpt.get();
        
        try {
            PrijavaServis.RezultatPrijave rezultat = prijavaServis.odjavi_se(id, korisnik.getId());
            
            response.put("success", rezultat.isUspeh());
            response.put("message", rezultat.getPoruka());
            
            return rezultat.isUspeh() 
                ? ResponseEntity.ok(response) 
                : ResponseEntity.badRequest().body(response);
                
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Greška pri odjavi sa časa: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    
    /**
     * REST endpoint za proveru da li je učenik prijavljen na čas.
     */
    @GetMapping("/api/{id}/status")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> statusPrijave(@PathVariable Long id) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        
        if (auth == null || !auth.isAuthenticated()) {
            response.put("prijavljen", false);
            return ResponseEntity.ok(response);
        }
        
        Optional<Korisnik> korisnikOpt = korisnikServis.pronadji_korisnika_po_emailu(auth.getName());
        if (!korisnikOpt.isPresent()) {
            response.put("prijavljen", false);
            return ResponseEntity.ok(response);
        }
        
        Korisnik korisnik = korisnikOpt.get();
        
        try {
            boolean prijavljen = prijavaServis.je_prijavljen(id, korisnik.getId());
            response.put("prijavljen", prijavljen);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("prijavljen", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
