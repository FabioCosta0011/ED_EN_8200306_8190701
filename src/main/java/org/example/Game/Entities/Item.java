package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IItem;

public class Item implements IItem {
    private IDivision division;
    private Integer recoveryPoints;
    private Integer extraPoints;
    private ItemType type;

    private Item(IDivision division, Integer recoveryPoints, Integer extraPoints, ItemType type) {
        this.division = division;
        this.recoveryPoints = recoveryPoints;
        this.extraPoints = extraPoints;
        this.type = type;
    }

    public static Item createLifeKit(IDivision division, int recoveryPoints) {
        return new Item(division, recoveryPoints, null, ItemType.LIFE_KIT);
    }

    public static Item createBulletProofVest(IDivision division, int extraPoints) {
        return new Item(division, null, extraPoints, ItemType.BULLET_PROOF_VEST);
    }

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

