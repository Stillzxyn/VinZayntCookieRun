package ui.views.pages;

import core.entities.cookies.CookieList;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/**
 * Row pane displaying all cookie selection cards in a horizontal layout.
 * Manages card selection and provides access to the selected cookie index.
 */
public class CardsRowPane extends HBox {

    private int selectedIndex;

    public CardsRowPane(int initialSelection) {
        this.selectedIndex = initialSelection;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-background-color: transparent;");

        // Create a card for each cookie
        for (int i = 0; i < CookieList.ALL.size(); i++) {
            CookieSelectionCard card = new CookieSelectionCard(
                CookieList.get(i),
                i,
                i == initialSelection
            );

            // Set click handler to select cookie
            final int index = i;
            card.setOnMouseClicked(e -> selectCard(index));

            this.getChildren().add(card);
        }
    }

    private void selectCard(int index) {
        this.selectedIndex = index;
        // Update visual selection on all cards
        for (int i = 0; i < this.getChildren().size(); i++) {
            CookieSelectionCard card = (CookieSelectionCard) this.getChildren().get(i);
            card.setSelected(i == index);
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
