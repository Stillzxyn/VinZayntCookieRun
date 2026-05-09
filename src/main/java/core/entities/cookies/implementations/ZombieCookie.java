package core.entities.cookies.implementations;

import core.entities.base.Cookie;
import javafx.scene.paint.Color;

public class ZombieCookie extends Cookie {

    public ZombieCookie() {
        super("ZombieCookie", Color.web("#556B2F"));
        setDisplayName("Zombie Cookie");
        setTier("B");
        setHex("#556B2F");
        setIconPath("/CookieSprite/ZombieCookie/ZombieCookie1.png");
        setPrice(0);
        setUnlocked(true);
        setMaxHpValue(150);
        setHp(150);
    }
}
