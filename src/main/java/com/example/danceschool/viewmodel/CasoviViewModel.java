package com.example.danceschool.viewmodel;

/**
 * ViewModel za stranicu sa časovima.
 */
public class CasoviViewModel {
    private String ime_korisnika;
    private boolean upisan_balet;
    private boolean upisan_hiphop;
    private boolean upisan_latino;
    
    public CasoviViewModel() {
        this.upisan_balet = false;
        this.upisan_hiphop = false;
        this.upisan_latino = false;
    }
    
    public String getIme_korisnika() {
        return ime_korisnika;
    }
    
    public void setIme_korisnika(String ime_korisnika) {
        this.ime_korisnika = ime_korisnika;
    }
    
    public boolean isUpisan_balet() {
        return upisan_balet;
    }
    
    public void setUpisan_balet(boolean upisan_balet) {
        this.upisan_balet = upisan_balet;
    }
    
    public boolean isUpisan_hiphop() {
        return upisan_hiphop;
    }
    
    public void setUpisan_hiphop(boolean upisan_hiphop) {
        this.upisan_hiphop = upisan_hiphop;
    }
    
    public boolean isUpisan_latino() {
        return upisan_latino;
    }
    
    public void setUpisan_latino(boolean upisan_latino) {
        this.upisan_latino = upisan_latino;
    }
}
