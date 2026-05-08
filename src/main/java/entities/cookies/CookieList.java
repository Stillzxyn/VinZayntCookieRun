package entities.cookies;

import entities.base.Cookie;
import entities.cookies.implementations.*;

import java.util.List;

/**
 * Central registry of all playable cookies.
 *
 * To add a new cookie:
 *   1. Create a subclass of Cookie in entity.cookies
 *   2. Add a new instance to ALL below
 *
 * Usage:
 *   CookieList.ALL           → all cookie objects (for UI / selection screen)
 *   CookieList.get(index)    → get a cookie by index
 */
public class CookieList {

    // Existing cookie instances
    private static final BlueberryCookie BLUEBERRY = new BlueberryCookie();
    private static final BraveGingerbreadCookie BRAVE_GINGERBREAD = new BraveGingerbreadCookie();
    private static final PirateCookie PIRATE = new PirateCookie();
    private static final ZombieCookie ZOMBIE = new ZombieCookie();
    private static final NinjaCookie NINJA = new NinjaCookie();

    public static final List<Cookie> ALL = List.of(
        BLUEBERRY,
        BRAVE_GINGERBREAD,
        PIRATE,
        ZOMBIE,
        NINJA
    );

    /** Get a cookie by index. */
    public static Cookie get(int index) {
        if (index < 0 || index >= ALL.size())
            throw new IndexOutOfBoundsException("No cookie at index " + index);
        return ALL.get(index);
    }

    private CookieList() {}  // utility class — not instantiable
}
