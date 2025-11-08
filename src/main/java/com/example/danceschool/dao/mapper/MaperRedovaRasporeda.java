package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.Raspored;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Maper koji preslikava redove iz baze podataka u Raspored objekte.
 * Rukuje mapiranjem snake_case (baza) -> camelCase (Java) konverzijom.
 */
public class MaperRedovaRasporeda implements ResultSetHandler<List<Raspored>> {
    
    @Override
    public List<Raspored> handle(ResultSet rs) throws SQLException {
        List<Raspored> rasporedi = new ArrayList<>();
        while (rs.next()) {
            rasporedi.add(mapuj_raspored(rs));
        }
        return rasporedi;
    }
    
    /**
     * Mapira jedan red Result seta u Raspored objekat.
     */
    private Raspored mapuj_raspored(ResultSet rs) throws SQLException {
        Raspored raspored = new Raspored();
        raspored.setId(rs.getLong("id"));
        raspored.setTip_casa(rs.getString("tip_casa"));
        raspored.setInstruktor_id(rs.getLong("instruktor_id"));
        
        // Konvertuj datum_vreme iz baze
        if (rs.getTimestamp("datum_vreme") != null) {
            raspored.setDatum_vreme(rs.getTimestamp("datum_vreme").toLocalDateTime());
        }
        
        raspored.setTrajanje_min(rs.getInt("trajanje_min"));
        raspored.setLokacija(rs.getString("lokacija"));
        raspored.setMaksimalno_polaznika(rs.getInt("maksimalno_polaznika"));
        raspored.setOpis(rs.getString("opis"));
        
        Long kreirao_id = rs.getLong("kreirao_id");
        if (!rs.wasNull()) {
            raspored.setKreirao_id(kreirao_id);
        }
        
        // Konvertuj datum_kreiranja iz baze
        if (rs.getTimestamp("datum_kreiranja") != null) {
            raspored.setDatum_kreiranja(rs.getTimestamp("datum_kreiranja").toLocalDateTime());
        }
        
        return raspored;
    }
    
    /**
     * Handler za vraćanje jednog rasporeda.
     */
    public static class JedanRed implements ResultSetHandler<Raspored> {
        @Override
        public Raspored handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                return new MaperRedovaRasporeda().mapuj_raspored(rs);
            }
            return null;
        }
    }
}
