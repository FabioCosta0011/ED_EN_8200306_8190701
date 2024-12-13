package org.example.Game.Entities.ENUMS;

/**
 * The DifficultyType enum represents the different difficulty levels available in the game.
 * Each difficulty level is associated with a power value and a maximum number of health kits.
 */
public enum DifficultyType {
    /**
     * EASY difficulty level.
     * Provides high power and a larger maximum number of health kits.
     */
    EASY(15, 8),

    /**
     * MEDIUM difficulty level.
     * Provides a moderate power and a moderate number of health kits.
     */
    MEDIUM(10, 5),

    /**
     * HARD difficulty level.
     * Provides low power and the fewest number of health kits.
     */
    HARD(5, 3);

    /**
     * The power value associated with the difficulty level.
     */
    private final int power;

    /**
     * The maximum number of health kits allowed at the given difficulty level.
     */
    private final int maxHealthKits;

    /**
     * Constructor for the DifficultyType enum.
     * Initializes the power and maxHealthKits values for each difficulty level.
     *
     * @param power The power value for this difficulty level.
     * @param maxHealthKits The maximum number of health kits for this difficulty level.
     */
    DifficultyType(int power, int maxHealthKits) {
        this.power = power;
        this.maxHealthKits = maxHealthKits;
    }

    /**
     * Retrieves the power value for this difficulty level.
     *
     * @return The power value for the difficulty level.
     */
    public int getPower() {
        return power;
    }

    /**
     * Retrieves the maximum number of health kits for this difficulty level.
     *
     * @return The maximum number of health kits for the difficulty level.
     */
    public int getMaxHealthKits() {
        return maxHealthKits;
    }
}
