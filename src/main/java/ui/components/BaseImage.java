package ui.components;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Base image class with error handling and fallback support.
 * Provides reusable image loading with canvas fallback.
 */
public class BaseImage extends StackPane {

    protected ImageView imageView;
    protected Canvas fallbackCanvas;
    protected Image image;

    public BaseImage(String resourcePath, double width, double height) {
        this.setPrefSize(width, height);
        loadImage(resourcePath, width, height);
    }

    protected void loadImage(String resourcePath, double width, double height) {
        try {
            // Try loading the image
            var stream = getClass().getResourceAsStream(resourcePath);
            if (stream == null) {
                // Try without leading slash
                String adjustedPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
                stream = getClass().getResourceAsStream(adjustedPath);
            }

            if (stream != null) {
                image = new Image(stream);
                if (image != null && !image.isError()) {
                    imageView = new ImageView(image);
                    imageView.setFitWidth(width);
                    imageView.setFitHeight(height);
                    imageView.setPreserveRatio(false);
                    this.getChildren().add(imageView);
                    return;
                }
            }

            // Fallback to canvas if image fails
            createFallback(width, height);
        } catch (Exception e) {
            System.err.println("Failed to load image: " + resourcePath + " - " + e.getMessage());
            createFallback(width, height);
        }
    }

    protected void createFallback(double width, double height) {
        fallbackCanvas = new Canvas(width, height);
        GraphicsContext gc = fallbackCanvas.getGraphicsContext2D();

        // Draw placeholder rectangle
        gc.setFill(Color.web("#CCCCCC"));
        gc.fillRoundRect(0, 0, width, height, 8, 8);

        // Draw error text
        gc.setFill(Color.web("#666666"));
        gc.setFont(javafx.scene.text.Font.font(12));
        gc.fillText("Image", width / 2 - 20, height / 2);

        this.getChildren().add(fallbackCanvas);
    }

    public ImageView getImageView() {
        return imageView;
    }
    public Image getImage() {
        return image;
    }
    public boolean isLoaded() {
        return imageView != null && image != null && !image.isError();
    }
}
