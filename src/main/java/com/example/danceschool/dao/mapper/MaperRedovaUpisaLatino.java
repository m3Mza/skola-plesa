package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.Upis;
import com.example.danceschool.model.UpisLatino;

/**
 * Maper koji preslikava redove iz tabele latino u UpisLatino objekte.
 */
public class MaperRedovaUpisaLatino extends MaperRedovaUpisa<UpisLatino> {
    
    @Override
    protected Upis kreiraj_novi_upis() {
        return new UpisLatino();
    }
}
