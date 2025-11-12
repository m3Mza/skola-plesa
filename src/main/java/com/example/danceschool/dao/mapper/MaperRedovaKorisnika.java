package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.Korisnik;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Maper koji preslikava redove iz baze podataka u Korisnik objekte.
 */
public class MaperRedovaKorisnika implements ResultSetHandler<List<Korisnik>> {
    
    @Override
    public List<Korisnik> handle(ResultSet rs) throws SQLException {
        List<Korisnik> korisnici = new ArrayList<>();
        while (rs.next()) {
            korisnici.add(mapuj_korisnika(rs));
        }
        return korisnici;
    }
    
    /**
     * Mapira jedan red Result seta u Korisnik objekat.
     */
    private Korisnik mapuj_korisnika(ResultSet rs) throws SQLException {
        Korisnik korisnik = new Korisnik();
        korisnik.setId(rs.getLong("id"));
        korisnik.setIme(rs.getString("ime"));
        korisnik.setPrezime(rs.getString("prezime"));
        korisnik.setEmail(rs.getString("email"));
        korisnik.setLozinka(rs.getString("lozinka"));
        korisnik.setTelefon(rs.getString("telefon"));
        
        // Konvertuj datum_registracije iz baze
        if (rs.getTimestamp("datum_registracije") != null) {
            korisnik.setDatum_registracije(rs.getTimestamp("datum_registracije").toLocalDateTime());
        }
        
        korisnik.setUloga(rs.getString("uloga"));
        korisnik.setAktivan(rs.getBoolean("aktivan"));
        
        return korisnik;
    }
    
    /**
     * Handler za vraćanje jednog korisnika.
     */
    public static class JedanRed implements ResultSetHandler<Korisnik> {
        @Override
        public Korisnik handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                return new MaperRedovaKorisnika().mapuj_korisnika(rs);
            }
            return null;
        }
    }
}
