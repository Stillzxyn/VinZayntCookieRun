# VinZaynt's Cookie Run - Technical Documentation

## 1. Project Overview
VinZaynt's Cookie Run is a high-performance 2D endless runner built with JavaFX. The project emphasizes clean Object-Oriented Programming (OOP) principles, modular architecture, and smooth gameplay mechanics.

### Core Features:
- **Playable Cookies**: Multiple cookie types with unique stats and special abilities (Magnetic, Ghost, Jump Boost).
- **Dynamic Obstacles**: A variety of ground and air obstacles with distinct avoidance patterns.
- **Manager-Based Architecture**: Decoupled systems for handling entities, health, and collectibles.
- **Parallax Scrolling**: Smooth multi-layered background rendering.
- **Adaptive Performance**: Frame-rate independent physics and visual effects.

---

## 2. Architecture & Design Patterns

### Manager Pattern
The game logic is divided into specialized managers to ensure high cohesion and low coupling:
- **ObstacleManager**: Handles spawning patterns, movement, and collision damage for obstacles.
- **CollectibleManager**: Manages jelly and coin spawning, pickup logic, and particle effects.
- **HealthManager**: Controls the spawning of health items and player healing logic.

### Strategy Pattern (Abilities)
The `CookieAbility` interface allows for extensible cookie behaviors. Instead of hardcoding logic into the `Cookie` class, abilities are injected at runtime:
- `MagneticAbility`: Passively attracts collectibles within a radius.
- `GhostAbility`: Active skill that allows passing through obstacles for a duration.
- `JumpBoostAbility`: Enhances physics parameters for higher jumps.

### Polymorphism & Interfaces
The engine relies on a set of core utility interfaces to handle game objects uniformly:
- `Updatable`: Standardizes state updates across all entities.
- `Renderable`: Ensures consistent drawing logic on the JavaFX Canvas.
- `Collidable`: Provides a unified interface for intersection checks.
- `Spawnable`: Defines lifecycle methods for objects entering/leaving the game world.

---

## 3. Project Structure

```text
src/main/java/
├── application/              # Entry point (CookieRunApp)
├── core/
│   ├── entities/            # All game actors (Cookies, Obstacles, Items)
│   ├── abilities/           # Strategy-based ability implementations
│   └── stages/              # Level definitions and difficulty scaling
├── game/
│   ├── GameController.java   # Central game loop coordinator
│   └── managers/            # Specialized logic managers
├── graphics/
│   ├── rendering/           # Scrolling and background logic
│   └── animation/           # Sprite animation handlers
├── ui/
│   ├── components/          # Reusable UI elements
│   └── views/               # Pages (Home, GameStage, Overlays)
└── utils/                    # Common interfaces and helper tools
```

---

## 4. Build & Run Instructions

### Prerequisites
- **Java 11** or higher.
- **Gradle** (included wrapper `gradlew` can be used).

### Running the Application
```bash
./gradlew run
```

### Building a Runnable JAR
```bash
./gradlew build
java -jar build/libs/cookierun-1.0.jar
```

---

## 5. Recent Optimizations

- **Frame-Rate Independence**: Physics updates are decoupled from the rendering loop using delta time, ensuring consistent movement on high-refresh-rate displays.
- **Memory Management**: Optimized list iteration and object pooling (via managers) to reduce GC pressure.
- **Type Safety**: Refactored core managers to use generic `List<GameObject>` types, eliminating unsafe casts and improving stability.
- **Visual Smoothness**: Time-based visual effects (glow, bobbing) ensure smooth animations regardless of logic-frame fluctuations.

---

## 6. Development & Troubleshooting

### Troubleshooting
- **JavaFX Not Found**: Ensure you are using the provided Gradle script, which automatically downloads the required JavaFX modules.
- **Main Class Error**: The project entry point is `application.CookieRunApp`.
- **Assets Loading**: Ensure the `src/main/resources` folder is correctly marked as a resource root in your IDE.

### Extending the Game
To add a new Cookie:
1. Create a new class extending `Cookie` in `core.entities.cookies.implementations`.
2. Assign a `CookieAbility` in the constructor.
3. Register the new cookie in the selection UI.

---
**Document Version:** 1.1
**Status:** ✅ Project Documentation Consolidated and Verified.
