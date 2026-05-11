package gui.pages;

import application.CookieRunApp;
import audio.SoundManager;
import core.abilities.CookieAbility;
import core.stages.StageList;
import game.GameController;
import game.config.GameConfig;
import gui.baseElements.BaseImageButton;
import gui.components.HUD;
import gui.components.AbilityHUD;
import gui.components.PauseOverlayPane;
import gui.graphics.ScrollingLayer;
import gui.components.GameOverPane;
import gui.graphics.FontLoader;
import gamelogic.events.EventBus;
import gamelogic.events.CoinCollectedEvent;
import gamelogic.events.ObstacleHitEvent;
import utils.Renderable;
import utils.Updatable;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.text.TextAlignment;

/**
 * GameStage manages the visual assembly and lifecycle of a game session.
 * It uses a StackPane to layer the Canvas (game world) beneath UI components (HUD/Menu).
 */
public class GameStage extends StackPane {

    private static final double W = CookieRunApp.WIDTH;
    private static final double H = CookieRunApp.HEIGHT;

    //game stage
    private final CookieRunApp app;
    private final GameController ctrl;
    private final Canvas canvas;
    private final int cookieIndex;
    private final ScrollingLayer bgLayer;
    private long lastNano;
    private boolean gameOverShown = false;

    // UI Overlay Components
    private final HUD hud;
    private final AbilityHUD abilityHUD;
    private final PauseOverlayPane pauseOverlay;
    private final BaseImageButton menuButton;

    // FPS Tracking variables
    private int frameCounter = 0;
    private long lastSecond = System.currentTimeMillis();
    private double displayedFps = GameConfig.TARGET_FPS;

    // Input flags: Volatile ensures the Game Loop thread sees updates from the UI thread immediately
    private volatile boolean jumpPressed = false;
    private volatile boolean slidePressed = false;
    private volatile boolean slideReleased = false;
    private volatile boolean abilityPressed = false;

    // Lists for bulk processing of logic (Updatable) and drawing (Renderable)
    private final List<Updatable> updatables = new ArrayList<>();
    private final List<Renderable> renderables = new ArrayList<>();

    public GameStage(CookieRunApp app, int cookieIndex, core.entities.base.Cookie cookie) {
        this.app = app;
        this.cookieIndex = cookieIndex;
        core.stages.Stage stage = StageList.getStage(app.getSelectedStageIndex());

        // Initialize the logic controller
        ctrl = new GameController(cookie, stage);
        canvas = new Canvas(W, H);
        bgLayer = new ScrollingLayer(W, 0, H, 80);

        this.hud = new HUD(ctrl, W, H);
        this.abilityHUD = new AbilityHUD(ctrl, W, H);
        this.pauseOverlay = new PauseOverlayPane(W, H);

        // Register core components for the game loop
        updatables.add(ctrl);
        updatables.add(bgLayer);
        renderables.add(bgLayer);
        renderables.add(ctrl);
        menuButton = createMenuButton();

        // StackPane children order: index 0 is bottom-most (canvas), last index is top-most
        getChildren().addAll(
                canvas,
                hud,
                abilityHUD,
                pauseOverlay,
                menuButton
        );

        setFocusTraversable(true); // Required to capture key events
        loadImages();
        bindInput();
        setupEventListeners();
    }

    private BaseImageButton createMenuButton() {
        gui.baseElements.BaseImageButton button = new BaseImageButton("/Stages/MainMenuButton.png","#FFFFFF");
        button.setVisible(false);
        button.setOnMouseClicked(e -> {
            SoundManager.getInstance().playClickSound();
            app.showHomePage();
        });
        StackPane.setAlignment(button, Pos.CENTER);
        button.setTranslateY(80);
        return button;
    }

    private void loadImages() {
        try {
            var stage = StageList.getStage(app.getSelectedStageIndex());
            var bgStream = getClass().getResourceAsStream(stage.getPlayBackgroundPath());
            if (bgStream != null) {
                bgLayer.setImage(new Image(bgStream));
            }
        } catch (Exception e) {
            System.err.println("Background load failed: " + e.getMessage());
        }
    }

    private void bindInput() {
        requestFocus();
        setOnKeyPressed(e -> handleKeyPressed(e.getCode()));
        setOnKeyReleased(e -> handleKeyReleased(e.getCode()));
    }

    /**
     * Maps physical key presses to game actions via boolean flags.
     */
    private void handleKeyPressed(KeyCode key) {
        switch (key) {
            case SPACE, UP -> jumpPressed = true;
            case DOWN, S -> slidePressed = true;
            case E -> abilityPressed = true;
            case P -> ctrl.togglePause();
        }
    }

    private void handleKeyReleased(KeyCode key) {
        if (key == KeyCode.DOWN || key == KeyCode.S) {
            slideReleased = true;
        }
    }

    /**
     * Subscribes to the global EventBus to react to game logic triggers (e.g., sound/logging).
     */
    private void setupEventListeners() {
        EventBus eventBus = EventBus.getInstance();
        eventBus.subscribe("COIN_COLLECTED", event -> {
            CoinCollectedEvent e = (CoinCollectedEvent) event;
            System.out.println("Coin collected! Value: " + e.getCoinValue());
        });
        eventBus.subscribe("OBSTACLE_HIT", event -> {
            ObstacleHitEvent e = (ObstacleHitEvent) event;
            System.out.println("Hit by " + e.getObstacleType() + "! Damage: " + e.getDamageAmount());
        });
    }

    /**
     * Starts the AnimationTimer (The Heartbeat of the Game).
     */
    public void startGame() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastNano == 0) {
                    lastNano = now;
                    return;
                }
                // Calculate time passed between frames (delta)
                double delta = (now - lastNano) / 1_000_000_000.0;
                if (delta > 0.1) delta = 0.1; // Cap delta to prevent huge jumps during lag
                lastNano = now;

                update(delta); // Process Physics/Logic
                render();      // Draw to Canvas

                // FPS Counter Logic
                frameCounter++;
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastSecond >= 1000) {
                    displayedFps = frameCounter;
                    frameCounter = 0;
                    lastSecond = currentTime;
                }

                // End game detection
                if (ctrl.isGameOver() && !gameOverShown) {
                    gameOverShown = true;
                    showGameOver();
                    stop();
                }
            }
        }.start();
        requestFocus();
    }

    /**
     * Consumes input flags and updates all registered game objects.
     */
    private void update(double delta) {
        if (jumpPressed) { ctrl.onJump(); jumpPressed = false; }
        if (slidePressed) { ctrl.onSlide(); slidePressed = false; }
        if (slideReleased) { ctrl.onReleaseSlide(); slideReleased = false; }
        if (abilityPressed) {
            CookieAbility ability = ctrl.getCookie().getAbility();
            if (ability != null && !ability.isPassive()) { ctrl.onAbility(); }
            abilityPressed = false;
        }

        for (Updatable u : updatables) {
            u.update(delta);
        }

        // Sync UI state with Logic state
        hud.update();
        abilityHUD.update();
        pauseOverlay.setVisible(ctrl.isPaused());
        menuButton.setVisible(ctrl.isPaused());
    }

    /**
     * Handles the drawing sequence on the GraphicsContext.
     */
    private void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, W, H); // Clear previous frame

        applyCameraShake(gc); // Apply view transformations

        for (Renderable r : renderables) { r.render(gc); }

        drawRedOverlay(gc); // Draw screen effects on top of game world

        if (GameConfig.SHOW_FPS) {
            gc.setFont(Font.font("Arial", 11));
            gc.setFill(Color.web("#FFFFFF", 0.7));
            gc.fillText("FPS: " + String.format("%.1f", displayedFps), 15, 435);
        }

        gc.restore(); // Restore context to original state (reverses camera shake translate)
    }

    /**
     * Translates the canvas randomly if logic dictates a shake (e.g., on collision).
     */
    private void applyCameraShake(GraphicsContext gc) {
        double shake = ctrl.getCameraShake();
        double offsetX = 0;
        double offsetY = 0;
        if (shake > 0) {
            offsetX = (Math.random() - 0.5) * shake;
            offsetY = (Math.random() - 0.5) * shake;
        }
        gc.save(); // Save state before translating
        gc.translate(offsetX, offsetY);
    }

    /**
     * Renders a red tint based on damage/health state.
     */
    private void drawRedOverlay(GraphicsContext gc) {
        double opacity = ctrl.getRedOverlayOpacity();
        if (opacity <= 0) {return;}
        gc.setFill(Color.web("#FF0000", opacity * 0.3));
        gc.fillRect(0, 0, W, H);
    }

    private void showGameOver() {

        GameOverPane pane = new GameOverPane(ctrl.getScore(),
                ctrl.getCoins());

        pane.setOnRetry(() -> app.startGame(cookieIndex));
        pane.setOnHome(app::showHomePage);
        VBox overlay = new VBox(pane);
        overlay.setAlignment(Pos.CENTER);
        this.getChildren().add(overlay);
    }
}