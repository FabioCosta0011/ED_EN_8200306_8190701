package org.example.Game.Entities;

import org.example.Game.Entities.Interfaces.IDivision;

/**
 * Class representing a division within the game environment.
 */
public class Division implements IDivision {
    private String name;  // Name of the division
    private String description;  // Optional, a description about the division

    // Constructor with description as optional (can be null)
    public Division(String name) {
        this.name = name;
        this.description = null; // Default to null if no description is provided
    }

    // Constructor that accepts both name and description
    public Division(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public String toString() {
        return "Division{" +
                "name='" + name + '\'' +
                ", description='" + (description != null ? description : "No description available") + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Division division = (Division) obj;
        return name.equals(division.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(IDivision other) {
        return this.name.compareTo(other.getName());
    }
}
