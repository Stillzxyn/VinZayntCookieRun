package cookierun.interfaces;

import javafx.geometry.Rectangle2D;

/**
 * Interface for objects that participate in collision detection.
 */
public interface Collidable {
    Rectangle2D getBounds();
    boolean intersects(Collidable other);
}
