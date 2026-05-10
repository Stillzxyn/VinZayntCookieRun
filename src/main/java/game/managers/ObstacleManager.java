package game.managers;

import core.entities.base.Cookie;
import core.entities.base.GameObject;
import core.entities.base.Obstacle;

import core.entities.obstacles.types.airobstacles.Bat;
import core.entities.obstacles.types.airobstacles.CloudSpike;
import core.entities.obstacles.types.airobstacles.Fireball;

import core.entities.obstacles.types.groundobstacles.Block;
import core.entities.obstacles.types.groundobstacles.CandyWall;
import core.entities.obstacles.types.groundobstacles.Spike;

import game.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles:
 * - Obstacle spawning
 * - Obstacle collision
 * - Damage handling
 */
public class ObstacleManager {

    // =========================
    // STATE
    // =========================

    private final List<Obstacle> obstacles =
            new ArrayList<>();

    private final Random rng =
            new Random();

    private double obstacleTimer = 0;

    private double nextObstacleIn = 2.2;

    // =========================
    // UPDATE
    // =========================

    /**
     * Update obstacle system.
     */
    public void update(
            double delta,
            double currentSpeed,
            List<GameObject> gameObjects
    ) {

        spawnObstacles(
                delta,
                currentSpeed,
                gameObjects
        );
    }

    // =========================
    // COLLISION
    // =========================

    /**
     * Damage cookie on collision.
     * @param cookie The player cookie.
     * @param gameOverFlag Array used to communicate game over state back.
     * @param difficultyMultiplier Damage scaling factor from the current stage.
     * @param gc The game controller to trigger effects like camera shake.
     */
    public void checkCollision(
            Cookie cookie,
            boolean[] gameOverFlag,
            double difficultyMultiplier,
            GameController gc
    ) {

        // Ignore damage during invincibility
        if (cookie.isGhost()
                || cookie.isInvincible()) {

            return;
        }

        for (int i = 0;
             i < obstacles.size();
             i++) {

            Obstacle obstacle =
                    obstacles.get(i);

            if (!cookie.intersects(obstacle)) {
                continue;
            }

            double damage =
                    obstacle.getBaseDamage()
                            * difficultyMultiplier;

            cookie.decreaseHp(damage);

            cookie.setInvincible(true);

            gc.shakeCamera(8);

            // Game over
            if (cookie.getHp() <= 0) {

                cookie.die();

                gameOverFlag[0] = true;
            }

            obstacle.destroy();

            obstacles.remove(i);

            return;
        }
    }

    // =========================
    // SPAWNING
    // =========================

    /**
     * Spawn obstacles periodically.
     */
    private void spawnObstacles(
            double delta,
            double currentSpeed,
            List<GameObject> gameObjects
    ) {

        obstacleTimer += delta;

        if (obstacleTimer
                < nextObstacleIn) {

            return;
        }

        obstacleTimer = 0;

        nextObstacleIn =
                1.3 + rng.nextDouble() * 1.8;

        int type =
                rng.nextInt(6);

        Obstacle obstacle =
                createObstacle(
                        type,
                        810,
                        currentSpeed
                );

        obstacles.add(obstacle);

        gameObjects.add(obstacle);
    }

    /**
     * Create random obstacle.
     */
    private Obstacle createObstacle(
            int type,
            double x,
            double speed
    ) {

        return switch (type) {

            case 0 ->
                    new CandyWall(x, speed);

            case 1 ->
                    new Spike(x, speed);

            case 2 ->
                    new Block(x, speed);

            case 3 ->
                    new Fireball(x, speed);

            case 4 ->
                    new Bat(x, speed);

            default ->
                    new CloudSpike(x, speed);
        };
    }

    // =========================
    // GETTERS
    // =========================

    public List<Obstacle> getObstacles() {

        return obstacles;
    }
}