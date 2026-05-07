package gamemanager;

import entity.base.Cookie;
import entity.cookies.*;

import java.util.List;
import java.util.function.Supplier;

/**
 * Central registry of all playable cookies.
 *
 * To add a new cookie:
 *   1. Create a subclass of Cookie in entity.cookies
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
     * @param name              display name shown in the UI
     * @param tier              card tier from C to S
     * @param hex               fallback hex color for the selection card
     * @param iconPath          resource path to the icon image (50x70)
     * @param unlocked          whether the cookie is unlocked (purchased)
     * @param price             price if not unlocked
     * @param maxHp             maximum HP of the cookie
     * @param factory           no-arg supplier that creates a fresh Cookie instance
     */
    public record Entry(
            String name,
            String tier,
            String hex,
            String iconPath,
            boolean unlocked,
            int price,
            int maxHp,
            Supplier<Cookie> factory
    ) {}

    public static final List<Entry> ALL = List.of(
        new Entry("Blueberry Cookie",    "S", "#4169E1", "/BlueberryCookie/BlueberryCookie1.png",           true,  0,    110, BlueberryCookie::new),
        new Entry("Brave Gingerbread",   "A", "#D2691E", "/BraveGingerBread/BraveGingerBread1.png",         true,  0,    110, BraveGingerbreadCookie::new),
        new Entry("Pirate Cookie",       "A", "#2C3E50", "/PirateCookie/PirateCookie1.png",                  true,  0,    105, PirateCookie::new),
        new Entry("Zombie Cookie",       "B", "#556B2F", "/ZombieCookie/ZombieCookie1.png",                  true,  0,    95,  ZombieCookie::new),
        new Entry("Ninja Cookie",        "S", "#1A1A1A", "/NinjaCookie/NinjaCookie1.png",                    false, 3000, 115, NinjaCookie::new)
    );

    /** Create a fresh Cookie instance for the given selection index. */
    public static Cookie create(int index) {
        if (index < 0 || index >= ALL.size())
            throw new IndexOutOfBoundsException("No cookie at index " + index);
        return ALL.get(index).factory().get();
    }

    private CookieList() {}  // utility class — not instantiable
}
