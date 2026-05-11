package gui.components;

import core.entities.base.Cookie;
import game.managers.CookieManager;
import audio.SoundManager;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

/**
 * Row pane displaying all cookie selection cards in a horizontal layout.
 * Manages card selection and provides access to the selected cookie index.
 * Uses CookieManager for centralized cookie access.
 * Cookies are sorted by tier (S→C) then by name alphabetically.
 */
public class CookieCardsRowPane extends HBox {

    private int selectedIndex;
    private final List<CookieSelectionCard> cards = new ArrayList<>();

    public CookieCardsRowPane(int initialSelection) {
        // Always select first cookie (after sorting)
        this.selectedIndex = 0;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.setStyle("-fx-background-color: transparent;");

        CookieManager manager = CookieManager.getInstance();

        // Get all cookies and sort by tier (S→A→B→C), then by name
        List<Cookie> allCookies = manager.getAllCookies();
        allCookies.sort(
            Comparator
                .comparingInt((Cookie c) -> getTierRank(c.getTier()))  // Sort by tier rank (S=0, A=1, B=2, C=3)
                .thenComparing(Cookie::getDisplayName)  // Then by name alphabetically
        );

        // Create cards in sorted order
        for (int i = 0; i < allCookies.size(); i++) {
            Cookie cookie = allCookies.get(i);
            CookieSelectionCard card = new CookieSelectionCard(
                cookie,
                i,
                i == 0  // Only first cookie is selected
            );

            // Set click handler to select cookie
            final int index = i;
            card.setOnMouseClicked(e -> selectCard(index));

            cards.add(card);
            this.getChildren().add(card);
        }

        // Set first cookie as selected with selected style (golden border)
        if (!cards.isEmpty()) {
            CookieSelectionCard firstCard = cards.get(0);
            firstCard.setSelected(true);
            // Selected style is applied automatically via setSelected(true)
        }
    }

    private void selectCard(int index) {
        // Play click sound
        SoundManager.getInstance().playClickSound();

        this.selectedIndex = index;

        // Notify CookieManager of button press
        CookieManager.getInstance().onCookieButtonPressed(index);

        // Update visual selection on all cards
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setSelected(i == index);
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * Get numeric rank for tier (lower = first).
     * C-tier=0, B-tier=1, A-tier=2, S-tier=3
     */
    private int getTierRank(String tier) {
        return switch (tier) {
            case "C" -> 0;
            case "B" -> 1;
            case "A" -> 2;
            case "S" -> 3;
            default -> 4;  // Unknown tiers go to the end
        };
    }
}
