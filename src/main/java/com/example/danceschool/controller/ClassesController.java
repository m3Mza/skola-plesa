package com.example.danceschool.controller;

import com.example.danceschool.model.User;
import com.example.danceschool.service.EnrollmentService;
import com.example.danceschool.viewmodel.ClassesViewModel;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ClassesController {
    
    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/classes")
    public String classes(Model model, HttpSession session) {
        // Get session data
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("role");
        boolean isLoggedIn = username != null;
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Long userId = loggedInUser != null ? loggedInUser.getId() : null;
        
        // Build ClassesViewModel
        ClassesViewModel viewModel = new ClassesViewModel(username, role, isLoggedIn, userId);
        
        // Get enrollment status if user is a learner
        if (loggedInUser != null && loggedInUser.isUcenik()) {
            EnrollmentService.EnrollmentStatus status = enrollmentService.getEnrollmentStatus(loggedInUser);
            viewModel.setEnrolledBalet(status.isEnrolledBalet());
            viewModel.setEnrolledHiphop(status.isEnrolledHiphop());
            viewModel.setEnrolledLatino(status.isEnrolledLatino());
        }
        
        model.addAttribute("viewModel", viewModel);
        return "classes";
    }
}