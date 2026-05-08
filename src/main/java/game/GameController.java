package game;

import javafx.scene.canvas.GraphicsContext;
import entities.base.Cookie;
import entities.base.Physics;
import entities.base.GameObject;
import entities.collectibles.Collectible;
import abilities.implementations.MagneticAbility;

import java.util.ArrayList;
import java.util.List;

/**
 * Refactored GameController using manager pattern.
 * Much cleaner and shorter!
 */
public class GameController {

    public static final double GROUND_Y   = Physics.GROUND_Y;
    public static final double GAME_WIDTH = 800.0;
    private final Cookie cookie;

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

    public GameController(Cookie cookie) {
        this.cookie = cookie;
    }

    // INPUT
    public void onJump() {
        if (!paused && !gameOver)
            cookie.jump();
    }

    public void onSlide() {
        if (!paused && !gameOver)
            cookie.slideDown();
    }

    public void onReleaseSlide() {
        cookie.releaseSlide();
    }

    public void togglePause() {
        if (!gameOver)
            paused = !paused;
    }

    // UPDATE
    public void update(double delta) {
        if (paused || gameOver)
            return;

        gameTime += delta;
        currentSpeed = 300.0 + gameTime * 16;

        score += (int)(delta * currentSpeed * 0.05);

        cookie.update(delta);
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
        }

        // Update game objects
        gameObjects.removeIf(obj -> {
            obj.update(delta);
            return !obj.isAlive();
        });

        // Check collisions
        boolean[] gameOverFlag = {gameOver};
        obstacleManager.checkCollision(cookie, gameOverFlag);
        gameOver = gameOverFlag[0];

        if (gameOver) return;

        int[] scoreCoins = {score, coins};
        collectibleManager.checkCollision(cookie, scoreCoins);
        score = scoreCoins[0];
        coins = scoreCoins[1];

        healthManager.checkCollision(cookie);
    }

    // RENDER
    public void render(GraphicsContext gc) {
        for(GameObject obj : gameObjects) {
            obj.render(gc);
        }
        cookie.render(gc);
    }

    // GETTERS
    public int getScore() { return score; }
    public int getCoins() { return coins; }
    public boolean isGameOver() { return gameOver; }
    public boolean isPaused() { return paused; }
    public Cookie getCookie() { return cookie; }
    public double getGameTime() { return gameTime; }

    // For magnetic ability
    public List<GameObject> getGameObjects() { return gameObjects; }
    public List<Collectible> getCollectibles() { return collectibleManager.getCollectibles(); }

    // Ability support
    public void applyMagneticForceToCollectibles(MagneticAbility ability) {
        for (Collectible c : collectibleManager.getCollectibles()) {
            ability.applyForceToCollectible(c, cookie);
        }
    }
}
