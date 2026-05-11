package game.config;

/**
 * Configuration for physics and collision.
 */
public class PhysicsConfig {
    
    // Ground settings
    public static final double GROUND_Y = 398.0;
    public static final double GRAVITY = 0.6;
    public static final double MAX_FALL_SPEED = 25.0;
    
    // Player (Cookie)
    public static final double COOKIE_WIDTH = 36.0;
    public static final double COOKIE_HEIGHT = 36.0;
    public static final double COOKIE_START_X = 100.0;
    public static final double COOKIE_START_Y = 362.0;
    
    // Jump settings
    public static final double JUMP_FORCE = 18.0;
    public static final double SLIDE_DURATION = 0.4;
    
    // Collision bounds
    public static final double COLLISION_MARGIN = 10.0;
    public static final double OFF_SCREEN_THRESHOLD = -100.0;
    
    private PhysicsConfig() {}
}
