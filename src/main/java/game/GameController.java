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

import java.util.ArrayList;
import java.util.List;

/**
 * Refactored GameController using manager pattern.
 * Much cleaner and shorter!
 */
public class GameController {
    private double cameraShake = 0;

    public static final double GROUND_Y   = Physics.GROUND_Y;
    public static final double GAME_WIDTH = 800.0;

    private final Cookie cookie;
    private final core.stages.Stage stage;
    private final double difficultyMultiplier;

    // Managers
    private final ObstacleManager obstacleManager = new ObstacleManager();
    private final CollectibleManager collectibleManager = new CollectibleManager();
    private final HealthManager healthManager = new HealthManager();

    private final List<GameObject> gameObjects = new ArrayList<>();

    private int score = 0;
    private int coins = 0;

    private double gameTime = 0;
    private double currentSpeed = 300.0;

    private boolean gameOver = false;
    private boolean paused = false;

    // Red overlay for hit feedback
    private double redOverlayTimer = 0;
    private static final double RED_OVERLAY_TOTAL = 0.7;  // 0.2s in + 0.2s stay + 0.3s out

    /**
     * Create a game controller for the given cookie and stage.
     * Resets the cookie to starting state for a new game.
     * @param cookie The cookie to control
     * @param stage The stage with difficulty multiplier
     */
    public GameController(Cookie cookie, core.stages.Stage stage) {
        this.cookie = cookie;
        this.stage = stage;
        this.difficultyMultiplier = stage.getDifficultyMultiplier();
        // Reset cookie to initial state for new game
        cookie.reset();
    }

    // INPUT
    public void onJump() {
        if (!paused && !gameOver)
            cookie.jump();
    }
    public void shakeCamera(double intensity) {

        cameraShake = intensity;
    }

    public void onSlide() {
        if (!paused && !gameOver)
            cookie.slideDown();
    }

    public void onReleaseSlide() {
        cookie.releaseSlide();
    }

    public void onAbility() {
        if (!paused && !gameOver && cookie.getAbility() != null) {
            // Activate the ability
            if (cookie.getAbility() instanceof core.abilities.implementations.GhostAbility) {
                core.abilities.implementations.GhostAbility ghost =
                    (core.abilities.implementations.GhostAbility) cookie.getAbility();
                ghost.activate();
            }
        }
    }

    public void togglePause() {
        if (!gameOver)
            paused = !paused;
    }

    // UPDATE
    public void update(double delta) {
        long frameStartTime = System.nanoTime();

        // Update red overlay timer (even when paused)
        if (redOverlayTimer > 0) {
            redOverlayTimer -= delta;
        }

        if (paused || gameOver)
            return;

        gameTime += delta;
        currentSpeed = 300.0 + gameTime * 16;

        score += (int)(delta * currentSpeed * 0.05);

        long cookieUpdateStart = System.nanoTime();
        cookie.update(delta);
        long cookieUpdateTime = (System.nanoTime() - cookieUpdateStart) / 1_000_000;
        if (cookieUpdateTime > 3) {
            System.out.println("[PROFILE] Cookie update: " + cookieUpdateTime + "ms");
        }

        cookie.decreaseHp(delta * 0.2);

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
            // Sync ghost state for GhostAbility
            if (cookie.getAbility() instanceof core.abilities.implementations.GhostAbility) {
                core.abilities.implementations.GhostAbility ghost =
                    (core.abilities.implementations.GhostAbility) cookie.getAbility();
                cookie.setGhost(ghost.isActive());
            }
        }

        // Update game objects
        gameObjects.removeIf(obj -> {
            obj.update(delta);
            return !obj.isAlive();
        });

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
        CookieAbility ability =
                cookie.getAbility();

        if (ability != null) {

            ability.update(
                    this,
                    cookie,
                    delta
            );
        }
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

        // Draw magnetic field indicator
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

        // IMPORTANT
        collectibleManager.renderParticles(gc);
    }

    // GETTERS
    public int getScore() { return score; }
    public int getCoins() { return coins; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPaused() { return paused; }
    public Cookie getCookie() { return cookie; }
    public double getGameTime() { return gameTime; }
    public String getDifficulty() { return stage.getDifficulty(); }
    public double getDifficultyMultiplier() { return difficultyMultiplier; }

    /**
     * Get the current red overlay opacity (0.0 to 1.0) based on hit feedback timing.
     * Smooth fade: 0-0.2s fade in, 0.2-0.4s stay, 0.4-0.7s fade out
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

    // For magnetic ability
    public List<GameObject> getGameObjects() { return gameObjects; }
    public List<Collectible> getCollectibles() { return collectibleManager.getCollectibles(); }

    // Ability support
    public void applyMagneticForceToCollectibles(MagneticAbility ability) {
        for (Collectible c : collectibleManager.getCollectibles()) {
            ability.applyForceToCollectible(c, cookie);
        }
    }
    public double getCameraShake() {

        return cameraShake;
    }
}
