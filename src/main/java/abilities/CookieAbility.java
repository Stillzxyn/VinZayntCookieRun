package abilities;

import entities.base.Cookie;
import game.GameController;

/**
 * Strategy interface for cookie special abilities.
 * Each ability controls its own logic and state.
 */
public interface CookieAbility {
    /**
     * Called each frame to update the ability.
     * Ability has full access to the game controller and cookie.
     */
    void update(GameController gc, Cookie cookie, double delta);
}
