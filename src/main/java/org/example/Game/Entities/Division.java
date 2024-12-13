package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;
import org.example.Game.Entities.Interfaces.IEnemy;
import org.example.Game.Entities.Interfaces.IItem;
import org.example.Game.Entities.Interfaces.ITarget;
import org.example.Structures.Implementations.Graph;
import org.example.Structures.Interfaces.UnorderedListADT;

import java.util.List;

/**
 * The Division class represents a division within the game environment, providing methods for managing and interacting
 * with the division, including its name and description.
 */
public class Division implements IDivision {

    /**
     * The name of the division.
     */
    private final String name;

    /**
     * Constructor to create a new Division with the given name.
     *
     * @param name The name of the division.
     */
    public Division(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the division.
     *
     * @return The name of the division.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Compares this division to another division by their names.
     *
     * @param obj The object to compare to.
     * @return true if both divisions have the same name, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Division division = (Division) obj;
        return name.equals(division.name);
    }

    /**
     * Compares this division to another division.
     *
     * @param other The division to compare to.
     * @return A negative integer, zero, or a positive integer as this division's name is lexicographically less than,
     *         equal to, or greater than the specified division's name.
     */
    @Override
    public int compareTo(IDivision other) {
        return this.name.compareTo(other.getName());
    }
}