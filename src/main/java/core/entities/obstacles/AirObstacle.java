package core.entities.obstacles;

import core.entities.base.Obstacle;

/**
 * Abstract base class for air obstacles — player must SLIDE to avoid.
 * Includes bobbing animation behavior.
 * Subclasses: Fireball, Bat, CloudSpike
 */
public abstract class AirObstacle extends Obstacle {

    protected double bobTimer = 0;
    protected double baseY;

    public AirObstacle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height, speed);
        this.baseY = this.y;
    }

    /** Bobbing animation for air obstacles */
    @Override
    protected void updateBehavior(double delta) {
        bobTimer += delta * 3.0;
        y = baseY + Math.sin(bobTimer) * 8;
    }
}
