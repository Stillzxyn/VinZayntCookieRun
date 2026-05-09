package graphics.rendering;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Handles seamless scrolling/tiling of an image on canvas.
 * Manages offset, scaling, and rendering.
 */
public class ScrollingLayer {

    private Image image;
    private double offset = 0;
    private double speed;
    private double yPos;
    private double height;
    private double canvasWidth;

    public ScrollingLayer(double canvasWidth, double yPos, double height, double speed) {
        this.canvasWidth = canvasWidth;
        this.yPos = yPos;
        this.height = height;
        this.speed = speed;
    }

    public void setImage(Image img) {
        this.image = img;
    }

    public void update(double delta, double extraSpeed) {
        if (image == null) return;

        offset += delta * (speed + extraSpeed);

        // Wrap offset at image width
        if (offset >= image.getWidth()) {
            offset -= image.getWidth();
        }
    }

    public void render(GraphicsContext gc, Color fallbackColor) {
        if (image == null || image.isError()) {
            if (fallbackColor != null) {
                gc.setFill(fallbackColor);
                gc.fillRect(0, yPos, canvasWidth, height);
            }
            return;
        }

        // Calculate scale ratio based on available height
        double scaleRatio = height / image.getHeight();
        double scaledWidth = image.getWidth() * scaleRatio;

        // Draw first copy
        gc.drawImage(image, -offset, yPos, scaledWidth, height);

        // Draw second copy for seamless looping
        gc.drawImage(image, scaledWidth - offset, yPos, scaledWidth, height);
    }

    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public Image getImage() {
        return image;
    }

    public boolean isLoaded() {
        return image != null && !image.isError();
    }
}
