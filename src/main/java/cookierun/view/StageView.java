package cookierun.view;

import cookierun.CookieRunApp;
import cookierun.controller.GameController;
import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/**
 * StageView: JavaFX AnimationTimer game loop, canvas rendering, HUD.
 */
public class StageView {

    private static final double W = CookieRunApp.WIDTH;
    private static final double H = CookieRunApp.HEIGHT;
    private static final double GY = GameController.GROUND_Y;

    private final CookieRunApp     app;
    private final GameController   ctrl;
    private final StackPane        root;
    private final Canvas           canvas;
    private AnimationTimer         loop;
    private long                   lastNano = 0;

    private double bgOff = 0, groundOff = 0;
    private VBox   gameOverOverlay;

    public StageView(CookieRunApp app, int cookieIndex) {
        this.app    = app;
        this.ctrl   = new GameController(cookieIndex);
        this.canvas = new Canvas(W, H);
        buildGameOver();
        root = new StackPane(canvas);
        bindInput();

    }


    // ---- Input ----

    private void bindInput() {
        root.setFocusTraversable(true);
        root.requestFocus();
        root.setOnKeyPressed(e -> {
            KeyCode k = e.getCode();
            if (k == KeyCode.SPACE || k == KeyCode.UP)            ctrl.onJump();
            else if (k == KeyCode.DOWN || k == KeyCode.S)         ctrl.onSlide();
            else if (k == KeyCode.P || k == KeyCode.ESCAPE)       ctrl.togglePause();
        });
        root.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) ctrl.onReleaseSlide();
        });
        canvas.setOnMouseClicked(e -> {
            if (ctrl.isGameOver()) return;
            if (e.getY() < H / 2) ctrl.onJump(); else ctrl.onSlide();
        });
        canvas.setOnMouseReleased(e -> ctrl.onReleaseSlide());
    }

    // ---- Game Loop ----

    public void startGame() {
        loop = new AnimationTimer() {
            @Override public void handle(long now) {
                if (lastNano == 0) { lastNano = now; return; }
                double delta = Math.min((now - lastNano) / 1_000_000_000.0, 0.05);
                lastNano = now;

                ctrl.update(delta);
                bgOff     = (bgOff     + delta * 80)   % W;
                groundOff = (groundOff + delta * (300 + ctrl.getGameTime() * 16)) % 60;

                draw(canvas.getGraphicsContext2D());

                if (ctrl.isGameOver() && !root.getChildren().contains(gameOverOverlay)) {
                    updateGameOverLabels();
                    root.getChildren().add(gameOverOverlay);
                    stop();
                }
            }
        };
        loop.start();
        root.requestFocus();
    }

    // ---- Rendering ----

    private void draw(GraphicsContext gc) {
        drawSky(gc);
        drawMidground(gc);
        drawGround(gc);
        ctrl.render(gc);         // polymorphic: each object renders itself
        drawHUD(gc);
        if (ctrl.isPaused()) drawPauseOverlay(gc);
    }

    private void drawSky(GraphicsContext gc) {
        gc.setFill(new LinearGradient(0,0,0,1,true,CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#1A0A3C")),
                new Stop(0.65, Color.web("#8B2FC9")),
                new Stop(1, Color.web("#CC5522"))));
        gc.fillRect(0, 0, W, H);

        // Moving clouds
        for (int i = 0; i < 5; i++) {
            double cx = ((i * 190 + W - bgOff * 0.35) % (W + 120)) - 60;
            double cy = 35 + i * 28;
            gc.setFill(Color.web("#FFFFFF", 0.10));
            gc.fillOval(cx, cy, 100, 38);
            gc.fillOval(cx + 22, cy - 14, 65, 32);
        }
    }

    private void drawMidground(GraphicsContext gc) {
        double bx = ((W * 0.55 - bgOff * 0.18) % (W + 160) + W + 160) % (W + 160) - 80;
        gc.setFill(Color.web("#3D1A6E", 0.55));
        gc.fillRect(bx, 185, 85, 105);
        gc.fillRect(bx + 10, 168, 22, 34);
        gc.fillRect(bx + 52, 172, 22, 30);
        gc.fillRect(bx - 22, 200, 42, 90);
    }

    private void drawGround(GraphicsContext gc) {
        gc.setFill(new LinearGradient(0, GY, 0, H, false, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#7B4A1F")),
                new Stop(1, Color.web("#3D1A08"))));
        gc.fillRect(0, GY, W, H - GY);
        // Top edge
        gc.setFill(Color.web("#D8820A"));
        gc.fillRect(0, GY, W, 8);
        // Bricks
        gc.setFill(Color.web("#8B4513", 0.45));
        for (int i = -1; i < (int)(W / 60) + 2; i++) {
            double bx = i * 60 - groundOff % 60;
            gc.fillRect(bx + 2, GY + 10, 56, 20);
            gc.fillRect(bx + 32, GY + 33, 56, 20);
        }
    }

    private void drawHUD(GraphicsContext gc) {
        gc.setFill(Color.web("#000000", 0.42));
        gc.fillRect(0, 0, W, 46);

        gc.setFill(Color.web("#FFD700"));
        gc.setFont(Font.font("Impact", FontWeight.BOLD, 22));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("★ " + ctrl.getScore(), 16, 31);

        gc.setFill(Color.web("#FFD700"));
        gc.fillOval(W / 2.0 - 52, 12, 18, 18);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        gc.fillText("× " + ctrl.getCoins(), W / 2.0 - 28, 27);

        gc.setFill(Color.web("#AAAAFF"));
        gc.setFont(Font.font("Arial", 13));
        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText(String.format("%.1fs", ctrl.getGameTime()), W - 65, 28);
        gc.setFill(Color.web("#FFFFFF", 0.55));
        gc.setFont(Font.font("Arial", 11));
        gc.fillText("[P] Pause", W - 6, 28);
        // HP BAR
        double hp = ctrl.getCookie().getHp();
        double maxHp = ctrl.getCookie().getMaxHp();

        double barWidth = 220;
        double barHeight = 18;
        double x = 16;
        double y = 52;

// background
        gc.setFill(Color.web("#330000"));
        gc.fillRoundRect(x, y, barWidth, barHeight, 10, 10);

// hp fill
        double currentWidth = (hp / maxHp) * barWidth;

        Color hpColor;

        if (hp > 60) {
            hpColor = Color.LIMEGREEN;
        }
        else if (hp > 30) {
            hpColor = Color.GOLD;
        }
        else {
            hpColor = Color.RED;
        }

        gc.setFill(hpColor);
        gc.fillRoundRect(x, y, currentWidth, barHeight, 10, 10);

// border
        gc.setStroke(Color.WHITE);
        gc.strokeRoundRect(x, y, barWidth, barHeight, 10, 10);

// text
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        gc.fillText("HP " + (int) hp + " / " + (int) maxHp, x + 70, y + 14);

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
        gc.setFill(Color.web("#FFD700"));
        gc.setFont(Font.font("Impact", 62));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("PAUSED", W / 2, H / 2 + 22);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 18));
        gc.fillText("Press P to resume", W / 2, H / 2 + 58);
        gc.setTextAlign(TextAlignment.LEFT);
    }

    // ---- Game Over Overlay ----

    private void buildGameOver() {
        gameOverOverlay = new VBox(16);
        gameOverOverlay.setAlignment(Pos.CENTER);
        gameOverOverlay.setMaxWidth(340);
        gameOverOverlay.setMaxHeight(290);
        gameOverOverlay.setStyle(
            "-fx-background-color:rgba(0,0,0,0.72);" +
            "-fx-padding:38 55 38 55;-fx-background-radius:20;");

        Label t = new Label("GAME OVER");
        t.setFont(Font.font("Impact", FontWeight.BOLD, 48));
        t.setTextFill(Color.web("#FF3300"));
        t.setStyle("-fx-effect:dropshadow(gaussian,#FF6600,10,0.5,2,2);");

        Label scoreLbl = new Label("Score: 0");
        scoreLbl.setId("goScore");
        scoreLbl.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        scoreLbl.setTextFill(Color.web("#FFD700"));

        Label coinsLbl = new Label("Coins: 0");
        coinsLbl.setId("goCoins");
        coinsLbl.setFont(Font.font("Arial", 16));
        coinsLbl.setTextFill(Color.web("#FFD700"));

        Button retry = btn("▶  Play Again", "#FF8C00", "#FF4500");
        retry.setOnAction(e -> app.startGame(ctrl.getCookie().getCookieIndex()));

        Button home = btn("⌂  Home", "#555555", "#333333");
        home.setOnAction(e -> app.showHomePage());

        HBox row = new HBox(14, retry, home);
        row.setAlignment(Pos.CENTER);

        gameOverOverlay.getChildren().addAll(t, scoreLbl, coinsLbl, row);
    }

    private void updateGameOverLabels() {
        gameOverOverlay.lookupAll("#goScore")
                .forEach(n -> ((Label) n).setText("Score: " + ctrl.getScore()));
        gameOverOverlay.lookupAll("#goCoins")
                .forEach(n -> ((Label) n).setText("Coins:  " + ctrl.getCoins()));
    }

    private Button btn(String text, String c1, String c2) {
        Button b = new Button(text);
        b.setStyle(String.format(
            "-fx-background-color:linear-gradient(to bottom,%s,%s);" +
            "-fx-text-fill:white;-fx-font-size:15px;-fx-font-weight:bold;" +
            "-fx-padding:10 26;-fx-background-radius:20;-fx-cursor:hand;", c1, c2));
        return b;
    }

    public Pane getRoot() { return root; }
}
