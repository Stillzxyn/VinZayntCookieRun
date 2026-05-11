package gamelogic.progression;

/**
 * ProgressionSystem - Tracks overall game progression and unlocks.
 */
public class ProgressionSystem {
    private int stagesUnlocked = 1; // Start with stage 1
    private int highestStageReached = 0;
    private int totalTimePlayed = 0;

    public void unlockStage(int stageNumber) {
        stagesUnlocked = Math.max(stagesUnlocked, stageNumber + 1);
        highestStageReached = Math.max(highestStageReached, stageNumber);
    }

    public boolean isStageUnlocked(int stageNumber) {
        return stageNumber < stagesUnlocked;
    }

    public int getStagesUnlocked() {
        return stagesUnlocked;
    }

    public int getHighestStageReached() {
        return highestStageReached;
    }

    public void addGameTime(int seconds) {
        totalTimePlayed += seconds;
    }

    public int getTotalTimePlayed() {
        return totalTimePlayed;
    }
}
