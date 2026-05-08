package game;

import entities.base.Cookie;
import entities.base.Obstacle;
import abilities.implementations.GhostAbility;
import entities.obstacles.types.airobstacles.Bat;
import entities.obstacles.types.airobstacles.CloudSpike;
import entities.obstacles.types.airobstacles.Fireball;
import entities.obstacles.types.groundobstacles.Block;
import entities.obstacles.types.groundobstacles.CandyWall;
import entities.obstacles.types.groundobstacles.Spike;

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

    public void update(double delta, double currentSpeed, List gameObjects) {
        spawnObstacles(currentSpeed, gameObjects);
    }

    public void checkCollision(Cookie cookie, boolean[] gameOverFlag) {
        for (Obstacle o : obstacles) {
            if (cookie.intersects(o)) {
                boolean hasGhostAbility = cookie.getAbility() instanceof GhostAbility;

                if (!hasGhostAbility) {
                    cookie.die();
                    gameOverFlag[0] = true;
                    return;
                }
            }
        }
    }

    private void spawnObstacles(double currentSpeed, List gameObjects) {
        obstacleTimer += 0.016;  // Approximate delta

        if (obstacleTimer < nextObstacleIn)
            return;

        obstacleTimer = 0;
        nextObstacleIn = 1.3 + rng.nextDouble() * 1.8;

        Obstacle obs = switch (rng.nextInt(6)) {
            case 0 -> new CandyWall(800.0 + 10, currentSpeed);
            case 1 -> new Spike(800.0 + 10, currentSpeed);
            case 2 -> new Block(800.0 + 10, currentSpeed);
            case 3 -> new Fireball(800.0 + 10, currentSpeed);
            case 4 -> new Bat(800.0 + 10, currentSpeed);
            default -> new CloudSpike(800.0 + 10, currentSpeed);
        };

        obstacles.add(obs);
        gameObjects.add(obs);
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public void clear() {
        obstacles.clear();
    }
}
