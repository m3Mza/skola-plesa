package com.example.danceschool.dao;

import com.example.danceschool.dao.mapper.MaperRedovaKorisnika;
import com.example.danceschool.model.Korisnik;
import org.apache.commons.dbutils.QueryRunner;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * DAO objekat za pristup podacima korisnika.
 * Razdvaja tehnologiju baze podataka (SQL, DBUtils) od poslovne logike.
 */
@Repository
public class KorisnikDAO {
    
    private final QueryRunner query_runner;
    
    public KorisnikDAO(QueryRunner query_runner) {
        this.query_runner = query_runner;
    }
    
    /**
     * Pronalazi sve korisnike u bazi podataka.
     */
    public List<Korisnik> pronadji_sve() throws SQLException {
        String sql = "SELECT * FROM korisnici";
        return query_runner.query(sql, new MaperRedovaKorisnika());
    }
    
    /**
     * Pronalazi korisnika po ID-u.
     */
    public Optional<Korisnik> pronadji_po_id(Long id) throws SQLException {
        String sql = "SELECT * FROM korisnici WHERE id = ?";
        Korisnik korisnik = query_runner.query(sql, new MaperRedovaKorisnika.JedanRed(), id);
        return Optional.ofNullable(korisnik);
    }
    
    /**
     * Pronalazi korisnika po email adresi.
     */
    public Optional<Korisnik> pronadji_po_emailu(String email) throws SQLException {
        String sql = "SELECT * FROM korisnici WHERE email = ?";
        Korisnik korisnik = query_runner.query(sql, new MaperRedovaKorisnika.JedanRed(), email);
        return Optional.ofNullable(korisnik);
    }
    
    /**
     * Čuva korisnika (insert ili update).
     */
    public Korisnik sacuvaj(Korisnik korisnik) throws SQLException {
        if (korisnik.getId() == null) {
            return umetni(korisnik);
        } else {
            return azuriraj(korisnik);
        }
    }
    
    /**
     * Umeće novog korisnika u bazu.
     */
    private Korisnik umetni(Korisnik korisnik) throws SQLException {
        String sql = "INSERT INTO korisnici (ime, prezime, email, lozinka, telefon, datum_registracije, uloga, aktivan) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        query_runner.update(sql, 
            korisnik.getIme(),
            korisnik.getPrezime(), 
            korisnik.getEmail(),
            korisnik.getLozinka(),
            korisnik.getTelefon(),
            korisnik.getDatum_registracije(),
            korisnik.getUloga(),
            korisnik.getAktivan()
        );
        
        // Preuzmi generisan ID
        String last_id_sql = "SELECT LAST_INSERT_ID()";
        Long id = query_runner.query(last_id_sql, rs -> {
            if (rs.next()) {
                return rs.getLong(1);
            }
            return null;
        });
        
        korisnik.setId(id);
        return korisnik;
    }
    
    /**
     * Ažurira postojećeg korisnika.
     */
    private Korisnik azuriraj(Korisnik korisnik) throws SQLException {
        String sql = "UPDATE korisnici SET ime = ?, prezime = ?, email = ?, lozinka = ?, " +
                     "telefon = ?, uloga = ?, aktivan = ? WHERE id = ?";
        
        query_runner.update(sql,
            korisnik.getIme(),
            korisnik.getPrezime(),
            korisnik.getEmail(),
            korisnik.getLozinka(),
            korisnik.getTelefon(),
            korisnik.getUloga(),
            korisnik.getAktivan(),
            korisnik.getId()
        );
        
        return korisnik;
    }
    
    /**
     * Briše korisnika iz baze.
     */
    public void obrisi(Long id) throws SQLException {
        String sql = "DELETE FROM korisnici WHERE id = ?";
        query_runner.update(sql, id);
    }
}
