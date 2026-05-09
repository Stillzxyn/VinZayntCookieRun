package ui.views.pages;

import application.CookieRunApp;
import core.entities.base.Cookie;
import core.stages.StageList;
import game.GameController;
import graphics.rendering.ScrollingLayer;
import ui.views.overlays.GameOverPane;
import ui.views.util.FontLoader;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameStage extends StackPane {

    private static final double W = CookieRunApp.WIDTH, H = CookieRunApp.HEIGHT, GY = GameController.GROUND_Y;

    private final CookieRunApp app;
    private final GameController ctrl;
    private final Canvas canvas;
    private final int cookieIndex;
    private final ScrollingLayer bgLayer;
    private long lastNano;

    public GameStage(CookieRunApp app, int cookieIndex, Cookie cookie) {
        this.app = app;
        this.cookieIndex = cookieIndex;
        core.stages.Stage stage = core.stages.StageList.getStage(app.getSelectedStageIndex());
        this.ctrl = new GameController(cookie, stage);
        this.canvas = new Canvas(W, H);
        this.lastNano = 0;

        // Create scrolling layer for full background (bg + floor combined)
        this.bgLayer = new ScrollingLayer(W, 0, H, 80);

        this.getChildren().add(canvas);
        this.setFocusTraversable(true);

        loadImages();
        bindInput();
    }

    private void loadImages() {
        try {
            var stage = StageList.getStage(app.getSelectedStageIndex());
            var bgStream = getClass().getResourceAsStream(stage.getPlayBackgroundPath());
            if (bgStream != null) bgLayer.setImage(new Image(bgStream));
        } catch (Exception e) {
            System.err.println("Failed to load background: " + e.getMessage());
        }
    }

    private void bindInput() {
        this.requestFocus();
        this.setOnKeyPressed(e -> {
            KeyCode k = e.getCode();
            if (k == KeyCode.SPACE || k == KeyCode.UP) ctrl.onJump();
            else if (k == KeyCode.DOWN || k == KeyCode.S) ctrl.onSlide();
            else if (k == KeyCode.P) ctrl.onAbility();
            else if (k == KeyCode.ESCAPE) ctrl.togglePause();
        });
        this.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) ctrl.onReleaseSlide();
        });
        canvas.setOnMouseClicked(e -> {
            if (!ctrl.isGameOver()) {
                if (e.getY() < H / 2) ctrl.onJump();
                else ctrl.onSlide();
            }
        });
        canvas.setOnMouseReleased(e -> ctrl.onReleaseSlide());
    }

    public void startGame() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastNano == 0) {
                    lastNano = now;
                    return;
                }
                double delta = Math.min((now - lastNano) / 1_000_000_000.0, 0.05);
                lastNano = now;

                ctrl.update(delta);
                bgLayer.update(delta, 0);

                render();

                if (ctrl.isGameOver() && getChildren().size() == 1) {
                    showGameOver();
                    stop();
                }
            }
        }.start();
        this.requestFocus();
    }

    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        bgLayer.render(gc, Color.web("#FFB6FF"));
        ctrl.render(gc);
        drawHUD(gc);
        if (ctrl.isPaused()) drawPauseOverlay(gc);
        drawRedOverlay(gc);
    }

    private void drawHUD(GraphicsContext gc) {
        // Score text - custom font, white fill with black shadow and stroke
        gc.setFont(FontLoader.getCookieRunBold(18));

        // Black shadow
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + ctrl.getScore(), 22, 32);

        // White stroke (outline)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeText("Score: " + ctrl.getScore(), 20, 30);

        // White fill
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + ctrl.getScore(), 20, 30);

        // Coins text - custom font, white fill with black shadow and stroke
        gc.setFont(FontLoader.getCookieRunBold(16));

        // Black shadow
        gc.setFill(Color.BLACK);
        gc.fillText("Coins: " + ctrl.getCoins(), 22, 57);

        // White stroke (outline)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeText("Coins: " + ctrl.getCoins(), 20, 55);

        // White fill
        gc.setFill(Color.WHITE);
        gc.fillText("Coins: " + ctrl.getCoins(), 20, 55);

        double hp = ctrl.getCookie().getHp();
        double maxHp = ctrl.getCookie().getMaxHpValue();
        double x = 20, y = 70, barWidth = 150, barHeight = 20;
        double hpWidth = (hp / maxHp) * barWidth;

        Color hpColor = hp > maxHp / 2 ? Color.web("#2ECC71") :
                        hp > maxHp / 4 ? Color.web("#F39C12") : Color.web("#E74C3C");
        gc.setFill(hpColor);
        gc.fillRoundRect(x, y, hpWidth, barHeight, 10, 10);
        gc.setStroke(Color.WHITE);
        gc.strokeRoundRect(x, y, barWidth, barHeight, 10, 10);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        gc.fillText("HP " + (int) hp + " / " + (int) maxHp, x + 70, y + 14);

        // Difficulty display
        gc.setFont(FontLoader.getCookieRunBold(12));
        gc.setFill(Color.web("#FFD700"));
        gc.fillText("Difficulty: " + ctrl.getDifficulty(), W - 220, 30);

        if (ctrl.getGameTime() < 4.0) {
            gc.setFill(Color.web("#FFFFFF", 0.75));
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            gc.setTextAlign(TextAlignment.CENTER);
            gc.fillText("SPACE / ↑  Jump       ↓  Slide", W / 2, H - 10);
        }
        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void drawPauseOverlay(GraphicsContext gc) {
        gc.setFill(Color.web("#000000", 0.52));
        gc.fillRect(0, 0, W, H);
        gc.setTextAlign(TextAlignment.CENTER);

        // PAUSED text with custom font, shadow, stroke, and fill
        gc.setFont(FontLoader.getCookieRunBold(62));

        // Black shadow
        gc.setFill(Color.BLACK);
        gc.fillText("PAUSED", W / 2 + 2, H / 2 + 24);

        // Black stroke (outline)
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeText("PAUSED", W / 2, H / 2 + 22);

        // Gold fill
        gc.setFill(Color.web("#FFD700"));
        gc.fillText("PAUSED", W / 2, H / 2 + 22);

        // Resume instruction
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 18));
        gc.fillText("Press ESC to resume", W / 2, H / 2 + 58);
        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void drawRedOverlay(GraphicsContext gc) {
        double opacity = ctrl.getRedOverlayOpacity();
        if (opacity > 0) {
            gc.setFill(Color.web("#FF0000", opacity * 0.3));  // Red at 30% opacity per stage
            gc.fillRect(0, 0, W, H);
        }
    }

    private void showGameOver() {
        GameOverPane gameOverPane = new GameOverPane();
        gameOverPane.setScore(ctrl.getScore());
        gameOverPane.setCoins(ctrl.getCoins());
        gameOverPane.setOnRetry(() -> app.startGame(cookieIndex));
        gameOverPane.setOnHome(() -> app.showHomePage());

        VBox overlay = new VBox();
        overlay.setAlignment(Pos.CENTER);
        overlay.getChildren().add(gameOverPane);
        getChildren().add(overlay);
    }
}
