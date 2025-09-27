# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Context
Turkish word game with comprehensive design system - Kotlin Multiplatform project (Android, JS/Web) using Compose Multiplatform. Features Circuit architecture, Material Design 3, and Turkish language optimization.

**Key Modules:**
- `composeApp` - Main application logic with Circuit presenters and test coverage
- `design-system` - UI component library with Turkish game show theming
- `androidApp` - Android-specific wrapper
- `build-logic` - Convention plugins for consistent Gradle configuration

## Development Commands

### Build & Test
```bash
# Build entire project
./gradlew build

# Run unit tests with coverage
./gradlew testDebugUnitTest koverHtmlReport

# Run specific test target
./gradlew :composeApp:testDebugUnitTest
./gradlew :composeApp:jsNodeTest

# Verify coverage thresholds (current: 52.5%, target: 80%+)
./gradlew koverVerify

# Check code quality
./gradlew lint
```

### Installation & Running
```bash
# Install Android debug APK
./gradlew installDebug

# Build JS/Web distribution
./gradlew :composeApp:jsBrowserDistribution
```

### Coverage & Quality
```bash
# Generate coverage reports (excluded: UI files, design-system)
./gradlew koverXmlReport koverHtmlReport

# SonarCloud analysis
# Note: SONAR_TOKEN is automatically provided by GitHub Actions CI
# For local testing: Contact repository administrators for a personal token
# Then: export SONAR_TOKEN=<personal-token> && ./gradlew sonar
./gradlew sonar

# Run tests and SonarCloud analysis together
./gradlew testDebugUnitTest koverXmlReport sonar

# Security checks
./gradlew dependencyCheckAnalyze
```

## Architecture Patterns

### Circuit Architecture (Slack)
- **Presenters**: Business logic in `/presentation/` (e.g., `AppPresenter.kt`)
- **UI Screens**: Pure Compose UI in `/ui/` (excluded from coverage)
- **State Management**: Unidirectional data flow with Circuit's `Presenter` interface

### Design System Structure
```text
design-system/src/commonMain/kotlin/com/erdalgunes/kelimeislem/designsystem/
├── components/          # Reusable game components
│   ├── LetterTile.kt   # Turkish character support
│   ├── WordBoard.kt    # Game word display
│   ├── MinimalCountdown.kt # Mobile-first timer
│   └── SimpleGameButton.kt # Touch-optimized buttons
├── theme/              # Material Design 3 theming
│   ├── Theme.kt        # TurkishGameShowTheme
│   ├── Color.kt        # Turkish brand colors
│   └── Typography.kt   # Turkish-optimized fonts
└── tokens/             # Design system tokens
    ├── Spacing.kt      # Mobile-first spacing
    └── Motion.kt       # Animation specifications
```

### Testing Strategy
- **Business Logic**: Kotest property-based testing (80% minimum threshold)
- **UI Components**: Screenshot testing with Paparazzi (planned)
- **Coverage Exclusions**: UI files, design-system module, generated code
- **Current Status**: 52.5% baseline coverage, targeting 80%+ for business logic

## Required Tool Usage
### 0. Check the current date
```bash
date
```

### 1. Use Grok for reasoning and complex decisions
```bash
uvx llm -m openrouter/x-ai/grok-code-fast-1 "your question here"
```
Use before making architectural decisions, optimizing workflows, or solving complex problems.

### 2. Use Tavily MCP for web research
Use tavily mcp tools to research best practices, find solutions, validate approaches.

### 3. Use Context7 for documentation
Use context7 mcp tools to get official docs for libraries and tools.

## Turkish Language Support
- **Character Set**: Full Turkish alphabet (ç, ğ, ı, İ, ö, ş, ü)
- **Case Handling**: Proper İ/ı uppercase/lowercase conversion
- **Localization**: Turkish-first UI strings and number formatting
- **Typography**: Optimized for Turkish character rendering

## Convention Plugins (build-logic/)
- `kelimeislem.kmp.library` - KMP library configuration
- `kelimeislem.compose.multiplatform` - Compose Multiplatform setup
- `kelimeislem.kmp.test` - Kotest configuration with property-based testing
- `kelimeislem.test.coverage` - Kover coverage with 80% minimum threshold

# important-instruction-reminders
Do what has been asked; nothing more, nothing less.
NEVER create files unless they're absolutely necessary for achieving your goal.
ALWAYS prefer editing an existing file to creating a new one.
NEVER proactively create documentation files (*.md) or README files. Only create documentation files if explicitly requested by the User.
