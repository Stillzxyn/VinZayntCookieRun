package core.observers;

import java.util.*;

/**
 * ObserverManager - Manages game observers.
 */
public class ObserverManager {
    private static ObserverManager instance;
    private List<GameObserver> observers = new ArrayList<>();

    private ObserverManager() {}

    public static synchronized ObserverManager getInstance() {
        if (instance == null) {
            instance = new ObserverManager();
        }
        return instance;
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyScoreChanged(int score, int coins) {
        for (GameObserver observer : observers) {
            observer.onScoreChanged(score, coins);
        }
    }

    public void notifyHealthChanged(int health) {
        for (GameObserver observer : observers) {
            observer.onHealthChanged(health);
        }
    }

    public void notifyGameOver(int score, int coins) {
        for (GameObserver observer : observers) {
            observer.onGameOver(score, coins);
        }
    }

    public void notifyComboChanged(int combo, double multiplier) {
        for (GameObserver observer : observers) {
            observer.onComboChanged(combo, multiplier);
        }
    }

    public void clearAll() {
        observers.clear();
    }
}
