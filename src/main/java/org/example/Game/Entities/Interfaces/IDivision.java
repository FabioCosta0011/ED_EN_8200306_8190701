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

    /**
     * Compares this division with another division to determine their relative order.
     * This method is typically used for sorting or ordering purposes.
     *
     * @param other the other {@link IDivision} instance to compare against.
     * @return a negative integer if this division is less than the other,
     *         zero if they are equal, or a positive integer if this division is greater.
     */
    int compareTo(IDivision other);
}
