package com.example.danceschool.viewmodel;

import com.example.danceschool.model.Raspored;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel za stranicu sa rasporedom.
 */
public class RasporedViewModel {
    private String ime_korisnika;
    private String username;
    private String role;
    private boolean instruktor;
    private boolean learner;
    private boolean instructor;
    private boolean loggedIn;
    private Long userId;
    private List<Raspored> rasporedi;
    private List<Raspored> scheduleItems;
    
    public RasporedViewModel() {
        this.instruktor = false;
        this.instructor = false;
        this.learner = true;
        this.loggedIn = false;
        this.rasporedi = new ArrayList<>();
        this.scheduleItems = new ArrayList<>();
    }
    
    public RasporedViewModel(String username, String role, boolean loggedIn, Long userId, List<Raspored> casovi) {
        this.username = username;
        this.role = role;
        this.loggedIn = loggedIn;
        this.userId = userId;
        this.instructor = "instruktor".equals(role);
        this.instruktor = this.instructor;
        this.learner = !this.instructor;
        this.rasporedi = casovi != null ? casovi : new ArrayList<>();
        this.scheduleItems = this.rasporedi;
        this.ime_korisnika = username;
    }
    
    public String getIme_korisnika() {
        return ime_korisnika;
    }
    
    public void setIme_korisnika(String ime_korisnika) {
        this.ime_korisnika = ime_korisnika;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public boolean isInstruktor() {
        return instruktor;
    }
    
    public void setInstruktor(boolean instruktor) {
        this.instruktor = instruktor;
        this.instructor = instruktor;
    }
    
    public boolean isInstructor() {
        return instructor;
    }
    
    public void setInstructor(boolean instructor) {
        this.instructor = instructor;
        this.instruktor = instructor;
    }
    
    public boolean isLearner() {
        return learner;
    }
    
    public void setLearner(boolean learner) {
        this.learner = learner;
    }
    
    public boolean isLoggedIn() {
        return loggedIn;
    }
    
    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public List<Raspored> getRasporedi() {
        return rasporedi;
    }
    
    public void setRasporedi(List<Raspored> rasporedi) {
        this.rasporedi = rasporedi;
        this.scheduleItems = rasporedi;
    }
    
    public List<Raspored> getScheduleItems() {
        return scheduleItems;
    }
    
    public void setScheduleItems(List<Raspored> scheduleItems) {
        this.scheduleItems = scheduleItems;
        this.rasporedi = scheduleItems;
    }
}
