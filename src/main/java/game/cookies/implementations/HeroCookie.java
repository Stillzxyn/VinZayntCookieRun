package core.entities.cookies.implementations;

import core.entities.base.Cookie;
import core.abilities.JumpBoostAbility;
import javafx.scene.paint.Color;

public class HeroCookie extends Cookie {

    public HeroCookie() {
        super("HeroCookie", Color.web("#1A1A1A"));
        setDisplayName("Hero Cookie");
        setTier("S");
        setHex("#1A1A1A");
        setIconPath("/CookieSprite/HeroCookie/HeroCookie1.png");
        setCookieAbilityDescription("Jump higher");
        setMaxHpValue(115);
        setHp(115);

        // Grant speed boost ability - jumps 20% faster
        this.ability = new JumpBoostAbility();
    }
}
