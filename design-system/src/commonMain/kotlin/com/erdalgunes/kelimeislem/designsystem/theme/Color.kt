/*
 * Turkish Game Show Design System - Color Palette
 * Inspired by "Bir Kelime Bir İşlem" TV show aesthetics
 * Material Design 3 compliant with Turkish cultural elements
 */

package com.erdalgunes.kelimeislem.designsystem.theme

import androidx.compose.ui.graphics.Color

/**
 * Turkish Game Show Color Palette
 * Based on Turkish flag colors (red, white) with TV studio lighting effects
 */

// Primary Colors - Turkish Red Theme
internal val TurkishRed10 = Color(0xFF3C0000)
internal val TurkishRed20 = Color(0xFF5D0000)
internal val TurkishRed30 = Color(0xFF860000)
internal val TurkishRed40 = Color(0xFFB71C1C)
internal val TurkishRed50 = Color(0xFFD32F2F)
internal val TurkishRed60 = Color(0xFFE53935)
internal val TurkishRed70 = Color(0xFFEF5350)
internal val TurkishRed80 = Color(0xFFEF9A9A)
internal val TurkishRed90 = Color(0xFFFFCDD2)
internal val TurkishRed95 = Color(0xFFFFE8E8)
internal val TurkishRed99 = Color(0xFFFFFBFF)

// Secondary Colors - Studio Cyan Theme  
internal val StudioCyan10 = Color(0xFF001F26)
internal val StudioCyan20 = Color(0xFF003544)
internal val StudioCyan30 = Color(0xFF004D61)
internal val StudioCyan40 = Color(0xFF006780)
internal val StudioCyan50 = Color(0xFF0288D1)
internal val StudioCyan60 = Color(0xFF29B6F6)
internal val StudioCyan70 = Color(0xFF4FC3F7)
internal val StudioCyan80 = Color(0xFF81D4FA)
internal val StudioCyan90 = Color(0xFFB3E5FC)
internal val StudioCyan95 = Color(0xFFE1F5FE)
internal val StudioCyan99 = Color(0xFFFFFBFF)

// Tertiary Colors - Accent Gold/Amber Theme
internal val AccentGold10 = Color(0xFF2A1D00)
internal val AccentGold20 = Color(0xFF453200)
internal val AccentGold30 = Color(0xFF624800)
internal val AccentGold40 = Color(0xFF805E00)
internal val AccentGold50 = Color(0xFFF57C00)
internal val AccentGold60 = Color(0xFFFF9800)
internal val AccentGold70 = Color(0xFFFFB74D)
internal val AccentGold80 = Color(0xFFFFCC02)
internal val AccentGold90 = Color(0xFFFFE0B2)
internal val AccentGold95 = Color(0xFFFFF3E0)
internal val AccentGold99 = Color(0xFFFFFBFF)

// Neutral Colors - TV Studio Grays
internal val StudioGray10 = Color(0xFF0A0A0A)
internal val StudioGray20 = Color(0xFF111111)
internal val StudioGray30 = Color(0xFF1E1E1E)
internal val StudioGray40 = Color(0xFF2C2C2C)
internal val StudioGray50 = Color(0xFF424242)
internal val StudioGray60 = Color(0xFF5F5F5F)
internal val StudioGray70 = Color(0xFF757575)
internal val StudioGray80 = Color(0xFF9E9E9E)
internal val StudioGray90 = Color(0xFFE0E0E0)
internal val StudioGray95 = Color(0xFFF5F5F5)
internal val StudioGray99 = Color(0xFFFFFBFF)

// Error Colors - Alert Red
internal val ErrorRed10 = Color(0xFF410E0B)
internal val ErrorRed20 = Color(0xFF601410)
internal val ErrorRed30 = Color(0xFF8C1D18)
internal val ErrorRed40 = Color(0xFFB3261E)
internal val ErrorRed50 = Color(0xFFDC362E)
internal val ErrorRed60 = Color(0xFFE46962)
internal val ErrorRed70 = Color(0xFFEC928E)
internal val ErrorRed80 = Color(0xFFF2B8B5)
internal val ErrorRed90 = Color(0xFFF9DEDC)
internal val ErrorRed95 = Color(0xFFFCEEEE)
internal val ErrorRed99 = Color(0xFFFFFBFF)

// Success Colors - Turkish Green (complementary to flag red)
internal val SuccessGreen10 = Color(0xFF002204)
internal val SuccessGreen20 = Color(0xFF003909)
internal val SuccessGreen30 = Color(0xFF00530F)
internal val SuccessGreen40 = Color(0xFF006E1C)
internal val SuccessGreen50 = Color(0xFF2E7D32)
internal val SuccessGreen60 = Color(0xFF43A047)
internal val SuccessGreen70 = Color(0xFF66BB6A)
internal val SuccessGreen80 = Color(0xFF81C784)
internal val SuccessGreen90 = Color(0xFFA5D6A7)
internal val SuccessGreen95 = Color(0xFFC8E6C9)
internal val SuccessGreen99 = Color(0xFFF1F8E9)

/**
 * Light theme color scheme for Turkish Game Show
 * High contrast for TV studio visibility
 */
val LightTurkishGameShowColors = androidx.compose.material3.lightColorScheme(
    primary = TurkishRed40,
    onPrimary = Color.White,
    primaryContainer = TurkishRed90,
    onPrimaryContainer = TurkishRed10,
    
    secondary = StudioCyan40,
    onSecondary = Color.White,
    secondaryContainer = StudioCyan90,
    onSecondaryContainer = StudioCyan10,
    
    tertiary = AccentGold40,
    onTertiary = Color.White,
    tertiaryContainer = AccentGold90,
    onTertiaryContainer = AccentGold10,
    
    error = ErrorRed40,
    onError = Color.White,
    errorContainer = ErrorRed90,
    onErrorContainer = ErrorRed10,
    
    surface = StudioGray99,
    onSurface = StudioGray10,
    surfaceVariant = StudioGray95,
    onSurfaceVariant = StudioGray30,
    
    background = StudioGray99,
    onBackground = StudioGray10,
    
    outline = StudioGray60,
    outlineVariant = StudioGray80,
    
    scrim = Color.Black,
    inverseSurface = StudioGray20,
    inverseOnSurface = StudioGray95,
    inversePrimary = TurkishRed80,
    
    surfaceTint = TurkishRed40
)

/**
 * Dark theme color scheme for Turkish Game Show
 * TV studio lighting with dramatic contrast
 */
val DarkTurkishGameShowColors = androidx.compose.material3.darkColorScheme(
    primary = TurkishRed80,
    onPrimary = TurkishRed20,
    primaryContainer = TurkishRed30,
    onPrimaryContainer = TurkishRed90,
    
    secondary = StudioCyan80,
    onSecondary = StudioCyan20,
    secondaryContainer = StudioCyan30,
    onSecondaryContainer = StudioCyan90,
    
    tertiary = AccentGold80,
    onTertiary = AccentGold20,
    tertiaryContainer = AccentGold30,
    onTertiaryContainer = AccentGold90,
    
    error = ErrorRed80,
    onError = ErrorRed20,
    errorContainer = ErrorRed30,
    onErrorContainer = ErrorRed90,
    
    surface = StudioGray10,
    onSurface = StudioGray90,
    surfaceVariant = StudioGray20,
    onSurfaceVariant = StudioGray80,
    
    background = StudioGray10,
    onBackground = StudioGray90,
    
    outline = StudioGray60,
    outlineVariant = StudioGray40,
    
    scrim = Color.Black,
    inverseSurface = StudioGray90,
    inverseOnSurface = StudioGray20,
    inversePrimary = TurkishRed40,
    
    surfaceTint = TurkishRed80
)

/**
 * Custom game-specific colors for special UI elements
 */
object GameShowColors {
    // Correct/Success feedback
    val CorrectAnswer = SuccessGreen50
    val CorrectAnswerContainer = SuccessGreen90
    val OnCorrectAnswer = Color.White
    val OnCorrectAnswerContainer = SuccessGreen10
    
    // Incorrect/Error feedback  
    val IncorrectAnswer = ErrorRed50
    val IncorrectAnswerContainer = ErrorRed90
    val OnIncorrectAnswer = Color.White
    val OnIncorrectAnswerContainer = ErrorRed10
    
    // Timer/Countdown states
    val TimerNormal = StudioCyan50
    val TimerWarning = AccentGold50
    val TimerCritical = TurkishRed50
    
    // Score display
    val ScorePositive = SuccessGreen50
    val ScoreNegative = ErrorRed50
    val ScoreNeutral = StudioGray50
    
    // Buzzer states
    val BuzzerActive = TurkishRed50
    val BuzzerDisabled = StudioGray40
    val BuzzerPressed = TurkishRed70
    
    // Studio lighting effects
    val SpotlightHighlight = Color.White.copy(alpha = 0.9f)
    val StageDim = StudioGray10.copy(alpha = 0.7f)
    val GlowEffect = StudioCyan80.copy(alpha = 0.3f)
}