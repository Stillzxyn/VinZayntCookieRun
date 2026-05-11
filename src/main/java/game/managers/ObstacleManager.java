package game.managers;

import core.entities.base.Cookie;
import core.entities.base.GameObject;
import core.entities.base.Obstacle;

import game.obstacles.types.airobstacles.Bat;
import game.obstacles.types.airobstacles.CloudSpike;
import game.obstacles.types.airobstacles.Fireball;

import game.obstacles.types.groundobstacles.Block;
import game.obstacles.types.groundobstacles.CandyWall;
import game.obstacles.types.groundobstacles.Spike;

import game.GameController;
import audio.SoundManager;

import java.util.ArrayList;
import java.util.Iterator;
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
     *
     * Optimization: Instead of removing obstacles immediately (O(n) operation),
     * we mark them as dead with destroy(). The GameController will batch-remove
     * them in a single removeIf() pass, which is O(n) but only happens once per frame.
     *
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

        // OPTIMIZATION: Use Iterator + early return for off-screen culling
        Iterator<Obstacle> iterator =
                obstacles.iterator();

        while (iterator.hasNext()) {

            Obstacle obstacle =
                    iterator.next();

            // Skip obstacles far off-screen left (optimization: avoid pixel-perfect collision check)
            if (obstacle.getX() < -100) {
                continue;
            }

            if (!cookie.intersects(obstacle)) {
                continue;
            }

            double damage =
                    obstacle.getBaseDamage()
                            * difficultyMultiplier;

            cookie.decreaseHp(damage);

            cookie.setInvincible(true);

            gc.shakeCamera(8);

            // Play hit sound effect
            SoundManager.getInstance().playHitSound();

            // Game over
            if (cookie.getHp() <= 0) {

                cookie.die();

                gameOverFlag[0] = true;
            }

            // Mark as dead instead of removing immediately
            // GameController will batch-remove in a single efficient pass
            obstacle.destroy();

            return;
        }
    }

    // =========================
    // SPAWNING
    // =========================

    /**
     * Spawn obstacles periodically.
     * OPTIMIZATION: Adaptive spawn rate scales with speed to prevent object accumulation.
     * As speed increases, spawn intervals increase, keeping object density constant.
     * Base interval: 2.2s at speed 300. At speed 600, interval becomes ~3.3s.
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

        // Adaptive spawn rate: uses sqrt scaling to balance performance with difficulty
        // sqrt scaling prevents excessive accumulation while keeping challenge at higher speeds
        // At speed 300: multiplier = 1.0 (no change)
        // At speed 600: multiplier = 1.41 (41% slower spawning)
        // At speed 900: multiplier = 1.73 (73% slower spawning)
        double speedMultiplier = Math.sqrt(currentSpeed / 300.0);

        nextObstacleIn =
                (1.3 + rng.nextDouble() * 1.8) * speedMultiplier;

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