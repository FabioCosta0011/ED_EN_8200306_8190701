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
     * Indicates whether some other division is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return true if this division is the same as the obj argument; false otherwise.
     */
    boolean equals(Object obj);

    /**
     * Compares the division to another division.
     *
     * @param other the division to compare to.
     * @return a negative integer, zero, or a positive integer as this division is less than, equal to, or greater than the specified division.
     */
    int compareTo(IDivision other);
}
