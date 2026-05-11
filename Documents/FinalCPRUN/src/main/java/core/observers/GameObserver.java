package core.observers;

/**
 * GameObserver interface - Implement to observe game events.
 */
public interface GameObserver {
    void onScoreChanged(int newScore, int coins);
    void onHealthChanged(int newHealth);
    void onGameOver(int finalScore, int finalCoins);
    void onComboChanged(int comboCount, double multiplier);
}
