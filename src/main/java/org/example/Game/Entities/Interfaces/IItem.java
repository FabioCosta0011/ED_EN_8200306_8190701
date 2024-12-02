package org.example.Game.Entities.Interfaces;

import org.example.Game.Entities.ENUMS.ItemType;

/**
 * Interface representing the contract for Item entities.
 */
public interface IItem {
    /**
     * Gets the division where the item is located.
     *
     * @return the division of the item.
     */
    IDivision getDivision();


    /**
     * Gets the recovery points of the item (if applicable).
     *
     * @return the recovery points of the item.
     */
    Integer getRecoveryPoints();


    /**
     * Gets the extra points of the item (if applicable).
     *
     * @return the extra points of the item.
     */
    Integer getExtraPoints();


    /**
     * Gets the type of the item.
     *
     * @return the item type.
     */
    ItemType getType();
}
