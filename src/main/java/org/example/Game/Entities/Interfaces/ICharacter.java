package org.example.Game.Entities.Interfaces;

/**
 * Interface representing the contract for Character entities.
 */
public interface ICharacter {

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
     * Gets the health level of the enemy.
     *
     * @return the health level of the enemy.
     */
    int getHealth();

    /**
     * Sets the health level of the enemy.
     *
     * @param health the health level to set.
     */
    void setHealth(int health);

    /**
     * Gets the division where the enemy is located.
     *
     * @return the division of the enemy.
     */
    IDivision getCurrentDivision();
}
