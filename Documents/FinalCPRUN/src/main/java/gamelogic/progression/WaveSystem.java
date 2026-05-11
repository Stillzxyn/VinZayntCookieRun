package gamelogic.progression;

/**
 * WaveSystem - Organizes enemies into waves with increasing difficulty.
 */
public class WaveSystem {
    private int currentWave = 1;
    private int enemiesInWave = 5;
    private int enemiesDefeated = 0;
    private static final int ENEMY_INCREASE_PER_WAVE = 2;

    public void startWave() {
        currentWave++;
        enemiesInWave = 5 + ((currentWave - 1) * ENEMY_INCREASE_PER_WAVE);
        enemiesDefeated = 0;
    }

    public void recordEnemyDefeated() {
        enemiesDefeated++;
        if (enemiesDefeated >= enemiesInWave) {
            startWave();
        }
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getEnemiesInWave() {
        return enemiesInWave;
    }

    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    public double getWaveProgress() {
        return (double) enemiesDefeated / enemiesInWave;
    }

    public void reset() {
        currentWave = 1;
        enemiesInWave = 5;
        enemiesDefeated = 0;
    }
}
