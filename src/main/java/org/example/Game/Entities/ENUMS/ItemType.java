package org.example.Game.Entities.ENUMS;

public enum ItemType {
    LIFE_KIT,
    BULLET_PROOF_VEST;

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