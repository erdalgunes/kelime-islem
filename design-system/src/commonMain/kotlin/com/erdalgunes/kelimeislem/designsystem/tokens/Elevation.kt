/*
 * Turkish Game Show Design System - Elevation Tokens
 * Material Design 3 elevation system with game show specific depth and shadows
 */

package com.erdalgunes.kelimeislem.designsystem.tokens

import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Material Design 3 elevation levels
 * Enhanced for game show dramatic lighting effects
 */
object Elevation {
    
    // Standard Material 3 elevation levels
    val Level0: Dp = 0.dp    // Surface level
    val Level1: Dp = 1.dp    // Subtle elevation
    val Level2: Dp = 3.dp    // Card elevation
    val Level3: Dp = 6.dp    // FAB elevation
    val Level4: Dp = 8.dp    // Navigation drawer
    val Level5: Dp = 12.dp   // Modal surfaces
    
    // Game show specific elevation levels
    val GameShowSurface: Dp = 0.dp      // Base game surface
    val GameShowCard: Dp = 4.dp         // Score cards, player cards
    val GameShowElement: Dp = 8.dp      // Interactive elements
    val GameShowFocused: Dp = 12.dp     // Focused/selected elements
    val GameShowFloating: Dp = 16.dp    // Floating action elements
    val GameShowModal: Dp = 24.dp       // Modals and overlays
    val GameShowSpotlight: Dp = 32.dp   // Main game elements under spotlight
}

/**
 * Shadow colors for different elevation contexts
 */
object ShadowColors {
    
    // Light theme shadows
    val LightAmbient = Color.Black.copy(alpha = 0.04f)
    val LightKey = Color.Black.copy(alpha = 0.08f)
    
    // Dark theme shadows
    val DarkAmbient = Color.Black.copy(alpha = 0.08f)
    val DarkKey = Color.Black.copy(alpha = 0.16f)
    
    // Game show specific shadows
    val StudioLightShadow = Color.Black.copy(alpha = 0.12f)
    val SpotlightShadow = Color.Black.copy(alpha = 0.20f)
    val DramaticShadow = Color.Black.copy(alpha = 0.30f)
    
    // Colored shadows for special effects
    val RedGlow = Color(0xFFFF1744).copy(alpha = 0.25f)
    val CyanGlow = Color(0xFF00E5FF).copy(alpha = 0.20f)
    val GoldGlow = Color(0xFFFFC107).copy(alpha = 0.15f)
}

/**
 * Game show specific elevation configurations
 */
object GameShowElevation {
    
    /**
     * Word display elevation - prominent but not floating
     */
    val WordDisplay = Elevation.GameShowCard
    val WordDisplayShadow = ShadowColors.StudioLightShadow
    
    /**
     * Score card elevation - elevated for importance
     */
    val ScoreCard = Elevation.GameShowElement
    val ScoreCardShadow = ShadowColors.SpotlightShadow
    
    /**
     * Timer elevation - floating above other elements
     */
    val Timer = Elevation.GameShowFloating
    val TimerShadow = ShadowColors.DramaticShadow
    
    /**
     * Buzzer elevation - prominent interactive element
     */
    val Buzzer = Elevation.GameShowFocused
    val BuzzerShadow = ShadowColors.StudioLightShadow
    val BuzzerPressed = Elevation.Level2  // Reduced when pressed
    val BuzzerPressedShadow = ShadowColors.LightKey
    
    /**
     * Player card elevation - moderate prominence
     */
    val PlayerCard = Elevation.GameShowCard
    val PlayerCardShadow = ShadowColors.LightAmbient
    val PlayerCardSelected = Elevation.GameShowElement  // Higher when selected
    val PlayerCardSelectedShadow = ShadowColors.StudioLightShadow
    
    /**
     * Result display elevation - maximum prominence
     */
    val ResultDisplay = Elevation.GameShowSpotlight
    val ResultDisplayShadow = ShadowColors.DramaticShadow
    
    /**
     * Game board elevation - base level
     */
    val GameBoard = Elevation.GameShowSurface
    val GameBoardShadow = Color.Transparent
}

/**
 * Interactive elevation states
 */
object InteractiveElevation {
    
    /**
     * Button elevation states
     */
    object Button {
        val Rest = Elevation.Level1
        val Hover = Elevation.Level2
        val Pressed = Elevation.Level0
        val Focused = Elevation.Level1
        val Disabled = Elevation.Level0
    }
    
    /**
     * Card elevation states
     */
    object Card {
        val Rest = Elevation.Level1
        val Hover = Elevation.Level2
        val Pressed = Elevation.Level0
        val Focused = Elevation.Level1
        val Selected = Elevation.Level3
    }
    
    /**
     * Game element elevation states
     */
    object GameElement {
        val Rest = GameShowElevation.WordDisplay
        val Hover = Elevation.GameShowElement
        val Active = Elevation.GameShowFocused
        val Selected = Elevation.GameShowFloating
        val Disabled = Elevation.Level0
    }
}

/**
 * Animated elevation configurations
 */
object AnimatedElevation {
    
    /**
     * Elevation animation durations
     */
    const val FastElevationChange = 100L
    const val StandardElevationChange = 200L
    const val SlowElevationChange = 300L
    
    /**
     * Elevation spring configurations
     */
    const val ElevationStiffness = 400f
    const val ElevationDamping = 0.8f
    
    /**
     * Game show elevation animations
     */
    object GameShow {
        const val BuzzerPressDuration = 100L
        const val ScoreUpdateDuration = 200L
        const val ResultRevealDuration = 400L
        const val TimerPulseDuration = 300L
    }
}

/**
 * Platform-specific elevation adjustments
 */
object PlatformElevation {
    
    /**
     * Android specific elevation
     */
    object Android {
        val AppBarElevation = Elevation.Level2
        val BottomSheetElevation = Elevation.Level5
        val DialogElevation = Elevation.Level5
        val MenuElevation = Elevation.Level3
    }
    
    /**
     * iOS specific elevation (using shadows instead of true elevation)
     */
    object iOS {
        val CardShadowRadius = 8.dp
        val FloatingShadowRadius = 16.dp
        val ModalShadowRadius = 24.dp
    }
    
    /**
     * Desktop specific elevation
     */
    object Desktop {
        val WindowElevation = Elevation.Level5
        val TooltipElevation = Elevation.Level3
        val DropdownElevation = Elevation.Level4
    }
}

/**
 * Accessibility elevation considerations
 */
object AccessibilityElevation {
    
    /**
     * High contrast elevation adjustments
     */
    val HighContrastMultiplier = 1.5f
    
    /**
     * Minimum elevation for touch targets
     */
    val MinimumTouchTargetElevation = Elevation.Level1
    
    /**
     * Focus indicator elevation
     */
    val FocusIndicatorElevation = Elevation.Level2
    
    /**
     * Apply high contrast elevation
     */
    fun highContrastElevation(baseElevation: Dp): Dp {
        return baseElevation * HighContrastMultiplier
    }
}

/**
 * Elevation utilities for game show components
 */
object ElevationUtils {
    
    /**
     * Get elevation based on component importance
     */
    fun getGameElementElevation(importance: ComponentImportance): Dp {
        return when (importance) {
            ComponentImportance.PRIMARY -> Elevation.GameShowSpotlight
            ComponentImportance.SECONDARY -> Elevation.GameShowFloating
            ComponentImportance.TERTIARY -> Elevation.GameShowElement
            ComponentImportance.BACKGROUND -> Elevation.GameShowCard
            ComponentImportance.SURFACE -> Elevation.GameShowSurface
        }
    }
    
    /**
     * Get shadow color based on theme and elevation
     */
    fun getShadowColor(isDarkTheme: Boolean, elevation: Dp): Color {
        return when {
            elevation >= Elevation.Level5 -> {
                if (isDarkTheme) ShadowColors.DarkKey else ShadowColors.SpotlightShadow
            }
            elevation >= Elevation.Level3 -> {
                if (isDarkTheme) ShadowColors.DarkAmbient else ShadowColors.StudioLightShadow
            }
            elevation >= Elevation.Level1 -> {
                if (isDarkTheme) ShadowColors.DarkAmbient else ShadowColors.LightKey
            }
            else -> Color.Transparent
        }
    }
    
    /**
     * Create card elevation with game show styling
     */
    fun gameShowCardElevation(
        defaultElevation: Dp = GameShowElevation.ScoreCard,
        pressedElevation: Dp = Elevation.Level1,
        focusedElevation: Dp = Elevation.GameShowElement,
        hoveredElevation: Dp = Elevation.GameShowFocused
    ) = CardDefaults.cardElevation(
        defaultElevation = defaultElevation,
        pressedElevation = pressedElevation,
        focusedElevation = focusedElevation,
        hoveredElevation = hoveredElevation
    )
}

/**
 * Component importance levels for elevation calculation
 */
enum class ComponentImportance {
    PRIMARY,    // Main game elements (word display, timer)
    SECONDARY,  // Important interactive elements (buzzer, score)
    TERTIARY,   // Supporting elements (player cards, status)
    BACKGROUND, // Background cards and containers
    SURFACE     // Base surfaces and layouts
}

/**
 * Elevation context for different game states
 */
enum class GameContext {
    MENU,           // Main menu and navigation
    GAME_SETUP,     // Game configuration
    GAME_ACTIVE,    // Active gameplay
    GAME_PAUSED,    // Game paused state
    GAME_RESULTS,   // Results and scores
    SETTINGS        // Settings and preferences
}

/**
 * Context-aware elevation provider
 */
object ContextualElevation {
    
    /**
     * Get appropriate elevation based on game context
     */
    fun getContextElevation(context: GameContext, baseElevation: Dp): Dp {
        val multiplier = when (context) {
            GameContext.MENU -> 0.8f
            GameContext.GAME_SETUP -> 1.0f
            GameContext.GAME_ACTIVE -> 1.2f
            GameContext.GAME_PAUSED -> 0.9f
            GameContext.GAME_RESULTS -> 1.5f
            GameContext.SETTINGS -> 1.0f
        }
        
        return baseElevation * multiplier
    }
}