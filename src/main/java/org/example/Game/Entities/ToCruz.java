package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.ITarget;
import org.example.Game.Entities.Interfaces.IToCruz;
import org.example.Structures.Implementations.LinkedStack;
import org.example.Structures.Interfaces.ListADT;
import org.example.Structures.Interfaces.StackADT;

public class ToCruz extends Character implements IToCruz {

    private static final int MAX_HEALTH = 100;

    private int health;

    private ITarget target;

    private StackADT<Item> healthKits;

    private boolean isUsingBulletProofVest;

    public ToCruz(String name, int power, int health, IDivision currentDivision, ITarget target,
            StackADT<Item> healthKits, boolean isUsingBulletProofVest) {
        super(name, power, currentDivision);
        this.health = health;
        this.target = target;
        this.healthKits = healthKits;
        this.isUsingBulletProofVest = false;
    }

    public ToCruz(int power, ITarget target, IDivision currentDivision) {
        super("To Cruz", power, currentDivision);
        this.health = 100;
        this.target = target;
        this.healthKits = new LinkedStack<>();
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
    public StackADT<Item> getHealthKits() {
        return healthKits;
    }

    public void setMaxHealthKits(int maxHealthKits) {
        this.maxHealthKits = maxHealthKits;
    }

    public int getMaxHealthKits() {
        return maxHealthKits;
    }

    @Override
    public IDivision getCurrentDivision() {
        return super.getCurrentDivision();
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
    public ITarget getTarget() {
        return target;
    }

    @Override
    public void setTarget(ITarget target) {
        this.target = target;
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
    public void attackEnemies(ListADT<Enemy> enemies) {
        for (Enemy enemy : enemies) {
            // TODO enemy.setHealth(enemy.getHealth() - this.getPower());
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

    public void addHealthKit(Item kit) {
        if (healthKits.size() < maxHealthKits) {
            healthKits.push(kit);
            System.out.println("Health kit added! Current kits: " + healthKits.size());
        } else {
            System.out.println("Cannot add more health kits. Maximum capacity reached.");
        }
    }

}
