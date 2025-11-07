package com.example.danceschool.controller;

import com.example.danceschool.model.*;
import com.example.danceschool.service.RasporedService;
import com.example.danceschool.viewmodel.RasporedViewModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${app.naziv:Škola plesa}")
    private String nazivSkole;
    
    @GetMapping
    public String showRaspored(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Morate biti prijavljeni da biste pristupili rasporedu.");
            return "redirect:/login";
        }
        
        // Get session data
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        Long userId = loggedInUser.getId();
        
        // Get schedule data
        List<Raspored> casovi = rasporedService.getScheduleForUser(loggedInUser);
        
        // Build RasporedViewModel
        RasporedViewModel viewModel = new RasporedViewModel(username, role, true, userId, casovi);
        
        model.addAttribute("viewModel", viewModel);
        model.addAttribute("user", loggedInUser); // Keep for backward compatibility
        
        return "raspored";
    }
    
    // Kreiranje novog časa (samo za instruktore)
    @PostMapping("/add")
    public String addRaspored(@ModelAttribute Raspored raspored,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null || !loggedInUser.isInstruktor()) {
            redirectAttributes.addFlashAttribute("error", "Nemate dozvolu za ovu akciju.");
            return "redirect:/raspored";
        }
        
        try {
            rasporedService.addSchedule(loggedInUser, raspored.getTipCasa(), 
                                       raspored.getDatumVreme(), raspored.getTrajanjeMin(),
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
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null || !loggedInUser.isInstruktor()) {
            redirectAttributes.addFlashAttribute("error", "Nemate dozvolu za ovu akciju.");
            return "redirect:/raspored";
        }
        
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
}
