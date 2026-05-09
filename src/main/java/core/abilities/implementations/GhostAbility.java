package core.abilities.implementations;

import core.abilities.CookieAbility;
import core.entities.base.Cookie;
import game.GameController;

/**
 * Ghost Ability
 * Pirate Cookie active skill.
 */
public class GhostAbility implements CookieAbility {

    private double activeTimer = 0;

    private double cooldownTimer = 0;

    private static final double DURATION = 3.0;

    private static final double COOLDOWN = 5.0;

    @Override
    public void update(
            GameController gc,
            Cookie cookie,
            double delta
    ) {

        // Active state
        if (activeTimer > 0) {

            activeTimer -= delta;

            cookie.setGhost(true);

            if (activeTimer <= 0) {

                activeTimer = 0;

                cooldownTimer = COOLDOWN;

                cookie.setGhost(false);
            }
        }

        // Cooldown
        if (cooldownTimer > 0) {

            cooldownTimer -= delta;

            if (cooldownTimer < 0) {
                cooldownTimer = 0;
            }
        }
    }

    @Override
    public void activate() {

        if (cooldownTimer > 0
                || activeTimer > 0) {
            return;
        }

        activeTimer = DURATION;
    }

    @Override
    public boolean isActive() {
        return activeTimer > 0;
    }

    @Override
    public double getCooldownRemaining() {

        return Math.max(0, cooldownTimer);
    }

    @Override
    public double getCooldownPercent() {

        return 1.0 -
                (cooldownTimer / COOLDOWN);
    }

    @Override
    public boolean isPassive() {
        return false;
    }
}