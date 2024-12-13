package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IItem;

/**
 * Represents an item in the game. The item can either be a life kit or a bulletproof vest.
 * The class encapsulates the details of the item, including the division it belongs to,
 * its recovery points (for life kits), extra points (for bulletproof vests), and the type of item.
 */
public class Item implements IItem {
    /**
     * The division in which the item is located.
     */
    private final IDivision division;

    /**
     * The recovery points associated with the item.
     * This is applicable only for items of type LIFE_KIT.
     */
    private final Integer recoveryPoints;

    /**
     * The extra points associated with the item.
     * This is applicable only for items of type BULLET_PROOF_VEST.
     */
    private final Integer extraPoints;

    /**
     * The type of the item, which could be either LIFE_KIT or BULLET_PROOF_VEST.
     */
    private final ItemType type;

    /**
     * Private constructor to create an item with the specified details.
     *
     * @param division The division in which the item is located.
     * @param recoveryPoints The recovery points associated with the item (for LIFE_KIT type).
     * @param extraPoints The extra points associated with the item (for BULLET_PROOF_VEST type).
     * @param type The type of the item, which could be either LIFE_KIT or BULLET_PROOF_VEST.
     */
    private Item(IDivision division, Integer recoveryPoints, Integer extraPoints, ItemType type) {
        this.division = division;
        this.recoveryPoints = recoveryPoints;
        this.extraPoints = extraPoints;
        this.type = type;
    }

    /**
     * Factory method to create a life kit item.
     *
     * @param division The division where the life kit is located.
     * @param recoveryPoints The recovery points the life kit provides.
     * @return A new life kit item with the specified division and recovery points.
     */
    public static Item createLifeKit(IDivision division, int recoveryPoints) {
        return new Item(division, recoveryPoints, null, ItemType.LIFE_KIT);
    }

    /**
     * Factory method to create a bulletproof vest item.
     *
     * @param division The division where the bulletproof vest is located.
     * @param extraPoints The extra points the bulletproof vest provides.
     * @return A new bulletproof vest item with the specified division and extra points.
     */
    public static Item createBulletProofVest(IDivision division, int extraPoints) {
        return new Item(division, null, extraPoints, ItemType.BULLET_PROOF_VEST);
    }

    /**
     * Retrieves the division where the item is located.
     *
     * @return The division where the item is located.
     */
    @Override
    public IDivision getDivision() {
        return division;
    }

    /**
     * Retrieves the recovery points associated with the item. Only applicable for life kit items.
     *
     * @return The recovery points, or null if the item is not a life kit.
     */
    @Override
    public Integer getRecoveryPoints() {
        return recoveryPoints;
    }

    /**
     * Retrieves the extra points associated with the item. Only applicable for bulletproof vest items.
     *
     * @return The extra points, or null if the item is not a bulletproof vest.
     */
    @Override
    public Integer getExtraPoints() {
        return extraPoints;
    }

    /**
     * Retrieves the type of the item, which can be either a LIFE_KIT or a BULLET_PROOF_VEST.
     *
     * @return The type of the item.
     */
    @Override
    public ItemType getType() {
        return type;
    }
}

