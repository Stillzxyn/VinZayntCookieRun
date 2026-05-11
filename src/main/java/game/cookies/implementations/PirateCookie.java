package core.entities.cookies.implementations;

import core.entities.base.Cookie;
import core.abilities.GhostAbility;
import javafx.scene.paint.Color;

public class PirateCookie extends Cookie {

    public PirateCookie() {
        super("PirateCookie", Color.web("#2C3E50"));
        setDisplayName("Pirate Cookie");
        setTier("A");
        setHex("#2C3E50");
        setIconPath("/CookieSprite/PirateCookie/PirateCookie1.png");
        setCookieAbilityDescription("Ghost Ability, Ignore Obstacles for 3 seconds with 5 s seconds cool down");
        setMaxHpValue(105);
        setHp(105);

        // Grant ghost ability - pass through obstacles
        this.ability = new GhostAbility();
    }
}
