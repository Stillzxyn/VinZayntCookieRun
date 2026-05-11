package gui.components;

import game.GameController;
import gui.graphics.FontLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * AbilityHUD - show coin and score
 * draw the Hud of cookies Health
 */
public class HUD extends Pane {
    private final Canvas hudCanvas;
    private final GameController ctrl;

    public HUD(GameController ctrl, double width, double height) {
        this.ctrl = ctrl;
        this.hudCanvas = new Canvas(width, height);
        this.getChildren().add(hudCanvas);
        this.setMouseTransparent(true);
    }

    public void update() {
        GraphicsContext gc = hudCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, hudCanvas.getWidth(), hudCanvas.getHeight());

        gc.setFont(FontLoader.getCookieRunBold(18));
        drawOutlinedText(gc, "Score: " + ctrl.getScore(), 20, 30);
        gc.setFont(FontLoader.getCookieRunBold(16));
        drawOutlinedText(gc, "Coins: " + ctrl.getCoins(), 20, 55);

        drawHPBar(gc);
    }

    private void drawHPBar(GraphicsContext gc) {
        double hp = ctrl.getCookie().getHp();
        double maxHp = ctrl.getCookie().getMaxHpValue();
        double barWidth = 200;
        double hpPercent = (maxHp > 0) ? (hp / maxHp) : 0;
        double hpWidth = hpPercent * barWidth;

        Color hpColor;
        if (hpPercent > 0.75) {
            hpColor = Color.web("#2ECC71");
        } else if (hpPercent > 0.50) {
            hpColor = Color.web("#F1C40F");
        } else if (hpPercent > 0.25) {
            hpColor = Color.web("#E67E22");
        } else {
            hpColor = Color.web("#E74C3C");
        }
        gc.setFill(hpColor);
        gc.fillRoundRect(20, 70, hpWidth, 20, 10, 10);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRoundRect(20, 70, barWidth, 20, 10, 10);
        String hpText = "HP " + (int) hp + " / " + (int) maxHp;
        gc.setFont(FontLoader.getCookieRunBold(12));
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeText(hpText, 90, 84);
        gc.setFill(Color.WHITE);
        gc.fillText(hpText, 90, 84);
    }

    private void drawOutlinedText(GraphicsContext gc, String text, double x, double y) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeText(text, x, y);
        gc.setFill(Color.WHITE);
        gc.fillText(text, x, y);
    }
}