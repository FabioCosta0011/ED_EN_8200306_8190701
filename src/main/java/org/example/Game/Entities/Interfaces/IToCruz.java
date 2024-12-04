package org.example.Game.Entities.Interfaces;

import org.example.Game.Entities.Enemy;
import org.example.Game.Entities.Item;
import org.example.Structures.Interfaces.ListADT;
import org.example.Structures.Interfaces.StackADT;

/**
 * Interface representing the contract for ToCruz entities.
 */
public interface IToCruz {

    /**
     * Consumes a bulletproof vest.
     *
     * @param vest the bulletproof vest to consume.
     */
    void consumeBulletProofVest(Item vest);

    /**
     * Gets the health kits of the ToCruz entity.
     *
     * @return the health kits of the ToCruz entity.
     */
    StackADT<Item> getHealthKits();

    /**
     * Checks if the ToCruz entity is using a bulletproof vest.
     *
     * @return true if the ToCruz entity is using a bulletproof vest, false otherwise.
     */
    boolean isUsingBulletProofVest();

    /**
     * Attacks all the enemies at the same time.
     *
     * @param enemies the list of enemies to attack.
     */
    void attackEnemies(ListADT<Enemy> enemies);

    /**
     * Uses a health kit from the top of the bag
     */
    void useHealthKit();

}
