package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IEnemy;

public class Enemy implements IEnemy {
    private String name;
    private int power;
    private IDivision division;

    public Enemy(String name, int power, IDivision division) {
        this.name = name;
        this.power = power;
        this.division = division;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPower() {
        return power;
    }

    @Override
    public IDivision getDivision() {
        return division;
    }
}
