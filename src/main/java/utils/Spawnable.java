package utils;

/**
 * Interface for objects that are spawned into the game world
 * and scroll off-screen (obstacles and collectibles).
 */
public interface Spawnable {
    boolean isAlive();
    void    setAlive(boolean alive);
    double  getX();
}
