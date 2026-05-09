package core.entities.base;

import javafx.scene.canvas.GraphicsContext;

public abstract class GameObject {

    protected double x;
    protected double y;

    protected double width;
    protected double height;

    protected double velocityX = 0;
    protected double velocityY = 0;

    protected boolean alive = true;

    public GameObject(
            double x,
            double y,
            double width,
            double height
    ) {

        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
    }

    public abstract void update(double delta);

    public abstract void render(GraphicsContext gc);

    public boolean collides(GameObject other) {

        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }

    public boolean intersects(GameObject other) {
        return collides(other);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void destroy() {
        alive = false;
    }

    public boolean isOutOfScreen() {
        return x + width < 0;
    }

    // GETTERS

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
}
