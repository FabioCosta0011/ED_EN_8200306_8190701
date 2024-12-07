package org.example.Game.Entities.ENUMS;

public enum DifficultyType {
    EASY(100, 8), // Power = 100, Max Health Kits = 8
    MEDIUM(50, 5), // Power = 50, Max Health Kits = 5
    HARD(25, 3); // Power = 25, Max Health Kits = 3

    private final int power;
    private final int maxHealthKits;

    // Constructor
    DifficultyType(int power, int maxHealthKits) {
        this.power = power;
        this.maxHealthKits = maxHealthKits;
    }

    // Getters for power and max health kits
    public int getPower() {
        return power;
    }

    public int getMaxHealthKits() {
        return maxHealthKits;
    }

    public static DifficultyType fromString(String difficulty) {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return EASY;
            case "medium":
                return MEDIUM;
            case "hard":
                return HARD;
            default:
                throw new IllegalArgumentException("Invalid difficulty: " + difficulty);
        }
    }
}
