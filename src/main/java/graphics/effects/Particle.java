package graphics.effects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Particle {

    private double x;
    private double y;

    private double vx;
    private double vy;

    private double life = 1;

    private double size;

    public Particle(double x, double y) {

        this.x = x;
        this.y = y;

        vx = (Math.random() - 0.5) * 90;
        vy = (Math.random() - 0.5) * 90;

        size = 2 + Math.random() * 3;
    }

    public void update(double delta) {

        x += vx * delta;

        y += vy * delta;

        vy += 200 * delta;

        life -= delta;
    }

    public void render(GraphicsContext gc) {

        gc.setGlobalAlpha(life);

        gc.setFill(Color.GOLD);

        gc.setFill(Color.WHITE);
        gc.fillOval(x, y, size, size);

        gc.setFill(Color.GOLD);
        gc.fillOval(
                x + 1,
                y + 1,
                size - 2,
                size - 2
        );

        gc.setGlobalAlpha(1);
    }

    public boolean isDead() {

        return life <= 0;
    }
}
