package core.entities.cookies;

import core.entities.base.Cookie;
import core.entities.cookies.implementations.*;

import java.util.List;
import java.util.function.Supplier;

public class CookieList {

    public static final List<Supplier<Cookie>> ALL =
            List.of(
                    BlueberryCookie::new,
                    BraveGingerbreadCookie::new,
                    PirateCookie::new,
                    ZombieCookie::new,
                    HeroCookie::new
            );

    public static Cookie get(int index) {

        if (index < 0 || index >= ALL.size()) {

            throw new IndexOutOfBoundsException(
                    "No cookie at index " + index
            );
        }

        return ALL.get(index).get();
    }

    public static int size() {

        return ALL.size();
    }

    private CookieList() {
    }
}