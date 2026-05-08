package utils;

/**
 * Interface for objects that have sprite-based frame animation.
 * Implemented by Cookie (and any future animated characters).
 */
public interface Animatable {
    void nextFrame();
    void setAnimation(String animationName);
    int  getCurrentFrame();
}
