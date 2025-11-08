package com.example.danceschool.viewmodel;

/**
 * ViewModel za početnu stranicu.
 */
public class PocetnaViewModel {
    private String ime_korisnika;
    private boolean prijavljen;
    private boolean instruktor;
    
    public PocetnaViewModel() {
        this.prijavljen = false;
        this.instruktor = false;
    }
    
    public String getIme_korisnika() {
        return ime_korisnika;
    }
    
    public void setIme_korisnika(String ime_korisnika) {
        this.ime_korisnika = ime_korisnika;
    }
    
    public boolean isPrijavljen() {
        return prijavljen;
    }
    
    public void setPrijavljen(boolean prijavljen) {
        this.prijavljen = prijavljen;
    }
    
    public boolean isInstruktor() {
        return instruktor;
    }
    
    public void setInstruktor(boolean instruktor) {
        this.instruktor = instruktor;
    }
}
