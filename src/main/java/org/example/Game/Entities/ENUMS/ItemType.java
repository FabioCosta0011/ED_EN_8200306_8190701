package org.example.Game.Entities.ENUMS;

public enum ItemType {
    LIFE_KIT,
    EXTRA_POINTS;

    @Override
    public String toString() {
        switch (this) {
            case LIFE_KIT:
                return "Life Kit";
            case EXTRA_POINTS:
                return "Extra Points";
            default:
                throw new IllegalArgumentException("Unexpected value: " + this);
        }
    }
}
