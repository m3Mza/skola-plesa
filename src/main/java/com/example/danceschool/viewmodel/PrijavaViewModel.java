package com.example.danceschool.viewmodel;

/**
 * ViewModel za stranicu za prijavu.
 */
public class PrijavaViewModel {
    private String email;
    private String lozinka;
    
    public PrijavaViewModel() {}
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getLozinka() {
        return lozinka;
    }
    
    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}
