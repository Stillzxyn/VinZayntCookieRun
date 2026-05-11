package core.state;

import core.entities.base.State;

/**
 * CookieStateManager - Manages cookie state transitions.
 */
public class CookieStateManager {
    private State currentState = State.RUNNING;
    private long stateStartTime = 0;

    public void setState(State newState) {
        if (currentState != newState) {
            currentState = newState;
            stateStartTime = System.currentTimeMillis();
        }
    }

    public State getCurrentState() {
        return currentState;
    }

    public long getStateElapsedTime() {
        return System.currentTimeMillis() - stateStartTime;
    }

    public boolean isInState(State state) {
        return currentState == state;
    }

    public void reset() {
        currentState = State.RUNNING;
        stateStartTime = System.currentTimeMillis();
    }
}
