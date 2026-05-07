package cookierun.util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads a sprite sheet PNG and slices it into individual frames.
 * Frames are indexed left-to-right, top-to-bottom.
 */
public class SpriteSheet {

    private final List<Image> frames = new ArrayList<>();
    private final int frameWidth;
    private final int frameHeight;

    public SpriteSheet(String resourcePath, int frameWidth, int frameHeight, int columns) {
        this.frameWidth  = frameWidth;
        this.frameHeight = frameHeight;

        try {
            var stream = SpriteSheet.class.getResourceAsStream(resourcePath);
            if (stream == null) return;
            Image src = new Image(stream);
            if (src.isError()) return;

            PixelReader pr = src.getPixelReader();
            int imgW = (int) src.getWidth();
            int imgH = (int) src.getHeight();
            int rows = (frameHeight > 0) ? imgH / frameHeight : 1;

            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < columns; col++) {
                    int sx = col * frameWidth;
                    int sy = row * frameHeight;
                    if (sx + frameWidth > imgW || sy + frameHeight > imgH) continue;
                    frames.add(new WritableImage(pr, sx, sy, frameWidth, frameHeight));
                }
            }
        } catch (Exception e) {
            System.err.println("SpriteSheet load failed: " + resourcePath + " — " + e.getMessage());
        }
    }

    public Image getFrame(int index) {
        if (frames.isEmpty() || index < 0 || index >= frames.size()) return null;
        return frames.get(index);
    }

    public int getTotalFrames() { return frames.size(); }
    public int getFrameWidth()  { return frameWidth; }
    public int getFrameHeight() { return frameHeight; }
}
