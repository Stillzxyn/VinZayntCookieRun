package entity.cookies;

import entity.base.Cookie;
import javafx.scene.paint.Color;

public class ZombieCookie extends Cookie {

    public ZombieCookie() {
        super("ZombieCookie", Color.web("#556B2F"));
        setDisplayName("Zombie Cookie");
        setTier("B");
        setPrice(0);
        setUnlocked(true);
        setMaxHpValue(95);
        setHp(95);
    }
}
