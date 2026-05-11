package core.entities.base;

import utils.Animatable;
import core.abilities.CookieAbility;
import core.abilities.implementations.JumpBoostAbility;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 * Base class for all playable cookies.
 * Responsibilities:
 * - Handle animation
 * - Handle HP system
 * - Handle movement and physics
 * - Support abilities
 * - Render cookie sprites/effects
 */
public abstract class Cookie
        extends GameObject
        implements Animatable {
    // PHYSICS DELEGATION
    private final Physics physics;

    // HP SYSTEM
    private double hp    = 100;
    private double maxHp = 100;
    private double healEffectTimer = 0;

    // STATE
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
    protected String hex = "#CCCCCC";
    protected String iconPath = "";
    protected int price = 0;
    protected boolean unlocked = true;
    protected int maxHpValue = 100;
    protected String cookieAbilityDesc = "";

    // SPECIAL ABILITY
    protected CookieAbility ability = null;
    private boolean isGhost = false;

    // HIT INVINCIBILITY
    private double invincibilityTimer = 0;
    private static final double INVINCIBILITY_DURATION = 0.5;

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

    /**
     * Load sprite frames for a specific animation state.
     */
    private void loadFrameSet(
            Image[] frames,
            String state
    ) {
        for (int i = 0; i < 4; i++) {
            String resourcePath =  "/CookieSprite/" + cookieName+"/"+cookieName + (i + 1) + ".png";
            // OR if cookieName includes "CookieSprite/" already, use this:
            // String resourcePath = "/" + cookieName + "/" + cookieName + (i + 1) + ".png";
            try {
                var stream = getClass().getResourceAsStream(resourcePath);
                if (stream == null) {
                    String adjustedPath = resourcePath.substring(1);
                    stream = getClass().getResourceAsStream(adjustedPath);
                }
                if (stream != null) {
                    Image img = new Image(stream);
                    if (img != null && !img.isError()) {
                        frames[i] = img;
                    } else {
                        System.err.println("Image error: " + resourcePath);
                    }
                } else {
                    System.err.println("Stream null: " + resourcePath);
                }
            } catch (Exception e) {
                System.err.println("Load failed: " + resourcePath + " - " + e.getMessage());
            }
        }
    }

    // PLAYER ACTIONS
    /**
     * Player jump action.
     * If the cookie has JumpBoostAbility, jump velocity is enhanced.
     */
    public void jump() {
        if (state == State.RUNNING && physics.isOnGround()) {
            state = State.JUMPING;
            physics.jump();

            // Apply speed boost if available
            if (ability instanceof JumpBoostAbility speedBoost) {
                double multiplier = speedBoost.getJumpVelocityMultiplier();
                physics.setVelocityY(Physics.JUMP_VELOCITY * multiplier);
            }

            physics.setHeight(Physics.NORMAL_H);
            setAnimation("JUMP");
        }
    }

    /**
     * Start slide state.
     * Reduces the hitbox height to avoid high obstacles.
     */
    public void slideDown() {
        if (state == State.RUNNING) {
            state = State.SLIDING;
            physics.setHeight(Physics.SLIDE_H);
            physics.setY(Physics.GROUND_Y - Physics.SLIDE_H);
            setAnimation("SLIDE");
        }
    }

    /**
     * Return from slide state back to running.
     * Restores the normal hitbox height.
     */
    public void releaseSlide() {
        if (state == State.SLIDING) {
            state = State.RUNNING;
            physics.setHeight(Physics.NORMAL_H);
            physics.setY(Physics.GROUND_Y - Physics.NORMAL_H);
            setAnimation("RUN");
        }
    }

    /**
     * Kill the cookie and switch to DEAD animation.
     */
    public void die() {
        state = State.DEAD;
        setAnimation("DEAD");
        alive = false;
    }


    // HP METHODS
    public double getHp()    { return hp; }
    public double getMaxHp() { return maxHp; }
    public void decreaseHp(double amount) {
        hp = Math.max(0, hp - amount);
    }
    /**
     * Heal the cookie and trigger heal effect.
     */
    public void heal(double amount) {
        hp = Math.min(maxHp, hp + amount);
        healEffectTimer = 0.4;
    }

    // UPDATE
    /**
     * Main update loop.
     * Handles:
     * - HP drain
     * - Physics update
     * - Animation update
     * - Invincibility timer
     * - State synchronization
     */
    @Override
    public void update(double delta) {

        if (state == State.DEAD) return;

        // HP drain
        decreaseHp(delta * 5);

        if (hp <= 0) {
            die();
            return;
        }

        // Heal effect timer
        if (healEffectTimer > 0) {
            healEffectTimer -= delta;
        }

        // Invincibility timer
        if (invincibilityTimer > 0) {
            invincibilityTimer -= delta;
        }
        // Jump physics
        if (state == State.JUMPING) {

            physics.update(delta);

            if (physics.isOnGround()) {

                state = State.RUNNING;

                physics.setHeight(Physics.NORMAL_H);

                setAnimation("RUN");
            }
        }

        // Sync physics state back to GameObject
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


    // RENDER
    /**
     * Render cookie sprite and visual effects.
     * Effects:
     * - Ghost transparency
     * - Low HP flashing
     * - Heal glow
     */
    @Override
    public void render(GraphicsContext gc) {
        // Ghost mode transparency
        if (isGhost) {
            gc.setGlobalAlpha(0.4);
        }
        // Low HP flashing
        if (hp < 30) {
            double alpha = 0.7 + 0.3 * Math.sin(System.currentTimeMillis() / 50.0);
            gc.setGlobalAlpha(alpha);
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

    /**
     * Draw placeholder sprite
     * if image loading fails.
     */
    private void drawPlaceholder(
            GraphicsContext gc
    ) {
        gc.setFill(placeholderColor);
        gc.fillRoundRect(x, y, width, height, 20, 20);
        gc.setFill(placeholderColor.darker());
        gc.fillOval(x + width / 2 - 10, y + 4, 20, 20);
        gc.setFill(Color.WHITE);
        gc.fillOval(x + width / 2 - 6, y + 8, 5, 5);
        gc.fillOval(x + width / 2 + 1, y + 8, 5, 5);
    }

    // ANIMATION
    /**
     * Advance animation frame.
     */
    @Override
    public void nextFrame() {
        currentFrame = (currentFrame + 1) % 4;
    }

    /**
     * Change current animation state.
     */
    @Override
    public void setAnimation(String name) {
        currentFrame = 0;
        frameTimer   = 0;
        currentAnimationFrames = switch (name.toUpperCase()) {
            case "RUN"   -> runFrames;
            case "JUMP"  -> jumpFrames;
            case "SLIDE" -> slideFrames;
            case "DEAD"  -> deadFrames;
            default      -> runFrames;
        };
    }

    @Override
    public int getCurrentFrame() { return currentFrame; }

    // GETTERS
    public State getState() {return state;}
    public Physics getPhysics() {return physics;}

    // Cookie Metadata Getters
    public String getDisplayName() {return displayName;}
    public String getTier() {return tier;}
    public String getHex() {return hex;}
    public String getIconPath() {return iconPath;}
    public int getPrice() {return price;}
    public boolean isUnlocked() {return unlocked;}
    public int getMaxHpValue() {return maxHpValue;}
    public String getCookieName() {return cookieName;}

    // =========================
    // METADATA SETTERS
    // =========================

    public void setDisplayName(String displayName) {this.displayName = displayName;}
    public void setTier(String tier) {this.tier = tier;}
    public void setHex(String hex) {this.hex = hex;}
    public void setIconPath(String iconPath) {this.iconPath = iconPath;}
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
            this.maxHp = 0;
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
    // ABILITY
    public CookieAbility getAbility() {
        return ability;
    }
    public void setAbility(CookieAbility ability) {
        this.ability = ability;
    }

    // GHOST MODE
    public boolean isGhost() {
        return isGhost;
    }
    /**
     * Enable or disable ghost mode.
     */
    public void setGhost(boolean ghost) {
        this.isGhost = ghost;
    }

    // INVINCIBILITY
    public boolean isInvincible() {
        return invincibilityTimer > 0;
    }
    /**
     * Enable temporary invincibility after hit.
     */
    public void setInvincible(boolean value) {

        if (value) {
            invincibilityTimer = INVINCIBILITY_DURATION;
        }
        else {
            invincibilityTimer = 0;
        }
    }

    // RESET FOR NEW GAME
    /**
     * Reset cookie to initial state for a new game.
     */
    public void reset() {

        hp = maxHp;

        state = State.RUNNING;

        healEffectTimer = 0;

        isGhost = false;

        invincibilityTimer = 0;

        physics.resetToStart();

        setAnimation("RUN");
    }
}
