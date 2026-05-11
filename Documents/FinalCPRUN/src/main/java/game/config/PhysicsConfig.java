package game.config;

/**
 * Physics configuration.
 * Only contains fields that are actually used by the Physics class.
 */
public class PhysicsConfig {

    // Gravity constant (affects falling speed)
    public static final double GRAVITY = 2000.0;

    // Jump velocity (initial upward velocity when jumping)
    public static final double JUMP_VELOCITY = -750.0;

    // Ground position
    public static final double GROUND_Y = 290.0;

    // Player hitbox heights
    public static final double NORMAL_H = 70.0;  // Normal standing height
    public static final double SLIDE_H = 40.0;   // Height when sliding

    private PhysicsConfig() {}
}
