package game.damage;

/**
 * DamageType - Enumeration of damage types.
 */
public enum DamageType {
    OBSTACLE("Obstacle"),
    COLLISION("Collision"),
    FALLING("Falling"),
    ABILITY("Ability"),
    ENVIRONMENT("Environment");

    private final String displayName;

    DamageType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
