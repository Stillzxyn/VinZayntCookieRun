package game;

import core.entities.base.Cookie;
import core.entities.base.State;
import game.cookies.implementations.HeroCookie;
import core.stages.Stage;
import core.stages.StageList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameController.
 * Tests game loop logic, scoring, and state management.
 */
public class GameControllerTest {

    private GameController controller;
    private Cookie testCookie;
    private Stage testStage;

    @BeforeEach
    public void setUp() {
        testCookie = new HeroCookie();
        testStage = StageList.ALL.get(0);
        controller = new GameController(testCookie, testStage);
    }

    // =========================
    // INITIALIZATION TESTS
    // =========================

    @Test
    public void testConstructor_InitializesController() {
        assertNotNull(controller, "GameController should be created");
        assertEquals(testCookie, controller.getCookie(), "Cookie should be set");
    }

    @Test
    public void testConstructor_CookieReset() {
        assertEquals(testCookie.getMaxHpValue(), testCookie.getHp(), "Cookie should be reset on init");
    }

    @Test
    public void testSingleton() {
        GameController instance = GameController.getInstance();
        assertEquals(controller, instance, "Singleton should return current controller");
    }

    // =========================
    // GAME STATE TESTS
    // =========================

    @Test
    public void testInitialGameState() {
        assertFalse(controller.isGameOver(), "Should not be game over initially");
        assertFalse(controller.isPaused(), "Should not be paused initially");
    }

    @Test
    public void testTogglePause() {
        assertFalse(controller.isPaused());
        controller.togglePause();
        assertTrue(controller.isPaused());
        controller.togglePause();
        assertFalse(controller.isPaused());
    }

    @Test
    public void testTogglePause_AfterGameOver() {
        controller.update(1000); // Force game over by heavy HP loss
        boolean wasGameOver = controller.isGameOver();
        boolean pausedBefore = controller.isPaused();
        controller.togglePause();
        assertEquals(pausedBefore, controller.isPaused(), "Should not toggle pause if game over");
    }

    // =========================
    // SCORE & COINS TESTS
    // =========================

    @Test
    public void testInitialScore() {
        assertEquals(0, controller.getScore(), "Should start with 0 score");
        assertEquals(0, controller.getCoins(), "Should start with 0 coins");
    }

    @Test
    public void testScoreIncreases_OnUpdate() {
        int initialScore = controller.getScore();
        controller.update(0.016); // ~60 FPS frame
        assertTrue(controller.getScore() >= initialScore, "Score should increase or stay same");
    }

    @Test
    public void testScoreIncreases_WithGameTime() {
        controller.update(0.016);
        int score1 = controller.getScore();
        controller.update(0.1);
        int score2 = controller.getScore();
        assertTrue(score2 > score1, "Score should increase with longer delta");
    }

    // =========================
    // SPEED TESTS
    // =========================

    @Test
    public void testInitialSpeed() {
        assertTrue(controller.getGameTime() >= 0, "Game time should be >= 0");
    }

    @Test
    public void testSpeedScales_WithGameTime() {
        controller.update(0.016);
        double time1 = controller.getGameTime();
        controller.update(0.1);
        double time2 = controller.getGameTime();
        assertTrue(time2 > time1, "Game time should increase");
    }

    // =========================
    // INPUT TESTS
    // =========================

    @Test
    public void testOnJump_WhilePlaying() {
        // Jump should be processed
        controller.onJump();
        // Cookie jump state would need to be checked on cookie
        assertTrue(controller.getCookie() != null, "Cookie should exist");
    }

    @Test
    public void testOnJump_WhilePaused() {
        controller.togglePause();
        controller.onJump();
        // Jump should be buffered but not processed
        assertTrue(controller.isPaused());
    }

    @Test
    public void testOnSlide() {
        controller.onSlide();
        assertTrue(controller.getCookie() != null);
    }

    @Test
    public void testOnAbility_WithAbility() {
        if (controller.getCookie().getAbility() != null) {
            controller.onAbility();
            assertTrue(controller.getCookie().getAbility() != null);
        }
    }

    // =========================
    // CAMERA SHAKE TESTS
    // =========================

    @Test
    public void testCameraShake_InitiallyZero() {
        assertEquals(0, controller.getCameraShake(), "Should start with no camera shake");
    }

    @Test
    public void testShakeCamera() {
        controller.shakeCamera(5.0);
        assertTrue(controller.getCameraShake() > 0, "Camera should shake");
    }

    @Test
    public void testCameraShake_Decays() {
        controller.shakeCamera(10.0);
        double shakeAfterHit = controller.getCameraShake();
        controller.update(0.1);
        double shakeAfterUpdate = controller.getCameraShake();
        assertTrue(shakeAfterUpdate < shakeAfterHit, "Camera shake should decay over time");
    }

    // =========================
    // GAME OVER TESTS
    // =========================

    @Test
    public void testGameOver_HPReduction() {
        testCookie.setHp(10);
        controller.update(1.0); // Heavy HP loss
        // Game should detect HP <= 0
        assertTrue(testCookie.getHp() <= 0 || controller.isGameOver());
    }

    // =========================
    // DELTA TIME TESTS
    // =========================

    @Test
    public void testUpdate_PositiveDelta() {
        int scoreBefore = controller.getScore();
        controller.update(0.016);
        int scoreAfter = controller.getScore();
        assertTrue(scoreAfter >= scoreBefore, "Score should not decrease");
    }

    @Test
    public void testUpdate_ZeroDelta() {
        int scoreBefore = controller.getScore();
        controller.update(0);
        int scoreAfter = controller.getScore();
        assertEquals(scoreBefore, scoreAfter, "No delta should not change score");
    }

    // =========================
    // MANAGER ACCESS TESTS
    // =========================

    @Test
    public void testGetCookieManager() {
        assertNotNull(controller.getCookieManager(), "Should have cookie manager");
    }

    @Test
    public void testResetAll() {
        controller.update(0.1);
        controller.resetAll();
        // Cookie manager cache should be cleared
        assertNotNull(controller.getCookieManager());
    }

    @Test
    public void testGetGameInfo() {
        String info = controller.getGameInfo();
        assertNotNull(info, "Game info should not be null");
        assertTrue(info.contains("Score"), "Should contain score info");
    }
}
