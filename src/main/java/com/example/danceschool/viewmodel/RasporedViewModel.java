package com.example.danceschool.viewmodel;

import com.example.danceschool.model.Raspored;
import java.util.List;

public class RasporedViewModel {
    private String username;
    private String role;
    private boolean isLoggedIn;
    private boolean isInstructor;
    private boolean isLearner;
    private Long userId;
    
    private List<Raspored> scheduleItems;

    public RasporedViewModel() {
    }

    public RasporedViewModel(String username, String role, boolean isLoggedIn, Long userId, List<Raspored> scheduleItems) {
        this.username = username;
        this.role = role;
        this.isLoggedIn = isLoggedIn;
        this.userId = userId;
        this.scheduleItems = scheduleItems;
        this.isInstructor = "instruktor".equalsIgnoreCase(role);
        this.isLearner = "ucenik".equalsIgnoreCase(role);
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
        this.isInstructor = "instruktor".equalsIgnoreCase(role);
        this.isLearner = "ucenik".equalsIgnoreCase(role);
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public boolean isInstructor() {
        return isInstructor;
    }

    public boolean isLearner() {
        return isLearner;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Raspored> getScheduleItems() {
        return scheduleItems;
    }

    public void setScheduleItems(List<Raspored> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }
}
