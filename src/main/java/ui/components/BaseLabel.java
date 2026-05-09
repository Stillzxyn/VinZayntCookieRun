package ui.components;

import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Base label class with common text styling.
 * Provides reusable label formatting and effects.
 */
public class BaseLabel extends Label {
    public BaseLabel(String text) {
        super(text);
        initializeDefaults();
    }
    public BaseLabel(String text, int fontSize, String fontFamily, boolean bold) {
        super(text);
        setFont(fontFamily, fontSize, bold);
    }
    protected void initializeDefaults() {
        setFont("Arial", 14, false);
        setStyle("-fx-text-fill: #FFE066;");
    }
    public void setFont(String fontFamily, int size, boolean bold) {
        Font font = bold
            ? Font.font(fontFamily, FontWeight.BOLD, size)
            : Font.font(fontFamily, size);
        this.setFont(font);
    }
    public void setTextColor(String hexColor) {
        this.setStyle(this.getStyle() + "-fx-text-fill: " + hexColor + ";");
    }
}
