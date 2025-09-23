# Bir Kelime Bir İşlem - Modern Design System

A modern, mobile-first Kotlin Multiplatform word game featuring a comprehensive design system optimized for Turkish language support.

[![CI](https://github.com/erdalgunes/kelime-islem/actions/workflows/ci.yml/badge.svg)](https://github.com/erdalgunes/kelime-islem/actions/workflows/ci.yml)
[![Coverage](https://img.shields.io/badge/coverage-98.9%25-brightgreen)](https://github.com/erdalgunes/kelime-islem)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=erdalgunes_kelime-islem&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=erdalgunes_kelime-islem)
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=erdalgunes_kelime-islem&metric=coverage)](https://sonarcloud.io/summary/new_code?id=erdalgunes_kelime-islem)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=erdalgunes_kelime-islem&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=erdalgunes_kelime-islem)

## 🎨 Modern Design System

Built with **2025 best practices** featuring:
- **Mobile-First Design**: 44dp touch targets, one-handed operation optimized
- **Material Design 3**: Latest Google design system with Turkish localization
- **Turkish Language Support**: Full character set (ç, ğ, ı, İ, ö, ş, ü) with proper İ/ı casing
- **Responsive Components**: Adaptive layouts for all screen sizes
- **Accessibility Ready**: High contrast, screen reader support, proper semantics

### Design System Components

- **LetterTile**: Interactive letter tiles with Turkish character support
- **WordBoard**: Prominent word display with animation support
- **WordGrid**: Interactive word formation and validation
- **MinimalCountdown**: Clean circular timer for mobile games
- **SimpleGameButton**: Touch-optimized action buttons
- **ScoreDisplay**: Animated score components with Turkish number formatting

## 🏗️ Architecture

This project follows the **Circuit** architecture pattern from Slack, providing:
- Clear separation between UI and business logic
- Unidirectional data flow
- High testability (98.9% coverage for business logic)
- Modern Kotlin Multiplatform with Compose UI

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
kelime-islem-design/
├── design-system/        # Modern UI component library
│   ├── src/commonMain/kotlin/com/erdalgunes/kelimeislem/designsystem/
│   │   ├── components/   # Reusable UI components
│   │   │   ├── LetterTile.kt
│   │   │   ├── WordBoard.kt
│   │   │   ├── WordGrid.kt
│   │   │   ├── MinimalCountdown.kt
│   │   │   ├── SimpleGameButton.kt
│   │   │   └── ScoreDisplay.kt
│   │   ├── theme/        # Design tokens and theming
│   │   └── tokens/       # Design system tokens
├── composeApp/           # Demo application
│   ├── src/
│   │   ├── commonMain/   # Shared demo code
│   │   ├── androidMain/  # Android-specific code
│   │   └── commonTest/   # Tests
├── build-logic/          # Convention plugins with modern KMP setup
└── docs/                 # Documentation
```

## 🏛️ Design System Philosophy

**Boy Scout Rule**: Leave code cleaner than you found it
- No commented-out code or TODOs in production
- Clean, minimal implementations over complex alternatives
- Mobile-first responsive design
- Turkish language as first-class citizen

**Modern Stack (2025)**:
- **Kotlin 2.2.20** with latest multiplatform features
- **Compose Multiplatform 1.8.0** with Material Design 3
- **Gradle 8.13** with Convention Plugins for optimal build performance
- **Type-safe dependencies** with version catalogs

## 🎮 Usage

```kotlin
// Turkish game UI with modern components
TurkishGameShowTheme {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        WordBoard(word = "TÜRKÇE")
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            "KELIME".forEach { char ->
                LetterTile(
                    letter = char,
                    state = LetterTileState.Available,
                    points = getTurkishLetterValue(char)
                )
            }
        }
        
        MinimalCountdown(totalSeconds = 60, currentSeconds = 45)
        
        SimpleGameButton(text = "Kelime Gönder") { 
            submitWord() 
        }
    }
}
```

## 🤝 Contributing

This is a private repository. If you have access:

1. Create a feature branch
2. Make your changes
3. Ensure tests pass and coverage remains >80%
4. Submit a pull request

## 📄 License

Copyright 2025 Bir Kelime Bir İşlem Project

Licensed under the Apache License, Version 2.0# CI/CD Status

GitHub Actions now running on public repository for free CI/CD.
