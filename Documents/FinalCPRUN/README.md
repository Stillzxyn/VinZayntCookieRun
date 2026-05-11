# CP

A 2D endless-runner built in Java with JavaFX. Pick a cookie, pick a world, then jump and slide your way past obstacles while collecting jellies, coins, and health items. The longer you survive, the faster the world scrolls.

## Gameplay

You run automatically. Your job is to:

- Jump over ground obstacles (spikes, blocks, candy walls)
- Slide under air obstacles (bats, cloud spikes, fireballs)
- Collect coins and jellies for score
- Pick up health items to top up HP
- Trigger your cookie's special ability at the right moment

HP drains naturally over time. If it hits zero, or an obstacle KOs you, the run ends.

## Controls

| Action  | Keys                  |
|---------|-----------------------|
| Jump    | Space or Up Arrow     |
| Slide   | Down Arrow or S       |
| Ability | E                     |
| Pause   | P                     |

Mouse also works during gameplay: click the upper half of the screen to jump, the lower half to slide.

## Cookies

Five cookies are unlocked from the start, each with their own HP pool and ability:

| Cookie               | Tier | Ability                                       |
|----------------------|------|-----------------------------------------------|
| Blueberry Cookie     | S    | Magnetic pull on nearby coins and jellies     |
| Brave Gingerbread    | A    | Ghost mode - phase through obstacles briefly  |
| Pirate Cookie        | A    | Jump boost                                    |
| Zombie Cookie        | B    | Passive perks                                 |
| Hero Cookie          | S    | Higher jumps                                  |

## Stages

Five worlds, each scaling up speed and obstacle density:

1. Forest - Easy
2. Palace - Decent
3. Theatre - Normal
4. City of Magic - Hard
5. CP Grave - CP

Difficulty also rises within a single run as you survive longer (handled by `DifficultyManager`).

## Build and Run

Requires JDK 21+ and the Gradle wrapper or local Gradle.

```bash
# Build
gradle clean build

# Run the game
gradle run

# Run unit tests
gradle test
```

JavaFX 24 is pulled in automatically via the `org.openjfx.javafxplugin` Gradle plugin - no separate JavaFX SDK install needed.

You can also open the project in IntelliJ IDEA and run `application.CookieRunApp` directly.

## Project Layout

```
FinalCPRUN/
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── application/      Entry point (CookieRunApp)
    │   │   ├── audio/            SoundManager (music + SFX)
    │   │   ├── core/
    │   │   │   ├── abilities/    Cookie abilities (Magnetic, Ghost, JumpBoost)
    │   │   │   ├── entities/     Cookie, GameObject, Obstacle, State
    │   │   │   ├── pooling/      Generic ObjectPool
    │   │   │   ├── stages/       Stage + StageList
    │   │   │   └── Physics       Gravity, jump, ground constants
    │   │   ├── game/
    │   │   │   ├── GameController        Orchestrates the game loop
    │   │   │   ├── cookies/              Cookie types + CookieList
    │   │   │   ├── obstacles/            Air + ground obstacle types
    │   │   │   ├── collectibles/         Coins, jellies
    │   │   │   ├── items/                HealthItem
    │   │   │   ├── managers/             Cookie/Obstacle/Collectible/Health/Ability managers
    │   │   │   └── config/               Tunable constants
    │   │   ├── gamelogic/
    │   │   │   ├── events/               EventBus + game events
    │   │   │   └── progression/          DifficultyManager
    │   │   ├── gui/
    │   │   │   ├── pages/                Home, CookieSelect, StageSelect, GameStage
    │   │   │   ├── components/           Cards, panes, overlays
    │   │   │   ├── graphics/             Scrolling layer, particles, fonts
    │   │   │   └── baseElements/         Reusable buttons and cards
    │   │   └── utils/                    Animatable / Renderable / Updatable / Collidable / Spawnable
    │   └── resources/
    │       ├── CookieSprite/             Per-cookie animation frames
    │       ├── Stages/                   Backgrounds, jellies, UI for each world
    │       ├── CookieSelectionPane/      Selection screen art
    │       ├── HomePage/                 Title screen art
    │       ├── audio/                    music/ and sfx/
    │       └── fonts/                    CookieRun Bold.ttf
    └── test/
        └── java/                         JUnit 5 + TestFX tests
```

## Architecture Notes

- **Game loop** runs in `GameStage` via a JavaFX `AnimationTimer`. Delta time is capped at 0.1s to avoid huge jumps on lag spikes.
- **GameController** owns score, coins, game-over state, and delegates per-system work to managers.
- **Managers** (`CookieManager`, `ObstacleManager`, `CollectibleManager`, `HealthManager`, `AbilityManager`) each handle their own update + collision pass and keep their object lists clean via a single `removeIf` per frame.
- **EventBus** (`gamelogic/events/EventBus`) is a lightweight pub/sub used for `COIN_COLLECTED`, `HEALTH_CHANGED`, `OBSTACLE_HIT`, and `GAME_OVER`. UI and audio react to events rather than being called directly.
- **DifficultyManager** ramps game speed and spawn rates based on stage and elapsed time.
- **Config classes** under `game/config/` (`GameConfig`, `PhysicsConfig`, `ObstacleConfig`, `CollectibleConfig`) keep tunable constants in one place.

## Tuning Quick Reference

| Constant                       | File              | Notes                                |
|--------------------------------|-------------------|--------------------------------------|
| Window size, target FPS, audio | `GameConfig`      | Window 800x450, target 60 FPS        |
| Gravity, jump velocity, ground | `PhysicsConfig`   | Ground at y=290                      |
| HP drain rate                  | `CookieManager.update` | ~2 HP/second                    |
| Health item heal amount        | `HealthManager.checkCollision` | +25 HP per pickup       |
| Stage speed/spawn multipliers  | `StageList`       | Per-world difficulty                 |

## Tests

Unit tests live under `src/test/java/`. Run with:

```bash
gradle test
```

The build uses JUnit 5 and TestFX (with the Glass robot) for JavaFX-aware tests.
