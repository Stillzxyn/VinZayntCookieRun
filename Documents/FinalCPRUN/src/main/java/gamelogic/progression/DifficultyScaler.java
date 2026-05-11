package gamelogic.progression;

import game.config.DifficultyConfig;

/**
 * DifficultyScaler - Calculates scaled difficulty values.
 */
public class DifficultyScaler {

    /**
     * Get scaled game speed based on stage and elapsed time.
     */
    public double getScaledSpeed(int stageNumber, double elapsedSeconds) {
        double baseSpeed = 300.0;
        
        // Stage multiplier
        double stageMultiplier = getStageMultiplier(stageNumber);
        
        // Time-based progressive increase
        double timeIncrease = elapsedSeconds * DifficultyConfig.SPEED_INCREASE_PER_SECOND;
        timeIncrease = Math.min(timeIncrease, DifficultyConfig.MAX_SPEED_INCREASE);
        
        return (baseSpeed * stageMultiplier) + timeIncrease;
    }

    /**
     * Get spawn rate multiplier.
     */
    public double getSpawnMultiplier(int stageNumber, double elapsedSeconds) {
        double stageMultiplier = getStageMultiplier(stageNumber);
        double timeMultiplier = 1.0 + (elapsedSeconds * 0.01); // +1% per second
        
        return stageMultiplier * timeMultiplier;
    }

    /**
     * Get stage-specific multiplier.
     */
    private double getStageMultiplier(int stageNumber) {
        double[] multipliers = DifficultyConfig.STAGE_SPEED_MULTIPLIERS;
        if (stageNumber < multipliers.length) {
            return multipliers[stageNumber];
        }
        return 1.0;
    }

    /**
     * Get difficulty curve (S-curve for smooth progression).
     */
    public double getDifficultyCurve(double elapsedSeconds) {
        // S-curve: slow start, faster middle, plateaus at end
        double maxTime = 300.0; // 5 minutes
        double t = Math.min(elapsedSeconds / maxTime, 1.0);
        
        // S-curve formula: 3t^2 - 2t^3
        return 3 * t * t - 2 * t * t * t;
    }
}
