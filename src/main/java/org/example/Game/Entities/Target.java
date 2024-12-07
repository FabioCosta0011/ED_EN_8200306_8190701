package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.ITarget;

class Target implements ITarget {

    private IDivision division;

    private String type;

    public Target(IDivision division, String type) {
        this.division = division;
        this.type = type;
    }

    @Override
    public IDivision getDivision() {
        return division;
    }

    @Override
    public String getType() {
        return type;
    }
}