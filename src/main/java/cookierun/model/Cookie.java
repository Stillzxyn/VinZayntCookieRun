package cookierun.model;

import cookierun.interfaces.Animatable;
import cookierun.util.SpriteSheet;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Cookie extends GameObject implements Animatable {

    public enum State {
        RUNNING,
        JUMPING,
        SLIDING,
        DEAD
    }

    // =========================
    // PHYSICS
    // =========================

    private static final double GRAVITY = 1800.0;
    private static final double JUMP_VELOCITY = -680.0;

    public static final double GROUND_Y = 290.0;

    private static final double NORMAL_H = 70.0;
    private static final double SLIDE_H = 40.0;

    // =========================
    // HP SYSTEM
    // =========================

    private double hp = 100;
    private double maxHp = 100;

    private double healEffectTimer = 0;

    // =========================
    // STATE
    // =========================

    private State state = State.RUNNING;

    // =========================
    // SPRITE
    // =========================

    private SpriteSheet spriteSheet;

    private final int cookieIndex;

    // =========================
    // ANIMATION
    // =========================

    private int currentFrame = 0;

    private int startFrame = 0;

    private int totalFrames = 8;

    private double frameTimer = 0;

    private static final double FRAME_DUR = 0.08;

    // =========================
    // SPRITE PATHS
    // =========================

    private static final String[] SPRITE_PATHS = {
            "/sprites/cookie_ginger.png",
            "/sprites/cookie_strawberry.png",
            "/sprites/cookie_chili.png",
            "/sprites/cookie_banana.png",
            "/sprites/cookie_choco.png",
            "/sprites/cookie_wizard.png",
            "/sprites/BraveGingerBreadCookie_SpriteSheet.png"
    };

    private static final int[] SPRITE_COLS = {
            8, 10, 9, 8, 10, 9, 4
    };

    // =========================
    // FALLBACK COLORS
    // =========================

    private static final Color[] PLACEHOLDER_COLORS = {
            Color.web("#C8822A"),
            Color.web("#E84060"),
            Color.web("#CC3300"),
            Color.web("#CCAA00"),
            Color.web("#4A2200"),
            Color.web("#6633AA"),
            Color.web("#C8822A")
    };

    // =========================
    // CONSTRUCTOR
    // =========================

    public Cookie(int cookieIndex) {

        super(
                80,
                GROUND_Y - NORMAL_H,
                70,
                NORMAL_H
        );

        this.cookieIndex = cookieIndex;

        loadSprite();

        setAnimation("RUN");
    }

    // =========================
    // LOAD SPRITE
    // =========================

    private void loadSprite() {

        if (cookieIndex < 0 ||
                cookieIndex >= SPRITE_PATHS.length)
            return;

        try {

            String path = SPRITE_PATHS[cookieIndex];

            int cols = SPRITE_COLS[cookieIndex];

            Image img = new Image(
                    getClass().getResourceAsStream(path)
            );

            if (img.isError())
                return;

            int fw = (int)(img.getWidth() / cols);

            // rows = total height divided by frame height.
            // For single-row sheets (e.g. BraveGingerBread) fh == full image height.
            int rows = Math.max(1, (int) Math.round(img.getHeight() / (double) fw));
            int fh   = Math.max(1, (int)(img.getHeight() / rows));

            spriteSheet = new SpriteSheet(
                    path,
                    fw,
                    fh,
                    cols
            );

        }
        catch (Exception e) {

            spriteSheet = null;
        }
    }

    // =========================
    // PLAYER ACTIONS
    // =========================

    public void jump() {

        if (state == State.RUNNING) {

            state = State.JUMPING;

            velocityY = JUMP_VELOCITY;

            height = NORMAL_H;

            y = GROUND_Y - NORMAL_H;

            setAnimation("JUMP");
        }
    }

    public void slideDown() {

        if (state == State.RUNNING) {

            state = State.SLIDING;

            height = SLIDE_H;

            y = GROUND_Y - SLIDE_H;

            setAnimation("SLIDE");
        }
    }

    public void releaseSlide() {

        if (state == State.SLIDING) {

            state = State.RUNNING;

            height = NORMAL_H;

            y = GROUND_Y - NORMAL_H;

            setAnimation("RUN");
        }
    }

    public void die() {

        state = State.DEAD;

        setAnimation("DEAD");

        alive = false;
    }

    // =========================
    // HP METHODS
    // =========================

    public double getHp() {
        return hp;
    }

    public double getMaxHp() {
        return maxHp;
    }

    public void decreaseHp(double amount) {

        hp -= amount;

        if (hp < 0) {
            hp = 0;
        }
    }

    public void heal(double amount) {

        hp += amount;

        if (hp > maxHp) {
            hp = maxHp;
        }

        healEffectTimer = 0.4;
    }

    // =========================
    // UPDATE
    // =========================

    @Override
    public void update(double delta) {

        if (state == State.DEAD)
            return;

        // HP drain

        decreaseHp(delta * 5);

        if (hp <= 0) {

            die();

            return;
        }

        // Heal effect

        if (healEffectTimer > 0) {

            healEffectTimer -= delta;
        }

        // Jump physics

        if (state == State.JUMPING) {

            velocityY += GRAVITY * delta;

            y += velocityY * delta;

            if (y >= GROUND_Y - NORMAL_H) {

                y = GROUND_Y - NORMAL_H;

                velocityY = 0;

                height = NORMAL_H;

                state = State.RUNNING;

                setAnimation("RUN");
            }
        }

        // Animation timer

        frameTimer += delta;

        if (frameTimer >= FRAME_DUR) {

            frameTimer = 0;

            nextFrame();
        }
    }

    // =========================
    // RENDER
    // =========================

    @Override
    public void render(GraphicsContext gc) {

        // Low HP flashing

        if (hp < 30) {

            gc.setGlobalAlpha(
                    0.7 + 0.3 *
                            Math.sin(frameTimer * 20)
            );
        }

        // Heal effect

        if (healEffectTimer > 0) {

            gc.setStroke(Color.LIME);

            gc.strokeOval(
                    x - 5,
                    y - 5,
                    width + 10,
                    height + 10
            );
        }

        // Sprite render

        if (spriteSheet != null) {

            Image frame =
                    spriteSheet.getFrame(
                            startFrame + currentFrame
                    );

            if (frame != null) {

                gc.drawImage(
                        frame,
                        x,
                        y,
                        width,
                        height
                );

                gc.setGlobalAlpha(1.0);

                return;
            }
        }

        // Fallback render

        drawPlaceholder(gc);

        gc.setGlobalAlpha(1.0);
    }

    // =========================
    // PLACEHOLDER
    // =========================

    private void drawPlaceholder(GraphicsContext gc) {

        Color c =
                (cookieIndex >= 0 &&
                        cookieIndex < PLACEHOLDER_COLORS.length)

                        ? PLACEHOLDER_COLORS[cookieIndex]

                        : Color.web("#C8822A");

        gc.setFill(c);

        gc.fillRoundRect(
                x,
                y,
                width,
                height,
                20,
                20
        );

        gc.setFill(c.darker());

        gc.fillOval(
                x + width / 2 - 10,
                y + 4,
                20,
                20
        );

        gc.setFill(Color.WHITE);

        gc.fillOval(
                x + width / 2 - 6,
                y + 8,
                5,
                5
        );

        gc.fillOval(
                x + width / 2 + 1,
                y + 8,
                5,
                5
        );
    }

    // =========================
    // ANIMATION
    // =========================

    @Override
    public void nextFrame() {

        currentFrame =
                (currentFrame + 1)
                        % totalFrames;
    }

    @Override
    public void setAnimation(String name) {

        currentFrame = 0;

        frameTimer = 0;

        switch (name) {

            case "RUN" -> {

                startFrame = 0;

                totalFrames = 8;
            }

            case "JUMP" -> {

                startFrame = 8;

                totalFrames = 6;
            }

            case "SLIDE" -> {

                startFrame = 14;

                totalFrames = 4;
            }

            case "DEAD" -> {

                startFrame = 18;

                totalFrames = 2;
            }

            default -> {

                startFrame = 0;

                totalFrames = 8;
            }
        }

        // Clamp sprite range

        if (spriteSheet != null) {

            int maxFrame =
                    spriteSheet.getTotalFrames();

            if (startFrame >= maxFrame)
                startFrame = 0;

            if (startFrame + totalFrames > maxFrame) {

                totalFrames =
                        Math.max(
                                1,
                                maxFrame - startFrame
                        );
            }
        }
    }

    @Override
    public int getCurrentFrame() {
        return currentFrame;
    }

    // =========================
    // GETTERS
    // =========================

    public State getState() {
        return state;
    }

    public int getCookieIndex() {
        return cookieIndex;
    }
}