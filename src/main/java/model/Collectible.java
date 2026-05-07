package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

/**
 * Collectible items (coins and jellies).
 * Extends GameObject → Renderable, Updatable, Collidable, Spawnable.
 */
public class Collectible extends GameObject {

    public enum Type { COIN, JELLY_SMALL, JELLY_BIG }

    private final Type   type;
    private final double speed;
    private final int    scoreValue;
    private double       glowTimer = 0;

    public Collectible(double x, double y, double speed, Type type) {
        super(x, y, sizeOf(type), sizeOf(type));
        this.type       = type;
        this.speed      = speed;
        this.scoreValue = scoreOf(type);
    }

    private static double sizeOf(Type t) {
        return switch (t) { case COIN -> 22; case JELLY_SMALL -> 20; case JELLY_BIG -> 32; };
    }
    private static int scoreOf(Type t) {
        return switch (t) { case COIN -> 10; case JELLY_SMALL -> 50; case JELLY_BIG -> 200; };
    }

    @Override
    public void update(double delta) {
        x -= speed * delta;
        glowTimer += delta * 4.0;
        if (x + width < -10) alive = false;
    }

    @Override
    public void render(GraphicsContext gc) {
        double glow = 0.7 + 0.3 * Math.sin(glowTimer);
        switch (type) {
            case COIN        -> drawCoin(gc, glow);
            case JELLY_SMALL -> drawJelly(gc, glow, false);
            case JELLY_BIG   -> drawJelly(gc, glow, true);
        }
    }

    private void drawCoin(GraphicsContext gc, double glow) {
        gc.setFill(Color.color(glow, glow * 0.78, 0));
        gc.fillOval(x, y, width, height);
        gc.setFill(Color.web("#FFD700"));
        gc.fillOval(x + 3, y + 3, width - 6, height - 6);
        gc.setFill(Color.web("#CC8800"));
        gc.setFont(javafx.scene.text.Font.font(10));
        gc.fillText("★", x + 4, y + width - 5);
    }

    private void drawJelly(GraphicsContext gc, double glow, boolean big) {
        Color base  = big ? Color.web("#FF3399") : Color.web("#33CCFF");
        Color light = big ? Color.web("#FF99CC") : Color.web("#99EEFF");
        gc.setFill(Color.color(base.getRed(), base.getGreen(), base.getBlue(), 0.9 * glow));
        gc.fillOval(x, y + height * 0.2, width, height * 0.8);
        gc.fillArc(x, y, width, height * 0.6, 0, 180, ArcType.CHORD);
        gc.setFill(light);
        gc.fillOval(x + width * 0.2, y + height * 0.1, width * 0.3, height * 0.2);
        gc.setFill(Color.web("#333333"));
        gc.fillOval(x + width * 0.28, y + height * 0.4, 4, 4);
        gc.fillOval(x + width * 0.55, y + height * 0.4, 4, 4);
    }

    public int  getScoreValue() { return scoreValue; }
    public Type getType()       { return type; }
}
