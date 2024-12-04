package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.ICharacter;
import org.example.Game.Entities.Interfaces.IDivision;

public class Character implements ICharacter {

    private String name;

    private int power;

    private int health;

    private IDivision currentDivision;

    public Character(String name, int power, int health, IDivision currentDivision) {
        this.name = name;
        this.power = power;
        this.health = health;
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
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public IDivision getCurrentDivision() {
        return currentDivision;
    }
}
