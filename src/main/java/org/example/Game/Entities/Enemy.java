package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IEnemy;

public class Enemy extends Character implements IEnemy {



    public Enemy(String name, int power, int health, IDivision currentDivision) {
        super(name, power, health, currentDivision);
    }

    @Override
    public void attackToCruz(ToCruz toCruz) {
        toCruz.setHealth(toCruz.getHealth() - this.getPower());
    }

}
