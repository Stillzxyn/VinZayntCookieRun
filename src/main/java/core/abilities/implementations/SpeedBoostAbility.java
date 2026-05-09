package core.abilities.implementations;

import core.abilities.CookieAbility;
import core.entities.base.Cookie;
import game.GameController;

/**
 * Speed Boost Ability
 * Passive ability for Ninja Cookie.
 */
public class SpeedBoostAbility implements CookieAbility {

    @Override
    public void update(
            GameController gc,
            Cookie cookie,
            double delta
    ) {

        // Passive
    }

    @Override
    public void activate() {

        // Passive
    }

    @Override
    public boolean isActive() {

        return true;
    }

    @Override
    public double getCooldownRemaining() {

        return 0;
    }

    @Override
    public double getCooldownPercent() {

        return 1.0;
    }

    @Override
    public boolean isPassive() {

        return true;
    }

    /**
     * Jump boost multiplier.
     */
    public double getJumpVelocityMultiplier() {

        return 1.2;
    }
}