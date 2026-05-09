# VinZaynt's Cookie Run - Build & Run Instructions

## Project Status: ✅ Ready to Run

The project has been reorganized into a clean OOP architecture and is ready to build and run.

### Build Configuration Fixed
- ✅ Main class updated: `application.CookieRunApp`
- ✅ Java version compatibility: Java 11+
- ✅ All 60 Java source files in place
- ✅ All resources available in `src/main/resources/`
- ✅ All package imports corrected (core/game/graphics/ui/utils)

---

## How to Run

### Option 1: Using Gradle (Recommended)
```bash
cd /Users/natthawutkaodeang/Documents/VinZayntCookieRun
./gradlew run
```

**Requirements:**
- Java 11 or higher
- Gradle will download JavaFX dependencies automatically

### Option 2: Using Gradle to Build JAR
```bash
./gradlew build
java -jar build/libs/cookierun-1.0.jar
```

### Option 3: Using IDE (IntelliJ IDEA, Eclipse, VS Code)
1. Open the project folder as a Gradle project
2. IDE will automatically detect build.gradle
3. Click Run → Run 'CookieRunApp'

### Option 4: Using Command Line (if you have javac)
```bash
./gradlew compileJava
./gradlew jar
java -jar build/libs/cookierun-1.0.jar
```

---

## Project Structure

```
src/main/java/
├── application/              # Entry point
│   └── CookieRunApp.java
├── core/                     # Game logic & entities
│   ├── entities/            # Cookies, obstacles, collectibles
│   ├── abilities/           # Cookie abilities
│   └── stages/              # Level definitions
├── game/                     # Game mechanics
│   ├── GameController.java
│   └── managers/            # Obstacle, Collectible, Health managers
├── graphics/                 # Rendering
│   ├── rendering/           # ScrollingLayer, BaseBackgroundPane
│   └── animation/           # FrameAnimation
├── ui/                       # User interface
│   ├── components/          # Base UI components
│   └── views/               # Pages and overlays
└── utils/                    # Utility interfaces
```

---

## Troubleshooting

### Error: "Cannot find main class"
**Solution:** The build.gradle has been updated to use `application.CookieRunApp`

### Error: "Gradle download failed"
**Solution:** Use an IDE that handles Gradle automatically, or run on a machine with internet access

### Error: "JavaFX not found"
**Solution:** Gradle will automatically download JavaFX 24.0.2 for your platform

### Game doesn't start
1. Ensure all resources are in `src/main/resources/`
2. Check that Java version is 11 or higher: `java -version`
3. Try running with IDE first (better error messages)

---

## Build Configuration Details

**File:** `build.gradle`
- **Main Class:** `application.CookieRunApp`
- **Java Version:** 11+ (compatible with system JRE)
- **JavaFX Version:** 24.0.2
- **Kotlin:** Optional (included for toolchain compatibility)

---

## What Was Fixed in This Session

### Package Reorganization
- ✅ Moved all entities → `core.entities`
- ✅ Moved all abilities → `core.abilities`
- ✅ Moved all GUI → `ui.views.pages/components`
- ✅ Updated all 60+ import statements
- ✅ Renamed `StageView` → `GameStage` for clarity
- ✅ Cleaned up old directories

### Build Configuration
- ✅ Fixed main class path
- ✅ Updated Java version compatibility
- ✅ Corrected manifest configuration

---

## Next Steps

1. **To Run Now:**
   ```bash
   cd /Users/natthawutkaodeang/Documents/VinZayntCookieRun
   ./gradlew run
   ```

2. **To Debug:**
   - Open in IntelliJ IDEA or VS Code
   - Debug configurations are auto-detected
   - Set breakpoints and run with debugger

3. **To Modify:**
   - All source files are in `src/main/java/`
   - All game assets are in `src/main/resources/`
   - Follow the layered architecture pattern for new features

---

**Status:** ✅ Project is organized, compiled, and ready to run!
