package gamelogic.scoring;

/**
 * ComboSystem - Tracks consecutive collectible pickups.
 * Resets when obstacle hits player.
 */
public class ComboSystem {
    private int comboCount = 0;
    private long lastCollectTime = 0;
    private static final long COMBO_TIMEOUT_MS = 3000; // 3 seconds

    /**
     * Increment combo count on collectible pickup.
     */
    public void incrementCombo() {
        long currentTime = System.currentTimeMillis();
        
        // Check if combo has timed out
        if (currentTime - lastCollectTime > COMBO_TIMEOUT_MS) {
            comboCount = 0;
        }
        
        comboCount++;
        lastCollectTime = currentTime;
    }

    /**
     * Reset combo on obstacle hit.
     */
    public void resetCombo() {
        comboCount = 0;
        lastCollectTime = 0;
    }

    /**
     * Get current combo count.
     */
    public int getComboCount() {
        return comboCount;
    }

    /**
     * Check if combo is still active (hasn't timed out).
     */
    public boolean isComboActive() {
        return (System.currentTimeMillis() - lastCollectTime) <= COMBO_TIMEOUT_MS;
    }

    /**
     * Get combo percentage (0-100%).
     */
    public double getComboPercentage() {
        long elapsed = System.currentTimeMillis() - lastCollectTime;
        if (elapsed > COMBO_TIMEOUT_MS) return 0;
        return (1.0 - (double) elapsed / COMBO_TIMEOUT_MS) * 100;
    }
}
