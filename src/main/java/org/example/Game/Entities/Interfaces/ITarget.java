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
}
