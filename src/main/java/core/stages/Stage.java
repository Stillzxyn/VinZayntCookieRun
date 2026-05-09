package core.stages;

/**
 * Stage entity representing a game level/world.
 * Contains stage properties and resource paths.
 */
public class Stage {

    private final int id;
    private final String displayName;
    private final String worldName;
    private final String description;
    private final String difficulty;
    private final double difficultyMultiplier;
    private final String menuBackgroundPath;
    private final String playBackgroundPath;
    private final String floorPath;
    private final String jellyLogoPath;

    /**
     * Create a stage with description, difficulty, and damage multiplier.
     */
    public Stage(int id, String displayName, String worldName, String description, String difficulty, double difficultyMultiplier) {
        this.id = id;
        this.displayName = displayName;
        this.worldName = worldName;
        this.description = description;
        this.difficulty = difficulty;
        this.difficultyMultiplier = difficultyMultiplier;
        this.menuBackgroundPath = String.format("/Stages/%s/%sMenuBg.png", worldName, worldName);
        this.playBackgroundPath = String.format("/Stages/%s/%sPlayBg.png", worldName, worldName);
        this.floorPath = String.format("/Stages/%s/%sFloor.png", worldName, worldName);
        this.jellyLogoPath = String.format("/Stages/%s/%sJelly.png", worldName, worldName);
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getWorldName() {
        return worldName;
    }

    public String getMenuBackgroundPath() {
        return menuBackgroundPath;
    }

    public String getPlayBackgroundPath() {
        return playBackgroundPath;
    }

    public String getFloorPath() {
        return floorPath;
    }

    public String getJellyLogoPath() {
        return jellyLogoPath;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public double getDifficultyMultiplier() {
        return difficultyMultiplier;
    }
}
