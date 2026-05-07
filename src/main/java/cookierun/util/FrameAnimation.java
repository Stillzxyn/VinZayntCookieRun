package cookierun.util;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads animation frames from individual PNG files in a resource folder.
 *
 * Files must be numbered starting at 1:
 *   /sprites/BraveGinger/1.png
 *   /sprites/BraveGinger/2.png
 *   /sprites/BraveGinger/3.png
 *   ...
 *
 * Usage:
 *   FrameAnimation anim = new FrameAnimation("/sprites/BraveGinger/", 4);
 *   Image frame = anim.getFrame(0);   // first frame
 *   int total   = anim.size();
 */
public class FrameAnimation {

    private final List<Image> frames = new ArrayList<>();

    /**
     * Load {@code count} frames from {@code folder}.
     * Files are expected to be named 1.png, 2.png, … count.png.
     *
     * @param folder resource folder path, must end with "/"
     * @param count  number of frames to load
     */
    public FrameAnimation(String folder, int count) {
        for (int i = 1; i <= count; i++) {
            String path = folder + i + ".png";
            try {
                InputStream stream = FrameAnimation.class.getResourceAsStream(path);
                if (stream == null) {
                    System.err.println("FrameAnimation: missing frame — " + path);
                    continue;
                }
                Image img = new Image(stream);
                if (!img.isError()) {
                    frames.add(img);
                } else {
                    System.err.println("FrameAnimation: error loading — " + path);
                }
            } catch (Exception e) {
                System.err.println("FrameAnimation: exception for " + path + " — " + e.getMessage());
            }
        }
    }

    /**
     * Get a frame by index. Wraps around automatically if index >= size().
     * Returns null if no frames were loaded.
     */
    public Image getFrame(int index) {
        if (frames.isEmpty()) return null;
        return frames.get(index % frames.size());
    }

    /** Number of successfully loaded frames. */
    public int size() {
        return frames.size();
    }

    public boolean isEmpty() {
        return frames.isEmpty();
    }
}
