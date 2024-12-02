package org.example.Game.Entities.Interfaces;

/**
 * Interface representing the contract for Enemy entities.
 */
public interface IEnemy {
    /**
     * Gets the name of the enemy.
     *
     * @return the name of the enemy.
     */
    String getName();

    /**
     * Gets the power level of the enemy.
     *
     * @return the power level of the enemy.
     */
    int getPower();

    /**
     * Gets the division where the enemy is located.
     *
     * @return the division of the enemy.
     */
    IDivision getDivision();
}
