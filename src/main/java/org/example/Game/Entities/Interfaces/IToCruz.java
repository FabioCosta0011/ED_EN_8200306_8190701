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
     * Gets the health level of the To Cruz.
     *
     * @return the health level of the To Cruz.
     */
    int getHealth();

    /**
     * Sets the health level of the To Cruz.
     *
     * @param health the health level to set.
     */
    void setHealth(int health);

    /**
     *  Gets the target with To Cruz.
     *
     * @return the target with To Cruz.
     */
    ITarget getTarget();

    /**
     * Sets the target with To Cruz.
     *
     * @param target the target to set.
     */
    void setTarget(ITarget target);

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
