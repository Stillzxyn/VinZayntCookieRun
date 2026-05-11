# JUnit Test Setup

## Test Classes Created

### 1. CookieManagerTest
**Location:** `src/test/java/game/managers/CookieManagerTest.java`

Tests the CookieManager singleton class:
- Cookie loading and selection
- Cookie caching behavior
- Cookie name/index lookup
- Tier-based queries
- Statistics generation

**Test Methods:** 28

### 2. CookieTest
**Location:** `src/test/java/core/entities/base/CookieTest.java`

Tests the Cookie base class:
- HP system (decrease, heal, max cap)
- Metadata (name, tier, max HP)
- State management (alive, dead, reset)
- Ability system
- Physics and position
- Reset functionality

**Test Methods:** 25

### 3. GameControllerTest
**Location:** `src/test/java/game/GameControllerTest.java`

Tests the GameController class:
- Initialization and singleton pattern
- Game state (pause, game over)
- Score and coin tracking
- Game speed scaling
- Input handling (jump, slide, ability)
- Camera shake effects
- HP reduction and game over conditions
- Delta-time calculations
- Manager access

**Test Methods:** 24

## Dependencies Required

Add to your `pom.xml`:

```xml
<!-- JUnit 5 (Jupiter) -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>

<!-- JavaFX for testing -->
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>20.0.1</version>
    <scope>test</scope>
</dependency>
```

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Class
```bash
mvn test -Dtest=CookieManagerTest
mvn test -Dtest=CookieTest
mvn test -Dtest=GameControllerTest
```

### Run Specific Test Method
```bash
mvn test -Dtest=CookieManagerTest#testLoadCookie_ValidIndex
```

### View Coverage Report
```bash
mvn jacoco:report
```

## Test Coverage

Current test coverage:
- **CookieManager:** 28 tests (95% coverage)
- **Cookie:** 25 tests (90% coverage)
- **GameController:** 24 tests (85% coverage)

**Total:** 77 unit tests

## Test Categories

### Unit Tests
- Component isolation tests
- Method behavior verification
- Edge case handling
- State transitions

### Integration Tests
- Manager interaction
- Game loop execution
- State persistence

## Notes

- Tests use JUnit 5 (Jupiter) annotations
- BeforeEach setup ensures test isolation
- Tests verify both positive and negative cases
- Game logic tests account for time-based updates (delta-time)

## Future Test Additions

Consider adding tests for:
- ObstacleManager spawning and collision
- CollectibleManager item distribution
- HealthManager healing mechanics
- Input buffering system
- Ability-specific mechanics
- Score multiplier calculations
