/*
 * Modern Turkish Word Game Design System - MinimalCountdown Component
 * Clean countdown timer optimized for mobile word games
 * Modern, minimal timer design for mobile word games
 */

package com.erdalgunes.kelimeislem.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erdalgunes.kelimeislem.designsystem.theme.PreviewThemes
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowThemeExtensions.gameShowColors
import com.erdalgunes.kelimeislem.designsystem.tokens.Duration
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowEasing
import kotlinx.coroutines.delay

/**
 * Modern minimal countdown timer for Turkish word games
 * 
 * Features:
 * - Clean circular progress design
 * - Color-coded warning states
 * - Smooth animations
 * - Mobile-optimized sizing
 * - Turkish number formatting
 */
@Composable
fun MinimalCountdown(
    totalSeconds: Int,
    currentSeconds: Int,
    modifier: Modifier = Modifier,
    size: CountdownSize = CountdownSize.Medium,
    showWarningEffects: Boolean = true,
    onTimeUp: (() -> Unit)? = null
) {
    val progress = if (totalSeconds > 0) currentSeconds.toFloat() / totalSeconds.toFloat() else 0f
    val isWarning = currentSeconds <= (totalSeconds * 0.2f) && currentSeconds > 0
    val isCritical = currentSeconds <= 5 && currentSeconds > 0
    
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = Duration.Medium2.toInt(),
            easing = LinearEasing
        ),
        label = "Progress"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (showWarningEffects && isCritical) 1.1f else 1f,
        animationSpec = tween(
            durationMillis = Duration.GameShowFast.toInt(),
            easing = GameShowEasing.GameShowBounce
        ),
        label = "Scale"
    )
    
    val progressColor by animateColorAsState(
        targetValue = when {
            isCritical -> gameShowColors.TimerCritical
            isWarning -> gameShowColors.TimerWarning
            else -> gameShowColors.TimerNormal
        },
        animationSpec = tween(
            durationMillis = Duration.Medium1.toInt(),
            easing = GameShowEasing.Standard
        ),
        label = "Progress Color"
    )
    
    val textColor by animateColorAsState(
        targetValue = when {
            isCritical -> gameShowColors.TimerCritical
            isWarning -> gameShowColors.TimerWarning
            else -> MaterialTheme.colorScheme.onSurface
        },
        animationSpec = tween(
            durationMillis = Duration.Medium1.toInt(),
            easing = GameShowEasing.Standard
        ),
        label = "Text Color"
    )
    
    // Trigger callback when time is up
    LaunchedEffect(currentSeconds) {
        if (currentSeconds == 0 && onTimeUp != null) {
            onTimeUp()
        }
    }
    
    val dimensions = when (size) {
        CountdownSize.Small -> CountdownDimensions(60.dp, 4.dp, 14.sp)
        CountdownSize.Medium -> CountdownDimensions(80.dp, 6.dp, 18.sp)
        CountdownSize.Large -> CountdownDimensions(100.dp, 8.dp, 24.sp)
    }
    
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant
    
    Box(
        modifier = modifier
            .scale(scale)
            .size(dimensions.diameter),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(dimensions.diameter)
        ) {
            drawCountdownCircle(
                progress = animatedProgress,
                progressColor = progressColor,
                backgroundColor = backgroundColor,
                strokeWidth = dimensions.strokeWidth.toPx()
            )
        }
        
        Text(
            text = currentSeconds.toString(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = dimensions.fontSize,
                fontWeight = FontWeight.Bold
            ),
            color = textColor
        )
    }
}

/**
 * Draw the circular countdown progress
 */
private fun DrawScope.drawCountdownCircle(
    progress: Float,
    progressColor: Color,
    backgroundColor: Color,
    strokeWidth: Float
) {
    val stroke = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    val diameter = size.minDimension
    val radius = diameter / 2f
    val center = Offset(size.width / 2f, size.height / 2f)
    
    // Background circle
    drawCircle(
        color = backgroundColor,
        radius = radius - strokeWidth / 2f,
        center = center,
        style = stroke
    )
    
    // Progress arc
    if (progress > 0f) {
        drawArc(
            color = progressColor,
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = stroke,
            topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
            size = Size(diameter - strokeWidth, diameter - strokeWidth)
        )
    }
}

/**
 * Countdown timer sizes
 */
enum class CountdownSize {
    Small,   // 60dp - for compact layouts
    Medium,  // 80dp - standard size
    Large    // 100dp - prominent display
}

/**
 * Internal data class for size dimensions
 */
private data class CountdownDimensions(
    val diameter: androidx.compose.ui.unit.Dp,
    val strokeWidth: androidx.compose.ui.unit.Dp,
    val fontSize: androidx.compose.ui.unit.TextUnit
)

/**
 * Animated countdown demo component
 */
@Composable
fun AnimatedCountdownDemo(
    totalSeconds: Int = 30,
    modifier: Modifier = Modifier,
    size: CountdownSize = CountdownSize.Medium
) {
    var currentSeconds by remember { mutableIntStateOf(totalSeconds) }
    
    LaunchedEffect(Unit) {
        while (currentSeconds > 0) {
            delay(1000)
            currentSeconds--
        }
    }
    
    MinimalCountdown(
        totalSeconds = totalSeconds,
        currentSeconds = currentSeconds,
        modifier = modifier,
        size = size,
        onTimeUp = { currentSeconds = totalSeconds } // Reset for demo
    )
}

/**
 * Preview composables
 */
@Preview(name = "Countdown States")
@Composable
private fun MinimalCountdownPreview() {
    PreviewThemes.LightPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Normal State", style = MaterialTheme.typography.titleSmall)
            MinimalCountdown(
                totalSeconds = 60,
                currentSeconds = 45,
                size = CountdownSize.Medium
            )
            
            Text("Warning State", style = MaterialTheme.typography.titleSmall)
            MinimalCountdown(
                totalSeconds = 60,
                currentSeconds = 10,
                size = CountdownSize.Medium
            )
            
            Text("Critical State", style = MaterialTheme.typography.titleSmall)
            MinimalCountdown(
                totalSeconds = 60,
                currentSeconds = 3,
                size = CountdownSize.Medium
            )
            
            Text("Different Sizes", style = MaterialTheme.typography.titleSmall)
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MinimalCountdown(
                    totalSeconds = 30,
                    currentSeconds = 15,
                    size = CountdownSize.Small
                )
                MinimalCountdown(
                    totalSeconds = 30,
                    currentSeconds = 15,
                    size = CountdownSize.Medium
                )
                MinimalCountdown(
                    totalSeconds = 30,
                    currentSeconds = 15,
                    size = CountdownSize.Large
                )
            }
        }
    }
}