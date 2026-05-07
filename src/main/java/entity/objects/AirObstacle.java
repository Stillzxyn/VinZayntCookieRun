package entity.objects;

import entity.base.Obstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Airborne obstacle — player must SLIDE to avoid.
 * Overrides updateBehavior() for bobbing animation (polymorphism hook).
 */
public class AirObstacle extends Obstacle {

    public enum Type { FIREBALL, BAT, CLOUD_SPIKE }

    private final Type  type;
    private final Color primary;
    private final Color accent;
    private double bobTimer = 0;
    private double baseY;

    public AirObstacle(double x, double speed, Type type) {
        super(x, baseYOf(type), widthOf(type), heightOf(type), speed);
        this.type    = type;
        this.baseY   = this.y;
        this.primary = primaryOf(type);
        this.accent  = accentOf(type);
    }

    private static double baseYOf(Type t) {
        return switch (t) { case FIREBALL -> 195; case BAT -> 170; case CLOUD_SPIKE -> 182; };
    }
    private static double widthOf(Type t) {
        return switch (t) { case FIREBALL -> 46; case BAT -> 56; case CLOUD_SPIKE -> 62; };
    }
    private static double heightOf(Type t) {
        return switch (t) { case FIREBALL -> 46; case BAT -> 36; case CLOUD_SPIKE -> 42; };
    }
    private static Color primaryOf(Type t) {
        return switch (t) {
            case FIREBALL    -> Color.web("#FF5500");
            case BAT         -> Color.web("#4A1060");
            case CLOUD_SPIKE -> Color.web("#AAAACC");
        };
    }
    private static Color accentOf(Type t) {
        return switch (t) {
            case FIREBALL    -> Color.web("#FFD700");
            case BAT         -> Color.web("#CC66FF");
            case CLOUD_SPIKE -> Color.WHITE;
        };
    }

    /** Polymorphism: overrides the hook from Obstacle to add bobbing */
    @Override
    protected void updateBehavior(double delta) {
        bobTimer += delta * 3.0;
        y = baseY + Math.sin(bobTimer) * 8;
    }

    @Override
    public void render(GraphicsContext gc) {
        switch (type) {
            case FIREBALL    -> drawFireball(gc);
            case BAT         -> drawBat(gc);
            case CLOUD_SPIKE -> drawCloudSpike(gc);
        }
    }

    private void drawFireball(GraphicsContext gc) {
        gc.setFill(accent);
        gc.fillOval(x - 4, y - 4, width + 8, height + 8);
        gc.setFill(primary);
        gc.fillOval(x + 4, y + 4, width - 8, height - 8);
        gc.setFill(Color.WHITE);
        gc.fillOval(x + 14, y + 12, 10, 10);
        gc.setFill(Color.web("#FF8800", 0.7));
        gc.fillOval(x + width - 5, y + 10, 18, 14);
    }

    private void drawBat(GraphicsContext gc) {
        double cx = x + width / 2, cy = y + height / 2;
        gc.setFill(primary);
        gc.fillPolygon(new double[]{cx, cx-28, cx-10, cx-18, cx-5},
                       new double[]{cy, cy-15, cy+5,  cy+18, cy+10}, 5);
        gc.fillPolygon(new double[]{cx, cx+28, cx+10, cx+18, cx+5},
                       new double[]{cy, cy-15, cy+5,  cy+18, cy+10}, 5);
        gc.fillOval(cx - 10, cy - 12, 20, 22);
        gc.setFill(accent);
        gc.fillOval(cx - 7, cy - 8, 6, 6);
        gc.fillOval(cx + 1, cy - 8, 6, 6);
        gc.setFill(Color.RED);
        gc.fillOval(cx - 5, cy - 6, 3, 3);
        gc.fillOval(cx + 3, cy - 6, 3, 3);
    }

    private void drawCloudSpike(GraphicsContext gc) {
        gc.setFill(accent);
        gc.fillOval(x,      y + 15, 40, 22);
        gc.fillOval(x + 10, y + 8,  35, 25);
        gc.fillOval(x + 20, y + 15, 40, 22);
        gc.setFill(primary);
        double cx = x + width / 2;
        gc.fillPolygon(new double[]{cx - 8, cx + 8, cx},
                       new double[]{y + 28,  y + 28, y + height + 10}, 3);
    }

    public Type getType() { return type; }
}
