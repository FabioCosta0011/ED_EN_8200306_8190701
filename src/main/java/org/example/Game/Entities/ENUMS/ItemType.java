package org.example.Game.Entities.ENUMS;

/**
 * The ItemType enum represents different types of items available in the game.
 * Each item type has a corresponding visual representation and description.
 */
public enum ItemType {
    /**
     * LIFE_KIT represents a health item that restores the player's health.
     */
    LIFE_KIT,

    /**
     * BULLET_PROOF_VEST represents an item that provides the player with bullet protection.
     */
    BULLET_PROOF_VEST;

    /**
     * Converts the enum constant to a user-friendly string representation.
     * The string includes an emoji to visually represent the item type.
     *
     * @return The string representation of the item type with an emoji.
     * @throws IllegalArgumentException if the item type is unexpected or invalid.
     */
    @Override
    public String toString() {
        switch (this) {
            case LIFE_KIT:
                return "💊 Life Kit";
            case BULLET_PROOF_VEST:
                return "🦺 Bullet Proof Vest";
            default:
                throw new IllegalArgumentException("Unexpected value: " + this);
        }
    }
}