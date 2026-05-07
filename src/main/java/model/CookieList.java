package model;

import model.cookies.*;
import model.cookies.*;

import java.util.List;
import java.util.function.Supplier;

/**
 * Central registry of all playable cookies.
 *
 * To add a new cookie:
 *   1. Create a subclass of Cookie in cookierun.model.cookies
 *   2. Add a new Entry to ALL below
 *
 * Usage:
 *   CookieList.ALL           → all entries (for UI / selection screen)
 *   CookieList.create(index) → instantiate a cookie by index
 */
public class CookieList {

    /**
     * Metadata + factory for a single cookie type.
     *
     * @param name       display name shown in the UI
     * @param hex        fallback hex color for the selection card
     * @param spritePath resource path to the sprite sheet (for preview icon)
     * @param spriteCols number of columns in the sprite sheet
     * @param factory    no-arg supplier that creates a fresh Cookie instance
     */
    public record Entry(
            String          name,
            String          hex,
            String          spritePath,
            int             spriteCols,
            Supplier<Cookie> factory
    ) {}

    public static final List<Entry> ALL = List.of(
        new Entry("GingerBrave", "#C8822A", "/sprites/cookie_ginger.png",                      8,  GingerBraveCookie::new),
        new Entry("Strawberry",  "#E84060", "/sprites/cookie_strawberry.png",                  10, StrawberryCookie::new),
        new Entry("Red Chili",   "#CC3300", "/sprites/cookie_chili.png",                       9,  RedChiliCookie::new),
        new Entry("Banana",      "#CCAA00", "/sprites/cookie_banana.png",                      8,  BananaCookie::new),
        new Entry("Choco",       "#4A2200", "/sprites/cookie_choco.png",                       10, ChocoCookie::new),
        new Entry("Wizard",      "#6633AA", "/sprites/cookie_wizard.png",                      9,  WizardCookie::new),
        new Entry("BraveGinger", "#C8822A", "/sprites/BraveGingerBreadCookie_SpriteSheet.png", 4,  BraveGingerCookie::new)
    );

    /** Create a fresh Cookie instance for the given selection index. */
    public static Cookie create(int index) {
        if (index < 0 || index >= ALL.size())
            throw new IndexOutOfBoundsException("No cookie at index " + index);
        return ALL.get(index).factory().get();
    }

    private CookieList() {}  // utility class — not instantiable
}
