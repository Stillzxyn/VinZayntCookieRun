package gui;

import gamemanager.CookieList;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * A card representing a single cookie in the selection screen.
 * Displays: icon, tier, selection status, price, and max HP.
 */
public class CookieSelectionCard {

    private final CookieList.Entry entry;
    private final int index;
    private boolean selected = false;
    private VBox cardContainer;
    private StackPane iconPane;

    public CookieSelectionCard(CookieList.Entry entry, int index) {
        this.entry = entry;
        this.index = index;
        buildCard();
    }

    /**
     * Build the card UI with icon, tier badge, and stats.
     */
    private void buildCard() {
        // Icon pane (50x70)
        iconPane = buildIconPane();

        // Tier badge (positioned at top-right)
        javafx.scene.control.Label tierLabel = new javafx.scene.control.Label(entry.tier());
        tierLabel.setStyle("""
            -fx-background-color: linear-gradient(to bottom,#FFD700,#FFA500);
            -fx-text-fill: #333;
            -fx-font-size: 12px;
            -fx-font-weight: bold;
            -fx-padding: 2 6;
            -fx-background-radius: 8;
            """);

        // Selection indicator (checkmark)
        javafx.scene.control.Label checkLabel = new javafx.scene.control.Label("");
        checkLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #00FF00;");
        updateCheckmark(checkLabel);

        // HP display
        javafx.scene.control.Label hpLabel = new javafx.scene.control.Label("HP: " + entry.maxHp());
        hpLabel.setStyle("""
            -fx-text-fill: #90EE90;
            -fx-font-size: 11px;
            -fx-font-weight: bold;
            """);

        // Price display (if not unlocked)
        javafx.scene.control.Label priceLabel = new javafx.scene.control.Label("");
        if (!entry.unlocked()) {
            priceLabel.setText(entry.price() + " coins");
            priceLabel.setStyle("""
                -fx-text-fill: #FFD700;
                -fx-font-size: 10px;
                -fx-font-weight: bold;
                """);
        }

        // Layout: vertical box with icon, tier badge, HP, and price
        VBox statsBox = new VBox(2);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.getChildren().addAll(hpLabel);
        if (!entry.unlocked()) {
            statsBox.getChildren().add(priceLabel);
        }

        cardContainer = new VBox(4, iconPane, tierLabel, statsBox);
        cardContainer.setAlignment(Pos.CENTER);
        cardContainer.setPrefSize(80, 130);
        cardContainer.setStyle(cardStyle(selected));
    }

    /**
     * Build the icon pane (50x70 with fallback).
     */
    private StackPane buildIconPane() {
        StackPane sp = new StackPane();
        sp.setPrefSize(50, 70);

        // Fallback canvas
        Canvas fallback = new Canvas(50, 70);
        GraphicsContext gc = fallback.getGraphicsContext2D();
        gc.setFill(Color.web(entry.hex()));
        gc.fillRoundRect(2, 2, 46, 66, 8, 8);
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Impact", 16));
        gc.fillText(entry.name().substring(0, 1), 18, 40);

        sp.getChildren().add(fallback);

        // Try to load the icon image
        try {
            String iconPath = entry.iconPath();
            // Remove leading slash if present for resource loading
            if (iconPath.startsWith("/")) {
                iconPath = iconPath.substring(1);
            }

            var stream = getClass().getResourceAsStream("/" + iconPath);
            if (stream == null) {
                stream = getClass().getResourceAsStream(iconPath);
            }

            if (stream != null) {
                Image icon = new Image(stream);
                if (icon != null && !icon.isError()) {
                    ImageView iv = new ImageView(icon);
                    iv.setFitWidth(50);
                    iv.setFitHeight(70);
                    iv.setPreserveRatio(true);
                    sp.getChildren().setAll(iv);
                }
            }
        } catch (Exception ignored) {}

        return sp;
    }

    /**
     * Update the card appearance based on selection state.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        if (cardContainer != null) {
            cardContainer.setStyle(cardStyle(selected));
        }
    }

    public boolean isSelected() {
        return selected;
    }

    /**
     * Get the card UI container.
     */
    public VBox getCardUI() {
        return cardContainer;
    }

    /**
     * Get the index of this card in the cookie list.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Get the cookie entry.
     */
    public CookieList.Entry getEntry() {
        return entry;
    }

    /**
     * Get the icon pane.
     */
    public StackPane getIconPane() {
        return iconPane;
    }

    /**
     * Update the checkmark label based on selection state.
     */
    private void updateCheckmark(javafx.scene.control.Label label) {
        label.setText(selected ? "✓" : "");
    }

    /**
     * Apply style to the card.
     */
    private String cardStyle(boolean selected) {
        return selected
            ? "-fx-background-color: rgba(255,215,0,0.3);" +
              "-fx-background-radius: 12;" +
              "-fx-border-color: #FFD700;" +
              "-fx-border-width: 2;" +
              "-fx-border-radius: 12;" +
              "-fx-cursor: hand;" +
              "-fx-effect: dropshadow(gaussian,#FFD700,10,0.5,0,0);"
            : "-fx-background-color: rgba(255,255,255,0.1);" +
              "-fx-background-radius: 12;" +
              "-fx-border-color: rgba(255,255,255,0.2);" +
              "-fx-border-width: 1;" +
              "-fx-border-radius: 12;" +
              "-fx-cursor: hand;";
    }
}
