package com.example.danceschool.viewmodel;

public class ClassesViewModel {
    private String username;
    private String role;
    private boolean isLoggedIn;
    private boolean isInstructor;
    private boolean isLearner;
    private Long userId;
    
    // Enrollment status for each class
    private boolean enrolledBalet;
    private boolean enrolledHiphop;
    private boolean enrolledLatino;

    public ClassesViewModel() {
    }

    public ClassesViewModel(String username, String role, boolean isLoggedIn, Long userId) {
        this.username = username;
        this.role = role;
        this.isLoggedIn = isLoggedIn;
        this.userId = userId;
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

    public boolean isEnrolledBalet() {
        return enrolledBalet;
    }

    public void setEnrolledBalet(boolean enrolledBalet) {
        this.enrolledBalet = enrolledBalet;
    }

    public boolean isEnrolledHiphop() {
        return enrolledHiphop;
    }

    public void setEnrolledHiphop(boolean enrolledHiphop) {
        this.enrolledHiphop = enrolledHiphop;
    }

    public boolean isEnrolledLatino() {
        return enrolledLatino;
    }

    public void setEnrolledLatino(boolean enrolledLatino) {
        this.enrolledLatino = enrolledLatino;
    }
}
