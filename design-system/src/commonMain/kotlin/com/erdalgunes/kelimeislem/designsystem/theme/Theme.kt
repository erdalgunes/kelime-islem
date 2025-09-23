/*
 * Turkish Game Show Design System - Main Theme
 * Material Design 3 theme implementation for "Bir Kelime Bir İşlem"
 * Supports dynamic theming, Turkish localization, and accessibility
 */

package com.erdalgunes.kelimeislem.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Local composition providers for game-specific theme elements
 */
val LocalGameShowColors = staticCompositionLocalOf { GameShowColors }
val LocalTurkishTypography = staticCompositionLocalOf { GameTypography }
val LocalAccessibilityMode = staticCompositionLocalOf { false }

/**
 * Theme configuration data class
 */
data class GameShowThemeConfig(
    val useDynamicColors: Boolean = true,
    val useHighContrast: Boolean = false,
    val useAccessibilityEnhancements: Boolean = false,
    val turkishOptimizations: Boolean = true
)

/**
 * Main theme composable for Turkish Game Show app
 * 
 * @param darkTheme Whether to use dark theme (follows system by default)
 * @param config Theme configuration options
 * @param content The content to be themed
 */
@Composable
fun TurkishGameShowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    config: GameShowThemeConfig = GameShowThemeConfig(),
    content: @Composable () -> Unit
) {
    // Determine color scheme based on configuration
    val colorScheme = if (darkTheme) {
        DarkTurkishGameShowColors
    } else {
        LightTurkishGameShowColors
    }
    
    // Apply high contrast adjustments if enabled
    val finalColorScheme = if (config.useHighContrast) {
        enhanceContrastForAccessibility(colorScheme, darkTheme)
    } else {
        colorScheme
    }
    
    // Select typography based on accessibility settings
    val typography = if (config.useAccessibilityEnhancements) {
        enhancedAccessibilityTypography()
    } else {
        TurkishGameShowTypography
    }
    
    // Provide game-specific theme elements
    CompositionLocalProvider(
        LocalGameShowColors provides GameShowColors,
        LocalTurkishTypography provides GameTypography,
        LocalAccessibilityMode provides config.useAccessibilityEnhancements
    ) {
        MaterialTheme(
            colorScheme = finalColorScheme,
            typography = typography,
            content = content
        )
    }
}

/**
 * Lightweight theme for components that don't need full game show styling
 */
@Composable
fun TurkishGameShowComponentTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    TurkishGameShowTheme(
        darkTheme = darkTheme,
        config = GameShowThemeConfig(
            useDynamicColors = false,
            useHighContrast = false,
            useAccessibilityEnhancements = false
        ),
        content = content
    )
}

/**
 * High accessibility theme variant
 */
@Composable
fun AccessibleTurkishGameShowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    TurkishGameShowTheme(
        darkTheme = darkTheme,
        config = GameShowThemeConfig(
            useDynamicColors = false,
            useHighContrast = true,
            useAccessibilityEnhancements = true,
            turkishOptimizations = true
        ),
        content = content
    )
}

/**
 * Enhance color scheme contrast for accessibility
 */
@Composable
private fun enhanceContrastForAccessibility(
    baseScheme: androidx.compose.material3.ColorScheme,
    darkTheme: Boolean
): androidx.compose.material3.ColorScheme {
    return if (darkTheme) {
        baseScheme.copy(
            surface = Color.Black,
            background = Color.Black,
            onSurface = Color.White,
            onBackground = Color.White,
            primary = TurkishRed90,
            onPrimary = TurkishRed10,
            secondary = StudioCyan90,
            onSecondary = StudioCyan10
        )
    } else {
        baseScheme.copy(
            surface = Color.White,
            background = Color.White,
            onSurface = Color.Black,
            onBackground = Color.Black,
            primary = TurkishRed30,
            onPrimary = Color.White,
            secondary = StudioCyan30,
            onSecondary = Color.White
        )
    }
}

/**
 * Enhanced typography for accessibility
 */
private fun enhancedAccessibilityTypography(): androidx.compose.material3.Typography {
    return TurkishGameShowTypography.copy(
        displayLarge = AccessibleGameTypography.HighContrastWordDisplay,
        headlineLarge = AccessibleGameTypography.HighContrastScore,
        headlineMedium = AccessibleGameTypography.HighContrastTimer,
        bodyLarge = AccessibleGameTypography.AccessibleBodyLarge,
        labelLarge = AccessibleGameTypography.AccessibleLabelLarge
    )
}

/**
 * Check if dynamic theming is supported (Android 12+)
 */
fun supportsDynamicTheming() = false // Dynamic theming not supported in commonMain

/**
 * Extension functions for accessing game-specific theme elements
 */
object TurkishGameShowThemeExtensions {
    
    /**
     * Get current game show colors
     */
    val gameShowColors: GameShowColors
        @Composable get() = LocalGameShowColors.current
    
    /**
     * Get current Turkish typography
     */
    val turkishTypography: GameTypography
        @Composable get() = LocalTurkishTypography.current
    
    /**
     * Check if accessibility mode is enabled
     */
    val isAccessibilityMode: Boolean
        @Composable get() = LocalAccessibilityMode.current
}

/**
 * Preview themes for development and testing
 */
object PreviewThemes {
    
    @Composable
    fun LightPreview(content: @Composable () -> Unit) {
        TurkishGameShowTheme(
            darkTheme = false,
            config = GameShowThemeConfig(useDynamicColors = false),
            content = content
        )
    }
    
    @Composable
    fun DarkPreview(content: @Composable () -> Unit) {
        TurkishGameShowTheme(
            darkTheme = true,
            config = GameShowThemeConfig(useDynamicColors = false),
            content = content
        )
    }
    
    @Composable
    fun AccessibilityPreview(content: @Composable () -> Unit) {
        AccessibleTurkishGameShowTheme(
            darkTheme = false,
            content = content
        )
    }
}

/**
 * Semantic color tokens for specific game show contexts
 */
object GameShowSemanticColors {
    
    /**
     * Colors for correct answers and positive feedback
     */
    @Composable
    fun correct() = GameShowColors.CorrectAnswer
    
    @Composable
    fun correctContainer() = GameShowColors.CorrectAnswerContainer
    
    @Composable
    fun onCorrect() = GameShowColors.OnCorrectAnswer
    
    @Composable
    fun onCorrectContainer() = GameShowColors.OnCorrectAnswerContainer
    
    /**
     * Colors for incorrect answers and negative feedback
     */
    @Composable
    fun incorrect() = GameShowColors.IncorrectAnswer
    
    @Composable
    fun incorrectContainer() = GameShowColors.IncorrectAnswerContainer
    
    @Composable
    fun onIncorrect() = GameShowColors.OnIncorrectAnswer
    
    @Composable
    fun onIncorrectContainer() = GameShowColors.OnIncorrectAnswerContainer
    
    /**
     * Timer state colors
     */
    @Composable
    fun timerNormal() = GameShowColors.TimerNormal
    
    @Composable
    fun timerWarning() = GameShowColors.TimerWarning
    
    @Composable
    fun timerCritical() = GameShowColors.TimerCritical
    
    /**
     * Buzzer state colors
     */
    @Composable
    fun buzzerActive() = GameShowColors.BuzzerActive
    
    @Composable
    fun buzzerDisabled() = GameShowColors.BuzzerDisabled
    
    @Composable
    fun buzzerPressed() = GameShowColors.BuzzerPressed
}