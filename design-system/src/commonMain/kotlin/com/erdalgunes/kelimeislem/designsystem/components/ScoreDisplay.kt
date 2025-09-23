/*
 * Modern Turkish Word Game Design System - ScoreDisplay Component
 * Clean, minimal score display for mobile word games
 * Replaces complex ScoreCard with modern design
 */

package com.erdalgunes.kelimeislem.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erdalgunes.kelimeislem.designsystem.theme.PreviewThemes
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowThemeExtensions.gameShowColors
import com.erdalgunes.kelimeislem.designsystem.tokens.Duration
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowEasing
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowElevation
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowSpacing

/**
 * Modern score display component for Turkish word games
 * 
 * Features:
 * - Animated score counting
 * - Clean mobile-optimized design
 * - Optional label and subtitle
 * - Color-coded score states
 * - Turkish number formatting
 */
@Composable
fun ScoreDisplay(
    score: Int,
    modifier: Modifier = Modifier,
    label: String? = null,
    subtitle: String? = null,
    variant: ScoreDisplayVariant = ScoreDisplayVariant.Primary,
    animateChanges: Boolean = true
) {
    val animatedScore by animateIntAsState(
        targetValue = score,
        animationSpec = tween(
            durationMillis = if (animateChanges) Duration.GameShowStandard.toInt() else 0,
            easing = GameShowEasing.GameShowBounce
        ),
        label = "Score Animation"
    )
    
    val containerColor by animateColorAsState(
        targetValue = when (variant) {
            ScoreDisplayVariant.Primary -> MaterialTheme.colorScheme.primaryContainer
            ScoreDisplayVariant.Secondary -> MaterialTheme.colorScheme.surfaceVariant
            ScoreDisplayVariant.Success -> gameShowColors.CorrectAnswerContainer
            ScoreDisplayVariant.Warning -> gameShowColors.TimerWarning.copy(alpha = 0.1f)
        },
        animationSpec = tween(
            durationMillis = Duration.Medium2.toInt(),
            easing = GameShowEasing.Standard
        ),
        label = "Container Color"
    )
    
    val contentColor by animateColorAsState(
        targetValue = when (variant) {
            ScoreDisplayVariant.Primary -> MaterialTheme.colorScheme.onPrimaryContainer
            ScoreDisplayVariant.Secondary -> MaterialTheme.colorScheme.onSurfaceVariant
            ScoreDisplayVariant.Success -> gameShowColors.OnCorrectAnswerContainer
            ScoreDisplayVariant.Warning -> gameShowColors.TimerWarning
        },
        animationSpec = tween(
            durationMillis = Duration.Medium2.toInt(),
            easing = GameShowEasing.Standard
        ),
        label = "Content Color"
    )
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = when (variant) {
                ScoreDisplayVariant.Primary -> GameShowElevation.ScoreDisplay
                ScoreDisplayVariant.Success -> GameShowElevation.WordDisplay
                else -> GameShowElevation.WordDisplay
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(GameShowSpacing.ScoreDisplayPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
            }
            
            Text(
                text = formatScore(animatedScore),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = when (variant) {
                        ScoreDisplayVariant.Primary -> 32.sp
                        else -> 24.sp
                    },
                    fontWeight = FontWeight.Bold
                ),
                color = contentColor,
                textAlign = TextAlign.Center
            )
            
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Compact horizontal score display
 */
@Composable
fun CompactScoreDisplay(
    score: Int,
    label: String,
    modifier: Modifier = Modifier,
    variant: ScoreDisplayVariant = ScoreDisplayVariant.Secondary
) {
    val animatedScore by animateIntAsState(
        targetValue = score,
        animationSpec = tween(
            durationMillis = Duration.Medium2.toInt(),
            easing = GameShowEasing.Standard
        ),
        label = "Compact Score Animation"
    )
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = when (variant) {
                ScoreDisplayVariant.Primary -> MaterialTheme.colorScheme.primaryContainer
                ScoreDisplayVariant.Secondary -> MaterialTheme.colorScheme.surfaceVariant
                ScoreDisplayVariant.Success -> gameShowColors.CorrectAnswerContainer
                ScoreDisplayVariant.Warning -> gameShowColors.TimerWarning.copy(alpha = 0.1f)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = formatScore(animatedScore),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Score display variants
 */
enum class ScoreDisplayVariant {
    Primary,    // Main game score
    Secondary,  // Secondary metrics
    Success,    // Positive achievement
    Warning     // Needs attention
}

/**
 * Format score for Turkish display
 */
private fun formatScore(score: Int): String {
    return when {
        score >= 1000000 -> "${score / 1000000}M"
        score >= 1000 -> "${score / 1000}K"
        else -> score.toString()
    }
}

/**
 * Preview composables
 */
@Preview(name = "Score Display Variants")
@Composable
private fun ScoreDisplayPreview() {
    PreviewThemes.LightPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScoreDisplay(
                score = 1250,
                label = "Toplam Puan",
                subtitle = "En yüksek skor",
                variant = ScoreDisplayVariant.Primary
            )
            
            ScoreDisplay(
                score = 350,
                label = "Bu Turda",
                variant = ScoreDisplayVariant.Success
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CompactScoreDisplay(
                    score = 85,
                    label = "Doğru",
                    variant = ScoreDisplayVariant.Success,
                    modifier = Modifier.weight(1f)
                )
                
                CompactScoreDisplay(
                    score = 15,
                    label = "Yanlış",
                    variant = ScoreDisplayVariant.Warning,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}