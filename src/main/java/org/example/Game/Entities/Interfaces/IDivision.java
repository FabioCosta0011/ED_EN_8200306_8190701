package org.example.Game.Entities.Interfaces;

/**
 * Interface representing the contract for Division entities.
 */
public interface IDivision {

    /**
     * Gets the name of the division.
     *
     * @return the name of the division.
     */
    String getName();

    /**
     * Gets the description of the division (optional).
     *
     * @return the description of the division.
     */
    String getDescription();

    int compareTo(IDivision other);
}
