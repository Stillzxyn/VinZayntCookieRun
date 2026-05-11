package core.entities.base;

import core.entities.base.Cookie;
import core.entities.base.State;
import game.cookies.implementations.HeroCookie;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Cookie base class.
 * Tests HP management, state, and abilities.
 */
public class CookieTest {

    private Cookie cookie;

    @BeforeEach
    public void setUp() {
        cookie = new HeroCookie();
    }

    // =========================
    // HP SYSTEM TESTS
    // =========================

    @Test
    public void testInitialHP() {
        assertTrue(cookie.getHp() > 0, "Should start with positive HP");
        assertEquals(cookie.getMaxHpValue(), cookie.getHp(), "Should start at max HP");
    }

    @Test
    public void testDecreaseHP() {
        double initialHP = cookie.getHp();
        cookie.decreaseHp(10);
        assertTrue(cookie.getHp() < initialHP, "HP should decrease");
    }

    @Test
    public void testDecreaseHP_NegativeFloor() {
        cookie.decreaseHp(1000);
        assertEquals(0, cookie.getHp(), "HP should not go below 0");
    }

    @Test
    public void testHeal() {
        cookie.decreaseHp(50);
        double hpBeforeHeal = cookie.getHp();
        cookie.heal(20);
        assertEquals(hpBeforeHeal + 20, cookie.getHp(), "Healing should increase HP");
    }

    @Test
    public void testHeal_MaxCap() {
        cookie.heal(1000);
        assertEquals(cookie.getMaxHpValue(), cookie.getHp(), "HP should not exceed max");
    }

    @Test
    public void testSetHP() {
        cookie.setHp(50);
        assertEquals(50, cookie.getHp());
    }

    @Test
    public void testSetHP_NegativeFloor() {
        cookie.setHp(-100);
        assertEquals(0, cookie.getHp(), "HP should not be negative");
    }

    // =========================
    // METADATA TESTS
    // =========================

    @Test
    public void testGetDisplayName() {
        assertNotNull(cookie.getDisplayName());
        assertFalse(cookie.getDisplayName().isEmpty());
    }

    @Test
    public void testSetDisplayName() {
        cookie.setDisplayName("Custom Cookie");
        assertEquals("Custom Cookie", cookie.getDisplayName());
    }

    @Test
    public void testGetTier() {
        assertNotNull(cookie.getTier());
        assertTrue(cookie.getTier().matches("[A-Z]"), "Tier should be a single letter");
    }

    @Test
    public void testSetTier() {
        cookie.setTier("A");
        assertEquals("A", cookie.getTier());
    }

    @Test
    public void testGetMaxHpValue() {
        assertTrue(cookie.getMaxHpValue() > 0, "Max HP should be positive");
    }

    @Test
    public void testSetMaxHpValue() {
        cookie.setMaxHpValue(150);
        assertEquals(150, cookie.getMaxHpValue());
    }

    // =========================
    // STATE TESTS
    // =========================

    @Test
    public void testInitialState() {
        assertEquals(State.RUNNING, cookie.getState());
    }

    @Test
    public void testDieStateChange() {
        cookie.die();
        assertEquals(State.DEAD, cookie.getState());
    }

    @Test
    public void testIsAlive() {
        assertTrue(cookie.isAlive(), "Cookie should be alive initially");
        cookie.die();
        assertFalse(cookie.isAlive(), "Dead cookie should not be alive");
    }

    // =========================
    // ABILITY TESTS
    // =========================

    @Test
    public void testGetAbility() {
        assertNotNull(cookie.getAbility(), "HeroCookie should have an ability");
    }

    @Test
    public void testGhost_Inactive() {
        assertFalse(cookie.isGhost(), "Should not be ghost initially");
    }

    @Test
    public void testSetGhost() {
        cookie.setGhost(true);
        assertTrue(cookie.isGhost());
    }

    @Test
    public void testInvincibility() {
        assertFalse(cookie.isInvincible(), "Should not be invincible initially");
        cookie.setInvincible(true);
        assertTrue(cookie.isInvincible());
    }

    // =========================
    // PHYSICS TESTS
    // =========================

    @Test
    public void testGetPhysics() {
        assertNotNull(cookie.getPhysics(), "Physics should not be null");
    }

    @Test
    public void testGetPosition() {
        assertTrue(cookie.getX() >= 0, "X position should be valid");
        assertTrue(cookie.getY() >= 0, "Y position should be valid");
    }

    @Test
    public void testGetDimensions() {
        assertTrue(cookie.getWidth() > 0, "Width should be positive");
        assertTrue(cookie.getHeight() > 0, "Height should be positive");
    }

    // =========================
    // RESET TESTS
    // =========================

    @Test
    public void testReset() {
        cookie.decreaseHp(50);
        cookie.setGhost(true);
        cookie.reset();

        assertEquals(cookie.getMaxHpValue(), cookie.getHp(), "HP should reset to max");
        assertEquals(State.RUNNING, cookie.getState(), "State should reset to running");
    }
}
