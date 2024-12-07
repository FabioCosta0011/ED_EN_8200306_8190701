package org.example.Game.Entities.Interfaces;

import org.example.Game.Entities.Enemy;
import org.example.Game.Entities.Item;
import org.example.Structures.Interfaces.ListADT;
import org.example.Structures.Interfaces.StackADT;

/**
 * Interface representing the contract for the ToCruz entities.
 *
 * The ToCruz is a character with health, power, health kits, and the ability to interact with enemies, items, and bulletproof vests.
 * This interface defines the operations that can be performed on a ToCruz character, including attacking enemies, using health kits, and consuming bulletproof vests.
 * Additionally, it provides methods for managing the health status and the current division the ToCruz is located in.
 */
public interface IToCruz {

    /**
     * Gets the current health of the ToCruz character.
     *
     * @return the current health value of the ToCruz character.
     */
    int getHealth();

    /**
     * Gets the name of the ToCruz character.
     *
     * @return the name of the ToCruz character.
     */
    String getName();

    /**
     * Gets the power level of the ToCruz character.
     *
     * @return the power value of the ToCruz character.
     */
    int getPower();

    /**
     * Gets the maximum number of health kits that the ToCruz character can carry.
     *
     * @return the maximum number of health kits.
     */
    int getMaxHealthKits();

    /**
     * Sets the maximum number of health kits that the ToCruz character can carry.
     *
     * @param maxHealthKits the maximum number of health kits.
     */
    void setMaxHealthKits(int maxHealthKits);

    /**
     * Sets the current health value of the ToCruz character.
     *
     * @param health the new health value for the ToCruz character.
     */
    void setHealth(int health);

    /**
     * Consumes a bulletproof vest, which provides protection to the ToCruz character.
     *
     * @param vest the bulletproof vest to be consumed.
     */
    void consumeBulletProofVest(Item vest);

    /**
     * Gets the stack of health kits that the ToCruz character currently has.
     *
     * @return a stack containing the health kits.
     */
    StackADT<Item> getHealthKits();

    /**
     * Checks if the ToCruz character is currently using a bulletproof vest.
     *
     * @return true if the ToCruz character is using a bulletproof vest, false otherwise.
     */
    boolean isUsingBulletProofVest();

    /**
     * Attacks all the enemies in the given list at the same time.
     * This method assumes that the ToCruz character will use its power and attack all enemies in one action.
     *
     * @param enemies the list of enemies to be attacked by the ToCruz character.
     */
    void attackEnemies(ListADT<Enemy> enemies);

    /**
     * Uses the health kit from the top of the health kit stack.
     * The health kit will be consumed, and the health of the ToCruz character will be restored based on the kit's properties.
     */
    void useHealthKit();

    /**
     * Gets the current division (location) of the ToCruz character in the game.
     *
     * @return the division where the ToCruz character is currently located.
     */
    IDivision getCurrentDivision();

}
