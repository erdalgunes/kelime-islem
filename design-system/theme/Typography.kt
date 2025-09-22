/*
 * Turkish Game Show Design System - Typography
 * Material Design 3 typography optimized for Turkish language and game show aesthetics
 * Supports proper Turkish locale handling and accessibility
 */

package com.erdalgunes.kelimeislem.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Turkish-aware font families with proper glyph support
 * Ensures all Turkish characters (ç, Ç, ğ, Ğ, ı, İ, ö, Ö, ş, Ş, ü, Ü) render correctly
 */

// Primary font family - Modern sans-serif for game show aesthetics
val GameShowFontFamily = FontFamily.Default // Fallback to system font with Turkish support

// Alternative: If you have custom fonts, use:
// val GameShowFontFamily = FontFamily(
//     Font(R.font.montserrat_regular, FontWeight.Normal),
//     Font(R.font.montserrat_medium, FontWeight.Medium),
//     Font(R.font.montserrat_semibold, FontWeight.SemiBold),
//     Font(R.font.montserrat_bold, FontWeight.Bold),
//     Font(R.font.montserrat_extrabold, FontWeight.ExtraBold)
// )

// Display font family - Bold, attention-grabbing for game elements
val DisplayFontFamily = FontFamily.Default // Using system font as fallback

// Monospace font family - For scores, timers, and numerical displays
val MonospaceFontFamily = FontFamily.Monospace

/**
 * Material 3 Typography optimized for Turkish Game Show
 * Scales designed for TV-like visibility and Turkish text requirements
 */
val TurkishGameShowTypography = Typography(
    // Display styles - Large, prominent text for game show elements
    displayLarge = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 64.sp,
        lineHeight = 72.sp,
        letterSpacing = (-0.25).sp
    ),
    displayMedium = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.sp
    ),
    displaySmall = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp
    ),
    
    // Headline styles - Section headers and prominent UI text
    headlineLarge = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp
    ),
    
    // Title styles - Card titles, dialog headers
    titleLarge = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    ),
    titleSmall = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    
    // Body styles - Main content text
    bodyLarge = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    ),
    
    // Label styles - Buttons, form labels, captions
    labelLarge = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * Game-specific typography styles for special UI elements
 */
object GameTypography {
    
    // Word display - Large, prominent word presentation
    val WordDisplay = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 72.sp,
        lineHeight = 80.sp,
        letterSpacing = 0.sp
    )
    
    // Score display - Monospace for consistent number alignment
    val ScoreDisplay = TextStyle(
        fontFamily = MonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        lineHeight = 56.sp,
        letterSpacing = 0.sp
    )
    
    // Timer display - Monospace for countdown consistency
    val TimerDisplay = TextStyle(
        fontFamily = MonospaceFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    )
    
    // Buzzer button - All caps, heavy weight for impact
    val BuzzerText = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        letterSpacing = 1.5.sp
    )
    
    // Result feedback - Bold announcement style
    val ResultFeedback = TextStyle(
        fontFamily = DisplayFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    )
    
    // Player name - Prominent but readable
    val PlayerName = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp
    )
    
    // Game instruction - Clear, readable instructions
    val GameInstruction = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    
    // Category label - Small caps style for game categories
    val CategoryLabel = TextStyle(
        fontFamily = GameShowFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 1.25.sp
    )
}

/**
 * Turkish language-specific typography utilities
 */
object TurkishTypographyUtils {
    
    /**
     * Line height multipliers for Turkish text
     * Turkish characters may require slightly more vertical space
     */
    const val TURKISH_LINE_HEIGHT_MULTIPLIER = 1.2f
    
    /**
     * Recommended letter spacing for Turkish text readability
     */
    val TURKISH_LETTER_SPACING = 0.1.sp
    
    /**
     * Typography styles optimized for Turkish character rendering
     */
    fun turkishOptimizedStyle(baseStyle: TextStyle): TextStyle {
        return baseStyle.copy(
            lineHeight = baseStyle.lineHeight * TURKISH_LINE_HEIGHT_MULTIPLIER,
            letterSpacing = TURKISH_LETTER_SPACING
        )
    }
}

/**
 * Accessibility-enhanced typography for game show context
 */
object AccessibleGameTypography {
    
    // High contrast styles for accessibility
    val HighContrastWordDisplay = GameTypography.WordDisplay.copy(
        fontSize = 80.sp,
        lineHeight = 88.sp,
        fontWeight = FontWeight.Black
    )
    
    val HighContrastScore = GameTypography.ScoreDisplay.copy(
        fontSize = 56.sp,
        lineHeight = 64.sp
    )
    
    val HighContrastTimer = GameTypography.TimerDisplay.copy(
        fontSize = 40.sp,
        lineHeight = 48.sp
    )
    
    // Large text styles for accessibility compliance
    val AccessibleBodyLarge = TurkishGameShowTypography.bodyLarge.copy(
        fontSize = 18.sp,
        lineHeight = 28.sp
    )
    
    val AccessibleLabelLarge = TurkishGameShowTypography.labelLarge.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
}