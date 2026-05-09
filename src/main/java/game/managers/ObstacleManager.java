package game.managers;

import core.entities.base.Cookie;
import core.entities.base.Obstacle;

import core.entities.obstacles.types.airobstacles.Bat;
import core.entities.obstacles.types.airobstacles.CloudSpike;
import core.entities.obstacles.types.airobstacles.Fireball;

import core.entities.obstacles.types.groundobstacles.Block;
import core.entities.obstacles.types.groundobstacles.CandyWall;
import core.entities.obstacles.types.groundobstacles.Spike;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages obstacle spawning and collision detection.
 */
public class ObstacleManager {

    private final List<Obstacle> obstacles = new ArrayList<>();

    private double obstacleTimer = 0;

    private double nextObstacleIn = 2.2;

    private final Random rng = new Random();

    public void update(
            double delta,
            double currentSpeed,
            List gameObjects
    ) {

        spawnObstacles(currentSpeed, gameObjects);
    }

    public void checkCollision(
            Cookie cookie,
            boolean[] gameOverFlag,
            double difficultyMultiplier,
            game.GameController gc
    ) {

        // Ignore collisions during ghost/invincible
        if (cookie.isGhost() || cookie.isInvincible()) {
            return;
        }

        for (int i = 0; i < obstacles.size(); i++) {

            Obstacle o = obstacles.get(i);

            if (cookie.intersects(o)) {

                // Damage calculation
                double damage =
                        o.getBaseDamage()
                                * difficultyMultiplier;

                cookie.decreaseHp(damage);
                gc.shakeCamera(8);

                // Temporary invincibility
                cookie.setInvincible(true);

                // Game over
                if (cookie.getHp() <= 0) {

                    cookie.die();

                    gameOverFlag[0] = true;
                }

                // Remove obstacle safely
                obstacles.remove(i);

                return;
            }
        }
    }

    private void spawnObstacles(
            double currentSpeed,
            List gameObjects
    ) {

        obstacleTimer += 0.016;

        if (obstacleTimer < nextObstacleIn) {
            return;
        }

        obstacleTimer = 0;

        nextObstacleIn =
                1.3 + rng.nextDouble() * 1.8;

        // Random obstacle type
        int type = rng.nextInt(6);

        Obstacle obs =
                createObstacle(
                        type,
                        800.0 + 10,
                        currentSpeed
                );

        obstacles.add(obs);

        gameObjects.add(obs);
    }

    private Obstacle createObstacle(
            int type,
            double x,
            double speed
    ) {

        return switch (type) {

            case 0 -> new CandyWall(x, speed);

            case 1 -> new Spike(x, speed);

            case 2 -> new Block(x, speed);

            case 3 -> new Fireball(x, speed);

            case 4 -> new Bat(x, speed);

            default -> new CloudSpike(x, speed);
        };
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void clear() {
        obstacles.clear();
    }
}