package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IEnemy;

public class Enemy extends Character implements IEnemy {

    public Enemy(String name, int power, IDivision currentDivision) {
        super(name, power, currentDivision);
    }

    @Override
    public void attackToCruz(ToCruz toCruz) {
        toCruz.setHealth(toCruz.getHealth() - this.getPower());
    }

    @Override
    public IDivision getCurrentDivision() {
        return super.getCurrentDivision();
    }

    @Override
    public void setCurrentDivision(IDivision currentDivision) {
        super.setCurrentDivision(currentDivision);
    }
}
