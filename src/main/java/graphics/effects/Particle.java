package graphics.effects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.Renderable;
import utils.Updatable;

/**
 * Small particle effect used when collecting items.
 * Gives the game more feedback and feel.
 */
public class Particle implements Renderable, Updatable {

    // Position
    private double x;
    private double y;

    // Velocity
    private double vx;
    private double vy;

    // Lifetime (seconds)
    private double life = 1;

    // Particle size
    private final double size;

    public Particle(double x, double y) {

        this.x = x;
        this.y = y;

        // Random movement
        vx = (Math.random() - 0.5) * 90;
        vy = (Math.random() - 0.5) * 90;

        // Random size
        size = 2 + Math.random() * 3;
    }

    /**
     * Update particle physics.
     */
    @Override
    public void update(double delta) {

        // Move particle
        x += vx * delta;
        y += vy * delta;

        // Gravity
        vy += 200 * delta;

        // Fade over time
        life -= delta;
    }

    /**
     * Render glowing particle.
     */
    @Override
    public void render(GraphicsContext gc) {

        // Fade effect
        gc.setGlobalAlpha(life);

        // Outer glow
        gc.setFill(Color.WHITE);
        gc.fillOval(x, y, size, size);

        // Inner gold
        gc.setFill(Color.GOLD);
        gc.fillOval(
                x + 1,
                y + 1,
                size - 2,
                size - 2
        );

        gc.setGlobalAlpha(1);
    }

    /**
     * Remove particle when life ends.
     */
    public boolean isDead() {

        return life <= 0;
    }
}