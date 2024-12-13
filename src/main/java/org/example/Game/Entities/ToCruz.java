package org.example.Game.Entities;

import org.example.Game.Entities.ENUMS.ItemType;
import org.example.Game.Entities.Interfaces.*;
import org.example.Structures.Implementations.LinkedStack;
import org.example.Structures.Interfaces.ListADT;
import org.example.Structures.Interfaces.StackADT;

/**
 * The ToCruz class represents the player character in the game, with health, a target,
 * the ability to use health kits, bulletproof vests, and attack enemies.
 * It also manages the division the character belongs to and the maximum health kits they can carry.
 */
public class ToCruz extends Character implements IToCruz {

    /**
     * The maximum health that ToCruz can have.
     */
    private static final int MAX_HEALTH = 100;

    /**
     * The maximum number of health kits that ToCruz can carry.
     */
    private int maxHealthKits;

    /**
     * The current health of ToCruz.
     */
    private int health;

    /**
     * The target that ToCruz is carrying in the mission.
     */
    private ITarget target;

    /**
     * The stack of health kits that ToCruz has available.
     */
    private final StackADT<IItem> healthKits;

    /**
     * A flag indicating whether ToCruz is using a bulletproof vest.
     */
    private boolean isUsingBulletProofVest;

    /**
     * Constructs a new ToCruz object with a specified power, target, and initial division.
     * ToCruz starts with full health (100) and no bulletproof vest.
     *
     * @param power the power level of ToCruz.
     * @param target the target assigned to ToCruz.
     * @param currentDivision the current division where ToCruz is located.
     */
    public ToCruz(int power, ITarget target, IDivision currentDivision) {
        super("To Cruz", power, currentDivision);
        this.health = 100;
        this.target = target;
        this.healthKits = new LinkedStack<>();
        this.isUsingBulletProofVest = false;
    }

    /**
     * Returns the current health of ToCruz.
     *
     * @return the health of ToCruz.
     */
    @Override
    public int getHealth() {
        return health;
    }

    /**
     * Sets the current health of ToCruz.
     *
     * @param health the new health value to set.
     */
    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    /**
     * Returns the stack of health kits ToCruz has.
     *
     * @return the stack of health kits.
     */
    @Override
    public StackADT<IItem> getHealthKits() {
        return healthKits;
    }

    /**
     * Sets the maximum number of health kits that ToCruz can carry.
     *
     * @param maxHealthKits the maximum health kits ToCruz can carry.
     */
    @Override
    public void setMaxHealthKits(int maxHealthKits) {
        this.maxHealthKits = maxHealthKits;
    }

    /**
     * Returns the maximum number of health kits ToCruz can carry.
     *
     * @return the maximum health kits.
     */
    @Override
    public int getMaxHealthKits() {
        return maxHealthKits;
    }

    /**
     * Returns the current division that ToCruz is located in.
     *
     * @return the current division.
     */
    @Override
    public IDivision getCurrentDivision() {
        return super.getCurrentDivision();
    }

    /**
     * Sets the current division that ToCruz is located in.
     *
     * @param currentDivision the new division to set.
     */
    @Override
    public void setCurrentDivision(IDivision currentDivision) {
        super.setCurrentDivision(currentDivision);
    }

    /**
     * Returns whether ToCruz is currently using a bulletproof vest.
     *
     * @return true if ToCruz is using a bulletproof vest, false otherwise.
     */
    @Override
    public boolean isUsingBulletProofVest() {
        return isUsingBulletProofVest;
    }

    /**
     * Sets whether ToCruz is using a bulletproof vest.
     *
     * @param usingBulletProofVest true if ToCruz is using a bulletproof vest, false otherwise.
     */
    @Override
    public void setUsingBulletProofVest(boolean usingBulletProofVest) {
        isUsingBulletProofVest = usingBulletProofVest;
    }

    /**
     * Makes ToCruz attack a list of enemies, reducing their power.
     * If an enemy's power reaches zero or below, it is removed from the mission.
     *
     * @param enemies the list of enemies ToCruz will attack.
     * @param mission the current mission where the enemies exist.
     */
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

    /**
     * Returns the target assigned to ToCruz.
     *
     * @return the target.
     */
    @Override
    public ITarget getTarget() {
        return target;
    }

    /**
     * Sets the target assigned to ToCruz.
     *
     * @param target the target to set.
     */
    @Override
    public void setTarget(ITarget target) {
        this.target = target;
    }

    /**
     * Makes ToCruz consume a bulletproof vest, which adds extra health points.
     * If ToCruz is already using a bulletproof vest, it cannot use another one.
     *
     * @param vest the bulletproof vest item to be consumed.
     */
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

    /**
     * Makes ToCruz use a health kit to recover health.
     * Health cannot exceed the maximum health (100), and a health kit is consumed in the process.
     *
     * If ToCruz's health is already full or no health kits are available, appropriate messages are displayed.
     */
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

    /**
     * Adds a health kit to ToCruz's inventory.
     * The maximum number of health kits is limited by the value of `maxHealthKits`.
     *
     * @param kit the health kit to be added.
     */
    @Override
    public void addHealthKit(IItem kit) {
        if (healthKits.size() < maxHealthKits) {
            healthKits.push(kit);
            System.out.println("Health kit added! Current kits: " + healthKits.size());
        } else {
            System.out.println("Cannot add more health kits. Maximum capacity reached.");
        }
    }

    /**
     * Returns whether ToCruz is currently carrying a target.
     *
     * @return true if ToCruz is carrying a target, false otherwise.
     */
    @Override
    public boolean isCarryingTarget() {
        return this.target != null;
    }

}
