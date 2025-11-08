package com.example.danceschool.model;

import java.time.LocalDateTime;

/**
 * Model rasporeda - Čist POJO bez anotacija za bazu podataka.
 * Predstavlja unos u rasporedu časova plesa.
 */
public class Raspored {
    
    private Long id;
    private String tip_casa; // 'balet', 'hiphop', 'latino'
    private Long instruktor_id;
    private LocalDateTime datum_vreme;
    private Integer trajanje_min = 60;
    private String lokacija;
    private Integer maksimalno_polaznika = 15;
    private String opis;
    private Long kreirao_id;
    private LocalDateTime datum_kreiranja;
    
    public Raspored() {
        this.datum_kreiranja = LocalDateTime.now();
    }
    
    // Getteri i Setteri
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTip_casa() {
        return tip_casa;
    }
    
    public void setTip_casa(String tip_casa) {
        this.tip_casa = tip_casa;
    }
    
    public Long getInstruktor_id() {
        return instruktor_id;
    }
    
    public void setInstruktor_id(Long instruktor_id) {
        this.instruktor_id = instruktor_id;
    }
    
    public LocalDateTime getDatum_vreme() {
        return datum_vreme;
    }
    
    public void setDatum_vreme(LocalDateTime datum_vreme) {
        this.datum_vreme = datum_vreme;
    }
    
    public Integer getTrajanje_min() {
        return trajanje_min;
    }
    
    public void setTrajanje_min(Integer trajanje_min) {
        this.trajanje_min = trajanje_min;
    }
    
    public String getLokacija() {
        return lokacija;
    }
    
    public void setLokacija(String lokacija) {
        this.lokacija = lokacija;
    }
    
    public Integer getMaksimalno_polaznika() {
        return maksimalno_polaznika;
    }
    
    public void setMaksimalno_polaznika(Integer maksimalno_polaznika) {
        this.maksimalno_polaznika = maksimalno_polaznika;
    }
    
    public String getOpis() {
        return opis;
    }
    
    public void setOpis(String opis) {
        this.opis = opis;
    }
    
    public Long getKreirao_id() {
        return kreirao_id;
    }
    
    public void setKreirao_id(Long kreirao_id) {
        this.kreirao_id = kreirao_id;
    }
    
    public LocalDateTime getDatum_kreiranja() {
        return datum_kreiranja;
    }
    
    public void setDatum_kreiranja(LocalDateTime datum_kreiranja) {
        this.datum_kreiranja = datum_kreiranja;
    }
}
