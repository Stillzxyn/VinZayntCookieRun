package gui.components;

import core.abilities.CookieAbility;
import game.GameController;
import gui.graphics.FontLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.text.TextAlignment;

/**
 * AbilityHUD - draw the Hud of cookies ability with cooldown system
 */
public class AbilityHUD extends Pane {
    private final Canvas canvas;
    private final GameController ctrl;
    private final double w, h;

    private static final double ABILITY_SIZE = 90;
    private static final double ABILITY_MARGIN = 120;

    public AbilityHUD(GameController ctrl, double width, double height) {
        this.ctrl = ctrl;
        this.w = width;
        this.h = height;
        this.canvas = new Canvas(width, height);
        this.getChildren().add(canvas);
        this.setMouseTransparent(true);
    }

    public void update() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);

        CookieAbility ability = ctrl.getCookie().getAbility();
        if (ability == null || ability.isPassive()) return;

        double x = w - ABILITY_MARGIN;
        double y = h - ABILITY_MARGIN;
        double size = ABILITY_SIZE;

        gc.setFill(Color.web("#ffd700"));
        gc.fillOval(x - 6, y - 6, size + 12, size + 12);

        gc.setFill(Color.web("#141414"));
        gc.fillOval(x, y, size, size);

        gc.setFill(Color.web("#2C3E50"));
        gc.fillOval(x + 6, y + 6, size - 12, size - 12);

        if (ability.getCooldownRemaining() > 0) {
            double percent = ability.getCooldownPercent();
            gc.setFill(Color.rgb(0, 0, 0, 0.68));
            gc.fillArc(x, y, size, size, 90, -360 * percent, ArcType.ROUND);

            gc.setFill(Color.WHITE);
            gc.setFont(FontLoader.getCookieRunBold(12));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText(String.format("%.0f", Math.ceil(ability.getCooldownRemaining())),
                    x + size / 2, y + size / 2 + 8);
        } else {
            gc.setFill(Color.LIME);
            gc.setFont(FontLoader.getCookieRunBold(16));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("READY", x + size / 2, y + size / 2 + 6);
        }

        gc.setFill(Color.rgb(0, 0, 0, 0.8));
        gc.fillOval(x + size / 2 - 14, y + size + 5, 28, 28);
        gc.setFill(Color.WHITE);
        gc.fillText("E", x + size / 2, y + size + 24);

        gc.setTextAlign(TextAlignment.LEFT);
    }
}