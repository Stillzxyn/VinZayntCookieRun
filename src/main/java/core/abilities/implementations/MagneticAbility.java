package core.abilities.implementations;

import core.abilities.CookieAbility;
import core.entities.base.Cookie;
import core.entities.collectibles.Collectible;
import game.GameController;

/**
 * Magnetic Ability
 * Passive ability for Blueberry Cookie.
 * Pulls collectibles automatically.
 */
public class MagneticAbility implements CookieAbility {

    private final double magneticRadius;

    private final double attractionStrength;

    public MagneticAbility() {

        this(170.0, 850.0);
    }

    public MagneticAbility(
            double magneticRadius,
            double attractionStrength
    ) {

        this.magneticRadius =
                magneticRadius;

        this.attractionStrength =
                attractionStrength;
    }

    @Override
    public void update(
            GameController gc,
            Cookie cookie,
            double delta
    ) {

        // Always active
        gc.applyMagneticForceToCollectibles(this);
    }

    @Override
    public void activate() {

        // Passive skill
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
     * Pull collectibles toward cookie.
     */
    public void applyForceToCollectible(
            Collectible c,
            Cookie cookie
    ) {

        double targetX =
                cookie.getX()
                        + cookie.getWidth() / 2;

        double targetY =
                cookie.getY()
                        + cookie.getHeight() / 2;

        c.applyMagneticForce(
                targetX,
                targetY,
                magneticRadius,
                attractionStrength
        );
    }

    public double getMagneticRadius() {

        return magneticRadius;
    }

    public double getAttractionStrength() {

        return attractionStrength;
    }
}