package core.entities.cookies.implementations;

import core.entities.base.Cookie;
import core.abilities.MagneticAbility;
import javafx.scene.paint.Color;

public class BlueberryCookie extends Cookie {

    public BlueberryCookie() {
        super("BlueberryCookie", Color.web("#4169E1"));
        setDisplayName("Blueberry Cookie");
        setTier("S");
        setHex("#4169E1");
        setIconPath("/CookieSprite/BlueberryCookie/BlueberryCookie1.png");
        setCookieAbilityDescription("Attract jelly and coins in the range");
        setMaxHpValue(110);
        setHp(110);


        // Grant magnetic coin ability
        this.ability = new MagneticAbility();
    }
}
