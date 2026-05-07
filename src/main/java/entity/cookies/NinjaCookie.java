package entity.cookies;

import entity.base.Cookie;
import javafx.scene.paint.Color;

public class NinjaCookie extends Cookie {

    public NinjaCookie() {
        super("NinjaCookie", Color.web("#1A1A1A"));
        setDisplayName("Ninja Cookie");
        setTier("S");
        setPrice(3000);
        setUnlocked(false);
        setMaxHpValue(115);
        setHp(115);
    }
}
