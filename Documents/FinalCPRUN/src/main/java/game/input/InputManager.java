package game.input;

import javafx.scene.input.KeyCode;
import java.util.*;

/**
 * InputManager - Centralized input handling.
 */
public class InputManager {
    private static InputManager instance;
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private List<InputListener> listeners = new ArrayList<>();

    private InputManager() {}

    public static synchronized InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    public void keyPressed(KeyCode key) {
        pressedKeys.add(key);
        notifyListeners(InputEvent.KEY_PRESSED, key);
    }

    public void keyReleased(KeyCode key) {
        pressedKeys.remove(key);
        notifyListeners(InputEvent.KEY_RELEASED, key);
    }

    public boolean isKeyPressed(KeyCode key) {
        return pressedKeys.contains(key);
    }

    public void addInputListener(InputListener listener) {
        listeners.add(listener);
    }

    public void removeInputListener(InputListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(String eventType, KeyCode key) {
        for (InputListener listener : listeners) {
            listener.onInput(eventType, key);
        }
    }

    public void reset() {
        pressedKeys.clear();
    }
}

interface InputListener {
    void onInput(String eventType, KeyCode key);
}

class InputEvent {
    public static final String KEY_PRESSED = "KEY_PRESSED";
    public static final String KEY_RELEASED = "KEY_RELEASED";
}
