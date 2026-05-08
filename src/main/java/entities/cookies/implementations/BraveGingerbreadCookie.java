package entities.cookies.implementations;

import entities.base.Cookie;
import javafx.scene.paint.Color;

public class BraveGingerbreadCookie extends Cookie {

    public BraveGingerbreadCookie() {
        super("BraveGingerBread", Color.web("#D2691E"));
        setDisplayName("Brave Gingerbread Cookie");
        setTier("A");
        setHex("#D2691E");
        setIconPath("/BraveGingerBread/BraveGingerBread1.png");
        setPrice(0);
        setUnlocked(true);
        setMaxHpValue(110);
        setHp(110);
    }
}
