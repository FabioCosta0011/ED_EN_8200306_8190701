package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.ITarget;

/**
 * The Target class represents a target in the game, which is associated with a division
 * and has a specific type. A target can be taken, marking it as no longer available.
 */
class Target implements ITarget {

    /**
     * The division where the target is located.
     */
    private IDivision division;

    /**
     * The type of the target (e.g., a specific mission type or objective).
     */
    private final String type;

    /**
     * A flag indicating whether the target has been taken.
     */
    private boolean isTaken;

    /**
     * Constructs a new Target with a specified division and type.
     * The target is initially not taken.
     *
     * @param division the division where the target is located.
     * @param type the type of the target.
     */
    public Target(IDivision division, String type) {
        this.division = division;
        this.type = type;
    }

    /**
     * Returns the division where the target is located.
     *
     * @return the division of the target.
     */
    @Override
    public IDivision getDivision() {
        return division;
    }

    /**
     * Returns the type of the target.
     *
     * @return the type of the target.
     */
    @Override
    public String getType() {
        return type;
    }

    /**
     * Returns whether the target has been taken or not.
     *
     * @return true if the target has been taken, false otherwise.
     */
    @Override
    public boolean isTaken() {
        return isTaken;
    }

    /**
     * Marks the target as taken, indicating that the target is no longer available.
     */
    @Override
    public void takeTarget() {
        isTaken = true;
    }
}