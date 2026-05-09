package ui.views.pages;

import application.CookieRunApp;

import core.abilities.CookieAbility;

import core.stages.StageList;

import game.GameController;

import graphics.rendering.ScrollingLayer;

import ui.views.overlays.GameOverPane;
import ui.views.util.FontLoader;

import javafx.animation.AnimationTimer;

import javafx.geometry.Pos;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.control.Button;

import javafx.scene.image.Image;

import javafx.scene.input.KeyCode;

import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class GameStage extends StackPane {

    private static final double W =
            CookieRunApp.WIDTH;

    private static final double H =
            CookieRunApp.HEIGHT;

    private static final double GY =
            GameController.GROUND_Y;

    private final CookieRunApp app;

    private final GameController ctrl;

    private final Canvas canvas;

    private final int cookieIndex;

    private final ScrollingLayer bgLayer;

    private long lastNano;

    private final Button menuButton;

    public GameStage(
            CookieRunApp app,
            int cookieIndex,
            core.entities.base.Cookie cookie
    ) {

        this.app = app;

        this.cookieIndex = cookieIndex;

        core.stages.Stage stage =
                StageList.getStage(
                        app.getSelectedStageIndex()
                );

        this.ctrl =
                new GameController(cookie, stage);

        this.canvas = new Canvas(W, H);

        this.lastNano = 0;

        // Background layer
        this.bgLayer =
                new ScrollingLayer(W, 0, H, 80);

        this.getChildren().add(canvas);

        this.setFocusTraversable(true);

        // Main Menu Button
        menuButton = new Button("Main Menu");

        menuButton.setVisible(false);

        menuButton.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: linear-gradient(#FFD95A,#F4B400);" +
                        "-fx-text-fill: #2B1B00;" +
                        "-fx-background-radius: 18;" +
                        "-fx-border-radius: 18;" +
                        "-fx-border-color: #FFF3B0;" +
                        "-fx-border-width: 2;" +
                        "-fx-padding: 12 30 12 30;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.45), 10,0,0,4);"
        );

        menuButton.setOnAction(e -> {
            app.showHomePage();
        });

        StackPane.setAlignment(menuButton,
                Pos.CENTER);

        menuButton.setTranslateY(80);

        getChildren().add(menuButton);

        loadImages();

        bindInput();
    }

    private void loadImages() {

        try {

            var stage =
                    StageList.getStage(
                            app.getSelectedStageIndex()
                    );

            var bgStream =
                    getClass().getResourceAsStream(
                            stage.getPlayBackgroundPath()
                    );

            if (bgStream != null) {

                bgLayer.setImage(
                        new Image(bgStream)
                );
            }

        } catch (Exception e) {

            System.err.println(
                    "Failed to load background: "
                            + e.getMessage()
            );
        }
    }

    private void bindInput() {

        this.requestFocus();

        this.setOnKeyPressed(e -> {

            KeyCode k = e.getCode();

            if (k == KeyCode.SPACE
                    || k == KeyCode.UP) {

                ctrl.onJump();
            }

            else if (k == KeyCode.DOWN
                    || k == KeyCode.S) {

                ctrl.onSlide();
            }

            else if (k == KeyCode.E) {

                CookieAbility ability =
                        ctrl.getCookie().getAbility();

                if (ability != null
                        && !ability.isPassive()) {

                    ctrl.onAbility();
                }
            }

            else if (k == KeyCode.P) {

                ctrl.togglePause();
            }
        });

        this.setOnKeyReleased(e -> {

            if (e.getCode() == KeyCode.DOWN
                    || e.getCode() == KeyCode.S) {

                ctrl.onReleaseSlide();
            }
        });

        canvas.setOnMouseClicked(e -> {

            if (!ctrl.isGameOver()) {

                if (e.getY() < H / 2) {

                    ctrl.onJump();
                }
                else {

                    ctrl.onSlide();
                }
            }
        });

        canvas.setOnMouseReleased(
                e -> ctrl.onReleaseSlide()
        );
    }

    public void startGame() {

        new AnimationTimer() {

            @Override
            public void handle(long now) {

                if (lastNano == 0) {

                    lastNano = now;

                    return;
                }

                double delta = Math.min(
                        (now - lastNano)
                                / 1_000_000_000.0,
                        0.05
                );

                lastNano = now;

                ctrl.update(delta);

                bgLayer.update(delta, 0);

                render();

                if (ctrl.isGameOver()
                        && getChildren().size() == 2) {

                    showGameOver();

                    stop();
                }
            }

        }.start();

        this.requestFocus();
    }

    private void render() {


        GraphicsContext gc =
                canvas.getGraphicsContext2D();
        double shake = ctrl.getCameraShake();

        double offsetX = 0;
        double offsetY = 0;

        if (shake > 0) {

            offsetX =
                    (Math.random() - 0.5)
                            * shake;

            offsetY =
                    (Math.random() - 0.5)
                            * shake;
        }

        gc.save();

        gc.translate(offsetX, offsetY);

        bgLayer.render(
                gc,
                Color.web("#FFB6FF")
        );

        ctrl.render(gc);

        drawHUD(gc);

        drawAbilityHUD(gc);

        if (ctrl.isPaused()) {

            drawPauseOverlay(gc);

            menuButton.setVisible(true);
        }
        else {

            menuButton.setVisible(false);
        }

        drawRedOverlay(gc);
        gc.restore();
    }

    private void drawHUD(GraphicsContext gc) {

        gc.setFont(
                FontLoader.getCookieRunBold(18)
        );

        // Score shadow
        gc.setFill(Color.BLACK);

        gc.fillText(
                "Score: " + ctrl.getScore(),
                22,
                32
        );

        // Score outline
        gc.setStroke(Color.BLACK);

        gc.setLineWidth(2);

        gc.strokeText(
                "Score: " + ctrl.getScore(),
                20,
                30
        );

        // Score fill
        gc.setFill(Color.WHITE);

        gc.fillText(
                "Score: " + ctrl.getScore(),
                20,
                30
        );

        // Coins
        gc.setFont(
                FontLoader.getCookieRunBold(16)
        );

        gc.setFill(Color.BLACK);

        gc.fillText(
                "Coins: " + ctrl.getCoins(),
                22,
                57
        );

        gc.setStroke(Color.BLACK);

        gc.setLineWidth(2);

        gc.strokeText(
                "Coins: " + ctrl.getCoins(),
                20,
                55
        );

        gc.setFill(Color.WHITE);

        gc.fillText(
                "Coins: " + ctrl.getCoins(),
                20,
                55
        );

        // HP Bar
        double hp = ctrl.getCookie().getHp();

        double maxHp =
                ctrl.getCookie().getMaxHpValue();

        double x = 20;
        double y = 70;

        double barWidth = 150;
        double barHeight = 20;

        double hpWidth =
                (hp / maxHp) * barWidth;

        Color hpColor =
                hp > maxHp / 2
                        ? Color.web("#2ECC71")
                        : hp > maxHp / 4
                        ? Color.web("#F39C12")
                        : Color.web("#E74C3C");

        gc.setFill(hpColor);

        gc.fillRoundRect(
                x,
                y,
                hpWidth,
                barHeight,
                10,
                10
        );

        gc.setStroke(Color.WHITE);

        gc.strokeRoundRect(
                x,
                y,
                barWidth,
                barHeight,
                10,
                10
        );

        gc.setFill(Color.WHITE);

        gc.setFont(
                Font.font(
                        "Arial",
                        FontWeight.BOLD,
                        12
                )
        );

        gc.fillText(
                "HP " + (int) hp
                        + " / "
                        + (int) maxHp,
                x + 70,
                y + 14
        );
    }

    private void drawAbilityHUD(GraphicsContext gc) {
        CookieAbility ability =
                ctrl.getCookie().getAbility();
        if (ability == null) return;
        if (ability.isPassive()) {
            return;
        }

        double x = W - 120;
        double y = H - 120;

        double size = 90;

        // Outer glow
        gc.setFill(Color.rgb(255, 215, 0, 0.25));
        gc.fillOval(x - 6, y - 6, size + 12, size + 12);

        // Outer circle
        gc.setFill(Color.rgb(20,20,20,0.88));
        gc.fillOval(x, y, size, size);

        // Inner circle
        gc.setFill(Color.web("#2C3E50"));
        gc.fillOval(x + 6, y + 6,
                size - 12,
                size - 12);

        // Cooldown overlay
        if (ability.getCooldownRemaining() > 0) {

            double percent =
                    ability.getCooldownPercent();

            double overlayHeight =
                    size * (1 - percent);

            gc.setFill(Color.rgb(0,0,0,0.68));

            gc.fillArc(
                    x,
                    y,
                    size,
                    size,
                    90,
                    -360 * percent,
                    javafx.scene.shape.ArcType.ROUND
            );

            gc.setFill(Color.WHITE);

            gc.setFont(Font.font(
                    "Arial",
                    FontWeight.EXTRA_BOLD,
                    20
            ));

            gc.setTextAlign(TextAlignment.CENTER);

            gc.fillText(
                    String.format(
                            "%.0f",
                            Math.ceil(
                                    ability.getCooldownRemaining()
                            )
                    ),
                    x + size / 2,
                    y + size / 2 + 8
            );
        }
        else {

            // READY glow
            gc.setFill(Color.LIME);

            gc.setFont(Font.font(
                    "Arial",
                    FontWeight.EXTRA_BOLD,
                    16
            ));

            gc.setTextAlign(TextAlignment.CENTER);

            gc.fillText(
                    "READY",
                    x + size / 2,
                    y + size / 2 + 6
            );
        }

        // Keybind bubble
        gc.setFill(Color.rgb(0,0,0,0.8));

        gc.fillOval(
                x + size/2 - 14,
                y + size + 5,
                28,
                28
        );

        gc.setFill(Color.WHITE);

        gc.setFont(Font.font(
                "Arial",
                FontWeight.BOLD,
                16
        ));

        gc.fillText(
                "E",
                x + size / 2,
                y + size + 24
        );

        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void drawPauseOverlay(GraphicsContext gc) {

        gc.setFill(Color.rgb(0,0,0,0.58));
        gc.fillRect(0,0,W,H);

        double panelW = 420;
        double panelH = 260;

        double px = W/2 - panelW/2;
        double py = H/2 - panelH/2;

        // Shadow
        gc.setFill(Color.rgb(0,0,0,0.4));
        gc.fillRoundRect(
                px + 6,
                py + 6,
                panelW,
                panelH,
                35,
                35
        );

        // Main panel
        gc.setFill(Color.rgb(40,25,15,0.92));
        gc.fillRoundRect(
                px,
                py,
                panelW,
                panelH,
                35,
                35
        );

        // Gold border
        gc.setStroke(Color.web("#FFD95A"));
        gc.setLineWidth(4);

        gc.strokeRoundRect(
                px,
                py,
                panelW,
                panelH,
                35,
                35
        );

        gc.setTextAlign(TextAlignment.CENTER);

        // Title
        gc.setFont(FontLoader.getCookieRunBold(58));

        gc.setFill(Color.BLACK);

        gc.fillText(
                "PAUSED",
                W/2 + 3,
                py + 88
        );

        gc.setFill(Color.web("#FFD700"));

        gc.fillText(
                "PAUSED",
                W/2,
                py + 84
        );

        // Subtitle
        gc.setFill(Color.WHITE);

        gc.setFont(Font.font(
                "Arial",
                FontWeight.SEMI_BOLD,
                20
        ));

        gc.fillText(
                "Press P to resume",
                W/2,
                py + 135
        );

        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void drawRedOverlay(
            GraphicsContext gc
    ) {

        double opacity =
                ctrl.getRedOverlayOpacity();

        if (opacity > 0) {

            gc.setFill(
                    Color.web(
                            "#FF0000",
                            opacity * 0.3
                    )
            );

            gc.fillRect(0, 0, W, H);
        }
    }

    private void showGameOver() {

        GameOverPane gameOverPane =
                new GameOverPane();

        gameOverPane.setScore(
                ctrl.getScore()
        );

        gameOverPane.setCoins(
                ctrl.getCoins()
        );

        gameOverPane.setOnRetry(
                () -> app.startGame(cookieIndex)
        );

        gameOverPane.setOnHome(
                () -> app.showHomePage()
        );

        VBox overlay = new VBox();

        overlay.setAlignment(Pos.CENTER);

        overlay.getChildren().add(gameOverPane);

        getChildren().add(overlay);
    }
}