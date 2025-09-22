/*
 * Turkish Game Show Design System - BuzzerButton Component
 * Interactive buzzer button with haptic feedback and game show animations
 * Designed for "Bir Kelime Bir İşlem" game interactions
 */

package com.erdalgunes.kelimeislem.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erdalgunes.kelimeislem.designsystem.theme.GameTypography
import com.erdalgunes.kelimeislem.designsystem.theme.PreviewThemes
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowThemeExtensions.gameShowColors
import com.erdalgunes.kelimeislem.designsystem.tokens.Duration
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowEasing
import com.erdalgunes.kelimeislem.designsystem.tokens.Elevation
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowElevation
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowSpacing

/**
 * Main buzzer button component for game show interactions
 */
@Composable
fun BuzzerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    state: BuzzerState = BuzzerState.Ready,
    style: BuzzerStyle = BuzzerStyle.Primary,
    size: BuzzerSize = BuzzerSize.Large,
    hapticEnabled: Boolean = true,
    icon: ImageVector? = null
) {
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animation states
    val scale by animateFloatAsState(
        targetValue = when {
            !enabled -> 0.9f
            isPressed -> 0.95f
            state == BuzzerState.Active -> 1.05f
            else -> 1f
        },
        animationSpec = tween(
            durationMillis = Duration.BuzzerPress.toInt(),
            easing = when {
                isPressed -> GameShowEasing.GameShowSnap
                state == BuzzerState.Active -> GameShowEasing.GameShowBounce
                else -> GameShowEasing.Standard
            }
        ),
        label = "Buzzer Scale"
    )
    
    val containerColor by animateColorAsState(
        targetValue = getContainerColor(style, state, enabled),
        animationSpec = tween(Duration.BuzzerActivate.toInt()),
        label = "Buzzer Container Color"
    )
    
    val contentColor by animateColorAsState(
        targetValue = getContentColor(style, state, enabled),
        animationSpec = tween(Duration.BuzzerActivate.toInt()),
        label = "Buzzer Content Color"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (enabled) 1f else 0.6f,
        animationSpec = tween(Duration.GameShowFast.toInt()),
        label = "Buzzer Alpha"
    )
    
    // Haptic feedback effect
    LaunchedEffect(isPressed) {
        if (isPressed && enabled && hapticEnabled) {
            hapticFeedback.performHapticFeedback(
                when (style) {
                    BuzzerStyle.Primary -> HapticFeedbackType.LongPress
                    BuzzerStyle.Secondary -> HapticFeedbackType.TextHandleMove
                    BuzzerStyle.Danger -> HapticFeedbackType.LongPress
                    BuzzerStyle.Success -> HapticFeedbackType.TextHandleMove
                }
            )
        }
    }
    
    val buttonSize = getBuzzerSize(size)
    val buttonShape = when (size) {
        BuzzerSize.Small -> RoundedCornerShape(8.dp)
        BuzzerSize.Medium -> RoundedCornerShape(12.dp)
        BuzzerSize.Large -> RoundedCornerShape(16.dp)
        BuzzerSize.ExtraLarge -> RoundedCornerShape(20.dp)
        BuzzerSize.Round -> CircleShape
    }
    
    Surface(
        modifier = modifier
            .size(buttonSize)
            .scale(scale)
            .alpha(alpha)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            ),
        shape = buttonShape,
        color = containerColor,
        shadowElevation = getElevation(state, isPressed),
        border = getBorder(style, state, enabled)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = getBackgroundBrush(style, state, isPressed)
                )
                .padding(GameShowSpacing.BuzzerPadding),
            contentAlignment = Alignment.Center
        ) {
            BuzzerContent(
                text = text,
                icon = icon,
                size = size,
                contentColor = contentColor,
                state = state
            )
        }
    }
}

/**
 * Buzzer content layout
 */
@Composable
private fun BuzzerContent(
    text: String,
    icon: ImageVector?,
    size: BuzzerSize,
    contentColor: Color,
    state: BuzzerState
) {
    val fontSize = getBuzzerTextSize(size)
    val iconSize = getBuzzerIconSize(size)
    
    when {
        icon != null && size != BuzzerSize.Small -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(iconSize)
                )
                
                if (text.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = text.uppercase(),
                        style = GameTypography.BuzzerText.copy(fontSize = fontSize),
                        color = contentColor,
                        textAlign = TextAlign.Center,
                        maxLines = if (size == BuzzerSize.Large || size == BuzzerSize.ExtraLarge) 2 else 1
                    )
                }
            }
        }
        
        icon != null -> {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = contentColor,
                modifier = Modifier.size(iconSize)
            )
        }
        
        else -> {
            Text(
                text = text.uppercase(),
                style = GameTypography.BuzzerText.copy(fontSize = fontSize),
                color = contentColor,
                textAlign = TextAlign.Center,
                maxLines = when (size) {
                    BuzzerSize.Small -> 1
                    BuzzerSize.Medium -> 1
                    BuzzerSize.Large, BuzzerSize.ExtraLarge -> 2
                    BuzzerSize.Round -> 1
                }
            )
        }
    }
}

/**
 * Compact buzzer button for secondary actions
 */
@Composable
fun CompactBuzzerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: BuzzerStyle = BuzzerStyle.Secondary,
    icon: ImageVector? = null,
    hapticEnabled: Boolean = true
) {
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    LaunchedEffect(isPressed) {
        if (isPressed && enabled && hapticEnabled) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }
    
    when (style) {
        BuzzerStyle.Primary -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                containerColor = gameShowColors.BuzzerActive,
                contentColor = Color.White,
                disabledContainerColor = gameShowColors.BuzzerDisabled,
                disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            CompactBuzzerContent(text, icon)
        }
        
        BuzzerStyle.Secondary -> FilledTonalButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            CompactBuzzerContent(text, icon)
        }
        
        BuzzerStyle.Danger -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                containerColor = gameShowColors.IncorrectAnswer,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            CompactBuzzerContent(text, icon)
        }
        
        BuzzerStyle.Success -> Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            interactionSource = interactionSource,
            colors = ButtonDefaults.buttonColors(
                containerColor = gameShowColors.CorrectAnswer,
                contentColor = Color.White
            ),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            CompactBuzzerContent(text, icon)
        }
    }
}

@Composable
private fun CompactBuzzerContent(text: String, icon: ImageVector?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
        }
        
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        )
    }
}

/**
 * Outline buzzer button variant
 */
@Composable
fun OutlineBuzzerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: BuzzerStyle = BuzzerStyle.Secondary,
    icon: ImageVector? = null,
    hapticEnabled: Boolean = true
) {
    val hapticFeedback = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    LaunchedEffect(isPressed) {
        if (isPressed && enabled && hapticEnabled) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }
    
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        border = androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = when (style) {
                BuzzerStyle.Primary -> gameShowColors.BuzzerActive
                BuzzerStyle.Danger -> gameShowColors.IncorrectAnswer
                BuzzerStyle.Success -> gameShowColors.CorrectAnswer
                else -> MaterialTheme.colorScheme.outline
            }
        ),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = when (style) {
                BuzzerStyle.Primary -> gameShowColors.BuzzerActive
                BuzzerStyle.Danger -> gameShowColors.IncorrectAnswer
                BuzzerStyle.Success -> gameShowColors.CorrectAnswer
                else -> MaterialTheme.colorScheme.onSurface
            }
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
    ) {
        CompactBuzzerContent(text, icon)
    }
}

/**
 * Configuration enums and data classes
 */
enum class BuzzerState {
    Ready,      // Ready to be pressed
    Active,     // Currently active/pressed
    Disabled,   // Cannot be pressed
    Cooldown    // Temporary disabled after use
}

enum class BuzzerStyle {
    Primary,    // Main action buzzer (red)
    Secondary,  // Secondary action buzzer (neutral)
    Danger,     // Dangerous action buzzer (error red)
    Success     // Success action buzzer (green)
}

enum class BuzzerSize {
    Small,       // 48dp - minimal space
    Medium,      // 64dp - standard size
    Large,       // 80dp - prominent
    ExtraLarge,  // 96dp - main game buzzer
    Round        // 72dp - circular buzzer
}

/**
 * Helper functions
 */
@Composable
private fun getContainerColor(style: BuzzerStyle, state: BuzzerState, enabled: Boolean): Color {
    if (!enabled) {
        return gameShowColors.BuzzerDisabled
    }
    
    return when (style) {
        BuzzerStyle.Primary -> when (state) {
            BuzzerState.Active -> gameShowColors.BuzzerPressed
            BuzzerState.Cooldown -> gameShowColors.BuzzerDisabled
            else -> gameShowColors.BuzzerActive
        }
        BuzzerStyle.Secondary -> MaterialTheme.colorScheme.surfaceVariant
        BuzzerStyle.Danger -> gameShowColors.IncorrectAnswer
        BuzzerStyle.Success -> gameShowColors.CorrectAnswer
    }
}

@Composable
private fun getContentColor(style: BuzzerStyle, state: BuzzerState, enabled: Boolean): Color {
    if (!enabled) {
        return MaterialTheme.colorScheme.onSurfaceVariant
    }
    
    return when (style) {
        BuzzerStyle.Primary -> Color.White
        BuzzerStyle.Secondary -> MaterialTheme.colorScheme.onSurfaceVariant
        BuzzerStyle.Danger -> Color.White
        BuzzerStyle.Success -> Color.White
    }
}

private fun getBuzzerSize(size: BuzzerSize): Dp {
    return when (size) {
        BuzzerSize.Small -> 48.dp
        BuzzerSize.Medium -> 64.dp
        BuzzerSize.Large -> 80.dp
        BuzzerSize.ExtraLarge -> 96.dp
        BuzzerSize.Round -> 72.dp
    }
}

private fun getBuzzerTextSize(size: BuzzerSize): TextUnit {
    return when (size) {
        BuzzerSize.Small -> 12.sp
        BuzzerSize.Medium -> 14.sp
        BuzzerSize.Large -> 16.sp
        BuzzerSize.ExtraLarge -> 18.sp
        BuzzerSize.Round -> 14.sp
    }
}

private fun getBuzzerIconSize(size: BuzzerSize): Dp {
    return when (size) {
        BuzzerSize.Small -> 16.dp
        BuzzerSize.Medium -> 20.dp
        BuzzerSize.Large -> 24.dp
        BuzzerSize.ExtraLarge -> 28.dp
        BuzzerSize.Round -> 24.dp
    }
}

private fun getElevation(state: BuzzerState, isPressed: Boolean): Dp {
    return when {
        isPressed -> GameShowElevation.BuzzerPressed
        state == BuzzerState.Active -> Elevation.GameShowFocused
        else -> GameShowElevation.Buzzer
    }
}

@Composable
private fun getBorder(style: BuzzerStyle, state: BuzzerState, enabled: Boolean): androidx.compose.foundation.BorderStroke? {
    return when {
        state == BuzzerState.Active && style == BuzzerStyle.Primary -> androidx.compose.foundation.BorderStroke(
            width = 3.dp,
            color = Color.White.copy(alpha = 0.7f)
        )
        else -> null
    }
}

@Composable
private fun getBackgroundBrush(style: BuzzerStyle, state: BuzzerState, isPressed: Boolean): Brush {
    return when {
        isPressed && style == BuzzerStyle.Primary -> Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.2f),
                Color.Transparent
            )
        )
        state == BuzzerState.Active && style == BuzzerStyle.Primary -> Brush.radialGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.1f),
                Color.Transparent
            )
        )
        else -> Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Transparent)
        )
    }
}

/**
 * Preview composables
 */
@Preview(name = "Buzzer Buttons")
@Composable
private fun BuzzerButtonsPreview() {
    PreviewThemes.LightPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BuzzerButton(
                    text = "BUZZER",
                    onClick = {},
                    size = BuzzerSize.Large,
                    state = BuzzerState.Ready
                )
                
                BuzzerButton(
                    text = "AKTİF",
                    onClick = {},
                    size = BuzzerSize.Large,
                    state = BuzzerState.Active
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BuzzerButton(
                    text = "BAŞLA",
                    onClick = {},
                    size = BuzzerSize.Medium,
                    style = BuzzerStyle.Success,
                    icon = Icons.Default.PlayArrow
                )
                
                BuzzerButton(
                    text = "DUR",
                    onClick = {},
                    size = BuzzerSize.Medium,
                    style = BuzzerStyle.Danger,
                    icon = Icons.Default.Stop
                )
            }
        }
    }
}

@Preview(name = "Compact Buzzer Buttons")
@Composable
private fun CompactBuzzerButtonsPreview() {
    PreviewThemes.LightPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CompactBuzzerButton(
                    text = "CEVAP VER",
                    onClick = {},
                    style = BuzzerStyle.Primary,
                    icon = Icons.Default.NotificationsActive
                )
                
                CompactBuzzerButton(
                    text = "GEÇME",
                    onClick = {},
                    style = BuzzerStyle.Secondary
                )
            }
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlineBuzzerButton(
                    text = "DOĞRU",
                    onClick = {},
                    style = BuzzerStyle.Success
                )
                
                OutlineBuzzerButton(
                    text = "YANLIŞ",
                    onClick = {},
                    style = BuzzerStyle.Danger
                )
            }
        }
    }
}

@Preview(name = "Buzzer Sizes")
@Composable
private fun BuzzerSizesPreview() {
    PreviewThemes.LightPreview {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BuzzerButton(
                text = "S",
                onClick = {},
                size = BuzzerSize.Small
            )
            
            BuzzerButton(
                text = "M",
                onClick = {},
                size = BuzzerSize.Medium
            )
            
            BuzzerButton(
                text = "L",
                onClick = {},
                size = BuzzerSize.Large
            )
            
            BuzzerButton(
                text = "XL",
                onClick = {},
                size = BuzzerSize.ExtraLarge
            )
            
            BuzzerButton(
                text = "●",
                onClick = {},
                size = BuzzerSize.Round
            )
        }
    }
}