package com.example.danceschool.controller;

import com.example.danceschool.viewmodel.HomeViewModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        // Build HomeViewModel with session data
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        boolean isLoggedIn = username != null;
        
        HomeViewModel viewModel = new HomeViewModel(username, role, isLoggedIn);
        model.addAttribute("viewModel", viewModel);
        
        return "index";
    }
}
