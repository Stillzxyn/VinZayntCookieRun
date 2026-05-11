package game.config;

/**
 * Central game configuration.
 * All hardcoded values go here for easy tweaking.
 */
public class GameConfig {
    
    // Window settings
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 450;
    public static final boolean RESIZABLE = false;
    
    // Game settings
    public static final double BASE_GAME_SPEED = 300.0;
    public static final double MIN_GAME_SPEED = 200.0;
    public static final double MAX_GAME_SPEED = 1000.0;
    
    // FPS and rendering
    public static final int TARGET_FPS = 60;
    public static final boolean SHOW_FPS = true;
    
    // Audio settings
    public static final boolean AUDIO_ENABLED = true;
    public static final double MASTER_VOLUME = 1.0;
    public static final double MUSIC_VOLUME = 0.7;
    public static final double SFX_VOLUME = 0.8;
    
    // Difficulty settings
    public static final int INITIAL_HEALTH = 3;
    public static final boolean PROGRESSIVE_DIFFICULTY = true;
    
    // Testing/Debug
    public static final boolean DEBUG_MODE = false;
    public static final boolean INFINITE_HEALTH = false;
    
    private GameConfig() {}
}
