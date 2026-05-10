# VinZaynt's Cookie Run

A high-performance, object-oriented endless runner game built with JavaFX. This project features a clean architecture, specialized game managers, and a robust entity-component-like system.

## 🎮 Game Overview
VinZaynt's Cookie Run is a side-scrolling endless runner where players control various "Cookies," each with unique abilities, to collect items, avoid obstacles, and achieve the highest score possible.

### Key Features
- **Multiple Playable Cookies**: Each with distinct stats and special abilities (e.g., Magnetic Pull, Ghost Mode, Jump Boost).
- **Dynamic Obstacle System**: Randomized air and ground obstacles with unique behaviors (bobbing, sliding requirements).
- **Infinite Scrolling Backgrounds**: Seamless parallax layers for immersive environments.
- **Advanced Game Mechanics**: HP management, difficulty scaling over time, and a particle-based feedback system.

---

## 🏗 Architecture & Design Patterns
The project follows a modular, layered architecture designed for maintainability and performance.

### 1. Manager Pattern (`game.managers`)
Core game systems are decoupled into specialized managers handled by the `GameController`:
- **`ObstacleManager`**: Handles spawning, collision detection, and damage scaling for obstacles.
- **`CollectibleManager`**: Manages jelly/coin spawning and collection effects.
- **`HealthManager`**: Controls health item spawning and player healing logic.

### 2. Strategy Pattern (`core.abilities`)
Cookie abilities are implemented using the Strategy pattern via the `CookieAbility` interface. This allows new abilities to be added without modifying the base `Cookie` class.
- `GhostAbility`: Allows passing through obstacles.
- `MagneticAbility`: Pulls collectibles toward the player.
- `JumpBoostAbility`: Enhances jumping height.

### 3. Polymorphism & Interfaces (`utils`)
The game engine relies heavily on abstraction to handle diverse game objects uniformly:
- **`Updatable`**: For objects requiring per-frame logic.
- **`Renderable`**: For objects that can be drawn on the Canvas.
- **`Collidable`**: Standardizes collision detection across all entities.
- **`Spawnable`**: Manages the lifecycle of objects that enter and exit the screen.

---

## 📂 Project Structure

- **`application`**: Entry point and main JavaFX application setup.
- **`core`**:
    - **`entities`**: Base classes (`GameObject`, `Cookie`, `Obstacle`) and concrete implementations.
    - **`abilities`**: The ability system and specific implementations.
    - **`stages`**: Stage-specific data and difficulty configurations.
- **`game`**: Core engine logic, `GameController`, and managers.
- **`graphics`**: Rendering engine, parallax layers, and particle effects.
- **`ui`**: Menu systems, HUD, and game overlays (Game Over, Pause).
- **`utils`**: Core interfaces defining the engine's behavior.

---

## 🚀 Recent Optimizations

- **High-FPS Rendering**: Decoupled visual animations (bobbing, glowing) from the logic loop for smooth 60+ FPS performance.
- **Memory Management**: Optimized list iterations and object cleanup in managers to prevent memory leaks and frame stutters.
- **Clean Code**: Refactored to eliminate `instanceof` checks in favor of polymorphic interface calls.
- **Type Safety**: Fully implemented Java Generics across all manager lists for better compile-time safety.

---

## 🛠 Build & Run

### Prerequisites
- Java 11 or higher
- Gradle (included wrapper)

### Commands
```bash
# Run the game
./gradlew run

# Build a JAR
./gradlew build
```
For detailed build instructions and troubleshooting, see [RUNNABLE.md](RUNNABLE.md).

---

## 📝 Developer Notes
The project uses a manual game loop via JavaFX `AnimationTimer`, targeting frame-rate independence. All entities inherit from `GameObject`, which provides basic physics and AABB collision detection.
