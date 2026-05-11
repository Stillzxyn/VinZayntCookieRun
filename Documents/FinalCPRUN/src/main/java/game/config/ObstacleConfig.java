package game.config;

/**
 * Configuration for obstacle spawning and behavior.
 */
public class ObstacleConfig {
    
    // Spawn rates
    public static final double BASE_SPAWN_INTERVAL = 0.8;
    public static final double MIN_SPAWN_INTERVAL = 0.3;
    public static final double SPAWN_VARIATION = 0.3;
    
    // Speed scaling
    public static final double SPEED_MULTIPLIER = 1.0;
    
    // Ground obstacles
    public static final int GROUND_OBSTACLE_DAMAGE = 1;
    public static final double GROUND_SPAWN_HEIGHT = 398.0; // Ground level
    
    // Air obstacles
    public static final int AIR_OBSTACLE_DAMAGE = 1;
    public static final double AIR_SPAWN_HEIGHT_LOW = 200.0;
    public static final double AIR_SPAWN_HEIGHT_HIGH = 100.0;
    
    // Obstacle variation
    public static final int GROUND_OBSTACLE_SPAWN_X = 810;
    public static final int AIR_OBSTACLE_SPAWN_X = 810;
    
    private ObstacleConfig() {}
}
