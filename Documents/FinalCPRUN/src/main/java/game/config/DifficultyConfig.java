package game.config;

/**
 * Configuration for difficulty progression.
 */
public class DifficultyConfig {
    
    // Speed scaling factors per stage
    public static final double[] STAGE_SPEED_MULTIPLIERS = {
        1.0,    // Stage 1
        1.1,    // Stage 2
        1.25,   // Stage 3
        1.4,    // Stage 4
        1.6     // Stage 5
    };
    
    // Speed increase per second
    public static final double SPEED_INCREASE_PER_SECOND = 0.5;
    public static final double MAX_SPEED_INCREASE = 200.0;
    
    // Spawn rate scaling
    public static final double[] STAGE_SPAWN_MULTIPLIERS = {
        1.0,    // Stage 1
        1.05,   // Stage 2
        1.1,    // Stage 3
        1.15,   // Stage 4
        1.2     // Stage 5
    };
    
    // Difficulty spikes at milestones
    public static final int[] MILESTONE_TIMES = {30, 60, 120, 180}; // seconds
    public static final double[] MILESTONE_MULTIPLIERS = {1.2, 1.4, 1.6, 1.8};
    
    private DifficultyConfig() {}
}
