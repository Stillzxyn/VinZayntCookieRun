package abilities.implementations;

import abilities.CookieAbility;
import entities.base.Cookie;
import entities.collectibles.Coin;
import entities.collectibles.Collectible;
import game.GameController;

/**
 * Magnetic ability - pulls coins toward the cookie within a radius.
 * Only affects coins (Jelly is not magnetic).
 */
public class MagneticAbility implements CookieAbility {

    private final double magneticRadius;
    private final double attractionStrength;

    public MagneticAbility() {
        this(150.0, 800.0);
    }

    public MagneticAbility(double magneticRadius, double attractionStrength) {
        this.magneticRadius = magneticRadius;
        this.attractionStrength = attractionStrength;
    }

    @Override
    public void update(GameController gc, Cookie cookie, double delta) {
        // Apply magnetic force to all collectibles in the game
        gc.applyMagneticForceToCollectibles(this);
    }

    // Public method for GameController to use
    public void applyForceToCollectible(Collectible c, Cookie cookie) {
        if (!(c instanceof Coin)) return;  // Only affect coins

        double targetX = cookie.getX() + cookie.getWidth() / 2;
        double targetY = cookie.getY() + cookie.getHeight() / 2;

        c.applyMagneticForce(targetX, targetY, magneticRadius, attractionStrength);
    }

    public double getMagneticRadius() {
        return magneticRadius;
    }

    public double getAttractionStrength() {
        return attractionStrength;
    }
}
