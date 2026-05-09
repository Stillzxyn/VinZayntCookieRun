package core.entities.cookies.implementations;

import core.entities.base.Cookie;
import core.abilities.implementations.SpeedBoostAbility;
import javafx.scene.paint.Color;

public class NinjaCookie extends Cookie {

    public NinjaCookie() {
        super("NinjaCookie", Color.web("#1A1A1A"));
        setDisplayName("Ninja Cookie");
        setTier("S");
        setHex("#1A1A1A");
        setIconPath("/CookieSprite/NinjaCookie/NinjaCookie1.png");
        setPrice(3000);
        setUnlocked(false);
        setMaxHpValue(115);
        setHp(115);

        // Grant speed boost ability - jumps 20% faster
        this.ability = new SpeedBoostAbility();
    }
}
