package entity.objects;

import entity.base.Physics;
import entity.base.Obstacle;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Ground obstacle — player must JUMP to avoid.
 * Extends Obstacle → GameObject (Renderable, Updatable, Collidable, Spawnable).
 */
public class GroundObstacle extends Obstacle {

    public enum Type { CANDY_WALL, SPIKE, BLOCK }

    private final Type  type;
    private final Color primary;
    private final Color accent;

    public GroundObstacle(double x, double speed, Type type) {
        super(x, 0, widthOf(type), heightOf(type), speed);
        this.type    = type;
        this.primary = primaryOf(type);
        this.accent  = accentOf(type);
        this.y       = Physics.GROUND_Y - this.height;
    }

    private static double widthOf(Type t) {
        return switch (t) { case CANDY_WALL -> 30; case SPIKE -> 44; case BLOCK -> 52; };
    }
    private static double heightOf(Type t) {
        return switch (t) { case CANDY_WALL -> 80; case SPIKE -> 56; case BLOCK -> 46; };
    }
    private static Color primaryOf(Type t) {
        return switch (t) {
            case CANDY_WALL -> Color.web("#E63B6F");
            case SPIKE      -> Color.web("#8B2FC9");
            case BLOCK      -> Color.web("#1E90C8");
        };
    }
    private static Color accentOf(Type t) {
        return switch (t) {
            case CANDY_WALL -> Color.WHITE;
            case SPIKE      -> Color.web("#D580FF");
            case BLOCK      -> Color.web("#A0D8F8");
        };
    }

    @Override
    public void render(GraphicsContext gc) {
        switch (type) {
            case CANDY_WALL -> drawCandyWall(gc);
            case SPIKE      -> drawSpike(gc);
            case BLOCK      -> drawBlock(gc);
        }
    }

    private void drawCandyWall(GraphicsContext gc) {
        gc.setFill(primary);
        gc.fillRoundRect(x, y, width, height, 8, 8);
        gc.setFill(accent);
        for (int i = 0; i < 4; i++) gc.fillRect(x + 4, y + 8 + i * 18, width - 8, 7);
        gc.setFill(Color.web("#FFD700"));
        gc.fillOval(x + width / 2 - 10, y - 12, 20, 20);
        gc.setFill(primary);
        gc.fillOval(x + width / 2 - 6,  y - 8,  12, 12);
    }

    private void drawSpike(GraphicsContext gc) {
        double cx = x + width / 2;
        gc.setFill(primary);
        gc.fillPolygon(new double[]{cx, cx - 14, cx + 14},
                       new double[]{y,  y + height, y + height}, 3);
        gc.fillPolygon(new double[]{cx - 10, cx - 22, cx - 2},
                       new double[]{y + 20,  y + height, y + height}, 3);
        gc.fillPolygon(new double[]{cx + 10, cx + 2,  cx + 22},
                       new double[]{y + 20,  y + height, y + height}, 3);
        gc.setFill(accent);
        gc.fillOval(cx - 3, y + 5, 6, 10);
    }

    private void drawBlock(GraphicsContext gc) {
        gc.setFill(primary);
        gc.fillRoundRect(x, y, width, height, 10, 10);
        gc.setFill(accent);
        gc.fillRoundRect(x + 5, y + 5, width - 10, height - 10, 6, 6);
        gc.setFill(Color.WHITE);
        gc.fillOval(x + width / 2 - 3, y + height / 2 - 3, 6, 6);
    }

    public Type getType() { return type; }
}
