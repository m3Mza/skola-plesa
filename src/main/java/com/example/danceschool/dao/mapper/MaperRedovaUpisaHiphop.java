package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.Upis;
import com.example.danceschool.model.UpisHiphop;

/**
 * Maper koji preslikava redove iz tabele hiphop u UpisHiphop objekte.
 */
public class MaperRedovaUpisaHiphop extends MaperRedovaUpisa<UpisHiphop> {
    
    @Override
    protected Upis kreiraj_novi_upis() {
        return new UpisHiphop();
    }
}
