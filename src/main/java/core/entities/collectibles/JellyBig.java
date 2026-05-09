package core.entities.collectibles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * Big Jelly collectible - 200 points, 32px size
 * Pink jelly, not attracted by magnets
 */
public class JellyBig extends Collectible {

    private static final double SIZE = 32;
    private static final int SCORE = 200;

    public JellyBig(double x, double y, double speed) {
        super(x, y, SIZE, speed, SCORE);
    }

    @Override
    protected boolean isMagnetic() {
        return false;  // Jellies are not magnetic
    }

    @Override
    public void render(GraphicsContext gc) {
        double glow = 0.7 + 0.3 * Math.sin(glowTimer);

        Color base = Color.web("#FF3399");
        Color light = Color.web("#FF99CC");

        gc.setFill(Color.color(base.getRed(), base.getGreen(), base.getBlue(), 0.9 * glow));
        gc.fillOval(x, y + height * 0.2, width, height * 0.8);
        gc.fillArc(x, y, width, height * 0.6, 0, 180, ArcType.CHORD);

        gc.setFill(light);
        gc.fillOval(x + width * 0.2, y + height * 0.1, width * 0.3, height * 0.2);

        gc.setFill(Color.web("#333333"));
        gc.fillOval(x + width * 0.28, y + height * 0.4, 4, 4);
        gc.fillOval(x + width * 0.55, y + height * 0.4, 4, 4);
    }
}
