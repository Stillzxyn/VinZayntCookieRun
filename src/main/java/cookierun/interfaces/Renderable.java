package cookierun.interfaces;

import javafx.scene.canvas.GraphicsContext;

/**
 * Interface for any object that can be drawn on a JavaFX Canvas.
 */
public interface Renderable {
    void render(GraphicsContext gc);
}
