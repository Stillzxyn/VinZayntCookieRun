package core.abilities.implementations;

import core.abilities.CookieAbility;
import core.entities.base.Cookie;
import game.GameController;

/**
 * Ghost Ability - Makes the cookie invisible and pass through obstacles.
 * Duration: 3 seconds, Cooldown: 5 seconds
 */
public class GhostAbility implements CookieAbility {

    private double activeTimer = 0;
    private double cooldownTimer = 0;

    private static final double DURATION = 3.0;
    private static final double COOLDOWN = 5.0;

    @Override
    public void update(GameController gc, Cookie cookie, double delta) {
        // Update active timer
        if (activeTimer > 0) {
            activeTimer -= delta;
            if (activeTimer <= 0) {
                activeTimer = 0;
                cooldownTimer = COOLDOWN;
            }
        }

        // Update cooldown timer
        if (cooldownTimer > 0) {
            cooldownTimer -= delta;
        }
    }

    /**
     * Activate the ghost ability if not on cooldown.
     */
    public void activate() {
        if (cooldownTimer <= 0) {
            activeTimer = DURATION;
        }
    }

    /**
     * Check if the ghost ability is currently active.
     */
    public boolean isActive() {
        return activeTimer > 0;
    }

    /**
     * Get the remaining cooldown time (0 if not on cooldown).
     */
    public double getCooldownRemaining() {
        return Math.max(0, cooldownTimer);
    }

    /**
     * Get the remaining active time (0 if not active).
     */
    public double getActiveRemaining() {
        return Math.max(0, activeTimer);
    }
}
