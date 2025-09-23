/*
 * Turkish Game Show Design System - Spacing Tokens
 * Material Design 3 spacing system with game show specific tokens
 */

package com.erdalgunes.kelimeislem.designsystem.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Material Design 3 spacing scale
 * Based on 4dp grid system with game show adaptations
 */
object Spacing {
    
    // Base spacing units
    val None: Dp = 0.dp
    val ExtraSmall: Dp = 4.dp
    val Small: Dp = 8.dp
    val Medium: Dp = 16.dp
    val Large: Dp = 24.dp
    val ExtraLarge: Dp = 32.dp
    val ExtraExtraLarge: Dp = 40.dp
    val Huge: Dp = 48.dp
    val ExtraHuge: Dp = 64.dp
    val Massive: Dp = 80.dp
    
    // Component-specific spacing
    object Component {
        // Button spacing
        val ButtonPaddingHorizontal: Dp = 24.dp
        val ButtonPaddingVertical: Dp = 12.dp
        val ButtonSpacing: Dp = 16.dp
        
        // Card spacing
        val CardPadding: Dp = 16.dp
        val CardSpacing: Dp = 12.dp
        val CardElevatedPadding: Dp = 20.dp
        
        // List spacing
        val ListItemPadding: Dp = 16.dp
        val ListItemSpacing: Dp = 8.dp
        
        // Form spacing
        val FormFieldSpacing: Dp = 16.dp
        val FormSectionSpacing: Dp = 24.dp
        
        // Navigation spacing
        val BottomNavPadding: Dp = 8.dp
        val TopAppBarPadding: Dp = 16.dp
    }
    
    // Screen layout spacing
    object Screen {
        val HorizontalPadding: Dp = 16.dp
        val VerticalPadding: Dp = 16.dp
        val ContentSpacing: Dp = 24.dp
        val SectionSpacing: Dp = 32.dp
    }
}

/**
 * Game show specific spacing tokens
 * Designed for TV-like layouts and prominence
 */
object GameShowSpacing {
    
    // Word display spacing
    val WordDisplayPadding: Dp = 32.dp
    val WordDisplayMargin: Dp = 24.dp
    val WordLetterSpacing: Dp = 8.dp
    
    // Score display spacing - for future ScoreDisplay component
    val ScoreDisplayPadding: Dp = 20.dp
    val ScoreDisplaySpacing: Dp = 16.dp
    val ScoreNumberSpacing: Dp = 12.dp
    
    // Timer spacing
    val TimerDisplayPadding: Dp = 16.dp
    
    // Game board spacing
    val GameBoardPadding: Dp = 24.dp
    val GameBoardSpacing: Dp = 20.dp
    val GameElementSpacing: Dp = 16.dp
    
    // Result feedback spacing
    val ResultPadding: Dp = 24.dp
    val ResultSpacing: Dp = 16.dp
    val ResultIconSpacing: Dp = 12.dp
    
    // Player area spacing
    val PlayerCardPadding: Dp = 16.dp
    val PlayerCardSpacing: Dp = 12.dp
    val PlayerAvatarSpacing: Dp = 8.dp
    
    // Stage/Studio spacing (for large screen layouts)
    val StudioMargin: Dp = 48.dp
    val StagePadding: Dp = 40.dp
    val SpotlightMargin: Dp = 32.dp
}

/**
 * Responsive spacing that adapts to screen size
 */
object ResponsiveSpacing {
    
    // Phone spacing (default)
    object Phone {
        val ScreenPadding: Dp = 16.dp
        val ContentSpacing: Dp = 16.dp
        val ComponentSpacing: Dp = 12.dp
    }
    
    // Tablet spacing
    object Tablet {
        val ScreenPadding: Dp = 24.dp
        val ContentSpacing: Dp = 24.dp
        val ComponentSpacing: Dp = 16.dp
    }
    
    // Desktop spacing
    object Desktop {
        val ScreenPadding: Dp = 32.dp
        val ContentSpacing: Dp = 32.dp
        val ComponentSpacing: Dp = 20.dp
    }
    
    // TV/Large display spacing
    object TV {
        val ScreenPadding: Dp = 48.dp
        val ContentSpacing: Dp = 40.dp
        val ComponentSpacing: Dp = 24.dp
    }
}

/**
 * Accessibility spacing tokens
 * Enhanced touch targets and spacing for accessibility
 */
object AccessibilitySpacing {
    
    // Minimum touch target sizes (44dp minimum for accessibility)
    val MinTouchTarget: Dp = 44.dp
    val RecommendedTouchTarget: Dp = 48.dp
    val LargeTouchTarget: Dp = 56.dp
    
    // Enhanced spacing for accessibility
    val AccessibleButtonSpacing: Dp = 24.dp
    val AccessibleContentSpacing: Dp = 24.dp
    val AccessibleSectionSpacing: Dp = 32.dp
    
    // Focus indicator spacing
    val FocusIndicatorPadding: Dp = 4.dp
    val FocusIndicatorStroke: Dp = 2.dp
}

/**
 * Animation and motion spacing
 * Spacing used in transitions and animations
 */
object MotionSpacing {
    
    // Slide distances
    val SlideSmall: Dp = 16.dp
    val SlideMedium: Dp = 32.dp
    val SlideLarge: Dp = 64.dp
    
    // Parallax offsets
    val ParallaxSmall: Dp = 8.dp
    val ParallaxMedium: Dp = 16.dp
    val ParallaxLarge: Dp = 24.dp
    
    // Expansion spacing
    val ExpansionSmall: Dp = 4.dp
    val ExpansionMedium: Dp = 8.dp
    val ExpansionLarge: Dp = 12.dp
}

/**
 * Grid system spacing
 * For complex layouts and alignment
 */
object GridSpacing {
    
    // Grid base unit (4dp)
    val BaseUnit: Dp = 4.dp
    
    // Common grid multiples
    val Grid1: Dp = 4.dp   // 1x
    val Grid2: Dp = 8.dp   // 2x
    val Grid3: Dp = 12.dp  // 3x
    val Grid4: Dp = 16.dp  // 4x
    val Grid5: Dp = 20.dp  // 5x
    val Grid6: Dp = 24.dp  // 6x
    val Grid7: Dp = 28.dp  // 7x
    val Grid8: Dp = 32.dp  // 8x
    val Grid10: Dp = 40.dp // 10x
    val Grid12: Dp = 48.dp // 12x
    val Grid16: Dp = 64.dp // 16x
    val Grid20: Dp = 80.dp // 20x
    
    // Column spacing for multi-column layouts
    val ColumnSpacing: Dp = 16.dp
    val ColumnSpacingTablet: Dp = 24.dp
    val ColumnSpacingDesktop: Dp = 32.dp
}

/**
 * Utility functions for spacing calculations
 */
object SpacingUtils {
    
    /**
     * Calculate responsive spacing based on screen width category
     */
    fun getScreenPadding(isTablet: Boolean = false, isDesktop: Boolean = false): Dp {
        return when {
            isDesktop -> ResponsiveSpacing.Desktop.ScreenPadding
            isTablet -> ResponsiveSpacing.Tablet.ScreenPadding
            else -> ResponsiveSpacing.Phone.ScreenPadding
        }
    }
    
    /**
     * Calculate spacing for game show elements based on component type
     */
    fun getGameElementSpacing(elementType: GameElementType): Dp {
        return when (elementType) {
            GameElementType.WORD_DISPLAY -> GameShowSpacing.WordDisplayPadding
            GameElementType.SCORE_CARD -> GameShowSpacing.ScoreDisplayPadding
            GameElementType.TIMER -> GameShowSpacing.TimerDisplayPadding
            GameElementType.RESULT -> GameShowSpacing.ResultPadding
            GameElementType.PLAYER_CARD -> GameShowSpacing.PlayerCardPadding
        }
    }
}

/**
 * Game element types for spacing calculations
 */
enum class GameElementType {
    WORD_DISPLAY,
    SCORE_CARD,
    TIMER,
    RESULT,
    PLAYER_CARD
}