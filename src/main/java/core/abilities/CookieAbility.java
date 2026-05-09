package core.abilities;

import core.entities.base.Cookie;
import game.GameController;

/**
 * Strategy interface for cookie abilities.
 */
public interface CookieAbility {

    /**
     * Update ability each frame.
     */
    void update(
            GameController gc,
            Cookie cookie,
            double delta
    );

    /**
     * Activate ability.
     */
    void activate();

    /**
     * Is ability currently active?
     */
    boolean isActive();

    /**
     * Remaining cooldown.
     */
    double getCooldownRemaining();

    /**
     * Cooldown percent.
     * 1.0 = ready
     * 0.0 = just used
     */
    double getCooldownPercent();

    /**
     * Is this a passive ability?
     */
    boolean isPassive();
}