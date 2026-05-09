package core.abilities.implementations;

import core.abilities.CookieAbility;
import core.entities.base.Cookie;
import game.GameController;

/**
 * Speed Boost Ability - Makes the cookie faster by increasing jump velocity.
 * Used by Ninja Cookie.
 */
public class SpeedBoostAbility implements CookieAbility {

    @Override
    public void update(GameController gc, Cookie cookie, double delta) {
        // Passive ability - no active update needed
    }
    /**
     * Get the jump velocity multiplier for speed boost.
     * Ninja Cookie jumps 20% higher/faster.
     */
    public double getJumpVelocityMultiplier() {
        return 1.2;  // 20% faster jumps
    }
}
