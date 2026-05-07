package entity.base;

import interfaces.Animatable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Abstract base class for all cookie characters.
 * Handles animation, HP, and rendering.
 * Physics is delegated to the Physics class.
 */
public abstract class Cookie extends GameObject implements Animatable {
    // =========================
    // PHYSICS DELEGATION

    private final Physics physics;

    // =========================
    // HP SYSTEM
    // =========================

    private double hp    = 100;
    private double maxHp = 100;
    private double healEffectTimer = 0;

    // =========================
    // STATE
    // =========================

    private State state = State.RUNNING;

    // ANIMATION
    private Image[] runFrames   = new Image[4];
    private Image[] jumpFrames  = new Image[4];
    private Image[] slideFrames = new Image[4];
    private Image[] deadFrames  = new Image[4];

    private Image[] currentAnimationFrames = runFrames;
    private int currentFrame = 0;
    private double frameTimer = 0;
    private static final double FRAME_DUR = 0.08;

    private final Color placeholderColor;
    private final String cookieName;


    // COOKIE METADATA
    protected String displayName = "Cookie";
    protected String tier = "C";
    protected int price = 0;
    protected boolean unlocked = true;
    protected int maxHpValue = 100;

    protected Cookie(String cookieName, Color placeholderColor) {
        super(80, Physics.GROUND_Y - Physics.NORMAL_H, 70, Physics.NORMAL_H);
        this.cookieName = cookieName;
        this.placeholderColor = placeholderColor;
        this.physics = new Physics(80, Physics.GROUND_Y - Physics.NORMAL_H, 70, Physics.NORMAL_H);
        setMaxHp(maxHpValue);
        setHp(maxHp);
        loadAnimationFrames();
        setAnimation("RUN");
    }

    // LOAD ANIMATION FRAMES
    private void loadAnimationFrames() {
        loadFrameSet(runFrames, "RUN");
        loadFrameSet(jumpFrames, "JUMP");
        loadFrameSet(slideFrames, "SLIDE");
        loadFrameSet(deadFrames, "DEAD");
    }

    private void loadFrameSet(Image[] frames, String state) {
        for (int i = 0; i < 4; i++) {
            String path = cookieName + "/" + cookieName + (i + 1) + ".png";
            try {
                var stream = getClass().getResourceAsStream("/" + path);
                if (stream == null) {
                    stream = getClass().getResourceAsStream(path);
                }

                if (stream != null) {
                    Image img = new Image(stream);
                    if (img != null && !img.isError()) {
                        frames[i] = img;
                    }
                }
            } catch (Exception e) {
                // Image not found, will use placeholder
            }
        }
    }

    // PLAYER ACTIONS
    public void jump() {
        if (state == State.RUNNING && physics.isOnGround()) {
            state = State.JUMPING;
            physics.jump();
            physics.setHeight(Physics.NORMAL_H);
            setAnimation("JUMP");
        }
    }

    public void slideDown() {
        if (state == State.RUNNING) {
            state = State.SLIDING;
            physics.setHeight(Physics.SLIDE_H);
            physics.setY(Physics.GROUND_Y - Physics.SLIDE_H);
            setAnimation("SLIDE");
        }
    }

    public void releaseSlide() {
        if (state == State.SLIDING) {
            state = State.RUNNING;
            physics.setHeight(Physics.NORMAL_H);
            physics.setY(Physics.GROUND_Y - Physics.NORMAL_H);
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

    public double getHp()    { return hp; }
    public double getMaxHp() { return maxHp; }

    public void decreaseHp(double amount) {
        hp = Math.max(0, hp - amount);
    }

    public void heal(double amount) {
        hp = Math.min(maxHp, hp + amount);
        healEffectTimer = 0.4;
    }

    // UPDATE
    @Override
    public void update(double delta) {
        if (state == State.DEAD) return;
        // HP drain
        decreaseHp(delta * 5);
        if (hp <= 0) { die(); return; }
        // Heal effect timer
        if (healEffectTimer > 0) healEffectTimer -= delta;
        // Jump physics
        if (state == State.JUMPING) {
            physics.update(delta);
            if (physics.isOnGround()) {
                state = State.RUNNING;
                physics.setHeight(Physics.NORMAL_H);
                setAnimation("RUN");
            }
        }

        // Sync physics state back to GameObject for collision detection
        this.x = physics.getX();
        this.y = physics.getY();
        this.width = physics.getWidth();
        this.height = physics.getHeight();

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
            gc.setGlobalAlpha(0.7 + 0.3 * Math.sin(frameTimer * 20));
        }
        // Heal glow
        if (healEffectTimer > 0) {
            gc.setStroke(Color.LIME);
            gc.strokeOval(x - 5, y - 5, width + 10, height + 10);
        }
        // Render current frame
        Image frame = currentAnimationFrames[currentFrame];
        if (frame != null && !frame.isError()) {
            gc.drawImage(frame, x, y, width, height);
            gc.setGlobalAlpha(1.0);
            return;
        }

        // Fallback
        drawPlaceholder(gc);
        gc.setGlobalAlpha(1.0);
    }

    // =========================
    // PLACEHOLDER
    // =========================

    private void drawPlaceholder(GraphicsContext gc) {

        gc.setFill(placeholderColor);
        gc.fillRoundRect(x, y, width, height, 20, 20);

        gc.setFill(placeholderColor.darker());
        gc.fillOval(x + width / 2 - 10, y + 4, 20, 20);

        gc.setFill(Color.WHITE);
        gc.fillOval(x + width / 2 - 6, y + 8, 5, 5);
        gc.fillOval(x + width / 2 + 1, y + 8, 5, 5);
    }

    // =========================
    // ANIMATION
    // =========================

    @Override
    public void nextFrame() {
        currentFrame = (currentFrame + 1) % 4;
    }

    @Override
    public void setAnimation(String name) {

        currentFrame = 0;
        frameTimer   = 0;

        switch (name.toUpperCase()) {
            case "RUN"   -> currentAnimationFrames = runFrames;
            case "JUMP"  -> currentAnimationFrames = jumpFrames;
            case "SLIDE" -> currentAnimationFrames = slideFrames;
            case "DEAD"  -> currentAnimationFrames = deadFrames;
            default      -> currentAnimationFrames = runFrames;
        }
    }

    @Override
    public int getCurrentFrame() { return currentFrame; }

    // =========================
    // GETTERS
    // =========================

    public State getState() {
        return state;
    }

    public Physics getPhysics() {
        return physics;
    }

    // Cookie Metadata Getters
    public String getDisplayName() {return displayName;}
    public String getTier() {return tier;}
    public int getPrice() {return price;}
    public boolean isUnlocked() {return unlocked;}
    public int getMaxHpValue() {return maxHpValue;}
    public String getCookieName() {return cookieName;}

    // =========================
    // METADATA SETTERS
    // =========================

    public void setDisplayName(String displayName) {this.displayName = displayName;}
    public void setTier(String tier) {this.tier = tier;}
    public void setPrice(int price) {
        if (price < 0) {
            this.price = 0;
        } else {
            this.price = price;
        }
    }
    public void setUnlocked(boolean unlocked) {this.unlocked = unlocked;}
    public void setMaxHpValue(int maxHpValue) {
        if (maxHpValue < 0) {
            this.maxHpValue = 0;
        } else {
            this.maxHpValue = maxHpValue;
            this.maxHp = maxHpValue;
        }
    }

    // HP SETTERS
    public void setHp(double hp) {
        if (hp < 0) {
            this.hp = 0;
        } else {
            this.hp = hp;
        }
    }
    public void setMaxHp(int maxHp) {
        if (maxHp < 0) {
            this.maxHpValue = 0;
        } else {
            this.maxHpValue = maxHp;
        }
    }

    // PHYSICS DELEGATION
    public double getX() {
        return physics.getX();
    }
    public double getY() {
        return physics.getY();
    }
    public void setX(double x) {
        physics.setX(x);
    }
    public void setY(double y) {
        physics.setY(y);
    }
}
