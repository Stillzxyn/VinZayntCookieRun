package gamelogic.events;

/**
 * Event fired when combo multiplier changes.
 */
public class ComboEvent implements GameEvent {
    private final int comboCount;
    private final double multiplier;
    private final long timestamp;

    public ComboEvent(int comboCount, double multiplier) {
        this.comboCount = comboCount;
        this.multiplier = multiplier;
        this.timestamp = System.currentTimeMillis();
    }

    @Override
    public String getEventType() {
        return "COMBO_CHANGED";
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public int getComboCount() {
        return comboCount;
    }

    public double getMultiplier() {
        return multiplier;
    }
}
