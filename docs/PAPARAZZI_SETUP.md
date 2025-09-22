# Paparazzi Setup Guide

## What is Paparazzi?

Paparazzi is a screenshot testing library for Android that renders UI without needing an emulator or physical device. It's perfect for visual regression testing of Compose UI.

## Installation

Add to your module's `build.gradle.kts`:

```kotlin
plugins {
    id("app.cash.paparazzi") version "1.3.4"
}

dependencies {
    testImplementation("app.cash.paparazzi:paparazzi:1.3.4")
}
```

## Configuration

In your `gradle.properties`:

```properties
# Paparazzi configuration
android.useAndroidX=true
android.nonTransitiveRClass=true
```

## Writing Screenshot Tests

Create test files in `src/test/kotlin`:

```kotlin
package com.erdalgunes.kelimeislem.ui

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.android.ide.common.rendering.api.SessionParams
import com.erdalgunes.kelimeislem.presentation.AppState
import org.junit.Rule
import org.junit.Test

class AppScreenScreenshotTest {
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5,
        renderingMode = SessionParams.RenderingMode.NORMAL,
        showSystemUi = false
    )

    @Test
    fun `default state`() {
        paparazzi.snapshot {
            AppContent(
                state = AppState(
                    greetingMessage = "Merhaba, Kelime İşlem from Android!",
                    appTitle = "Bir Kelime Bir İşlem",
                    isLoading = false
                )
            )
        }
    }

    @Test
    fun `loading state`() {
        paparazzi.snapshot {
            AppContent(
                state = AppState(
                    greetingMessage = "",
                    appTitle = "",
                    isLoading = true
                )
            )
        }
    }

    @Test
    fun `error state`() {
        paparazzi.snapshot {
            AppContent(
                state = AppState(
                    greetingMessage = "Error loading content",
                    appTitle = "Please try again",
                    isLoading = false
                )
            )
        }
    }
}
```

## Running Tests

### Record baseline screenshots:
```bash
./gradlew recordPaparazziDebug
```

### Verify against baseline:
```bash
./gradlew verifyPaparazziDebug
```

## CI/CD Integration

Add to your CI workflow:

```yaml
- name: Run Screenshot Tests
  run: ./gradlew verifyPaparazziDebug

- name: Upload Screenshot Failures
  if: failure()
  uses: actions/upload-artifact@v3
  with:
    name: screenshot-test-failures
    path: "**/build/paparazzi/failures/"
```

## Best Practices

1. **Test States, Not Interactions**: Paparazzi is for visual testing, not interaction testing
2. **Use Meaningful Names**: Test names should describe the state being tested
3. **Test Edge Cases**: Include loading, error, and empty states
4. **Keep Tests Fast**: Avoid animations and delays in screenshot tests
5. **Version Control**: Commit baseline images to track visual changes

## Advantages

- **No Emulator Required**: Tests run on JVM, much faster than instrumented tests
- **Deterministic**: Same output every time, no flakiness
- **Visual Regression**: Catch unintended UI changes
- **Works with Compose**: Full support for Compose UI

## Limitations

- **Android Only**: Doesn't work for iOS or Web targets
- **Static Only**: Can't test animations or interactions
- **Compose Desktop**: Use different tools for desktop screenshot testing

## Alternative for Multiplatform

For true multiplatform screenshot testing, consider:
- **Compose Multiplatform Test**: Built-in testing support
- **Maestro**: Cross-platform UI testing
- **Manual Visual QA**: For web/desktop targets

## Integration with Coverage

Screenshot tests are **not included** in code coverage metrics as they test UI rendering, not business logic. This aligns with our architecture where:

- **Presenters**: 80% coverage target (business logic)
- **UI Components**: Visual testing via Paparazzi (not in coverage)
- **Total Coverage**: Focuses on testable business logic only