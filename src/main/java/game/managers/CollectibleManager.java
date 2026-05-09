package game.managers;

import core.entities.base.Cookie;
import core.entities.base.Physics;
import core.entities.collectibles.Collectible;
import core.entities.collectibles.Coin;
import core.entities.collectibles.JellySmall;
import core.entities.collectibles.JellyBig;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Manages collectible spawning and collision detection.
 */
public class CollectibleManager {

    private final List<Collectible> collectibles = new ArrayList<>();
    private double collectibleTimer = 0;
    private double nextCollectibleIn = 0.9;
    private final Random rng = new Random();

    public void update(double delta, double currentSpeed, List gameObjects) {
        spawnCollectibles(currentSpeed, gameObjects);
    }

    public void checkCollision(Cookie cookie, int[] scoreCoins) {
        Iterator<Collectible> cIt = collectibles.iterator();

        while (cIt.hasNext()) {
            Collectible c = cIt.next();

            if (cookie.intersects(c)) {
                scoreCoins[0] += c.getScoreValue();  // score

                if (c instanceof Coin) {
                    scoreCoins[1]++;  // coins counter
                }

                c.destroy();
                cIt.remove();
            }
        }
    }

    private void spawnCollectibles(double currentSpeed, List gameObjects) {
        collectibleTimer += 0.016;  // Approximate delta

        if (collectibleTimer < nextCollectibleIn)
            return;

        collectibleTimer = 0;
        nextCollectibleIn = 0.4 + rng.nextDouble() * 0.7;

        int roll = rng.nextInt(10);

        if (roll < 6) {
            // Spawn coins
            int count = 3 + rng.nextInt(4);
            double coinY = rng.nextBoolean()
                    ? Physics.GROUND_Y - 80
                    : Physics.GROUND_Y - 42;

            for (int i = 0; i < count; i++) {
                Collectible c = new Coin(800.0 + 10 + i * 32, coinY, currentSpeed);
                collectibles.add(c);
                gameObjects.add(c);
            }
        } else {
            // Spawn jelly
            Collectible c = roll < 9
                    ? new JellySmall(800.0 + 10, Physics.GROUND_Y - 65, currentSpeed)
                    : new JellyBig(800.0 + 10, Physics.GROUND_Y - 65, currentSpeed);

            collectibles.add(c);
            gameObjects.add(c);
        }
    }

    public List<Collectible> getCollectibles() {
        return collectibles;
    }

    public void clear() {
        collectibles.clear();
    }
}
