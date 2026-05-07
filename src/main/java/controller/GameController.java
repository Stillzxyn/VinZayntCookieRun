package controller;


import javafx.scene.canvas.GraphicsContext;
import model.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameController {

    public static final double GROUND_Y   = Cookie.GROUND_Y;
    public static final double GAME_WIDTH = 800.0;

    private final Cookie cookie;

    private final List<Obstacle> obstacles = new ArrayList<>();
    private final List<Collectible> collectibles = new ArrayList<>();
    private final List<HealthItem> healthItems = new ArrayList<>();

    private final List<GameObject> gameObjects = new ArrayList<>();

    private int score = 0;
    private int coins = 0;

    private double gameTime = 0;
    private double currentSpeed = 300.0;

    private boolean gameOver = false;
    private boolean paused = false;

    private double obstacleTimer = 0;
    private double collectibleTimer = 0;
    private double healthSpawnTimer = 0;

    private double nextObstacleIn = 2.2;
    private double nextCollectibleIn = 0.9;

    private final Random rng = new Random();

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

        // HP drain
        cookie.decreaseHp(delta * 5);

        if(cookie.getHp() <= 0) {
            gameOver = true;
        }

        spawnObstacles(delta);
        spawnCollectibles(delta);
        spawnHealthItems(delta);

        // update all objects
        gameObjects.removeIf(obj -> {

            obj.update(delta);

            return !obj.isAlive();
        });

        // obstacle collision
        for (Obstacle o : obstacles) {

            if (cookie.intersects(o)) {

                cookie.die();

                gameOver = true;

                return;
            }
        }

        // collectible collision
        Iterator<Collectible> cIt = collectibles.iterator();

        while (cIt.hasNext()) {

            Collectible c = cIt.next();

            if (cookie.intersects(c)) {

                score += c.getScoreValue();

                if (c.getType() == Collectible.Type.COIN) {
                    coins++;
                }

                c.destroy();

                cIt.remove();
            }
        }

        // health collision
        Iterator<HealthItem> hIt = healthItems.iterator();

        while (hIt.hasNext()) {

            HealthItem h = hIt.next();

            if (h.collides(cookie)) {

                cookie.heal(25);

                h.destroy();

                hIt.remove();
            }
        }
    }

    // SPAWN

    private void spawnObstacles(double delta) {

        obstacleTimer += delta;

        if (obstacleTimer < nextObstacleIn)
            return;

        obstacleTimer = 0;

        nextObstacleIn = 1.3 + rng.nextDouble() * 1.8;

        double spd = currentSpeed;

        Obstacle obs = switch (rng.nextInt(6)) {

            case 0 -> new GroundObstacle(
                    GAME_WIDTH + 10,
                    spd,
                    GroundObstacle.Type.CANDY_WALL);

            case 1 -> new GroundObstacle(
                    GAME_WIDTH + 10,
                    spd,
                    GroundObstacle.Type.SPIKE);

            case 2 -> new GroundObstacle(
                    GAME_WIDTH + 10,
                    spd,
                    GroundObstacle.Type.BLOCK);

            case 3 -> new AirObstacle(
                    GAME_WIDTH + 10,
                    spd,
                    AirObstacle.Type.FIREBALL);

            case 4 -> new AirObstacle(
                    GAME_WIDTH + 10,
                    spd,
                    AirObstacle.Type.BAT);

            default -> new AirObstacle(
                    GAME_WIDTH + 10,
                    spd,
                    AirObstacle.Type.CLOUD_SPIKE);
        };

        obstacles.add(obs);
        gameObjects.add(obs);
    }

    private void spawnCollectibles(double delta) {

        collectibleTimer += delta;

        if (collectibleTimer < nextCollectibleIn)
            return;

        collectibleTimer = 0;

        nextCollectibleIn = 0.4 + rng.nextDouble() * 0.7;

        int roll = rng.nextInt(10);

        if (roll < 6) {

            int count = 3 + rng.nextInt(4);

            double coinY = rng.nextBoolean()
                    ? GROUND_Y - 80
                    : GROUND_Y - 42;

            for (int i = 0; i < count; i++) {

                Collectible c = new Collectible(
                        GAME_WIDTH + 10 + i * 32,
                        coinY,
                        currentSpeed,
                        Collectible.Type.COIN
                );

                collectibles.add(c);
                gameObjects.add(c);
            }
        }
        else {

            Collectible.Type cType =
                    roll < 9
                            ? Collectible.Type.JELLY_SMALL
                            : Collectible.Type.JELLY_BIG;

            Collectible c = new Collectible(
                    GAME_WIDTH + 10,
                    GROUND_Y - 65,
                    currentSpeed,
                    cType
            );

            collectibles.add(c);
            gameObjects.add(c);
        }
    }

    private void spawnHealthItems(double delta) {

        healthSpawnTimer += delta;

        if (healthSpawnTimer < 8)
            return;

        healthSpawnTimer = 0;

        HealthItem item = new HealthItem(
                GAME_WIDTH + 10,
                GROUND_Y - 120
        );

        healthItems.add(item);
        gameObjects.add(item);
    }

    // RENDER

    public void render(GraphicsContext gc) {

        for(GameObject obj : gameObjects) {
            obj.render(gc);
        }

        cookie.render(gc);
    }

    // GETTERS

    public int getScore() {
        return score;
    }

    public int getCoins() {
        return coins;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isPaused() {
        return paused;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public double getGameTime() {
        return gameTime;
    }
}