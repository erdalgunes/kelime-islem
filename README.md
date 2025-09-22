# Bir Kelime Bir İşlem

A Kotlin Multiplatform implementation of the popular Turkish word and number game show "Bir Kelime Bir İşlem".

[![CI](https://github.com/erdalgunes/kelime-islem/actions/workflows/ci.yml/badge.svg)](https://github.com/erdalgunes/kelime-islem/actions/workflows/ci.yml)
[![Coverage](https://img.shields.io/badge/coverage-98.9%25-brightgreen)](https://github.com/erdalgunes/kelime-islem)

## 🏗️ Architecture

This project follows the **Circuit** architecture pattern from Slack, providing:
- Clear separation between UI and business logic
- Unidirectional data flow
- High testability (98.9% coverage for business logic)
- Ready for multi-platform targets (Android, Web/WASM planned)

## 🚀 Getting Started

### Prerequisites
- JDK 17 or higher
- Android SDK
- Gradle 8.13+

### Building the Project

```bash
# Clone the repository
git clone https://github.com/erdalgunes/kelime-islem.git
cd kelime-islem

# Build the project
./gradlew build

# Run tests
./gradlew testDebugUnitTest

# Generate coverage report
./gradlew koverHtmlReport
# Open composeApp/build/reports/kover/html/index.html
```

### Running the App

```bash
# Run on Android device/emulator
./gradlew installDebug
```

## 🧪 Testing

The project maintains high test coverage using:
- **Kotest** - Property-based testing framework
- **Kover** - Code coverage tool
- **Paparazzi** - Screenshot testing (planned)

### Test Coverage Goals
- Business logic: **80% minimum** (currently at 98.9%)
- UI components: Tested via screenshot tests (excluded from coverage)

### Running Tests

```bash
# Run all tests
./gradlew test

# Run with coverage
./gradlew koverXmlReport

# Verify coverage thresholds
./gradlew koverVerify
```

## 📁 Project Structure

```
kelime-islem/
├── composeApp/           # Main application module
│   ├── src/
│   │   ├── commonMain/   # Shared code
│   │   │   ├── kotlin/
│   │   │   │   └── presentation/  # Presenters (business logic)
│   │   │   │   └── ui/           # Composables (UI only)
│   │   ├── androidMain/  # Android-specific code
│   │   └── commonTest/   # Tests
├── build-logic/          # Convention plugins
└── docs/                 # Documentation
```

## 🏛️ Architecture Details

Following Circuit pattern:
- **Presenters**: Contain all business logic, fully testable
- **State**: Immutable data classes
- **UI**: Pure Composables that only render state

See [ARCHITECTURE.md](docs/ARCHITECTURE.md) for detailed information.

## 🤝 Contributing

This is a private repository. If you have access:

1. Create a feature branch
2. Make your changes
3. Ensure tests pass and coverage remains >80%
4. Submit a pull request

## 📄 License

Copyright 2025 Bir Kelime Bir İşlem Project

Licensed under the Apache License, Version 2.0