package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.Raspored;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom row mapper for Raspored to handle database column name to Java field mapping.
 * Maps snake_case database columns to camelCase Java fields.
 */
public class RasporedRowMapper implements ResultSetHandler<List<Raspored>> {
    
    @Override
    public List<Raspored> handle(ResultSet rs) throws SQLException {
        List<Raspored> results = new ArrayList<>();
        while (rs.next()) {
            Raspored raspored = new Raspored();
            raspored.setId(rs.getLong("id"));
            raspored.setTipCasa(rs.getString("tip_casa"));
            raspored.setInstruktorId(rs.getLong("instruktor_id"));
            
            Timestamp datumVreme = rs.getTimestamp("datum_vreme");
            if (datumVreme != null) {
                raspored.setDatumVreme(datumVreme.toLocalDateTime());
            }
            
            raspored.setTrajanjeMin(rs.getInt("trajanje_min"));
            raspored.setLokacija(rs.getString("lokacija"));
            raspored.setMaksimalnoPolaznika(rs.getInt("maksimalno_polaznika"));
            raspored.setOpis(rs.getString("opis"));
            raspored.setKeiraoId(rs.getLong("kreirao_id"));
            
            Timestamp datumKreiranja = rs.getTimestamp("datum_kreiranja");
            if (datumKreiranja != null) {
                raspored.setDatumKreiranja(datumKreiranja.toLocalDateTime());
            }
            
            results.add(raspored);
        }
        return results;
    }
    
    public static class SingleRowMapper implements ResultSetHandler<Raspored> {
        @Override
        public Raspored handle(ResultSet rs) throws SQLException {
            if (rs.next()) {
                Raspored raspored = new Raspored();
                raspored.setId(rs.getLong("id"));
                raspored.setTipCasa(rs.getString("tip_casa"));
                raspored.setInstruktorId(rs.getLong("instruktor_id"));
                
                Timestamp datumVreme = rs.getTimestamp("datum_vreme");
                if (datumVreme != null) {
                    raspored.setDatumVreme(datumVreme.toLocalDateTime());
                }
                
                raspored.setTrajanjeMin(rs.getInt("trajanje_min"));
                raspored.setLokacija(rs.getString("lokacija"));
                raspored.setMaksimalnoPolaznika(rs.getInt("maksimalno_polaznika"));
                raspored.setOpis(rs.getString("opis"));
                raspored.setKeiraoId(rs.getLong("kreirao_id"));
                
                Timestamp datumKreiranja = rs.getTimestamp("datum_kreiranja");
                if (datumKreiranja != null) {
                    raspored.setDatumKreiranja(datumKreiranja.toLocalDateTime());
                }
                
                return raspored;
            }
            return null;
        }
    }
}
