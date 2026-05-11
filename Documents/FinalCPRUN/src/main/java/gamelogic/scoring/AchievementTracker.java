package gamelogic.scoring;

import java.util.*;

/**
 * AchievementTracker - Tracks achievements and milestones.
 */
public class AchievementTracker {
    private Set<String> unlockedAchievements = new HashSet<>();
    private int maxComboAchieved = 0;
    private int totalCoinsCollected = 0;
    private int totalObstaclesAvoided = 0;
    private long totalGameTime = 0;

    public void trackCoinCollected() {
        totalCoinsCollected++;
        checkCoinMilestones();
    }

    public void trackObstacleAvoided() {
        totalObstaclesAvoided++;
    }

    public void trackCombo(int comboCount) {
        if (comboCount > maxComboAchieved) {
            maxComboAchieved = comboCount;
            checkComboMilestones();
        }
    }

    public void trackGameTime(long milliseconds) {
        totalGameTime += milliseconds;
    }

    private void checkCoinMilestones() {
        if (totalCoinsCollected == 50) {
            unlockedAchievements.add("COIN_COLLECTOR_50");
        } else if (totalCoinsCollected == 100) {
            unlockedAchievements.add("COIN_COLLECTOR_100");
        }
    }

    private void checkComboMilestones() {
        if (maxComboAchieved >= 10) {
            unlockedAchievements.add("COMBO_MASTER_10");
        } else if (maxComboAchieved >= 20) {
            unlockedAchievements.add("COMBO_MASTER_20");
        }
    }

    public Set<String> getUnlockedAchievements() {
        return Collections.unmodifiableSet(unlockedAchievements);
    }

    public int getMaxComboAchieved() {
        return maxComboAchieved;
    }

    public int getTotalCoinsCollected() {
        return totalCoinsCollected;
    }

    public void reset() {
        unlockedAchievements.clear();
        maxComboAchieved = 0;
        totalCoinsCollected = 0;
        totalObstaclesAvoided = 0;
        totalGameTime = 0;
    }
}
