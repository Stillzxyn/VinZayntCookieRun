package entity.base;

/**
 * Handles all physics calculations for a game object.
 * Manages position, velocity, gravity, and ground collision.
 */
public class Physics {

    // =========================
    // PHYSICS CONSTANTS
    // =========================

    public static final double GRAVITY       = 1800.0;
    public static final double JUMP_VELOCITY = -680.0;
    public static final double GROUND_Y      = 290.0;
    public static final double NORMAL_H      = 70.0;
    public static final double SLIDE_H       = 40.0;

    // =========================
    // STATE
    // =========================

    private double x;
    private double y;
    private double velocityY = 0;

    private double width;
    private double height;

    // =========================
    // CONSTRUCTOR
    // =========================

    /**
     * Initialize physics with starting position and dimensions.
     * @param x Starting x position
     * @param y Starting y position
     * @param width Object width
     * @param height Object height
     */
    public Physics(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocityY = 0;
    }

    // =========================
    // PHYSICS UPDATE
    // =========================

    /**
     * Update physics for the given frame.
     * @param delta Time since last frame (in seconds)
     */
    public void update(double delta) {
        // Apply physics if in air OR if jumping (velocityY != 0)
        if (isInAir() || velocityY != 0) {
            velocityY += GRAVITY * delta;
            y += velocityY * delta;

            // Ground collision
            if (y >= GROUND_Y - NORMAL_H) {
                y = GROUND_Y - NORMAL_H;
                velocityY = 0;
            }
        }
    }

    // =========================
    // ACTIONS
    // =========================

    public void jump() {
        if (!isInAir()) {
            velocityY = JUMP_VELOCITY;
        }
    }

    public void resetToGround() {
        y = GROUND_Y - NORMAL_H;
        velocityY = 0;
    }

    // =========================
    // STATE CHECKS
    // =========================

    public boolean isInAir() {
        return y < GROUND_Y - NORMAL_H;
    }

    public boolean isOnGround() {
        return !isInAir();
    }

    // =========================
    // POSITION SETTERS
    // =========================

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setDimensions(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    // =========================
    // GETTERS
    // =========================

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }
    public double getVelocityY() {
        return velocityY;
    }
    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }
}
