package game.managers;

import core.entities.base.Cookie;
import core.entities.base.GameObject;
import core.entities.base.Physics;

import core.entities.collectibles.Collectible;
import core.entities.collectibles.Coin;
import core.entities.collectibles.JellyBig;
import core.entities.collectibles.JellySmall;

import graphics.effects.Particle;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Handles:
 * - Collectible spawning
 * - Collectible collision
 * - Score / coin gain
 * - Collection particles
 */
public class CollectibleManager {

    // =========================
    // STATE
    // =========================

    private final List<Collectible> collectibles =
            new ArrayList<>();

    private final List<Particle> particles =
            new ArrayList<>();

    private final Random rng =
            new Random();

    private double collectibleTimer = 0;

    private double nextCollectibleIn = 0.9;

    // =========================
    // UPDATE
    // =========================

    /**
     * Update collectibles and particles.
     * @param delta time elapsed since last frame.
     * @param currentSpeed current game speed for spawning movement.
     * @param gameObjects list of all game objects to add new spawns to.
     */
    public void update(
            double delta,
            double currentSpeed,
            List<GameObject> gameObjects
    ) {

        spawnCollectibles(
                delta,
                currentSpeed,
                gameObjects
        );

        updateParticles(delta);
    }

    /**
     * Update particle effects.
     */
    private void updateParticles(
            double delta
    ) {

        for (int i = particles.size() - 1;
             i >= 0;
             i--) {

            Particle particle =
                    particles.get(i);

            particle.update(delta);

            if (particle.isDead()) {

                particles.remove(i);
            }
        }
    }

    // =========================
    // COLLISION
    // =========================

    /**
     * Handle collectible pickup and update score/coins.
     * @param cookie the player cookie.
     * @param scoreCoins array [score, coins] to be updated.
     */
    public void checkCollision(
            Cookie cookie,
            int[] scoreCoins
    ) {

        Iterator<Collectible> iterator =
                collectibles.iterator();

        while (iterator.hasNext()) {

            Collectible collectible =
                    iterator.next();

            if (!cookie.intersects(collectible)) {
                continue;
            }

            // Add score
            scoreCoins[0] +=
                    collectible.getScoreValue();

            // Add coin count
            if (collectible instanceof Coin) {

                scoreCoins[1]++;
            }

            spawnParticles(
                    collectible.getX(),
                    collectible.getY()
            );

            collectible.destroy();

            iterator.remove();
        }
    }

    /**
     * Spawn collection particles.
     */
    private void spawnParticles(
            double x,
            double y
    ) {

        for (int i = 0; i < 5; i++) {

            particles.add(
                    new Particle(x, y)
            );
        }
    }

    // =========================
    // SPAWNING
    // =========================

    /**
     * Spawn collectibles periodically.
     */
    private void spawnCollectibles(
            double delta,
            double currentSpeed,
            List<GameObject> gameObjects
    ) {

        collectibleTimer += delta;

        if (collectibleTimer
                < nextCollectibleIn) {

            return;
        }

        collectibleTimer = 0;

        nextCollectibleIn =
                0.4
                        + rng.nextDouble() * 0.7;

        int roll = rng.nextInt(10);

        // Spawn coins
        if (roll < 6) {

            spawnCoinLine(
                    currentSpeed,
                    gameObjects
            );
        }

        // Spawn jelly
        else {

            spawnJelly(
                    currentSpeed,
                    gameObjects,
                    roll
            );
        }
    }

    /**
     * Spawn a line of coins.
     */
    private void spawnCoinLine(
            double currentSpeed,
            List<GameObject> gameObjects
    ) {

        int count =
                3 + rng.nextInt(4);

        double coinY =
                rng.nextBoolean()
                        ? Physics.GROUND_Y - 80
                        : Physics.GROUND_Y - 42;

        for (int i = 0; i < count; i++) {

            Collectible coin =
                    new Coin(
                            810 + i * 32,
                            coinY,
                            currentSpeed
                    );

            collectibles.add(coin);

            gameObjects.add(coin);
        }
    }

    /**
     * Spawn jelly collectible.
     */
    private void spawnJelly(
            double currentSpeed,
            List<GameObject> gameObjects,
            int roll
    ) {

        Collectible jelly =
                roll < 9
                        ? new JellySmall(
                        810,
                        Physics.GROUND_Y - 65,
                        currentSpeed
                )
                        : new JellyBig(
                        810,
                        Physics.GROUND_Y - 65,
                        currentSpeed
                );

        collectibles.add(jelly);

        gameObjects.add(jelly);
    }

    // =========================
    // RENDER
    // =========================

    /**
     * Render particle effects.
     */
    public void renderParticles(
            GraphicsContext gc
    ) {

        for (Particle particle : particles) {

            particle.render(gc);
        }
    }

    // =========================
    // GETTERS
    // =========================

    public List<Collectible> getCollectibles() {

        return collectibles;
    }
}