package utils;

/**
 * Interface for any object that updates its state each game frame.
 */
public interface Updatable {
    /**
     * @param delta time elapsed since last frame in seconds
     */
    void update(double delta);
}
