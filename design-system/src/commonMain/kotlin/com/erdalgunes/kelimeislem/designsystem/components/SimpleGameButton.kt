/*
 * Modern Turkish Word Game Design System - SimpleGameButton Component
 * Clean, mobile-first button for word game interactions
 * Optimized for touch interaction and Turkish localization
 */

package com.erdalgunes.kelimeislem.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.erdalgunes.kelimeislem.designsystem.theme.PreviewThemes
import com.erdalgunes.kelimeislem.designsystem.tokens.Duration
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowEasing

/**
 * Simple, modern button component for Turkish word games
 * 
 * Features:
 * - Touch-optimized 44dp minimum size
 * - Haptic feedback for mobile interaction
 * - Clean Material Design 3 styling
 * - Turkish text optimization
 * - Smooth press animations
 */
@Composable
fun SimpleGameButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GameButtonStyle = GameButtonStyle.Primary,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    hapticEnabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    
    val scale by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.95f,
        animationSpec = tween(
            durationMillis = Duration.GameShowFast.toInt(),
            easing = GameShowEasing.Standard
        ),
        label = "Button Scale"
    )
    
    val clickHandler = {
        if (hapticEnabled) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
        onClick()
    }
    
    val buttonModifier = modifier
        .scale(scale)
    
    val contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    
    when (style) {
        GameButtonStyle.Primary -> Button(
            onClick = clickHandler,
            modifier = buttonModifier,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(),
            contentPadding = contentPadding
        ) {
            ButtonContent(text, icon)
        }
        GameButtonStyle.Secondary -> FilledTonalButton(
            onClick = clickHandler,
            modifier = buttonModifier,
            enabled = enabled,
            colors = ButtonDefaults.filledTonalButtonColors(),
            contentPadding = contentPadding
        ) {
            ButtonContent(text, icon)
        }
        GameButtonStyle.Outline -> OutlinedButton(
            onClick = clickHandler,
            modifier = buttonModifier,
            enabled = enabled,
            colors = ButtonDefaults.outlinedButtonColors(),
            contentPadding = contentPadding
        ) {
            ButtonContent(text, icon)
        }
    }
}

@Composable
private fun ButtonContent(text: String, icon: ImageVector?) {
    if (icon != null) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    }
    
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Medium
        )
    )
}

/**
 * Button style variants
 */
enum class GameButtonStyle {
    Primary,    // Filled button for main actions
    Secondary,  // Tonal button for secondary actions  
    Outline     // Outlined button for tertiary actions
}

