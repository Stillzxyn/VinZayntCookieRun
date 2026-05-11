package gamelogic.progression;

import game.config.DifficultyConfig;

/**
 * DifficultyManager - Manages overall game difficulty progression.
 * Singleton that tracks and applies difficulty scaling.
 */
public class DifficultyManager {
    private static DifficultyManager instance;
    private DifficultyScaler scaler;
    private int currentStage = 0;
    private long gameStartTime = 0;
    private double currentGameSpeed = 300.0;

    private DifficultyManager() {
        this.scaler = new DifficultyScaler();
    }

    public static synchronized DifficultyManager getInstance() {
        if (instance == null) {
            instance = new DifficultyManager();
        }
        return instance;
    }

    /**
     * Initialize difficulty for a stage.
     */
    public void initializeStage(int stageNumber) {
        this.currentStage = stageNumber;
        this.gameStartTime = System.currentTimeMillis();
        this.currentGameSpeed = 300.0 * getStageMultiplier();
    }

    /**
     * Update difficulty based on elapsed time.
     * Called each frame to dynamically increase difficulty.
     */
    public void update() {
        long elapsedMs = System.currentTimeMillis() - gameStartTime;
        double elapsedSeconds = elapsedMs / 1000.0;
        
        // Check for milestone spikes
        checkMilestones(elapsedSeconds);
        
        // Apply progressive speed increase
        currentGameSpeed = scaler.getScaledSpeed(
            currentStage,
            elapsedSeconds
        );
    }

    /**
     * Check if milestones have been reached and apply spike multipliers.
     */
    private void checkMilestones(double elapsedSeconds) {
        int[] milestoneTimes = DifficultyConfig.MILESTONE_TIMES;
        double[] milestoneMultipliers = DifficultyConfig.MILESTONE_MULTIPLIERS;
        
        for (int i = 0; i < milestoneTimes.length; i++) {
            if (elapsedSeconds >= milestoneTimes[i]) {
                currentGameSpeed *= milestoneMultipliers[i];
            }
        }
    }

    /**
     * Get current game speed (affected by difficulty).
     */
    public double getCurrentGameSpeed() {
        return currentGameSpeed;
    }

    /**
     * Get spawn rate multiplier (obstacles/collectibles spawn faster).
     */
    public double getSpawnRateMultiplier() {
        long elapsedMs = System.currentTimeMillis() - gameStartTime;
        double elapsedSeconds = elapsedMs / 1000.0;
        return scaler.getSpawnMultiplier(currentStage, elapsedSeconds);
    }

    /**
     * Get stage multiplier.
     */
    public double getStageMultiplier() {
        if (currentStage < DifficultyConfig.STAGE_SPEED_MULTIPLIERS.length) {
            return DifficultyConfig.STAGE_SPEED_MULTIPLIERS[currentStage];
        }
        return 1.0;
    }

    /**
     * Get difficulty percentage (0-100).
     */
    public double getDifficultyPercentage() {
        long elapsedMs = System.currentTimeMillis() - gameStartTime;
        double elapsedSeconds = elapsedMs / 1000.0;
        
        // Max out at 300 seconds
        return Math.min((elapsedSeconds / 300.0) * 100, 100);
    }

    /**
     * Reset difficulty manager.
     */
    public void reset() {
        currentStage = 0;
        gameStartTime = 0;
        currentGameSpeed = 300.0;
    }
}
