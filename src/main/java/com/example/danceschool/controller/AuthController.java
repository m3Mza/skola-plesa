package com.example.danceschool.controller;

import com.example.danceschool.model.User;
import com.example.danceschool.service.UserService;
import com.example.danceschool.viewmodel.LoginViewModel;
import com.example.danceschool.viewmodel.RegisterViewModel;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Value("${app.naziv:Škola plesa}")
    private String nazivSkole;
    
    // Prikaz login forme
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        LoginViewModel viewModel = new LoginViewModel();
        model.addAttribute("viewModel", viewModel);
        return "login";
    }
    
    // Obrada login zahteva
    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                              @RequestParam String lozinka,
                              HttpSession session,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        
        try {
            User user = userService.authenticate(email, lozinka);
            // Uspešna prijava
            session.setAttribute("loggedInUser", user);
            session.setAttribute("username", user.getPunoIme());
            session.setAttribute("role", user.getUloga());
            redirectAttributes.addFlashAttribute("success", "Dobrodošli, " + user.getPunoIme() + "!");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            LoginViewModel viewModel = new LoginViewModel("Neispravni email ili lozinka.");
            model.addAttribute("viewModel", viewModel);
            return "login";
        }
    }
    
    // Prikaz registracione forme
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        RegisterViewModel viewModel = new RegisterViewModel(null, null);
        model.addAttribute("viewModel", viewModel);
        return "register";
    }
    
    // Obrada registracije
    @PostMapping("/register")
    public String processRegister(@Valid @ModelAttribute("user") User user,
                                  BindingResult bindingResult,
                                  @RequestParam String lozinkaPotvrda,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        
        // Provera grešaka validacije
        if (bindingResult.hasErrors()) {
            RegisterViewModel viewModel = new RegisterViewModel("Molimo ispravite greške u formi.", null);
            model.addAttribute("viewModel", viewModel);
            return "register";
        }
        
        // Provera da li lozinke odgovaraju
        if (!user.getLozinka().equals(lozinkaPotvrda)) {
            RegisterViewModel viewModel = new RegisterViewModel("Lozinke se ne poklapaju.", null);
            model.addAttribute("viewModel", viewModel);
            return "register";
        }
        
        try {
            userService.registerUser(user.getIme(), user.getPrezime(), user.getEmail(), 
                                    user.getLozinka(), user.getTelefon());
            
            redirectAttributes.addFlashAttribute("success", "Uspešno ste se registrovali! Molimo prijavite se.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            RegisterViewModel viewModel = new RegisterViewModel(e.getMessage(), null);
            model.addAttribute("viewModel", viewModel);
            return "register";
        } catch (Exception e) {
            RegisterViewModel viewModel = new RegisterViewModel("Greška pri registraciji. Pokušajte ponovo.", null);
            model.addAttribute("viewModel", viewModel);
            return "register";
        }
    }
    
    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Uspešno ste se odjavili.");
        return "redirect:/";
    }
}
