package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.ICharacter;
import org.example.Game.Entities.Interfaces.IDivision;

/**
 * The Character class represents a character in the game, encapsulating attributes such as the character's name,
 * power, and the division they belong to.
 */
public class Character implements ICharacter {

    /**
     * The name of the character.
     */
    private final String name;

    /**
     * The power level of the character, indicating their strength.
     */
    private int power;

    /**
     * The division the character belongs to.
     */
    private IDivision currentDivision;

    /**
     * Constructor to create a new Character with the given name, power, and division.
     *
     * @param name The name of the character.
     * @param power The power of the character.
     * @param currentDivision The division that the character belongs to.
     */
    public Character(String name, int power, IDivision currentDivision) {
        this.name = name;
        this.power = power;
        this.currentDivision = currentDivision;
    }

    /**
     * Gets the name of the character.
     *
     * @return The name of the character.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the power of the character.
     *
     * @return The power level of the character.
     */
    @Override
    public int getPower() {
        return power;
    }

    /**
     * Sets the power of the character.
     *
     * @param power The new power value for the character.
     */
    @Override
    public void setPower(int power) {
        this.power = power;
    }

    /**
     * Gets the current division the character belongs to.
     *
     * @return The current division of the character.
     */
    @Override
    public IDivision getCurrentDivision() {
        return currentDivision;
    }

    /**
     * Sets the division the character belongs to.
     *
     * @param division The new division to assign to the character.
     */
    @Override
    public void setCurrentDivision(IDivision division) {
        this.currentDivision = division;
    }
}
