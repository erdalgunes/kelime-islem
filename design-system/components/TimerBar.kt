/*
 * Turkish Game Show Design System - TimerBar Component
 * Animated countdown timer with TV show dramatic effects
 * Supports different timer states and visual feedback
 */

package com.erdalgunes.kelimeislem.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erdalgunes.kelimeislem.designsystem.theme.GameTypography
import com.erdalgunes.kelimeislem.designsystem.theme.PreviewThemes
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowThemeExtensions.gameShowColors
import com.erdalgunes.kelimeislem.designsystem.tokens.Duration
import com.erdalgunes.kelimeislem.designsystem.tokens.Easing
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowElevation
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowSpacing
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

/**
 * Main timer bar component with countdown functionality
 */
@Composable
fun TimerBar(
    totalTimeSeconds: Int,
    currentTimeSeconds: Int,
    modifier: Modifier = Modifier,
    style: TimerStyle = TimerStyle.Linear,
    state: TimerState = TimerState.Running,
    showTimeText: Boolean = true,
    showWarningEffects: Boolean = true,
    warningThresholdSeconds: Int = 10,
    criticalThresholdSeconds: Int = 5,
    onTimeUp: (() -> Unit)? = null,
    onWarning: (() -> Unit)? = null,
    onCritical: (() -> Unit)? = null
) {
    val progress = if (totalTimeSeconds > 0) {
        (currentTimeSeconds.toFloat() / totalTimeSeconds.toFloat()).coerceIn(0f, 1f)
    } else 0f
    
    val timerState = when {
        currentTimeSeconds <= 0 -> TimerState.Finished
        currentTimeSeconds <= criticalThresholdSeconds -> TimerState.Critical
        currentTimeSeconds <= warningThresholdSeconds -> TimerState.Warning
        else -> state
    }
    
    // Trigger callbacks based on state changes
    LaunchedEffect(timerState) {
        when (timerState) {
            TimerState.Finished -> onTimeUp?.invoke()
            TimerState.Critical -> onCritical?.invoke()
            TimerState.Warning -> onWarning?.invoke()
            else -> { /* No callback needed */ }
        }
    }
    
    when (style) {
        TimerStyle.Linear -> LinearTimerBar(
            progress = progress,
            currentTimeSeconds = currentTimeSeconds,
            state = timerState,
            showTimeText = showTimeText,
            showWarningEffects = showWarningEffects,
            modifier = modifier
        )
        TimerStyle.Circular -> CircularTimerBar(
            progress = progress,
            currentTimeSeconds = currentTimeSeconds,
            state = timerState,
            showTimeText = showTimeText,
            showWarningEffects = showWarningEffects,
            modifier = modifier
        )
        TimerStyle.Countdown -> CountdownTimerDisplay(
            currentTimeSeconds = currentTimeSeconds,
            state = timerState,
            showWarningEffects = showWarningEffects,
            modifier = modifier
        )
        TimerStyle.Card -> CardTimerDisplay(
            progress = progress,
            currentTimeSeconds = currentTimeSeconds,
            state = timerState,
            showTimeText = showTimeText,
            showWarningEffects = showWarningEffects,
            modifier = modifier
        )
    }
}

/**
 * Linear progress bar timer
 */
@Composable
private fun LinearTimerBar(
    progress: Float,
    currentTimeSeconds: Int,
    state: TimerState,
    showTimeText: Boolean,
    showWarningEffects: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Timer Pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (showWarningEffects && state == TimerState.Critical) 0.3f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = when (state) {
                    TimerState.Critical -> Duration.FinalCountdown.toInt()
                    TimerState.Warning -> Duration.TimerTick.toInt() * 3
                    else -> 1000
                },
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Timer Pulse Alpha"
    )
    
    val timerColor by animateColorAsState(
        targetValue = getTimerColor(state),
        animationSpec = tween(Duration.GameShowFast.toInt()),
        label = "Timer Color"
    )
    
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (showTimeText) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SÜRE",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                TimerText(
                    timeSeconds = currentTimeSeconds,
                    state = state,
                    modifier = Modifier.alpha(pulseAlpha)
                )
            }
        }
        
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(GameShowSpacing.TimerBarHeight)
                .clip(RoundedCornerShape(GameShowSpacing.TimerBarHeight / 2))
                .alpha(if (showWarningEffects) pulseAlpha else 1f),
            color = timerColor,
            trackColor = trackColor,
            strokeCap = StrokeCap.Round
        )
    }
}

/**
 * Circular progress timer
 */
@Composable
private fun CircularTimerBar(
    progress: Float,
    currentTimeSeconds: Int,
    state: TimerState,
    showTimeText: Boolean,
    showWarningEffects: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Circular Timer")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (showWarningEffects && state == TimerState.Critical) 1.1f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = Duration.FinalCountdown.toInt(),
                easing = Easing.TimerPulse
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Circular Timer Scale"
    )
    
    val timerColor by animateColorAsState(
        targetValue = getTimerColor(state),
        animationSpec = tween(Duration.GameShowFast.toInt()),
        label = "Circular Timer Color"
    )
    
    Box(
        modifier = modifier
            .size(120.dp)
            .scale(if (showWarningEffects) pulseScale else 1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(120.dp)
        ) {
            drawCircularTimer(
                progress = progress,
                color = timerColor,
                trackColor = Color.Gray.copy(alpha = 0.3f),
                strokeWidth = 8.dp.toPx()
            )
        }
        
        if (showTimeText) {
            TimerText(
                timeSeconds = currentTimeSeconds,
                state = state,
                size = TimerTextSize.Large
            )
        }
    }
}

/**
 * Simple countdown text display
 */
@Composable
private fun CountdownTimerDisplay(
    currentTimeSeconds: Int,
    state: TimerState,
    showWarningEffects: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Countdown")
    val bounce by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (showWarningEffects && state == TimerState.Critical) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = Duration.CountdownSecond.toInt(),
                easing = Easing.GameShowBounce
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Countdown Bounce"
    )
    
    TimerText(
        timeSeconds = currentTimeSeconds,
        state = state,
        size = TimerTextSize.ExtraLarge,
        modifier = modifier.scale(if (showWarningEffects) bounce else 1f)
    )
}

/**
 * Card-style timer display
 */
@Composable
private fun CardTimerDisplay(
    progress: Float,
    currentTimeSeconds: Int,
    state: TimerState,
    showTimeText: Boolean,
    showWarningEffects: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "Card Timer")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (showWarningEffects && state != TimerState.Running) 0.5f else 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = Duration.TimerTick.toInt() * 2,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Card Timer Glow"
    )
    
    val cardColor by animateColorAsState(
        targetValue = when (state) {
            TimerState.Critical -> gameShowColors.TimerCritical.copy(alpha = 0.1f)
            TimerState.Warning -> gameShowColors.TimerWarning.copy(alpha = 0.1f)
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(Duration.GameShowStandard.toInt()),
        label = "Card Timer Color"
    )
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = GameShowElevation.Timer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            getTimerColor(state).copy(alpha = glowAlpha),
                            Color.Transparent
                        )
                    )
                )
                .padding(GameShowSpacing.TimerDisplayPadding)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (state == TimerState.Critical || state == TimerState.Warning) {
                            Icons.Default.Warning
                        } else {
                            Icons.Default.Timer
                        },
                        contentDescription = "Timer",
                        tint = getTimerColor(state),
                        modifier = Modifier.size(20.dp)
                    )
                    
                    if (showTimeText) {
                        TimerText(
                            timeSeconds = currentTimeSeconds,
                            state = state,
                            size = TimerTextSize.Medium
                        )
                    }
                }
                
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = getTimerColor(state),
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

/**
 * Timer text component
 */
@Composable
private fun TimerText(
    timeSeconds: Int,
    state: TimerState,
    modifier: Modifier = Modifier,
    size: TimerTextSize = TimerTextSize.Medium
) {
    val minutes = timeSeconds / 60
    val seconds = timeSeconds % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)
    
    val textColor by animateColorAsState(
        targetValue = getTimerTextColor(state),
        animationSpec = tween(Duration.GameShowFast.toInt()),
        label = "Timer Text Color"
    )
    
    val fontSize = when (size) {
        TimerTextSize.Small -> 14.sp
        TimerTextSize.Medium -> 18.sp
        TimerTextSize.Large -> 24.sp
        TimerTextSize.ExtraLarge -> 36.sp
    }
    
    Text(
        text = timeText,
        style = GameTypography.TimerDisplay.copy(fontSize = fontSize),
        color = textColor,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

/**
 * Auto-updating timer component
 */
@Composable
fun AutoTimerBar(
    totalTimeSeconds: Int,
    modifier: Modifier = Modifier,
    style: TimerStyle = TimerStyle.Linear,
    showTimeText: Boolean = true,
    showWarningEffects: Boolean = true,
    warningThresholdSeconds: Int = 10,
    criticalThresholdSeconds: Int = 5,
    onTimeUp: (() -> Unit)? = null,
    onWarning: (() -> Unit)? = null,
    onCritical: (() -> Unit)? = null,
    onTick: ((Int) -> Unit)? = null
) {
    var currentTime by remember { mutableLongStateOf(totalTimeSeconds.toLong()) }
    var isRunning by remember { mutableFloatStateOf(1f) }
    
    LaunchedEffect(totalTimeSeconds, isRunning) {
        while (currentTime > 0 && isRunning > 0f) {
            delay(1000)
            currentTime -= 1
            onTick?.invoke(currentTime.toInt())
        }
    }
    
    TimerBar(
        totalTimeSeconds = totalTimeSeconds,
        currentTimeSeconds = currentTime.toInt(),
        modifier = modifier,
        style = style,
        showTimeText = showTimeText,
        showWarningEffects = showWarningEffects,
        warningThresholdSeconds = warningThresholdSeconds,
        criticalThresholdSeconds = criticalThresholdSeconds,
        onTimeUp = onTimeUp,
        onWarning = onWarning,
        onCritical = onCritical
    )
}

/**
 * Canvas drawing functions
 */
private fun DrawScope.drawCircularTimer(
    progress: Float,
    color: Color,
    trackColor: Color,
    strokeWidth: Float
) {
    val diameter = size.minDimension
    val radius = diameter / 2f
    val center = Offset(size.width / 2f, size.height / 2f)
    
    // Draw track
    drawCircle(
        color = trackColor,
        radius = radius - strokeWidth / 2,
        center = center,
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
    
    // Draw progress arc
    val sweepAngle = 360f * progress
    drawArc(
        color = color,
        startAngle = -90f,
        sweepAngle = sweepAngle,
        useCenter = false,
        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
        size = Size(diameter - strokeWidth, diameter - strokeWidth),
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
    )
}

/**
 * Configuration enums and data classes
 */
enum class TimerStyle {
    Linear,     // Horizontal progress bar
    Circular,   // Circular progress indicator
    Countdown,  // Large countdown text only
    Card        // Card with progress and text
}

enum class TimerState {
    Running,    // Normal countdown
    Paused,     // Timer is paused
    Warning,    // In warning threshold
    Critical,   // In critical threshold
    Finished    // Time is up
}

enum class TimerTextSize {
    Small,       // 14sp
    Medium,      // 18sp
    Large,       // 24sp
    ExtraLarge   // 36sp
}

/**
 * Helper functions
 */
@Composable
private fun getTimerColor(state: TimerState): Color {
    return when (state) {
        TimerState.Running, TimerState.Paused -> gameShowColors.TimerNormal
        TimerState.Warning -> gameShowColors.TimerWarning
        TimerState.Critical, TimerState.Finished -> gameShowColors.TimerCritical
    }
}

@Composable
private fun getTimerTextColor(state: TimerState): Color {
    return when (state) {
        TimerState.Running, TimerState.Paused -> MaterialTheme.colorScheme.onSurface
        TimerState.Warning -> gameShowColors.TimerWarning
        TimerState.Critical, TimerState.Finished -> gameShowColors.TimerCritical
    }
}

/**
 * Preview composables
 */
@Preview(name = "Timer Styles")
@Composable
private fun TimerStylesPreview() {
    PreviewThemes.LightPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TimerBar(
                totalTimeSeconds = 60,
                currentTimeSeconds = 45,
                style = TimerStyle.Linear,
                state = TimerState.Running
            )
            
            TimerBar(
                totalTimeSeconds = 60,
                currentTimeSeconds = 8,
                style = TimerStyle.Circular,
                state = TimerState.Warning
            )
            
            TimerBar(
                totalTimeSeconds = 60,
                currentTimeSeconds = 3,
                style = TimerStyle.Countdown,
                state = TimerState.Critical
            )
            
            TimerBar(
                totalTimeSeconds = 60,
                currentTimeSeconds = 30,
                style = TimerStyle.Card,
                state = TimerState.Running
            )
        }
    }
}

@Preview(name = "Timer States")
@Composable
private fun TimerStatesPreview() {
    PreviewThemes.LightPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TimerBar(
                totalTimeSeconds = 60,
                currentTimeSeconds = 45,
                style = TimerStyle.Card,
                state = TimerState.Running
            )
            
            TimerBar(
                totalTimeSeconds = 60,
                currentTimeSeconds = 8,
                style = TimerStyle.Card,
                state = TimerState.Warning
            )
            
            TimerBar(
                totalTimeSeconds = 60,
                currentTimeSeconds = 3,
                style = TimerStyle.Card,
                state = TimerState.Critical
            )
            
            TimerBar(
                totalTimeSeconds = 60,
                currentTimeSeconds = 0,
                style = TimerStyle.Card,
                state = TimerState.Finished
            )
        }
    }
}