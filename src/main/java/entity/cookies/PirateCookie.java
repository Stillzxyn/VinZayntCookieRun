package entity.cookies;

import entity.base.Cookie;
import javafx.scene.paint.Color;

public class PirateCookie extends Cookie {

    public PirateCookie() {
        super("PirateCookie", Color.web("#2C3E50"));
        setDisplayName("Pirate Cookie");
        setTier("A");
        setPrice(0);
        setUnlocked(true);
        setMaxHpValue(105);
        setHp(105);
    }
}
