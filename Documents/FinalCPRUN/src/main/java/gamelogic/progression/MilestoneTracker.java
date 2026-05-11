package gamelogic.progression;

import java.util.*;

/**
 * MilestoneTracker - Tracks game milestones and triggers events.
 */
public class MilestoneTracker {
    private Set<String> achievedMilestones = new HashSet<>();
    private int maxScoreInGame = 0;
    private int maxComboAchieved = 0;

    public void recordMilestone(String milestone) {
        achievedMilestones.add(milestone);
    }

    public void recordScore(int score) {
        maxScoreInGame = Math.max(maxScoreInGame, score);
    }

    public void recordCombo(int combo) {
        maxComboAchieved = Math.max(maxComboAchieved, combo);
    }

    public boolean hasMilestone(String milestone) {
        return achievedMilestones.contains(milestone);
    }

    public Set<String> getAllMilestones() {
        return Collections.unmodifiableSet(achievedMilestones);
    }

    public void reset() {
        achievedMilestones.clear();
    }
}
