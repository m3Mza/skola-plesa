package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.Upis;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Bazni maper koji preslikava redove iz baze podataka u Upis objekte.
 */
public class MaperRedovaUpisa<T extends Upis> implements ResultSetHandler<List<T>> {
    
    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> upisi = new ArrayList<>();
        while (rs.next()) {
            upisi.add((T) mapuj_upis(rs));
        }
        return upisi;
    }
    
    /**
     * Mapira jedan red Result seta u Upis objekat.
     */
    protected Upis mapuj_upis(ResultSet rs) throws SQLException {
        Upis upis = kreiraj_novi_upis();
        upis.setId(rs.getLong("id"));
        upis.setKorisnik_id(rs.getLong("korisnik_id"));
        upis.setInstruktor_id(rs.getLong("instruktor_id"));
        
        // Konvertuj datum_prijave iz baze
        if (rs.getTimestamp("datum_prijave") != null) {
            upis.setDatum_prijave(rs.getTimestamp("datum_prijave").toLocalDateTime());
        }
        
        upis.setNapomena(rs.getString("napomena"));
        
        return upis;
    }
    
    /**
     * Metoda koju naslednici mogu pregaziti da kreiraju odgovarajući tip upisa.
     */
    protected Upis kreiraj_novi_upis() {
        // Implementacija u specificnim mapperima
        return null;
    }
    
    /**
     * Handler za vraćanje jednog upisa.
     */
    public static class JedanRed<T extends Upis> implements ResultSetHandler<T> {
        private final MaperRedovaUpisa<T> maper;
        
        public JedanRed(MaperRedovaUpisa<T> maper) {
            this.maper = maper;
        }
        
        @Override
        public T handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                return (T) maper.mapuj_upis(rs);
            }
            return null;
        }
    }
}
