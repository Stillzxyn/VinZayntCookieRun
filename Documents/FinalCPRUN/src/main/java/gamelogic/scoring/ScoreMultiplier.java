package gamelogic.scoring;

/**
 * Manages score multiplier based on combo count.
 */
public class ScoreMultiplier {
    private int comboCount = 0;
    private static final double BASE_MULTIPLIER = 1.0;
    private static final double MULTIPLIER_PER_COMBO = 0.05; // +5% per combo
    private static final double MAX_MULTIPLIER = 5.0;

    /**
     * Set the current combo count and recalculate multiplier.
     */
    public void setComboCount(int count) {
        this.comboCount = Math.max(0, count);
    }

    /**
     * Get the current multiplier value.
     */
    public double getCurrentMultiplier() {
        double calculated = BASE_MULTIPLIER + (comboCount * MULTIPLIER_PER_COMBO);
        return Math.min(calculated, MAX_MULTIPLIER);
    }

    /**
     * Reset multiplier to base.
     */
    public void reset() {
        this.comboCount = 0;
    }

    /**
     * Get milestone bonuses (e.g., 1.25x at 5 combo, 1.5x at 10 combo).
     */
    public double getMilestoneBonus() {
        if (comboCount >= 20) return 1.5;
        if (comboCount >= 10) return 1.3;
        if (comboCount >= 5) return 1.1;
        return 1.0;
    }
}
