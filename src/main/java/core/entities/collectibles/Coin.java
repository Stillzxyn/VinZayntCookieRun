package core.entities.collectibles;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Coin collectible - 10 points, 22px size
 * Attracted by magnetic cookies
 */
public class Coin extends Collectible {

    private static final double SIZE = 22;
    private static final int SCORE = 10;

    public Coin(double x, double y, double speed) {
        super(x, y, SIZE, speed, SCORE);
    }

    @Override
    protected boolean isMagnetic() {
        return true;  // Coins are magnetic
    }

    @Override
    public void render(GraphicsContext gc) {
        double glow = 0.7 + 0.3 * Math.sin(glowTimer);

        gc.setFill(Color.color(glow, glow * 0.78, 0));
        gc.fillOval(x, y, width, height);

        gc.setFill(Color.web("#FFD700"));
        gc.fillOval(x + 3, y + 3, width - 6, height - 6);

        gc.setFill(Color.web("#CC8800"));
        gc.setFont(javafx.scene.text.Font.font(10));
        gc.fillText("★", x + 4, y + width - 5);
    }
}
