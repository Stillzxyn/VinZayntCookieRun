package ui.components;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Base card class - HBox container with left and right panes.
 * Provides common card styling, selection state, and hover effects.
 * Subclasses populate leftPane and rightPane with their content.
 */
public class BaseCard extends VBox {

    protected boolean selected = false;
    protected VBox upPane;
    protected HBox downPane;
    protected String selectedStyle;
    protected String normalStyle;
    protected String hoverStyle;
    protected double hoverScale = 1.1;

    /**
     * Create a base card with default sizing.
     */
    public BaseCard() {
        super(15);
        this.setAlignment(Pos.CENTER);
        initializePanes(300, 200);
    }

    /**
     * Create a base card with custom dimensions.
     * @param width card width
     * @param height card height
     */
    public BaseCard(double width, double height) {
        super(15);
        this.setPrefSize(width, height);
        initializePanes(width, height);
    }

    /**
     * Initialize the left and right panes.
     */
    protected void initializePanes(double width, double height) {
        // Left pane
        upPane = new VBox(8);
        upPane.setAlignment(Pos.CENTER);
        upPane.setPrefWidth(width * 0.20);
        upPane.setStyle("-fx-background-color: transparent;");

        // Right pane
        downPane = new HBox(10);
        downPane.setAlignment(Pos.TOP_LEFT);
        downPane.setPrefWidth(width * 0.80);
        downPane.setStyle("-fx-background-color: transparent;");

        // Add panes to this HBox
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-padding: 12; -fx-background-color: transparent;");
        this.getChildren().addAll(upPane, downPane);

        // Initialize styling
        initializeDefaults();
    }

    /**
     * Initialize default styling.
     */
    protected void initializeDefaults() {
        // Default style
        this.normalStyle = """
            -fx-background-color: rgba(70, 70, 110, 0.85);
            -fx-background-radius: 12;
            -fx-border-color: rgba(200, 200, 255, 0.4);
            -fx-border-width: 2;
            -fx-border-radius: 12;
            -fx-cursor: hand;
            """;

        // Selected style (with golden border)
        this.selectedStyle = """
            -fx-background-color: rgba(70, 70, 110, 0.85);
            -fx-background-radius: 12;
            -fx-border-color: #FFD700;
            -fx-border-width: 3;
            -fx-border-radius: 12;
            -fx-cursor: hand;
            """;

        // Hover style (brightens up)
        this.hoverStyle = """
            -fx-background-color: rgba(120, 120, 180, 0.95);
            -fx-background-radius: 12;
            -fx-border-color: rgba(255, 255, 255, 0.8);
            -fx-border-width: 2;
            -fx-border-radius: 12;
            -fx-cursor: hand;
            """;

        applyNormalStyle();
        setupHoverEffects();
    }

    /**
     * Setup hover effects.
     */
    protected void setupHoverEffects() {
        this.setOnMouseEntered(e -> onHoverEnter());
        this.setOnMouseExited(e -> onHoverExit());
    }

    /**
     * Called when mouse enters card.
     */
    protected void onHoverEnter() {
        this.setScaleX(hoverScale);
        this.setScaleY(hoverScale);
        // Only show hover style if NOT selected
        if (!selected) {
            this.setStyle(hoverStyle);
        }
    }

    /**
     * Called when mouse exits card.
     */
    protected void onHoverExit() {
        this.setScaleX(1.0);
        this.setScaleY(1.0);
        // Always return to current style (selected or normal)
        applyCurrentStyle();
    }

    /**
     * Set selection state.
     * @param selected whether card is selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
        applyCurrentStyle();
    }

    /**
     * Get selection state.
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Apply normal style.
     */
    protected void applyNormalStyle() {
        this.setStyle(normalStyle);
    }

    /**
     * Apply selected style.
     */
    protected void applySelectedStyle() {
        this.setStyle(selectedStyle);
    }

    /**
     * Apply the appropriate style based on selection state.
     */
    protected void applyCurrentStyle() {
        if (selected) {
            applySelectedStyle();
        } else {
            applyNormalStyle();
        }
    }

    public void setCustomStyles(String normalStyle, String selectedStyle) {
        this.normalStyle = normalStyle;
        this.selectedStyle = selectedStyle;
        applyCurrentStyle();
    }

    /**
     * Set hover scale factor.
     */
    public void setHoverScale(double scale) {
        this.hoverScale = scale;
    }

}
