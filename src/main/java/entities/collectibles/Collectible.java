package entities.collectibles;

import entities.base.GameObject;
import javafx.scene.canvas.GraphicsContext;

/**
 * Abstract base class for collectible items.
 * Subclasses: Coin, JellySmall, JellyBig
 */
public abstract class Collectible extends GameObject {

    protected final double speed;
    protected final int scoreValue;
    protected double glowTimer = 0;
    protected double magneticVelocityX = 0;
    protected double magneticVelocityY = 0;

    public Collectible(double x, double y, double size, double speed, int scoreValue) {
        super(x, y, size, size);
        this.speed = speed;
        this.scoreValue = scoreValue;
    }

    @Override
    public void update(double delta) {
        x -= speed * delta;
        // Apply magnetic velocity if active
        x += magneticVelocityX * delta;
        y += magneticVelocityY * delta;
        glowTimer += delta * 4.0;
        if (x + width < -10) alive = false;
    }

    @Override
    public abstract void render(GraphicsContext gc);

    // MAGNETIC ATTRACTION
    public void applyMagneticForce(double targetX, double targetY, double magneticRadius, double attractionStrength) {
        // Only coins are attracted (subclasses can override)
        if (!isMagnetic()) return;

        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Within magnetic radius?
        if (distance < magneticRadius && distance > 0) {
            // Normalize direction and apply attraction
            double dirX = dx / distance;
            double dirY = dy / distance;

            // Stronger attraction closer to the cookie
            double attractionFactor = (1.0 - (distance / magneticRadius)) * attractionStrength;

            magneticVelocityX = dirX * attractionFactor;
            magneticVelocityY = dirY * attractionFactor;
        } else {
            // Outside radius, no attraction
            magneticVelocityX = 0;
            magneticVelocityY = 0;
        }
    }

    public void resetMagneticVelocity() {
        magneticVelocityX = 0;
        magneticVelocityY = 0;
    }

    // Override in subclasses to enable/disable magnetism
    protected boolean isMagnetic() {
        return false;
    }

    // GETTERS
    public int getScoreValue() { return scoreValue; }
}
