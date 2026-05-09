package ui.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Base button class with common hover effects and image support.
 * Can be used as an image button with hover scale effects.
 */
public class BaseButton extends ImageView {

    protected double hoverScale = 1.1;
    protected Runnable onAction;

    /**
     * Constructor for image-based button.
     */
    public BaseButton() {
        setupDefaults();
    }

    /**
     * Constructor with image path.
     */
    public BaseButton(String imagePath) {
        loadImage(imagePath);
        setupDefaults();
    }

   //Load image from resource path.
    protected void loadImage(String imagePath) {
        try {
            var imageStream = getClass().getResourceAsStream(imagePath);
            if (imageStream != null) {
                Image image = new Image(imageStream);
                this.setImage(image);
            }
        } catch (Exception ignored) {}
    }

     //Initialize default settings.
    protected void setupDefaults() {
        this.setCursor(javafx.scene.Cursor.HAND);
        setupHoverEffects();
    }

     //Setup hover effects (scale).
    protected void setupHoverEffects() {
        this.setOnMouseEntered(this::onHoverEnter);
        this.setOnMouseExited(this::onHoverExit);
        this.setOnMouseClicked(e -> {
            if (onAction != null) {
                onAction.run();
            }
        });
    }

    /**
     * Called when mouse enters button.
     */
    protected void onHoverEnter(MouseEvent e) {
        this.setScaleX(hoverScale);
        this.setScaleY(hoverScale);
    }

    /**
     * Called when mouse exits button.
     */
    protected void onHoverExit(MouseEvent e) {
        this.setScaleX(1.0);
        this.setScaleY(1.0);
    }

    /**
     * Set the action to perform on click.
     */
    public void setOnAction(Runnable action) {
        this.onAction = action;
    }

    /**
     * Set the hover scale factor.
     */
    public void setHoverScale(double scale) {
        this.hoverScale = scale;
    }
}
