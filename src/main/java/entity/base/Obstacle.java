package entity.base;

import javafx.scene.canvas.GraphicsContext;

/**
 * Abstract obstacle — subclasses define shape and behavior.
 * Inherits: GameObject → Renderable, Updatable, Collidable, Spawnable
 */
public abstract class Obstacle extends GameObject {

    protected double speed;

    public Obstacle(double x, double y, double width, double height, double speed) {
        super(x, y, width, height);
        this.speed = speed;
    }

    @Override
    public void update(double delta) {
        x -= speed * delta;
        if (x + width < -20) alive = false;
        updateBehavior(delta);
    }

    /** Hook: subclasses override for extra per-frame behavior (bobbing, etc.) */
    protected void updateBehavior(double delta) {}

    @Override
    public abstract void render(GraphicsContext gc);

    public void  setSpeed(double s) { speed = s; }
    public double getSpeed()        { return speed; }
}
