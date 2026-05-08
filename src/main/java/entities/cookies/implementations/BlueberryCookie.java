package entities.cookies.implementations;

import entities.base.Cookie;
import abilities.implementations.MagneticAbility;
import javafx.scene.paint.Color;

public class BlueberryCookie extends Cookie {

    public BlueberryCookie() {
        super("BlueberryCookie", Color.web("#4169E1"));
        setDisplayName("Blueberry Cookie");
        setTier("S");
        setHex("#4169E1");
        setIconPath("/BlueberryCookie/BlueberryCookie1.png");
        setPrice(0);
        setUnlocked(true);
        setMaxHpValue(110);
        setHp(110);

        // Grant magnetic coin ability
        this.ability = new MagneticAbility();
    }
}
