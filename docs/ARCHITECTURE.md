# Architecture Guide: Circuit Pattern

## Overview

This project follows the Circuit architecture pattern from Slack for Compose Multiplatform applications. Circuit provides a unidirectional data flow (UDF) architecture that separates business logic from UI rendering.

## Core Concepts

### 1. Screen
Represents a destination in your app. It's a data class that contains the necessary data to present that screen.

```kotlin
@Parcelize
data class GameScreen(val gameId: String) : Screen
```

### 2. State
Immutable representation of the UI state at a given point in time.

```kotlin
data class GameState(
    val word: String,
    val targetNumber: Int,
    val operations: List<Operation>,
    val isLoading: Boolean = false,
    val error: String? = null
) : CircuitUiState
```

### 3. Presenter
Contains all business logic and state management. Presenters are easily testable as they don't depend on UI frameworks.

```kotlin
class GamePresenter(
    private val navigator: Navigator,
    private val gameRepository: GameRepository
) : Presenter<GameState> {
    @Composable
    override fun present(): GameState {
        var word by remember { mutableStateOf("") }
        // Business logic here
        return GameState(word = word, ...)
    }
}
```

### 4. UI (Composable)
Pure presentation layer that renders the state. Contains no business logic.

```kotlin
@Composable
fun GameScreen(
    state: GameState,
    modifier: Modifier = Modifier
) {
    // Pure UI rendering based on state
    Column(modifier) {
        Text(state.word)
        // ...
    }
}
```

## Testing Strategy

### Business Logic Testing (80% Coverage Target)
- **Presenters**: Unit test all business logic
- **Repositories**: Test data operations
- **Use Cases**: Test business rules
- **State Management**: Test state transitions

### UI Testing (Not Included in Coverage)
- **Paparazzi**: Screenshot testing for visual regression
- **Compose UI Tests**: Interaction testing
- **Manual Testing**: User experience validation

## File Organization

```
src/
├── commonMain/
│   ├── kotlin/
│   │   ├── data/           # Data layer
│   │   ├── domain/         # Business logic
│   │   ├── presentation/   # Presenters and State
│   │   │   ├── presenters/
│   │   │   └── models/
│   │   └── ui/            # Composables (excluded from coverage)
│   │       ├── screens/
│   │       └── components/
└── commonTest/
    ├── kotlin/
    │   ├── presentation/   # Presenter tests (high coverage)
    │   └── domain/        # Business logic tests
```

## Coverage Exclusions

The following are excluded from code coverage as they are UI-only:
- `@Composable` functions
- `@Preview` functions
- Files in `ui/` packages
- Generated Compose code

## Web/WASM Support

Circuit fully supports Kotlin/WASM targets, enabling:
- Shared business logic across platforms
- Consistent architecture for web and mobile
- Reusable presenters for different UI implementations

## Boy Scout Rule

When working on any part of the codebase:
1. Leave it cleaner than you found it
2. Add tests for untested business logic
3. Refactor complex presenters into smaller, focused ones
4. Document complex business rules

## References

- [Circuit GitHub](https://github.com/slackhq/circuit)
- [Circuit Documentation](https://slackhq.github.io/circuit/)
- [Paparazzi](https://github.com/cashapp/paparazzi)