package game.damage;

/**
 * DamageCalculator - Centralized damage calculation logic.
 */
public class DamageCalculator {
    private static DamageCalculator instance;

    private DamageCalculator() {}

    public static synchronized DamageCalculator getInstance() {
        if (instance == null) {
            instance = new DamageCalculator();
        }
        return instance;
    }

    /**
     * Calculate damage based on type and armor.
     */
    public int calculateDamage(DamageType type, int baseDamage, double armorReduction) {
        double reducedDamage = baseDamage * (1.0 - armorReduction);
        
        // Apply type multipliers
        switch (type) {
            case OBSTACLE:
                reducedDamage *= 1.0;
                break;
            case COLLISION:
                reducedDamage *= 0.8;
                break;
            case FALLING:
                reducedDamage *= 0.5;
                break;
            default:
                break;
        }
        
        return Math.max(1, (int) reducedDamage); // Minimum 1 damage
    }

    /**
     * Check if damage would be lethal.
     */
    public boolean isLethalDamage(int damage, int currentHealth) {
        return damage >= currentHealth;
    }
}
