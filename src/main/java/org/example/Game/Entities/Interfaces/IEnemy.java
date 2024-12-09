package org.example.Game.Entities.Interfaces;

import org.example.Game.Entities.ToCruz;

/**
 * Interface representing the contract for Enemy entities.
 */
public interface IEnemy extends ICharacter {

    /**
     * Attacks the ToCruz character.
     *
     * @param toCruz the ToCruz character to attack.
     */
    void attackToCruz(ToCruz toCruz);

    /**
     * Gets the current division (location) of the enemy character in the game.
     *
     * @return the division where the enemy character is currently located.
     */
    IDivision getCurrentDivision();

    /**
     * Sets the current division (location) of the enemy character in the game.
     *
     * @param currentDivision the division where the enemy character is currently located.
     */
    void setCurrentDivision(IDivision currentDivision);
}
