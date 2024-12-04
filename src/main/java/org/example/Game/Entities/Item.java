package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IItem;

public class Item implements IItem {
    private IDivision division;
    private Integer recoveryPoints;
    private Integer extraPoints;
    private ItemType type;

    // Private constructor to enforce type rules
    private Item(IDivision division, Integer recoveryPoints, Integer extraPoints, ItemType type) {
        this.division = division;
        this.recoveryPoints = recoveryPoints;
        this.extraPoints = extraPoints;
        this.type = type;
    }

    // Static factory method for "kit de vida"
    public static Item createLifeKit(IDivision division, int recoveryPoints) {
        return new Item(division, recoveryPoints, null, ItemType.LIFE_KIT);
    }

    // Static factory method for "pontos-extra"
    public static Item createExtraPoints(IDivision division, int extraPoints) {
        return new Item(division, null, extraPoints, ItemType.BULLET_PROOF_VEST);
    }

    // Getters and Setters
    @Override
    public IDivision getDivision() {
        return division;
    }

    @Override
    public Integer getRecoveryPoints() {
        return recoveryPoints;
    }

    @Override
    public Integer getExtraPoints() {
        return extraPoints;
    }

    @Override
    public ItemType getType() {
        return type;
    }

}

