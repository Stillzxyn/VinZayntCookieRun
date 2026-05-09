package game.managers;

import core.entities.base.Cookie;
import core.entities.base.Physics;
import core.entities.items.HealthItem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages health item spawning and collision detection.
 */
public class HealthManager {

    private final List<HealthItem> healthItems = new ArrayList<>();
    private double healthSpawnTimer = 0;
    private static final double HEALTH_SPAWN_INTERVAL = 8.0;

    public void update(double delta, List gameObjects) {
        spawnHealthItems(gameObjects);
    }

    public void checkCollision(Cookie cookie) {
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

    private void spawnHealthItems(List gameObjects) {
        healthSpawnTimer += 0.016;  // Approximate delta

        if (healthSpawnTimer < HEALTH_SPAWN_INTERVAL)
            return;

        healthSpawnTimer = 0;

        HealthItem item = new HealthItem(800.0 + 10, Physics.GROUND_Y - 120);

        healthItems.add(item);
        gameObjects.add(item);
    }

    public List<HealthItem> getHealthItems() {
        return healthItems;
    }

    public void clear() {
        healthItems.clear();
    }
}
