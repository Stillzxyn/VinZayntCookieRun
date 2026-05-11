package core.stages;

import java.util.ArrayList;
import java.util.List;

/**
 * StageList - Contains all available game stages/worlds.
 * Similar to CookieList, provides centralized access to all stages.
 */
public class StageList {

    /**
     * All available stages in the game.
     */
    public static final List<Stage> ALL = new ArrayList<>();

    static {
        // Initialize all stages with description and difficulty
        ALL.add(new Stage(0, "Forest", "World1", "A peaceful forest where beginners start their journey", "Easy", 1.0));
        ALL.add(new Stage(1, "Palace", "World2", "Grand palace halls await the experienced runner", "Decent", 1.1));
        ALL.add(new Stage(2, "Theatre", "World3", "Show your skill of escaping in this theatre", "Normal", 1.2));
        ALL.add(new Stage(3, "City of Magic", "World4", "Only the toughest can survive these obstacles", "Hard", 1.3));
        ALL.add(new Stage(4, "CP Grave", "World5", "This is a grave for all of us, CP student", "CP", 1.4));
    }

    /**
     * Get a stage by index.
     */
    public static Stage getStage(int index) {
        if (index >= 0 && index < ALL.size()) {
            return ALL.get(index);
        }
        return ALL.getFirst(); // Default to World 1
    }

}
