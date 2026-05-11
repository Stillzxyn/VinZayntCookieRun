# 🍪 Cookie Run - Improved Edition

A high-performance JavaFX-based Cookie Run game with advanced game systems, event-driven architecture, and dynamic difficulty progression.

---

## 📋 Project Overview

This is an improved version of the Cookie Run game featuring:
- **Event-driven architecture** for decoupled systems
- **Centralized configuration** system
- **Dynamic scoring** with combo multipliers
- **Progressive difficulty** that scales with gameplay
- **Robust ability management** system
- **Damage calculation** engine
- **Object pooling** for performance optimization

---

## 📁 Project Structure

```
FinalCPRUN/
├── src/main/java/
│   ├── application/
│   │   └── CookieRunApp.java          [Main entry point]
│   │
│   ├── audio/
│   │   └── SoundManager.java          [Audio management - Singleton]
│   │
│   ├── core/
│   │   ├── Physics.java               [Physics constants]
│   │   ├── abilities/                 [Cookie abilities]
│   │   ├── entities/base/             [Game entities]
│   │   ├── stages/                    [Stage configurations]
│   │   ├── state/                     [State machine - NEW]
│   │   ├── pooling/                   [Object pooling - NEW]
│   │   └── observers/                 [Observer pattern - NEW]
│   │
│   ├── game/
│   │   ├── GameController.java        [Main game orchestrator]
│   │   ├── managers/                  [Game system managers]
│   │   ├── collectibles/              [Coin, Jelly items]
│   │   ├── cookies/                   [Cookie implementations]
│   │   ├── obstacles/                 [Obstacle types]
│   │   ├── config/                    [Configuration classes - NEW]
│   │   ├── damage/                    [Damage system - NEW]
│   │   └── input/                     [Input handling - NEW]
│   │
│   ├── gamelogic/                     [NEW - Core game systems]
│   │   ├── events/                    [Event system]
│   │   ├── scoring/                   [Scoring system]
│   │   └── progression/               [Difficulty progression]
│   │
│   ├── gui/
│   │   ├── baseElements/              [Reusable UI components]
│   │   ├── components/                [UI panels and cards]
│   │   ├── graphics/                  [Rendering and effects]
│   │   └── pages/                     [Game screens]
│   │
│   └── utils/
│       └── [Interface contracts]
│
└── README.md (this file)
```

---

## 🆕 New Systems (Phase 1, 2, 3)

### Phase 1 - High Priority ✅

#### 1. **Event System** (`gamelogic/events/`)
Central pub-sub system for decoupling game logic from UI.

```java
// Publish events
EventBus.getInstance().publish(new CoinCollectedEvent(10, x, y));

// Listen to events
EventBus.getInstance().subscribe("COIN_COLLECTED", event -> {
    handleCoinCollection((CoinCollectedEvent) event);
});
```

**Events:**
- `CoinCollectedEvent` - When coin is picked up
- `ObstacleHitEvent` - When obstacle hits player
- `HealthChangedEvent` - When health changes
- `GameOverEvent` - When game ends
- `ComboEvent` - When combo changes

---

#### 2. **Config Classes** (`game/config/`)
Centralized configuration - no magic numbers!

```java
// Use instead of hardcoded values
int damage = ObstacleConfig.GROUND_OBSTACLE_DAMAGE;
double speed = GameConfig.BASE_GAME_SPEED;
int maxHealth = GameConfig.INITIAL_HEALTH;
```

**Config Files:**
- `GameConfig` - Window, audio, debug settings
- `ObstacleConfig` - Obstacle spawning/behavior
- `CollectibleConfig` - Coin/jelly settings
- `PhysicsConfig` - Gravity, jump force, bounds
- `DifficultyConfig` - Stage multipliers, milestones

---

#### 3. **Scoring System** (`gamelogic/scoring/`)
Dynamic score calculation with combos and multipliers.

```java
ScoreCalculator scorer = ScoreCalculator.getInstance();

// Collect coin
int score = scorer.calculateCoinScore(10); // Affected by combo multiplier

// Reset on obstacle hit
scorer.resetCombo();

// Get multiplier
double multiplier = scorer.getMultiplier(); // Up to 5x
```

**Features:**
- Combo tracking (resets on obstacle hit)
- Score multipliers (1.0x to 5.0x)
- Proximity bonuses
- Achievement tracking

---

#### 4. **Difficulty Progression** (`gamelogic/progression/`)
Automatic difficulty scaling throughout gameplay.

```java
DifficultyManager difficulty = DifficultyManager.getInstance();

difficulty.initializeStage(stageNumber);
// Game speed and spawn rates automatically increase
difficulty.update(); // Call every frame

double currentSpeed = difficulty.getCurrentGameSpeed();
double spawnRate = difficulty.getSpawnRateMultiplier();
```

**Features:**
- Stage-specific speed multipliers
- Time-based progressive increase
- Milestone spikes for challenge peaks
- Wave-based enemy spawning
- Difficulty percentage tracking

---

### Phase 2 - Medium Priority ✅

#### 5. **Ability Manager** (`game/managers/AbilityManager.java`)
Manages ability activation and cooldowns.

```java
AbilityManager abilityMgr = new AbilityManager();
abilityMgr.activateAbility(cookieAbility);
abilityMgr.update(delta);

if (!abilityMgr.isOnCooldown("GhostAbility")) {
    abilityMgr.activateAbility(new GhostAbility());
}
```

---

#### 6. **Damage System** (`game/damage/`)
Centralized damage calculation with armor and types.

```java
DamageCalculator dmg = DamageCalculator.getInstance();

int finalDamage = dmg.calculateDamage(
    DamageType.OBSTACLE,  // Damage type
    baseDamage,           // Base amount
    armorReduction        // Armor percentage
);
```

**Damage Types:**
- `OBSTACLE` - Standard obstacle damage
- `COLLISION` - Collision damage (reduced)
- `FALLING` - Fall damage (heavily reduced)
- `ABILITY` - Ability-based damage
- `ENVIRONMENT` - Environmental damage

---

#### 7. **State Machine** (`core/state/CookieStateManager.java`)
Proper state transitions for the cookie.

```java
CookieStateManager stateManager = new CookieStateManager();
stateManager.setState(State.JUMPING);

if (stateManager.isInState(State.SLIDING)) {
    // Handle slide logic
}
```

---

#### 8. **Input Manager** (`game/input/InputManager.java`)
Centralized input handling with listeners.

```java
InputManager input = InputManager.getInstance();
input.keyPressed(KeyCode.SPACE);

if (input.isKeyPressed(KeyCode.SPACE)) {
    cookie.jump();
}
```

---

### Phase 3 - Polish ✅

#### 9. **Object Pooling** (`core/pooling/ObjectPool.java`)
Reuse objects for better GC performance.

```java
ObjectPool<Projectile> pool = new ObjectPool<>(
    () -> new Projectile(),  // Factory
    10,                       // Initial size
    50                        // Max size
);

Projectile p = pool.acquire();
// Use projectile
pool.release(p); // Return to pool
```

---

#### 10. **Observer Pattern** (`core/observers/`)
Game observers for UI updates without coupling.

```java
ObserverManager.getInstance().addObserver(new GameObserver() {
    @Override
    public void onScoreChanged(int score, int coins) {
        updateScoreDisplay(score, coins);
    }
    
    @Override
    public void onHealthChanged(int health) {
        updateHealthBar(health);
    }
});
```

---

## 🔌 Integration Guide

### Step 1: Use EventBus in Managers

**Before:**
```java
// Direct UI updates (tightly coupled)
gameController.updateUI(score, coins);
```

**After:**
```java
// Publish events (loosely coupled)
EventBus.getInstance().publish(new CoinCollectedEvent(value, x, y));
```

### Step 2: Replace Hardcoded Values

**Before:**
```java
private static final double SPAWN_INTERVAL = 0.8;
private static final int BASE_DAMAGE = 1;
```

**After:**
```java
// Use config classes
double interval = CollectibleConfig.BASE_SPAWN_INTERVAL;
int damage = ObstacleConfig.GROUND_OBSTACLE_DAMAGE;
```

### Step 3: Integrate DifficultyManager

In `GameController`:
```java
private DifficultyManager difficultyManager = DifficultyManager.getInstance();

@Override
public void update(double delta) {
    difficultyManager.update(); // Update difficulty first
    
    // Managers use current speed/spawn rate
    double speed = difficultyManager.getCurrentGameSpeed();
    obstacleManager.update(delta, speed, gameObjects);
}
```

### Step 4: Wire Up Event Listeners

In `GameStage` or main UI:
```java
EventBus bus = EventBus.getInstance();

// Listen for coin collection
bus.subscribe("COIN_COLLECTED", event -> {
    CoinCollectedEvent e = (CoinCollectedEvent) event;
    score += e.getCoinValue();
    updateScoreDisplay();
});

// Listen for game over
bus.subscribe("GAME_OVER", event -> {
    GameOverEvent e = (GameOverEvent) event;
    showGameOverScreen(e.getFinalScore(), e.getFinalCoins());
});
```

---

## 🎮 Game Flow

```
User Input
    ↓
InputManager (processes input)
    ↓
GameController (orchestrates)
    ↓
Managers (execute logic):
  - ObstacleManager
  - CollectibleManager
  - HealthManager
  - AbilityManager
  - DifficultyManager
    ↓
EventBus (publishes events)
    ↓
Listeners react:
  - UI updates
  - Sound effects
  - Visual effects
    ↓
Render & Display
```

---

## 📊 Configuration Example

Change gameplay without touching code:

```java
// In PhysicsConfig.java
public static final double JUMP_FORCE = 18.0; // Increase for higher jumps

// In DifficultyConfig.java
public static final double[] STAGE_SPEED_MULTIPLIERS = {
    1.0,    // Stage 1 - easy
    1.1,    // Stage 2
    1.25,   // Stage 3 - harder
    1.4,    // Stage 4
    1.6     // Stage 5 - hardest
};
```

---

## 🚀 Performance Optimizations

1. **Event Bus** - Eliminates tight coupling, reduces dependencies
2. **Object Pooling** - Reuses objects, reduces GC pressure
3. **Config Classes** - Single source of truth, easy tweaking
4. **Difficulty Scaler** - Uses math curves instead of hardcoded values
5. **Input Manager** - Centralized, reduces redundant checks

---

## 🐛 Known Issues & Future Work

- [ ] Integrate all EventBus calls into existing managers
- [ ] Replace all magic numbers with config values
- [ ] Add UI observers for score/health displays
- [ ] Implement achievement unlock notifications
- [ ] Add difficulty settings UI

---

## 🛠 Build & Run

```bash
# Build
gradle clean build

# Run
gradle run

# Or in IntelliJ: Run → CookieRunApp
```

---

## 📝 Key Classes

| Class | Purpose | Type |
|-------|---------|------|
| `EventBus` | Event publishing | Singleton |
| `ScoreCalculator` | Score logic | Singleton |
| `DifficultyManager` | Difficulty scaling | Singleton |
| `InputManager` | Input handling | Singleton |
| `GameConfig` | Configuration | Static |
| `DamageCalculator` | Damage calculation | Singleton |
| `ObserverManager` | Observer pattern | Singleton |

---

## 📖 Documentation

Each class has detailed JavaDoc comments explaining:
- Purpose and responsibility
- Usage examples
- Integration points
- Key methods

---

## 👨‍💻 Development Notes

- All singletons are thread-safe
- Event system is fully decoupled
- Config classes use static finals for compile-time constants
- Observer pattern supports multiple listeners
- Object pooling is generic and reusable

---

## 🎯 Next Steps

1. Open `FinalCPRUN` in IntelliJ
2. Review the new systems in their packages
3. Start integrating into `GameController`
4. Replace hardcoded values with config classes
5. Wire up event listeners in UI components
6. Test difficulty progression
7. Tune config values for gameplay balance

---

## 📄 License

Game code for learning purposes.

---

**Last Updated:** May 11, 2026  
**Version:** 2.0 (Improved Edition)  
**Status:** Ready for Integration ✅
