package org.example.Game.Entities.Interfaces;

/**
 * Interface representing the contract for Character entities.
 */
public interface ICharacter {

    /**
     * Gets the name of the Character.
     *
     * @return the name of the Character.
     */
    String getName();

    /**
     * Gets the power level of the Character.
     *
     * @return the power level of the Character.
     */
    int getPower();

    /**
     * Sets the power level of the Character.
     *
     */
    void setPower(int power);

    /**
     * Gets the division where the Character is located.
     *
     * @return the division of the Character.
     */
    IDivision getCurrentDivision();

    /**
     * Sets the division where the Character is located.
     *
     */
    void setCurrentDivision(IDivision division);
}
