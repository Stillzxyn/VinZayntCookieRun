package game.managers;

import core.entities.base.Cookie;
import core.entities.base.Physics;

import core.entities.collectibles.Collectible;
import core.entities.collectibles.Coin;
import core.entities.collectibles.JellySmall;
import core.entities.collectibles.JellyBig;

import graphics.effects.Particle;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Manages collectible spawning,
 * collisions,
 * and particle effects.
 */
public class CollectibleManager {

    private final List<Collectible> collectibles =
            new ArrayList<>();

    private final List<Particle> particles =
            new ArrayList<>();

    private double collectibleTimer = 0;

    private double nextCollectibleIn = 0.9;

    private final Random rng = new Random();

    public void update(
            double delta,
            double currentSpeed,
            List gameObjects
    ) {

        spawnCollectibles(
                currentSpeed,
                gameObjects
        );

        // Update particles
        for (int i = particles.size() - 1;
             i >= 0;
             i--) {

            Particle p = particles.get(i);

            p.update(delta);

            if (p.isDead()) {

                particles.remove(i);
            }
        }
    }

    public void checkCollision(
            Cookie cookie,
            int[] scoreCoins
    ) {

        Iterator<Collectible> cIt =
                collectibles.iterator();

        while (cIt.hasNext()) {

            Collectible c = cIt.next();

            if (cookie.intersects(c)) {

                // Score
                scoreCoins[0] +=
                        c.getScoreValue();

                // Coin count
                if (c instanceof Coin) {

                    scoreCoins[1]++;
                }

                // Spawn particles
                for (int i = 0; i < 5; i++){

                    particles.add(
                            new Particle(
                                    c.getX(),
                                    c.getY()
                            )
                    );
                }

                c.destroy();

                cIt.remove();
            }
        }
    }

    private void spawnCollectibles(
            double currentSpeed,
            List gameObjects
    ) {

        collectibleTimer += 0.016;

        if (collectibleTimer
                < nextCollectibleIn) {

            return;
        }

        collectibleTimer = 0;

        nextCollectibleIn =
                0.4
                        + rng.nextDouble() * 0.7;

        int roll = rng.nextInt(10);

        // Coins
        if (roll < 6) {

            int count =
                    3 + rng.nextInt(4);

            double coinY =
                    rng.nextBoolean()
                            ? Physics.GROUND_Y - 80
                            : Physics.GROUND_Y - 42;

            for (int i = 0; i < count; i++) {

                Collectible c =
                        new Coin(
                                800.0 + 10 + i * 32,
                                coinY,
                                currentSpeed
                        );

                collectibles.add(c);

                gameObjects.add(c);
            }
        }

        // Jelly
        else {

            Collectible c =
                    roll < 9
                            ? new JellySmall(
                            800.0 + 10,
                            Physics.GROUND_Y - 65,
                            currentSpeed
                    )
                            : new JellyBig(
                            800.0 + 10,
                            Physics.GROUND_Y - 65,
                            currentSpeed
                    );

            collectibles.add(c);

            gameObjects.add(c);
        }
    }

    /**
     * Render particles.
     */
    public void renderParticles(
            GraphicsContext gc
    ) {

        for (Particle p : particles) {

            p.render(gc);
        }
    }

    public List<Collectible> getCollectibles() {

        return collectibles;
    }

    public void clear() {

        collectibles.clear();

        particles.clear();
    }
}