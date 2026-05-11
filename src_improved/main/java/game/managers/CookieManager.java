package game.managers;

import core.entities.base.Cookie;
import game.cookies.CookieList;

import java.util.ArrayList;
import java.util.List;

/**
 * Game-level cookie manager.
 *
 * Responsibilities:
 * - Load and manage cookies for gameplay
 * - Handle cookie selection and UI interactions
 * - Provide access to cookies by index, name, or tier
 * - Manage cookie state during selection/loading
 * - Cache cookie instances efficiently
 *
 * Uses CookieList from game.cookies as the data source.
 * Cookie class contains basic data (HP, ability, stats).
 */
public class CookieManager {

    // Singleton instance
    private static CookieManager instance;

    // Cookie cache for frequently accessed instances
    private final Cookie[] cookieCache = new Cookie[CookieList.size()];

    // Game state
    private int selectedCookieIndex = 0;
    private Cookie loadedCookie = null;

    private CookieManager() {
    }

    /**
     * Get singleton instance of CookieManager.
     */
    public static CookieManager getInstance() {
        if (instance == null) {
            instance = new CookieManager();
        }
        return instance;
    }

    // =========================
    // COOKIE LOADING & SELECTION
    // =========================

    /**
     * Load a cookie for gameplay.
     * Creates a fresh instance, prepares frames and resources for the game.
     *
     * @param index The cookie index to load
     * @return The loaded cookie instance (fully prepared)
     */
    public Cookie loadCookie(int index) {
        if (index < 0 || index >= CookieList.size()) {
            throw new IndexOutOfBoundsException(
                    "No cookie at index " + index
            );
        }

        this.selectedCookieIndex = index;
        this.loadedCookie = getFreshCookie(index);

        // Ensure all animation frames are loaded
        loadCookieFrames(this.loadedCookie);

        return loadedCookie;
    }

    /**
     * Load all animation frames for a cookie.
     * Prepares RUN, JUMP, SLIDE, and DEAD animation frames.
     *
     * @param cookie The cookie to load frames for
     */
    private void loadCookieFrames(Cookie cookie) {
        // Cookie class handles frame loading internally via loadAnimationFrames()
        // This is called during Cookie construction, so frames are already loaded
        // This method can be extended for pre-loading or resource verification
    }

    /**
     * Get the currently loaded cookie.
     *
     * @return The loaded cookie, or null if none loaded
     */
    public Cookie getLoadedCookie() {
        return loadedCookie;
    }

    /**
     * Preload a cookie's resources without loading it for gameplay.
     * Useful for pre-caching during selection menu.
     *
     * @param index The cookie index to preload
     */
    public void preloadCookieResources(int index) {
        if (index >= 0 && index < CookieList.size()) {
            Cookie cookie = getCookie(index);
            // Cookie resources are loaded automatically
            // This ensures the instance is cached for quick access
        }
    }

    /**
     * Handle cookie button press from UI.
     * Updates selection and prepares cookie for preview.
     *
     * @param index The index of the button pressed
     */
    public void onCookieButtonPressed(int index) {
        if (index < 0 || index >= CookieList.size()) {
            return;
        }
        this.selectedCookieIndex = index;

        // Preload resources for smooth selection
        preloadCookieResources(index);
    }

    /**
     * Get the currently selected cookie index.
     *
     * @return The selected cookie index
     */
    public int getSelectedCookieIndex() {
        return selectedCookieIndex;
    }

    /**
     * Set the selected cookie index.
     *
     * @param index The index to select
     */
    public void setSelectedCookieIndex(int index) {
        if (index >= 0 && index < CookieList.size()) {
            this.selectedCookieIndex = index;
            preloadCookieResources(index);
        }
    }

    /**
     * Get preview of a cookie for selection UI.
     * Returns cached instance with all resources loaded.
     *
     * @param index The cookie index to preview
     * @return The cookie instance for preview (fully prepared)
     */
    public Cookie previewCookie(int index) {
        Cookie cookie = getCookie(index);
        // Ensure frames are loaded
        loadCookieFrames(cookie);
        return cookie;
    }

    /**
     * Unload cookie resources and clear loaded state.
     * Call when returning to menu or changing scenes.
     */
    public void unloadCookie() {
        this.loadedCookie = null;
        this.selectedCookieIndex = 0;
    }

    // =========================
    // COOKIE ACCESS & QUERIES
    // =========================

    /**
     * Get a cookie by index with caching.
     * Uses cache to avoid recreating instances if accessed multiple times.
     * Use for UI preview and data access.
     *
     * @param index The cookie index (0-based)
     * @return The cookie instance
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public Cookie getCookie(int index) {
        if (index < 0 || index >= CookieList.size()) {
            throw new IndexOutOfBoundsException(
                    "No cookie at index " + index
            );
        }

        // Return cached instance if available
        if (cookieCache[index] != null) {
            return cookieCache[index];
        }

        // Create new instance and cache it
        Cookie cookie = CookieList.get(index);
        cookieCache[index] = cookie;
        return cookie;
    }

    /**
     * Get a fresh cookie instance (uncached).
     * Use this when you need a new independent instance for gameplay.
     *
     * @param index The cookie index (0-based)
     * @return A new cookie instance
     */
    public Cookie getFreshCookie(int index) {
        return CookieList.get(index);
    }

    /**
     * Get cookie by display name.
     *
     * @param displayName The display name of the cookie
     * @return The cookie with matching name, or null if not found
     */
    public Cookie getCookieByName(String displayName) {
        for (int i = 0; i < CookieList.size(); i++) {
            Cookie cookie = getCookie(i);
            if (cookie.getDisplayName().equalsIgnoreCase(displayName)) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * Get the index of a cookie by display name.
     *
     * @param displayName The display name of the cookie
     * @return The index, or -1 if not found
     */
    public int getCookieIndexByName(String displayName) {
        for (int i = 0; i < CookieList.size(); i++) {
            Cookie cookie = getCookie(i);
            if (cookie.getDisplayName().equalsIgnoreCase(displayName)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Get all cookies of a specific tier.
     *
     * @param tier The tier letter (e.g., "S", "A", "B", "C")
     * @return List of cookies matching the tier
     */
    public List<Cookie> getCookiesByTier(String tier) {
        List<Cookie> result = new ArrayList<>();
        for (int i = 0; i < CookieList.size(); i++) {
            Cookie cookie = getCookie(i);
            if (cookie.getTier().equals(tier)) {
                result.add(cookie);
            }
        }
        return result;
    }

    /**
     * Get all available cookies.
     *
     * @return List of all cookies in order
     */
    public List<Cookie> getAllCookies() {
        List<Cookie> result = new ArrayList<>();
        for (int i = 0; i < CookieList.size(); i++) {
            result.add(getCookie(i));
        }
        return result;
    }

    /**
     * Get the total number of cookies.
     *
     * @return Number of available cookies
     */
    public int getTotalCookies() {
        return CookieList.size();
    }

    /**
     * Get cookie statistics summary.
     *
     * @return String with cookie count and tier distribution
     */
    public String getStatistics() {
        int sCount = getCookiesByTier("S").size();
        int aCount = getCookiesByTier("A").size();
        int bCount = getCookiesByTier("B").size();
        int cCount = getCookiesByTier("C").size();

        return String.format(
                "Total Cookies: %d | S-Tier: %d | A-Tier: %d | B-Tier: %d | C-Tier: %d",
                CookieList.size(), sCount, aCount, bCount, cCount
        );
    }

    /**
     * Clear the cookie cache.
     * Use this when you need fresh instances for all cookies.
     */
    public void clearCache() {
        for (int i = 0; i < cookieCache.length; i++) {
            cookieCache[i] = null;
        }
    }
}
