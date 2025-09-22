/*
 * Turkish Game Show Design System - Motion Tokens
 * Material Design 3 motion system with game show specific animations
 */

package com.erdalgunes.kelimeislem.designsystem.tokens

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing

/**
 * Material Design 3 motion duration tokens
 * Optimized for game show timing and user engagement
 */
object Duration {
    
    // Standard Material 3 durations
    const val Short1 = 50L    // Micro-interactions
    const val Short2 = 100L   // Small component changes
    const val Short3 = 150L   // Simple transitions
    const val Short4 = 200L   // Standard transitions
    const val Medium1 = 250L  // Entering/exiting elements
    const val Medium2 = 300L  // Page transitions
    const val Medium3 = 350L  // Large element changes
    const val Medium4 = 400L  // Complex transitions
    const val Long1 = 450L    // Large-scale changes
    const val Long2 = 500L    // Layout changes
    const val Long3 = 550L    // Elaborate transitions
    const val Long4 = 600L    // Complex choreography
    
    // Game show specific durations
    const val GameShowFast = 150L      // Quick feedback
    const val GameShowStandard = 300L  // Normal game transitions
    const val GameShowSlow = 500L      // Dramatic reveals
    const val GameShowDramatic = 800L  // Big moments
    const val GameShowEpic = 1000L     // Major transitions
    
    // Timer and countdown durations
    const val TimerTick = 100L         // Timer updates
    const val CountdownSecond = 1000L  // Countdown intervals
    const val FinalCountdown = 200L    // Last seconds urgency
    
    // Result and feedback durations
    const val CorrectAnswer = 600L     // Positive feedback
    const val IncorrectAnswer = 400L   // Negative feedback
    const val ScoreUpdate = 300L       // Score changes
    const val ResultReveal = 800L      // Result presentations
    
    // Buzzer and interaction durations
    const val BuzzerPress = 100L       // Buzzer response
    const val BuzzerActivate = 200L    // Buzzer activation
    const val HapticFeedback = 50L     // Haptic timing
    
    // Entrance and exit durations
    const val EnterScreen = 400L       // Screen entry
    const val ExitScreen = 300L        // Screen exit
    const val EnterElement = 250L      // Element entry
    const val ExitElement = 200L       // Element exit
}

/**
 * Material Design 3 easing curves
 * Enhanced with game show specific easing functions
 */
object GameShowEasing {
    
    // Standard Material 3 easing
    val Standard = FastOutSlowInEasing
    val Decelerate = LinearOutSlowInEasing
    val Accelerate = CubicBezierEasing(0.4f, 0.0f, 1f, 1f)
    val Linear = LinearEasing
    
    // Emphasized easing for dramatic effects
    val Emphasized = CubicBezierEasing(0.2f, 0.0f, 0f, 1f)
    val EmphasizedDecelerate = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
    val EmphasizedAccelerate = CubicBezierEasing(0.3f, 0f, 0.8f, 0.15f)
    
    // Game show specific easing
    val GameShowBounce = CubicBezierEasing(0.68f, -0.6f, 0.32f, 1.6f)
    val GameShowSnap = CubicBezierEasing(0.25f, 0.46f, 0.45f, 0.94f)
    val GameShowSlide = CubicBezierEasing(0.16f, 1f, 0.3f, 1f)
    val GameShowReveal = CubicBezierEasing(0f, 0f, 0.2f, 1f)
    
    // Timer specific easing
    val TimerLinear = LinearEasing
    val TimerAccelerate = CubicBezierEasing(0.4f, 0f, 1f, 1f)
    val TimerPulse = CubicBezierEasing(0.4f, 0f, 0.6f, 1f)
    
    // Result feedback easing
    val CorrectBounce = CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f)
    val IncorrectShake = CubicBezierEasing(0.36f, 0f, 0.66f, -0.56f)
    val ScoreCount = CubicBezierEasing(0.25f, 0.46f, 0.45f, 0.94f)
}

/**
 * Motion patterns for specific game show interactions
 */
object MotionPatterns {
    
    /**
     * Word reveal animation pattern
     */
    object WordReveal {
        const val letterDelay = 100L
        const val wordDuration = Duration.GameShowStandard
        val letterEasing = GameShowEasing.GameShowReveal
        val wordEasing = GameShowEasing.Emphasized
    }
    
    /**
     * Score update animation pattern
     */
    object ScoreUpdate {
        const val numberDuration = Duration.ScoreUpdate
        const val glowDuration = Duration.GameShowFast
        val numberEasing = GameShowEasing.ScoreCount
        val glowEasing = GameShowEasing.Standard
    }
    
    /**
     * Timer countdown animation pattern
     */
    object TimerCountdown {
        const val normalDuration = Duration.TimerTick
        const val warningDuration = Duration.FinalCountdown
        const val criticalDuration = Duration.Short2
        val normalEasing = GameShowEasing.TimerLinear
        val warningEasing = GameShowEasing.TimerPulse
        val criticalEasing = GameShowEasing.TimerAccelerate
    }
    
    /**
     * Buzzer press animation pattern
     */
    object BuzzerPress {
        const val pressDuration = Duration.BuzzerPress
        const val releaseDuration = Duration.Short3
        const val activateDuration = Duration.BuzzerActivate
        val pressEasing = GameShowEasing.GameShowSnap
        val releaseEasing = GameShowEasing.Standard
        val activateEasing = GameShowEasing.GameShowBounce
    }
    
    /**
     * Result feedback animation pattern
     */
    object ResultFeedback {
        const val correctDuration = Duration.CorrectAnswer
        const val incorrectDuration = Duration.IncorrectAnswer
        const val revealDuration = Duration.ResultReveal
        val correctEasing = GameShowEasing.CorrectBounce
        val incorrectEasing = GameShowEasing.IncorrectShake
        val revealEasing = GameShowEasing.GameShowReveal
    }
    
    /**
     * Screen transition animation pattern
     */
    object ScreenTransition {
        const val enterDuration = Duration.EnterScreen
        const val exitDuration = Duration.ExitScreen
        val enterEasing = GameShowEasing.EmphasizedDecelerate
        val exitEasing = GameShowEasing.EmphasizedAccelerate
    }
}

/**
 * Spring configurations for natural motion
 */
object SpringConfig {
    
    // Stiffness values
    const val StiffnessHigh = 1000f
    const val StiffnessMedium = 400f
    const val StiffnessLow = 200f
    const val StiffnessVeryLow = 50f
    
    // Damping values
    const val DampingHigh = 0.9f
    const val DampingMedium = 0.7f
    const val DampingLow = 0.5f
    const val DampingVeryLow = 0.3f
    
    // Game show specific spring configs
    object GameShow {
        const val BuzzerStiffness = StiffnessHigh
        const val BuzzerDamping = DampingMedium
        
        const val ScoreStiffness = StiffnessMedium
        const val ScoreDamping = DampingHigh
        
        const val WordStiffness = StiffnessLow
        const val WordDamping = DampingMedium
        
        const val TimerStiffness = StiffnessVeryLow
        const val TimerDamping = DampingLow
    }
}

/**
 * Keyframe timing for complex animations
 */
object KeyframeTiming {
    
    /**
     * Word letter-by-letter reveal timing
     */
    object LetterReveal {
        const val baseDelay = 80L
        const val accelerationFactor = 0.9f // Each letter appears slightly faster
        
        fun getLetterDelay(index: Int): Long {
            return (baseDelay * Math.pow(accelerationFactor.toDouble(), index.toDouble())).toLong()
        }
    }
    
    /**
     * Score counting animation timing
     */
    object ScoreCount {
        const val digitDuration = 50L
        const val pauseBetweenDigits = 30L
        const val finalPause = 200L
        
        fun getTotalDuration(digitCount: Int): Long {
            return (digitCount * digitDuration) + ((digitCount - 1) * pauseBetweenDigits) + finalPause
        }
    }
    
    /**
     * Timer warning animation timing
     */
    object TimerWarning {
        const val pulseDuration = 600L
        const val pulseCount = 3
        const val finalFlashDuration = 200L
        
        fun getWarningDuration(): Long {
            return (pulseDuration * pulseCount) + finalFlashDuration
        }
    }
}

/**
 * Motion accessibility settings
 */
object AccessibilityMotion {
    
    // Reduced motion durations (50% of normal)
    const val ReducedMotionFactor = 0.5f
    
    // Minimum durations for accessibility
    const val MinimumDuration = 100L
    const val MaximumDuration = 1000L
    
    /**
     * Apply reduced motion factor to duration
     */
    fun reducedDuration(normalDuration: Long): Long {
        val reduced = (normalDuration * ReducedMotionFactor).toLong()
        return reduced.coerceAtLeast(MinimumDuration)
    }
    
    /**
     * Get accessible easing (always standard for consistency)
     */
    val AccessibleEasing = GameShowEasing.Standard
    
    /**
     * Check if motion should be reduced based on system settings
     */
    // This would be implemented based on platform-specific accessibility settings
    fun shouldReduceMotion(): Boolean {
        // Implementation would check system accessibility preferences
        return false // Placeholder
    }
}

/**
 * Performance optimization for motion
 */
object MotionPerformance {
    
    /**
     * Optimal frame rates for different animation types
     */
    const val StandardFrameRate = 60
    const val HighPerformanceFrameRate = 120
    const val BatteryOptimizedFrameRate = 30
    
    /**
     * Animation complexity levels
     */
    enum class ComplexityLevel {
        LOW,    // Simple property changes
        MEDIUM, // Multiple property changes
        HIGH,   // Complex transformations
        ULTRA   // Particle effects, complex shaders
    }
    
    /**
     * Get recommended frame rate based on complexity
     */
    fun getRecommendedFrameRate(complexity: ComplexityLevel): Int {
        return when (complexity) {
            ComplexityLevel.LOW -> BatteryOptimizedFrameRate
            ComplexityLevel.MEDIUM -> StandardFrameRate
            ComplexityLevel.HIGH -> StandardFrameRate
            ComplexityLevel.ULTRA -> HighPerformanceFrameRate
        }
    }
}