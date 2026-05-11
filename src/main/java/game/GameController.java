package game;

import core.abilities.CookieAbility;
import javafx.scene.canvas.GraphicsContext;
import core.entities.base.Cookie;
import core.entities.base.Physics;
import core.entities.base.GameObject;
import core.entities.collectibles.Collectible;
import core.abilities.implementations.MagneticAbility;
import game.managers.ObstacleManager;
import game.managers.CollectibleManager;
import game.managers.HealthManager;
import utils.Renderable;
import utils.Updatable;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game controller.
 *
 * Responsibilities:
 * - Handle game loop
 * - Update all game systems
 * - Manage input
 * - Handle collisions
 * - Control score/game state
 *
 * Uses manager pattern to separate systems:
 * - ObstacleManager
 * - CollectibleManager
 * - HealthManager
 */
public class GameController implements Renderable, Updatable {
    private double cameraShake = 0;

    public static final double GROUND_Y   = Physics.GROUND_Y;
    public static final double GAME_WIDTH = 800.0;

    private final Cookie cookie;
    private final core.stages.Stage stage;
    private final double difficultyMultiplier;

    /**
     * Manager systems.
     *
     * Each manager handles its own logic
     * to keep GameController cleaner.
     */
    private final ObstacleManager obstacleManager =
            new ObstacleManager();

    private final CollectibleManager collectibleManager =
            new CollectibleManager();

    private final HealthManager healthManager =
            new HealthManager();

    /**
     * All active game objects.
     *
     * Includes:
     * - Obstacles
     * - Collectibles
     * - Effects
     */
    private final List<GameObject> gameObjects =
            new ArrayList<>();

    private int score = 0;
    private int coins = 0;

    private double gameTime = 0;
    private double currentSpeed = 300.0;

    private boolean gameOver = false;
    private boolean paused = false;

    /**
     * Red hit effect timer.
     *
     * Used when player gets damaged.
     */
    private double redOverlayTimer = 0;
    private static final double RED_OVERLAY_TOTAL = 0.7;  // 0.2s in + 0.2s stay + 0.3s out

    /**
     * Create game controller.
     *
     * Also resets cookie state
     * before starting a new game.
     */
    public GameController(
            Cookie cookie,
            core.stages.Stage stage
    ) {
        this.cookie = cookie;
        this.stage = stage;
        this.difficultyMultiplier = stage.getDifficultyMultiplier();
        // Reset cookie to initial state for new game
        cookie.reset();
    }

    // INPUT
    /**
     * Handles jump input.
     */
    public void onJump() {
        if (!paused && !gameOver)
            cookie.jump();
    }

    /**
     * Triggers a camera shake effect.
     * @param intensity The magnitude of the shake.
     */
    public void shakeCamera(double intensity) {

        cameraShake = intensity;
    }

    /**
     * Handles slide input.
     */
    public void onSlide() {
        if (!paused && !gameOver)
            cookie.slideDown();
    }

    /**
     * Handles releasing slide input.
     */
    public void onReleaseSlide() {
        cookie.releaseSlide();
    }

    /**
     * Handles special ability activation input.
     */
    public void onAbility() {
        if (!paused && !gameOver && cookie.getAbility() != null) {
            cookie.getAbility().activate();
        }
    }

    /**
     * Toggles the pause state.
     */
    public void togglePause() {
        if (!gameOver)
            paused = !paused;
    }

    /**
     * Main game update loop.
     *
     * Handles:
     * - Speed scaling
     * - Score update
     * - Cookie update
     * - Manager updates
     * - Ability updates
     * - Collision detection
     * - Game over logic
     */
    public void update(double delta) {

        // Update red overlay timer (even when paused)
        if (redOverlayTimer > 0) {
            redOverlayTimer -= delta;
        }

        if (paused || gameOver)
            return;

        gameTime += delta;
        currentSpeed = 300.0 + gameTime * 16;

        score += (int)(delta * currentSpeed * 0.05);

        cookie.update(delta);

        cookie.decreaseHp(delta * 0.1);

        if(cookie.getHp() <= 0) {
            gameOver = true;
        }

        // Update managers
        obstacleManager.update(delta, currentSpeed, gameObjects);
        collectibleManager.update(delta, currentSpeed, gameObjects);
        healthManager.update(delta, gameObjects);

        // Update cookie ability
        if (cookie.getAbility() != null) {
            cookie.getAbility().update(this, cookie, delta);
            cookie.setGhost(cookie.getAbility().isActive() && !cookie.getAbility().isPassive());
        }

        // Update game objects
        // We update objects and remove them if they are dead.
        // We use an index-based loop for better performance and to avoid concurrent modification issues,
        // although removeIf is generally safe and fast enough for modern JVMs.
        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject obj = gameObjects.get(i);
            obj.update(delta);
            if (!obj.isAlive()) {
                gameObjects.remove(i);
            }
        }

        // Clean up manager lists as well
        // These lists should stay in sync with gameObjects list.
        obstacleManager.getObstacles().removeIf(obj -> !obj.isAlive());
        collectibleManager.getCollectibles().removeIf(obj -> !obj.isAlive());
        healthManager.getHealthItems().removeIf(obj -> !obj.isAlive());

        // Check collisions
        boolean[] gameOverFlag = {gameOver};
        double oldHp = cookie.getHp();
        obstacleManager.checkCollision(cookie, gameOverFlag, difficultyMultiplier,this);
        if (cookie.getHp() < oldHp) {
            // Cookie was hit - trigger red overlay
            redOverlayTimer = RED_OVERLAY_TOTAL;
        }
        gameOver = gameOverFlag[0];

        if (gameOver) return;

        int[] scoreCoins = {score, coins};
        collectibleManager.checkCollision(cookie, scoreCoins);
        score = scoreCoins[0];
        coins = scoreCoins[1];

        healthManager.checkCollision(cookie);

        if (cameraShake > 0) {

            cameraShake -= delta * 20;

            if (cameraShake < 0) {
                cameraShake = 0;
            }
        }
    }

    public void render(GraphicsContext gc) {

        for (GameObject obj : gameObjects) {

            obj.render(gc);
        }

        cookie.render(gc);

/**
 * Draw magnetic aura effect
 * for MagneticAbility cookies.
 */
        if (cookie.getAbility()
                instanceof MagneticAbility) {

            MagneticAbility magnetic =
                    (MagneticAbility)
                            cookie.getAbility();

            double radius =
                    magnetic.getMagneticRadius() * 0.5;

            double cookieX =
                    cookie.getX()
                            + cookie.getWidth()
                            - 20;

            double cookieY =
                    cookie.getY()
                            + cookie.getHeight()
                            - 20;

            gc.setFill(
                    javafx.scene.paint.Color.web(
                            "#FFD700",
                            0.2
                    )
            );

            gc.fillOval(
                    cookieX - radius,
                    cookieY - radius,
                    120,
                    120
            );
        }

        /**
         * Render collectible particles/effects.
         */
        collectibleManager.renderParticles(gc);
    }

    // GETTERS
    public int getScore() { return score; }
    public int getCoins() { return coins; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPaused() { return paused; }
    public Cookie getCookie() { return cookie; }

    /**
     * Calculate red overlay opacity
     * for smooth damage feedback.
     */
    public double getRedOverlayOpacity() {
        if (redOverlayTimer <= 0) return 0.0;

        double elapsed = RED_OVERLAY_TOTAL - redOverlayTimer;

        if (elapsed < 0.2) {
            // Fade in: 0 to 1 over 0.2s
            return elapsed / 0.2;
        } else if (elapsed < 0.4) {
            // Stay: full opacity for 0.2s
            return 1.0;
        } else {
            // Fade out: 1 to 0 over 0.3s
            return 1.0 - ((elapsed - 0.4) / 0.3);
        }
    }

    /**
     * Apply magnetic force to collectibles.
     *
     * Used by MagneticAbility.
     */
    public void applyMagneticForceToCollectibles(
            MagneticAbility ability
    ) {
        for (Collectible c : collectibleManager.getCollectibles()) {
            ability.applyForceToCollectible(c, cookie);
        }
    }
    public double getCameraShake() {

        return cameraShake;
    }
}
