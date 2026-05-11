package game.managers;

import core.entities.base.Cookie;
import game.cookies.implementations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CookieManager.
 * Tests cookie loading, selection, and querying functionality.
 */
public class CookieManagerTest {

    private CookieManager manager;

    @BeforeEach
    public void setUp() {
        // Get fresh instance for each test
        manager = CookieManager.getInstance();
        manager.clearCache();
    }

    // =========================
    // LOADING & SELECTION TESTS
    // =========================

    @Test
    public void testLoadCookie_ValidIndex() {
        Cookie cookie = manager.loadCookie(0);
        assertNotNull(cookie, "Loaded cookie should not be null");
        assertEquals(cookie, manager.getLoadedCookie(), "Loaded cookie should match getLoadedCookie()");
    }

    @Test
    public void testLoadCookie_InvalidIndex_ThrowsException() {
        assertThrows(IndexOutOfBoundsException.class, () -> manager.loadCookie(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> manager.loadCookie(999));
    }

    @Test
    public void testGetLoadedCookie_BeforeLoad() {
        manager.clearCache();
        assertNull(manager.getLoadedCookie(), "Should be null before loading");
    }

    @Test
    public void testOnCookieButtonPressed_ValidIndex() {
        manager.onCookieButtonPressed(0);
        assertEquals(0, manager.getSelectedCookieIndex(), "Selected index should update");
    }

    @Test
    public void testOnCookieButtonPressed_InvalidIndex_IgnoresChange() {
        manager.onCookieButtonPressed(0);
        int originalIndex = manager.getSelectedCookieIndex();
        manager.onCookieButtonPressed(-1);
        assertEquals(originalIndex, manager.getSelectedCookieIndex(), "Invalid index should not change selection");
    }

    @Test
    public void testSetSelectedCookieIndex() {
        manager.setSelectedCookieIndex(2);
        assertEquals(2, manager.getSelectedCookieIndex());
    }

    // =========================
    // COOKIE ACCESS TESTS
    // =========================

    @Test
    public void testGetCookie_Caching() {
        Cookie cookie1 = manager.getCookie(0);
        Cookie cookie2 = manager.getCookie(0);
        assertSame(cookie1, cookie2, "Cached cookies should be the same instance");
    }

    @Test
    public void testGetFreshCookie_NoCache() {
        Cookie cookie1 = manager.getFreshCookie(0);
        Cookie cookie2 = manager.getFreshCookie(0);
        assertNotSame(cookie1, cookie2, "Fresh cookies should be different instances");
    }

    @Test
    public void testGetCookieByName_Found() {
        Cookie cookie = manager.getCookieByName("Hero Cookie");
        assertNotNull(cookie, "Should find cookie by name");
        assertEquals("Hero Cookie", cookie.getDisplayName());
    }

    @Test
    public void testGetCookieByName_NotFound() {
        Cookie cookie = manager.getCookieByName("NonExistent Cookie");
        assertNull(cookie, "Should return null for non-existent cookie");
    }

    @Test
    public void testGetCookieByName_CaseInsensitive() {
        Cookie cookie1 = manager.getCookieByName("hero cookie");
        Cookie cookie2 = manager.getCookieByName("HERO COOKIE");
        assertNotNull(cookie1);
        assertNotNull(cookie2);
    }

    @Test
    public void testGetCookieIndexByName_Found() {
        int index = manager.getCookieIndexByName("Blueberry Cookie");
        assertNotEquals(-1, index, "Should find cookie index");
    }

    @Test
    public void testGetCookieIndexByName_NotFound() {
        int index = manager.getCookieIndexByName("NonExistent");
        assertEquals(-1, index, "Should return -1 for non-existent cookie");
    }

    // =========================
    // TIER TESTS
    // =========================

    @Test
    public void testGetCookiesByTier_STier() {
        List<Cookie> sTierCookies = manager.getCookiesByTier("S");
        assertTrue(sTierCookies.size() > 0, "Should have S-tier cookies");
        for (Cookie cookie : sTierCookies) {
            assertEquals("S", cookie.getTier());
        }
    }

    @Test
    public void testGetCookiesByTier_AllTiers() {
        assertFalse(manager.getCookiesByTier("S").isEmpty());
        assertFalse(manager.getCookiesByTier("A").isEmpty());
        assertFalse(manager.getCookiesByTier("B").isEmpty());
        assertFalse(manager.getCookiesByTier("C").isEmpty());
    }

    @Test
    public void testGetCookiesByTier_NonExistent() {
        List<Cookie> cookies = manager.getCookiesByTier("Z");
        assertTrue(cookies.isEmpty(), "Non-existent tier should return empty list");
    }

    // =========================
    // COLLECTION TESTS
    // =========================

    @Test
    public void testGetAllCookies() {
        List<Cookie> allCookies = manager.getAllCookies();
        assertFalse(allCookies.isEmpty(), "Should have at least one cookie");
        assertEquals(manager.getTotalCookies(), allCookies.size());
    }

    @Test
    public void testGetTotalCookies() {
        int total = manager.getTotalCookies();
        assertTrue(total > 0, "Should have at least one cookie");
    }

    // =========================
    // STATISTICS TESTS
    // =========================

    @Test
    public void testGetStatistics_NotNull() {
        String stats = manager.getStatistics();
        assertNotNull(stats, "Statistics should not be null");
        assertTrue(stats.contains("Total Cookies"), "Statistics should mention total cookies");
        assertTrue(stats.contains("Tier"), "Statistics should mention tiers");
    }

    // =========================
    // CACHE TESTS
    // =========================

    @Test
    public void testClearCache() {
        manager.getCookie(0);
        manager.clearCache();
        Cookie cookie1 = manager.getCookie(0);
        Cookie cookie2 = manager.getCookie(0);
        assertSame(cookie1, cookie2, "Cache should work after clearing");
    }
}
