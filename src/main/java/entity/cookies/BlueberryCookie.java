package entity.cookies;

import entity.base.Cookie;
import javafx.scene.paint.Color;

public class BlueberryCookie extends Cookie {

    public BlueberryCookie() {
        super("BlueberryCookie", Color.web("#4169E1"));
        setDisplayName("Blueberry Cookie");
        setTier("S");
        setPrice(0);
        setUnlocked(true);
        setMaxHpValue(110);
        setHp(110);
    }
}
