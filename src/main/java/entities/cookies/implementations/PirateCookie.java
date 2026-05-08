package entities.cookies.implementations;

import entities.base.Cookie;
import abilities.implementations.GhostAbility;
import javafx.scene.paint.Color;

public class PirateCookie extends Cookie {

    public PirateCookie() {
        super("PirateCookie", Color.web("#2C3E50"));
        setDisplayName("Pirate Cookie");
        setTier("A");
        setHex("#2C3E50");
        setIconPath("/PirateCookie/PirateCookie1.png");
        setPrice(0);
        setUnlocked(true);
        setMaxHpValue(105);
        setHp(105);

        // Grant ghost ability - pass through obstacles
        this.ability = new GhostAbility();
    }
}
