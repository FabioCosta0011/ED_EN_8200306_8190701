package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IEnemy;
import org.example.Game.Entities.Interfaces.IToCruz;

/**
 * The Enemy class represents an enemy character in the game.
 * It extends the Character class and implements the IEnemy interface.
 * This class provides functionality for attacking a "ToCruz" object and modifying the current division of the enemy.
 */
public class Enemy extends Character implements IEnemy {

    /**
     * Constructor to create an enemy with the specified name, power, and current division.
     *
     * @param name The name of the enemy.
     * @param power The power level of the enemy.
     * @param currentDivision The division the enemy belongs to.
     */
    public Enemy(String name, int power, IDivision currentDivision) {
        super(name, power, currentDivision);
    }

    /**
     * Attacks the specified ToCruz object by reducing its health based on the enemy's power.
     *
     * @param toCruz The ToCruz object that will be attacked.
     */
    @Override
    public void attackToCruz(IToCruz toCruz) {
        toCruz.setHealth(toCruz.getHealth() - this.getPower());
    }

    /**
     * Gets the current division of the enemy.
     *
     * @return The current division of the enemy.
     */
    @Override
    public IDivision getCurrentDivision() {
        return super.getCurrentDivision();
    }

    /**
     * Sets the current division of the enemy.
     *
     * @param currentDivision The new division to set for the enemy.
     */
    @Override
    public void setCurrentDivision(IDivision currentDivision) {
        super.setCurrentDivision(currentDivision);
    }
}
