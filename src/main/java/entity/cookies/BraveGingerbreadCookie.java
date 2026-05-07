package entity.cookies;

import entity.base.Cookie;
import javafx.scene.paint.Color;

public class BraveGingerbreadCookie extends Cookie {

    public BraveGingerbreadCookie() {
        super("BraveGingerBread", Color.web("#D2691E"));
        setDisplayName("Brave Gingerbread");
        setTier("A");
        setPrice(0);
        setUnlocked(true);
        setMaxHpValue(110);
        setHp(110);
    }
}
