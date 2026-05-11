package game.config;

/**
 * Configuration for collectible spawning and behavior.
 */
public class CollectibleConfig {
    
    // Spawn rates
    public static final double BASE_SPAWN_INTERVAL = 0.9;
    public static final double MIN_SPAWN_INTERVAL = 0.4;
    public static final double SPAWN_VARIATION = 0.7;
    
    // Speed scaling
    public static final double SPEED_MULTIPLIER = 1.0;
    
    // Coins
    public static final int COIN_SCORE_VALUE = 10;
    public static final int COIN_SPAWN_X = 810;
    
    // Jelly
    public static final int JELLY_SMALL_SCORE_VALUE = 25;
    public static final int JELLY_BIG_SCORE_VALUE = 50;
    public static final int JELLY_SPAWN_X = 810;
    
    // Spawn heights
    public static final double SPAWN_HEIGHT_GROUND = 398.0 - 42;
    public static final double SPAWN_HEIGHT_MID = 398.0 - 80;
    
    private CollectibleConfig() {}
}
