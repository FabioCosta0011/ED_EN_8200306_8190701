package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.ICharacter;
import org.example.Game.Entities.Interfaces.IDivision;

public class Character implements ICharacter {

    private String name;

    private int power;

    private IDivision currentDivision;

    public Character(String name, int power, IDivision currentDivision) {
        this.name = name;
        this.power = power;
        this.currentDivision = currentDivision;
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
    public void setPower(int power) {
        this.power = power;
    }

    @Override
    public IDivision getCurrentDivision() {
        return currentDivision;
    }
}
