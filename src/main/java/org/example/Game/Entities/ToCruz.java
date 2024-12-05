package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IToCruz;
import org.example.Structures.Interfaces.ListADT;
import org.example.Structures.Interfaces.StackADT;


public class ToCruz extends Character implements IToCruz {

    private static final int MAX_HEALTH = 100;

    private int health;

    private StackADT<Item> healthKits;

    private boolean isUsingBulletProofVest;

    public ToCruz(String name, int power, int health, IDivision currentDivision, StackADT<Item> healthKits, boolean isUsingBulletProofVest) {
        super(name, power, currentDivision);
        this.health = health;
        this.healthKits = healthKits;
        this.isUsingBulletProofVest = false;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public void consumeBulletProofVest(Item vest) {
        if (isUsingBulletProofVest) {
            System.out.println("You are already using a bulletproof vest! Cannot use another one.");
            return;
        }

        if (vest.getType() == ItemType.BULLET_PROOF_VEST && vest.getExtraPoints() != null) {
            this.setHealth(this.getHealth() + vest.getExtraPoints());
            isUsingBulletProofVest = true;
            System.out.println("Bulletproof vest consumed! Current health: " + this.getHealth());
        } else {
            System.out.println("The item is not a valid bulletproof vest.");
        }
    }

    @Override
    public StackADT<Item> getHealthKits() {
        return healthKits;
    }

    @Override
    public boolean isUsingBulletProofVest() {
        return isUsingBulletProofVest;
    }

    @Override
    public void attackEnemies(ListADT<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            enemy.setPower(enemy.getPower() - this.getPower());
        }
    }

    @Override
    public void useHealthKit() {
        if (this.getHealth() == MAX_HEALTH) {
            System.out.println("Life is already full! Cannot use a health kit.");
            return;
        }

        if (!healthKits.isEmpty()) {
            Item kit = healthKits.pop();
            if (kit.getRecoveryPoints() != null) {
                int newHealth = this.getHealth() + kit.getRecoveryPoints();
                this.setHealth(Math.min(newHealth, MAX_HEALTH));
                System.out.println("Used a health kit! New health: " + this.getHealth());
            } else {
                System.out.println("The item is not a valid health kit!");
            }
        } else {
            System.out.println("No health kits available!");
        }
    }
}
