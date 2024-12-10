package org.example.Game.Entities.Interfaces;

public interface ITarget {
    /**
     * Gets the division where the target is located.
     *
     * @return the division of the target.
     */
    IDivision getDivision();

    /**
     * Gets the type of the target.
     *
     * @return the target type.
     */
    String getType();

    /**
     * Checks if the target has been taken.
     *
     * @return true if the target is taken, false otherwise.
     */
    boolean isTaken();

    /**
     * Marks the target as taken.
     * <p>
     * This method is called when the target is picked up, changing its state
     * to indicate that it has been captured or taken by an entity. Once the target
     * is marked as taken, it should not be available for interaction by other entities.
     */
    public void takeTarget();
}
