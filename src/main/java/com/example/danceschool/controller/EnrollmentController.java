package com.example.danceschool.controller;

import com.example.danceschool.model.*;
import com.example.danceschool.service.EnrollmentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/enrollment")
public class EnrollmentController {
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @PostMapping("/enroll/{classType}")
    public String enrollInClass(@PathVariable String classType,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Morate biti prijavljeni da biste se prijavili na časove.");
            return "redirect:/login";
        }
        
        try {
            enrollmentService.enrollUser(loggedInUser, classType);
            redirectAttributes.addFlashAttribute("success", "Uspešno ste se prijavili na čas!");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Already enrolled")) {
                redirectAttributes.addFlashAttribute("info", "Već ste prijavljeni na ovaj čas.");
            } else {
                redirectAttributes.addFlashAttribute("error", e.getMessage());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri prijavi. Pokušajte ponovo.");
            e.printStackTrace();
        }
        
        return "redirect:/classes";
    }
    
    @PostMapping("/unenroll/{classType}")
    public String unenrollFromClass(@PathVariable String classType,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Morate biti prijavljeni.");
            return "redirect:/login";
        }
        
        try {
            enrollmentService.unenrollUser(loggedInUser, classType);
            redirectAttributes.addFlashAttribute("success", "Uspešno ste se odjavili sa časa.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Greška pri odjavi. Pokušajte ponovo.");
            e.printStackTrace();
        }
        
        return "redirect:/classes";
    }
}
