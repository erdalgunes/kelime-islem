# Turkish Game Show Design System

A comprehensive Material Design 3 design system for "Bir Kelime Bir İşlem" - a Turkish word game show application built with Kotlin Multiplatform and Jetpack Compose.

## 🎯 Overview

This design system provides a complete set of components, themes, and tokens specifically designed for Turkish game show applications. It combines Material Design 3 principles with Turkish cultural elements and TV game show aesthetics.

## 🌟 Key Features

### 🇹🇷 Turkish Localization
- **Full Turkish Character Support**: Properly handles ç, Ç, ğ, Ğ, ı, İ, ö, Ö, ş, Ş, ü, Ü
- **Locale-Aware Text Processing**: Uses `Locale("tr","TR")` for proper İ/ı casing
- **Turkish Number Formatting**: Correct comma and decimal separators
- **Optimized Typography**: Line heights and spacing optimized for Turkish text

### 🎬 Game Show Aesthetics
- **TV Studio Colors**: Turkish flag-inspired red primary with studio lighting cyan accents
- **High Contrast**: Designed for TV visibility and accessibility
- **Dramatic Animations**: TV game show-style transitions and effects
- **Professional Typography**: Bold, attention-grabbing text styles

### 📱 Modern Material Design 3
- **Dynamic Colors**: Android 12+ dynamic theming support
- **Accessibility First**: WCAG AA compliance, high contrast modes
- **Responsive Design**: Adaptive layouts for phones, tablets, and TV
- **Performance Optimized**: Efficient animations and rendering

## 📂 Structure

```
design-system/
├── theme/                    # Theme foundation
│   ├── Color.kt             # Turkish game show color palette
│   ├── Typography.kt        # Turkish-aware typography system
│   └── Theme.kt             # Material 3 theme composition
├── tokens/                   # Design tokens
│   ├── Spacing.kt           # Spacing and layout tokens
│   ├── Motion.kt            # Animation and motion tokens
│   └── Elevation.kt         # Depth and shadow tokens
├── components/               # Game-specific components
│   ├── WordBoard.kt         # Word display component
│   ├── ScoreCard.kt         # Player score display
│   ├── BuzzerButton.kt      # Interactive buzzer controls
│   └── TimerBar.kt          # Countdown timer component
└── sample/                   # Demo application
    └── GameShowSampleApp.kt # Complete sample implementation
```

## 🎨 Color System

### Primary Palette (Turkish Red Theme)
- **Primary**: Turkish flag red (#B71C1C) for main actions
- **Secondary**: Studio cyan (#006780) for lighting effects
- **Tertiary**: Accent gold (#F57C00) for highlights

### Game-Specific Colors
- **Correct Answer**: Success green (#2E7D32)
- **Incorrect Answer**: Error red (#B3261E)
- **Timer Normal**: Studio cyan (#0288D1)
- **Timer Warning**: Amber (#FF9800)
- **Timer Critical**: Turkish red (#D32F2F)

## 🔤 Typography

### Turkish-Optimized Fonts
- **Display Fonts**: Extra bold for prominent game elements
- **Monospace**: For scores and timers
- **Game Typography**: Specialized styles for buzzer text, word display, and results

### Accessibility Features
- **High Contrast Variants**: Enhanced readability options
- **Scalable Text**: Supports dynamic text sizing
- **Screen Reader Support**: Proper semantic labeling

## 🧩 Components

### WordBoard
Prominent word display component with Turkish character support and game show animations.

```kotlin
WordBoard(
    word = "KELİME",
    state = WordBoardState.Normal,
    style = WordBoardStyle(variant = WordBoardVariant.Primary),
    animateEntry = true
)
```

### ScoreCard
TV show-style score display with multiple layouts and animated score counting.

```kotlin
ScoreCard(
    playerName = "Ayşe Yılmaz",
    score = 1250,
    previousScore = 1150,
    style = ScoreCardStyle(layout = ScoreCardLayout.Detailed),
    state = ScoreCardState.Leading,
    rank = 1,
    avatar = { DefaultPlayerAvatar() }
)
```

### BuzzerButton
Interactive buzzer button with haptic feedback and multiple styles.

```kotlin
BuzzerButton(
    text = "BUZZER",
    onClick = { /* Handle buzzer press */ },
    size = BuzzerSize.Large,
    style = BuzzerStyle.Primary,
    hapticEnabled = true
)
```

### TimerBar
Animated countdown timer with warning states and multiple display styles.

```kotlin
TimerBar(
    totalTimeSeconds = 60,
    currentTimeSeconds = 30,
    style = TimerStyle.Linear,
    showWarningEffects = true,
    onTimeUp = { /* Handle time up */ }
)
```

## 🎬 Animations

### Motion System
- **Game Show Timing**: Dramatic 300-800ms durations
- **Turkish Easing**: Custom easing curves for TV effects
- **Performance Optimized**: Reduced motion support

### Key Animations
- **Word Reveal**: Letter-by-letter appearance
- **Score Counting**: Animated number transitions
- **Buzzer Press**: Haptic feedback with visual response
- **Timer Countdown**: Pulsing and color changes

## 🔧 Usage

### Basic Setup

```kotlin
@Composable
fun MyGameApp() {
    TurkishGameShowTheme {
        // Your app content
        GamePlayScreen()
    }
}
```

### With Configuration

```kotlin
@Composable
fun MyGameApp() {
    TurkishGameShowTheme(
        darkTheme = isSystemInDarkTheme(),
        config = GameShowThemeConfig(
            useDynamicColors = true,
            useAccessibilityEnhancements = false,
            turkishOptimizations = true
        )
    ) {
        // Your app content
    }
}
```

### Accessing Game Colors

```kotlin
@Composable
fun MyComponent() {
    val gameColors = TurkishGameShowThemeExtensions.gameShowColors
    
    Surface(
        color = gameColors.CorrectAnswer
    ) {
        Text(
            text = "Doğru!",
            color = gameColors.OnCorrectAnswer
        )
    }
}
```

## 📱 Platform Support

- ✅ **Android**: Full Material 3 support with dynamic colors
- ✅ **iOS**: Consistent styling with platform adaptations
- ✅ **Desktop**: Large screen optimizations
- ✅ **Web**: Progressive web app support

## ♿ Accessibility

### Features
- **High Contrast Mode**: Enhanced color contrast ratios
- **Reduced Motion**: Respects system accessibility preferences
- **Screen Reader Support**: Semantic labeling and descriptions
- **Touch Targets**: Minimum 44dp touch targets
- **Focus Management**: Keyboard navigation support

### Usage
```kotlin
AccessibleTurkishGameShowTheme {
    // Automatically applies accessibility enhancements
    MyGameContent()
}
```

## 🧪 Testing

The design system includes comprehensive preview composables for development and testing:

```kotlin
@Preview(name = "Word Board States")
@Composable
private fun WordBoardPreview() {
    PreviewThemes.LightPreview {
        WordBoard(word = "MERHABA")
    }
}
```

## 🤝 Contributing

When adding new components:

1. Follow Material Design 3 principles
2. Support Turkish localization
3. Include accessibility features
4. Add comprehensive previews
5. Document usage examples

## 📄 License

Copyright 2025 Turkish Game Show Design System
Licensed under the Apache License, Version 2.0

## 🎮 Sample App

Run the included sample app to see all components in action:

```kotlin
@Composable
fun App() {
    GameShowSampleApp()
}
```

The sample app demonstrates:
- Complete game interface
- All component variations
- Theme switching
- Accessibility features
- Turkish text handling

---

**Built with ❤️ for Turkish game shows and Material Design 3**