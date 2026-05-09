package ui.views.pages;

import core.stages.StageList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

/**
 * Stage row pane that displays all stage selection cards horizontally.
 * Manages card creation, selection state, and click handlers.
 * All stages are visible at once with the initially selected stage highlighted.
 */
public class StagesRowPane extends HBox {

    private final List<StageSelectionCard> cards;
    private int selectedIndex;
    private Runnable onStageSelected;

    /**
     * Create a stage row pane with all stages visible.
     * @param initialSelectedIndex The index of the stage to select initially (0-4 for Worlds 1-5)
     */
    public StagesRowPane(int initialSelectedIndex) {
        super(12);
        // Clamp initial index to valid range
        this.selectedIndex = Math.max(0, Math.min(initialSelectedIndex, StageList.ALL.size() - 1));
        this.cards = new ArrayList<>();
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

        buildStages();
    }

    /**
     * Build all stage selection cards horizontally.
     */
    private void buildStages() {
        for (int i = 0; i < StageList.ALL.size(); i++) {
            final int index = i;
            StageSelectionCard card = new StageSelectionCard(StageList.ALL.get(index), index);
            cards.add(card);

            // Click handler
            card.setOnMouseClicked(e -> {
                selectedIndex = index;
                refreshStages();
                if (onStageSelected != null) {
                    onStageSelected.run();
                }
            });

            this.getChildren().add(card);
        }

        // Set initial selection
        cards.get(selectedIndex).setSelected(true);
    }

    /**
     * Refresh stage selection states.
     */
    private void refreshStages() {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setSelected(i == selectedIndex);
        }
    }

    /**
     * Get the currently selected stage index.
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Set the selected stage index.
     */
    public void setSelectedIndex(int index) {
        if (index >= 0 && index < cards.size()) {
            selectedIndex = index;
            refreshStages();
        }
    }

    /**
     * Set callback for when a stage is selected.
     */
    public void setOnStageSelected(Runnable callback) {
        this.onStageSelected = callback;
    }

    /**
     * Get all stage cards.
     */
    public List<StageSelectionCard> getCards() {
        return cards;
    }
}
