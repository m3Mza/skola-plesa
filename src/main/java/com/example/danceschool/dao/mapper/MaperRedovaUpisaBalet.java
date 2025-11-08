package com.example.danceschool.dao.mapper;

import com.example.danceschool.model.Upis;
import com.example.danceschool.model.UpisBalet;

/**
 * Maper koji preslikava redove iz tabele balet u UpisBalet objekte.
 */
public class MaperRedovaUpisaBalet extends MaperRedovaUpisa<UpisBalet> {
    
    @Override
    protected Upis kreiraj_novi_upis() {
        return new UpisBalet();
    }
}
