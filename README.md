# CP RUN 🍪

A JavaFX-based 2D endless runner game built with Java. Control your cookie, avoid obstacles, collect items, and survive as long as possible!

## Overview

Cookie Run is a fast-paced endless runner game where you control a cookie character through an infinite level. Jump to avoid obstacles, slide under barriers, and use special abilities to survive. The game features multiple playable cookies with unique abilities, dynamic difficulty scaling, and a scoring system.

## Features

- **5 Playable Cookies** - Each with unique abilities and stats
- **6 Obstacle Types** - 3 ground-based, 3 air-based obstacles
- **Collectible System** - Coins and jelly items for scoring
- **Health System** - HP drains over time, health items to recover
- **Special Abilities** - Cookie-specific powers (Jump Boost, Magnetic Pull, etc.)
- **Adaptive Performance** - Speed scales with game time
- **Black Glass UI** - Modern card-based selection interface
- **Tier System** - Cookies organized by S/A/B/C tiers

## Requirements

- **Java 17** or higher
- **JavaFX 20.0.1**
- **Gradle 8.1.1**

## Setup

### 1. Clone/Download the Project
```bash
cd FinalCookieRun
```

### 2. Build the Project
```bash
gradle clean build
```

### 3. Download Dependencies
Gradle will automatically download all required dependencies (JavaFX, JUnit 5, etc.)

## Running the Game

### Start the Game
```bash
gradle run
```

### From IntelliJ IDEA
- Click the green play button next to `CookieRunApp`
- Or: Run → Run 'CookieRunApp'

## Running Tests

### Run All Tests
```bash
gradle test
```

### Run Specific Test Class
```bash
gradle test --tests CookieManagerTest
gradle test --tests CookieTest
gradle test --tests GameControllerTest
```

### View Test Report
```bash
gradle test --info
```

## Project Structure

```
src/
├── main/java/
│   ├── application/          # Main app entry point
│   │   └── CookieRunApp.java
│   ├── core/
│   │   ├── entities/         # Game objects (cookies, obstacles, collectibles)
│   │   ├── abilities/        # Cookie special abilities
│   │   └── stages/           # Game stages/levels
│   ├── game/
│   │   ├── GameController.java       # Main game logic
│   │   └── managers/         # Obstacle, Collectible, Health, Cookie managers
│   ├── graphics/
│   │   ├── rendering/        # Background rendering
│   │   └── effects/          # Particle effects
│   ├── gui/
│   │   ├── views/pages/      # Game screens (selection, gameplay)
│   │   ├── components/       # Reusable UI components
│   │   └── util/             # UI utilities (font loading)
│   └── utils/                # Core interfaces and utilities
│
├── test/java/                # JUnit tests
│   ├── game/GameControllerTest.java
│   └── core/entities/base/CookieTest.java
│
└── resources/                # Images, sprites, assets
    └── CookieSprite/         # Cookie animations
```

## Game Controls

| Control | Action |
|---------|--------|
| **Space** | Jump |
| **S Key** | Slide Down |
| **A Key** | Use Ability |
| **P Key** | Pause |

## Architecture

### Design Patterns Used

1. **Singleton Pattern** - GameController, CookieManager
2. **Manager Pattern** - ObstacleManager, CollectibleManager, HealthManager
3. **Strategy Pattern** - Cookie abilities (CookieAbility interface)
4. **Object Pooling** - ParticlePool for efficient memory usage
5. **Observer Pattern** - Input buffering system

### Key Classes

- **GameController** - Main game loop, update logic, collision detection
- **CookieManager** - Cookie loading, selection, tier management
- **Cookie** - Base cookie class with HP, physics, abilities
- **GameStage** - JavaFX stage containing game canvas and rendering
- **ObstacleManager** - Spawns and manages obstacle collisions
- **CollectibleManager** - Spawns coins/jelly and handles collection

## Performance Optimizations

- **Adaptive Spawn Scaling** - Spawn rates scale with game speed to prevent lag
- **Batch Object Cleanup** - Single-pass removal of dead objects
- **Collision Culling** - Skip off-screen object collision checks
- **Delta-Time Capping** - Max 0.1s per frame to handle lag spikes
- **Input Buffering** - Reduces input lag during high-speed gameplay
- **Camera Shake Decay** - Smooth easing for visual effects
- **Particle Pooling** - Reuse particle objects instead of creating new ones

## Cookies Available

| Cookie | Tier | Ability | HP |
|--------|------|---------|-----|
| Hero Cookie | S | Jump Boost | 115 |
| Blueberry Cookie | S | Magnetic Pull | 110 |
| Pirate Cookie | A | - | 105 |
| Brave Gingerbread | B | - | 100 |
| Zombie Cookie | C | - | 95 |

## Game Stages

- **World 1-5** - Increasing difficulty multipliers
- **Difficulty Levels** - Easy, Decent, Normal, Hard, CP
- **Dynamic Speed** - Speed increases with game time

## Testing

### Test Coverage

- **CookieManagerTest** (28 tests) - Cookie loading, selection, querying
- **CookieTest** (25 tests) - HP system, state management, abilities
- **GameControllerTest** (24 tests) - Game loop, scoring, input handling

### Run with Coverage
```bash
gradle test jacocoTestReport
```

Report location: `build/reports/jacoco/test/html/index.html`

## Building for Distribution

### Create JAR File
```bash
gradle jar
```

### Create Executable JAR
```bash
gradle shadowJar
```

## Troubleshooting

### Tests not recognized
```bash
gradle clean build
```

### JavaFX not loading
Ensure `build.gradle` has JavaFX plugin:
```gradle
plugins {
    id 'org.openjfx.javafxplugin' version '0.0.13'
}
```

### Game window won't open
- Check Java version: `java -version` (should be 17+)
- Rebuild: `gradle clean build`

## Future Enhancements

- [ ] Leaderboard system
- [ ] Power-up items
- [ ] Boss battles
- [ ] Multiple game modes
- [ ] Sound effects and music
- [ ] Mobile version
- [ ] Online multiplayer

## Documentation

- **JavaDoc**: Generated documentation in `/javaDocs` folder
- **Architecture Guide**: See `javaDocs/guides/architecture.html`
- **Quick Start**: See `javaDocs/guides/quick-start.html`
- **Features Guide**: See `javaDocs/guides/features.html`

## License

This project is created for educational purposes.

## Credits

- Game Design: VinZaynt
- Built with: JavaFX, Gradle, JUnit 5

## Getting Help

1. Check the troubleshooting section above
2. Review JavaDoc documentation
3. Check test files for usage examples
4. Review architecture guide for design patterns

---

**Happy playing! 🎮**
