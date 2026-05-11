package gamelogic.scoring;

/**
 * ScoreCalculator - Centralized score calculation logic.
 * Handles base scores, multipliers, combos, and bonuses.
 */
public class ScoreCalculator {
    private static ScoreCalculator instance;
    private ScoreMultiplier multiplier;
    private ComboSystem comboSystem;

    private ScoreCalculator() {
        this.multiplier = new ScoreMultiplier();
        this.comboSystem = new ComboSystem();
    }

    /**
     * Get singleton instance.
     */
    public static synchronized ScoreCalculator getInstance() {
        if (instance == null) {
            instance = new ScoreCalculator();
        }
        return instance;
    }

    /**
     * Calculate score for collecting a coin.
     * @param baseValue Base coin value
     * @return Calculated score with multipliers applied
     */
    public int calculateCoinScore(int baseValue) {
        comboSystem.incrementCombo();
        double multiplied = baseValue * multiplier.getCurrentMultiplier();
        return (int) multiplied;
    }

    /**
     * Calculate score for collecting jelly.
     * @param baseValue Base jelly value
     * @return Calculated score with multipliers applied
     */
    public int calculateJellyScore(int baseValue) {
        comboSystem.incrementCombo();
        double multiplied = baseValue * multiplier.getCurrentMultiplier();
        return (int) multiplied;
    }

    /**
     * Calculate bonus for perfect timing/near-miss.
     * @param distanceFromCookie Distance from player to collectible
     * @return Bonus score (0 if not close enough)
     */
    public int calculateProximityBonus(double distanceFromCookie) {
        if (distanceFromCookie < 50) {
            return 50; // Near miss bonus
        }
        return 0;
    }

    /**
     * Get current combo count.
     */
    public int getComboCount() {
        return comboSystem.getComboCount();
    }

    /**
     * Get current multiplier value.
     */
    public double getMultiplier() {
        return multiplier.getCurrentMultiplier();
    }

    /**
     * Reset combo when obstacle hits.
     */
    public void resetCombo() {
        comboSystem.resetCombo();
    }

    /**
     * Update multiplier based on combo.
     */
    public void updateMultiplier() {
        multiplier.setComboCount(comboSystem.getComboCount());
    }

    /**
     * Get score breakdown for UI.
     */
    public String getScoreBreakdown() {
        return String.format("Score Multiplier: %.2fx | Combo: %d",
                multiplier.getCurrentMultiplier(),
                comboSystem.getComboCount());
    }
}
