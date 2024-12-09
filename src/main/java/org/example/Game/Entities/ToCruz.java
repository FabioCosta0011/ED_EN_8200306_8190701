package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.*;
import org.example.Structures.Implementations.LinkedStack;
import org.example.Structures.Interfaces.ListADT;
import org.example.Structures.Interfaces.StackADT;

public class ToCruz extends Character implements IToCruz {

    private static final int MAX_HEALTH = 100;

    private int maxHealthKits;

    private int health;

    private ITarget target;

    private StackADT<IItem> healthKits;

    private boolean isUsingBulletProofVest;

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
    public StackADT<IItem> getHealthKits() {
        return healthKits;
    }

    @Override
    public void setMaxHealthKits(int maxHealthKits) {
        this.maxHealthKits = maxHealthKits;
    }

    @Override
    public int getMaxHealthKits() {
        return maxHealthKits;
    }

    @Override
    public IDivision getCurrentDivision() {
        return super.getCurrentDivision();
    }

    @Override
    public void setCurrentDivision(IDivision currentDivision) {
        super.setCurrentDivision(currentDivision);
    }

    @Override
    public boolean isUsingBulletProofVest() {
        return isUsingBulletProofVest;
    }

    @Override
    public void setUsingBulletProofVest(boolean usingBulletProofVest) {
        isUsingBulletProofVest = usingBulletProofVest;
    }

    @Override
    public void attackEnemies(ListADT<IEnemy> enemies, Mission mission) {
        for (IEnemy enemy : enemies) {
            enemy.setPower(enemy.getPower() - this.getPower());
            System.out.println("════════════════════════════════════════════════════");
            System.out.println("To Cruz attacked " + enemy.getName() + " with power " + this.getPower());
            if (enemy.getPower() <= 0) {
                System.out.println(enemy.getName() + " has been defeated!");
                mission.getEnemies().remove(enemy);
            } else {
                System.out.println(enemy.getName() + " has " + enemy.getPower() + " power left.");
            }
            System.out.println("════════════════════════════════════════════════════");
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
    public void consumeBulletProofVest(IItem vest) {
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
    public void useHealthKit() {
        if (!(this.getHealth() < MAX_HEALTH)) {
            System.out.println("Life is already full! Cannot use a health kit.");
            return;
        }

        if (!healthKits.isEmpty()) {
            IItem kit = healthKits.pop();
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

    @Override
    public void addHealthKit(IItem kit) {
        if (healthKits.size() < maxHealthKits) {
            healthKits.push(kit);
            System.out.println("Health kit added! Current kits: " + healthKits.size());
        } else {
            System.out.println("Cannot add more health kits. Maximum capacity reached.");
        }
    }

    //TODO MOVE TO NEW DIVISION

}
