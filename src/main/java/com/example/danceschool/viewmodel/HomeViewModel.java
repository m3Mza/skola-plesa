package com.example.danceschool.viewmodel;

public class HomeViewModel {
    private String username;
    private String role;
    private boolean isLoggedIn;
    private boolean isInstructor;
    private boolean isLearner;

    public HomeViewModel() {
    }

    public HomeViewModel(String username, String role, boolean isLoggedIn) {
        this.username = username;
        this.role = role;
        this.isLoggedIn = isLoggedIn;
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
}
