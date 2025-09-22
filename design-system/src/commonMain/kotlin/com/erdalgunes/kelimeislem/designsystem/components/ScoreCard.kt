/*
 * Turkish Game Show Design System - ScoreCard Component
 * TV show-style score display with dramatic animations
 * Supports Turkish number formatting and game show aesthetics
 */

package com.erdalgunes.kelimeislem.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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
import java.text.NumberFormat
import java.util.Locale

/**
 * ScoreCard component for displaying player scores with TV show aesthetics
 */
@Composable
fun ScoreCard(
    playerName: String,
    score: Int,
    modifier: Modifier = Modifier,
    style: ScoreCardStyle = ScoreCardStyle(),
    state: ScoreCardState = ScoreCardState.Normal,
    showTrend: Boolean = true,
    previousScore: Int = score,
    rank: Int? = null,
    avatar: @Composable (() -> Unit)? = null,
    animateScore: Boolean = true
) {
    val scale by animateFloatAsState(
        targetValue = when (state) {
            ScoreCardState.Normal -> 1f
            ScoreCardState.Leading -> 1.05f
            ScoreCardState.Winner -> 1.1f
            ScoreCardState.Eliminated -> 0.95f
        },
        animationSpec = tween(
            durationMillis = Duration.GameShowStandard.toInt(),
            easing = when (state) {
                ScoreCardState.Winner -> GameShowEasing.GameShowBounce
                else -> GameShowEasing.Standard
            }
        ),
        label = "ScoreCard Scale"
    )
    
    val containerColor by animateColorAsState(
        targetValue = getContainerColor(style, state),
        animationSpec = tween(Duration.GameShowFast.toInt()),
        label = "ScoreCard Container Color"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (state == ScoreCardState.Eliminated) 0.6f else 1f,
        animationSpec = tween(Duration.GameShowStandard.toInt()),
        label = "ScoreCard Alpha"
    )
    
    Card(
        modifier = modifier
            .scale(scale)
            .alpha(alpha),
        shape = RoundedCornerShape(style.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = getElevation(state)
        ),
        border = getBorder(style, state)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = getBackgroundBrush(style, state)
                )
                .padding(GameShowSpacing.ScoreCardPadding)
        ) {
            when (style.layout) {
                ScoreCardLayout.Compact -> CompactScoreLayout(
                    playerName = playerName,
                    score = score,
                    style = style,
                    state = state,
                    rank = rank,
                    avatar = avatar,
                    animateScore = animateScore
                )
                ScoreCardLayout.Detailed -> DetailedScoreLayout(
                    playerName = playerName,
                    score = score,
                    previousScore = previousScore,
                    style = style,
                    state = state,
                    showTrend = showTrend,
                    rank = rank,
                    avatar = avatar,
                    animateScore = animateScore
                )
                ScoreCardLayout.Leaderboard -> LeaderboardScoreLayout(
                    playerName = playerName,
                    score = score,
                    style = style,
                    state = state,
                    rank = rank,
                    avatar = avatar,
                    animateScore = animateScore
                )
            }
        }
    }
}

/**
 * Compact score layout for minimal space
 */
@Composable
private fun CompactScoreLayout(
    playerName: String,
    score: Int,
    style: ScoreCardStyle,
    state: ScoreCardState,
    rank: Int?,
    avatar: @Composable (() -> Unit)?,
    animateScore: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (rank != null) {
                RankBadge(rank = rank, state = state)
            }
            
            if (avatar != null) {
                Box(
                    modifier = Modifier.size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    avatar()
                }
            }
            
            Text(
                text = playerName,
                style = GameTypography.PlayerName.copy(fontSize = 16.sp),
                color = getTextColor(style, state),
                maxLines = 1
            )
        }
        
        AnimatedScore(
            score = score,
            style = style,
            state = state,
            animate = animateScore,
            size = ScoreSize.Small
        )
    }
}

/**
 * Detailed score layout with trends and additional info
 */
@Composable
private fun DetailedScoreLayout(
    playerName: String,
    score: Int,
    previousScore: Int,
    style: ScoreCardStyle,
    state: ScoreCardState,
    showTrend: Boolean,
    rank: Int?,
    avatar: @Composable (() -> Unit)?,
    animateScore: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Player info row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (rank != null) {
                    RankBadge(rank = rank, state = state)
                }
                
                if (avatar != null) {
                    Box(
                        modifier = Modifier.size(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        avatar()
                    }
                }
                
                Text(
                    text = playerName,
                    style = GameTypography.PlayerName,
                    color = getTextColor(style, state)
                )
            }
            
            if (state == ScoreCardState.Winner) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Winner",
                    tint = gameShowColors.ScorePositive,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        // Score and trend row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            AnimatedScore(
                score = score,
                style = style,
                state = state,
                animate = animateScore,
                size = ScoreSize.Large
            )
            
            if (showTrend && score != previousScore) {
                ScoreTrend(
                    currentScore = score,
                    previousScore = previousScore,
                    style = style
                )
            }
        }
    }
}

/**
 * Leaderboard score layout for ranking display
 */
@Composable
private fun LeaderboardScoreLayout(
    playerName: String,
    score: Int,
    style: ScoreCardStyle,
    state: ScoreCardState,
    rank: Int?,
    avatar: @Composable (() -> Unit)?,
    animateScore: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank and player info
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            if (rank != null) {
                RankBadge(
                    rank = rank, 
                    state = state,
                    size = RankBadgeSize.Large
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (avatar != null) {
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        avatar()
                    }
                }
                
                Text(
                    text = playerName,
                    style = GameTypography.PlayerName.copy(fontSize = 18.sp),
                    color = getTextColor(style, state)
                )
            }
        }
        
        // Score
        AnimatedScore(
            score = score,
            style = style,
            state = state,
            animate = animateScore,
            size = ScoreSize.Medium
        )
    }
}

/**
 * Animated score display with counting effect
 */
@Composable
private fun AnimatedScore(
    score: Int,
    style: ScoreCardStyle,
    state: ScoreCardState,
    animate: Boolean,
    size: ScoreSize = ScoreSize.Medium
) {
    var animatedScore by remember { mutableIntStateOf(if (animate) 0 else score) }
    
    LaunchedEffect(score) {
        if (animate) {
            animatedScore = score
        }
    }
    
    val displayScore by animateIntAsState(
        targetValue = animatedScore,
        animationSpec = tween(
            durationMillis = Duration.ScoreUpdate.toInt(),
            easing = GameShowEasing.ScoreCount
        ),
        label = "Score Animation"
    )
    
    val fontSize = when (size) {
        ScoreSize.Small -> 18.sp
        ScoreSize.Medium -> 24.sp
        ScoreSize.Large -> 32.sp
    }
    
    AnimatedContent(
        targetState = displayScore,
        transitionSpec = {
            slideInVertically { height -> -height } + fadeIn() togetherWith
            slideOutVertically { height -> height } + fadeOut()
        },
        label = "Score Content Animation"
    ) { targetScore ->
        Text(
            text = formatScore(targetScore),
            style = GameTypography.ScoreDisplay.copy(fontSize = fontSize),
            color = getScoreColor(style, state),
            textAlign = TextAlign.End
        )
    }
}

/**
 * Score trend indicator
 */
@Composable
private fun ScoreTrend(
    currentScore: Int,
    previousScore: Int,
    style: ScoreCardStyle
) {
    val difference = currentScore - previousScore
    val isPositive = difference > 0
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = if (isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingUp,
            contentDescription = if (isPositive) "Score increased" else "Score decreased",
            tint = if (isPositive) gameShowColors.ScorePositive else gameShowColors.ScoreNegative,
            modifier = Modifier
                .size(16.dp)
                .scale(scaleX = 1f, scaleY = if (isPositive) 1f else -1f)
        )
        
        Text(
            text = formatScoreDifference(difference),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = if (isPositive) gameShowColors.ScorePositive else gameShowColors.ScoreNegative
        )
    }
}

/**
 * Rank badge component
 */
@Composable
private fun RankBadge(
    rank: Int,
    state: ScoreCardState,
    size: RankBadgeSize = RankBadgeSize.Standard
) {
    val badgeSize = when (size) {
        RankBadgeSize.Small -> 24.dp
        RankBadgeSize.Standard -> 32.dp
        RankBadgeSize.Large -> 40.dp
    }
    
    val fontSize = when (size) {
        RankBadgeSize.Small -> 12.sp
        RankBadgeSize.Standard -> 14.sp
        RankBadgeSize.Large -> 16.sp
    }
    
    Surface(
        modifier = Modifier.size(badgeSize),
        shape = CircleShape,
        color = getRankBadgeColor(rank, state)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rank.toString(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold
                ),
                color = getRankBadgeTextColor(rank, state)
            )
        }
    }
}

/**
 * Default avatar component
 */
@Composable
fun DefaultPlayerAvatar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    iconColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = backgroundColor
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Player Avatar",
                tint = iconColor
            )
        }
    }
}

/**
 * Style configurations
 */
data class ScoreCardStyle(
    val cornerRadius: androidx.compose.ui.unit.Dp = 12.dp,
    val layout: ScoreCardLayout = ScoreCardLayout.Detailed,
    val variant: ScoreCardVariant = ScoreCardVariant.Primary
)

enum class ScoreCardLayout {
    Compact,     // Minimal layout for tight spaces
    Detailed,    // Full layout with trends and details
    Leaderboard  // Optimized for ranking display
}

enum class ScoreCardVariant {
    Primary,     // Standard player card
    Secondary,   // Less prominent card
    Winner,      // Highlighted winner card
    Eliminated   // Grayed out eliminated player
}

enum class ScoreCardState {
    Normal,      // Default state
    Leading,     // Currently in the lead
    Winner,      // Game winner
    Eliminated   // Eliminated from game
}

enum class ScoreSize {
    Small,   // Compact score display
    Medium,  // Standard score display
    Large    // Prominent score display
}

enum class RankBadgeSize {
    Small,     // Compact rank badge
    Standard,  // Normal rank badge
    Large      // Prominent rank badge
}

/**
 * Helper functions
 */
@Composable
private fun getContainerColor(style: ScoreCardStyle, state: ScoreCardState): Color {
    return when (state) {
        ScoreCardState.Winner -> gameShowColors.CorrectAnswerContainer
        ScoreCardState.Leading -> MaterialTheme.colorScheme.primaryContainer
        ScoreCardState.Eliminated -> MaterialTheme.colorScheme.surfaceVariant
        else -> when (style.variant) {
            ScoreCardVariant.Winner -> gameShowColors.CorrectAnswerContainer
            ScoreCardVariant.Eliminated -> MaterialTheme.colorScheme.surfaceVariant
            else -> MaterialTheme.colorScheme.surface
        }
    }
}

@Composable
private fun getTextColor(style: ScoreCardStyle, state: ScoreCardState): Color {
    return when (state) {
        ScoreCardState.Winner -> gameShowColors.OnCorrectAnswerContainer
        ScoreCardState.Eliminated -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSurface
    }
}

@Composable
private fun getScoreColor(style: ScoreCardStyle, state: ScoreCardState): Color {
    return when (state) {
        ScoreCardState.Winner -> gameShowColors.OnCorrectAnswerContainer
        ScoreCardState.Leading -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }
}

@Composable
private fun getElevation(state: ScoreCardState): androidx.compose.ui.unit.Dp {
    return when (state) {
        ScoreCardState.Winner -> Elevation.GameShowSpotlight
        ScoreCardState.Leading -> Elevation.GameShowFocused
        else -> GameShowElevation.ScoreCard
    }
}

@Composable
private fun getBorder(style: ScoreCardStyle, state: ScoreCardState): androidx.compose.foundation.BorderStroke? {
    return when (state) {
        ScoreCardState.Leading -> androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )
        ScoreCardState.Winner -> androidx.compose.foundation.BorderStroke(
            width = 2.dp,
            color = gameShowColors.ScorePositive
        )
        else -> null
    }
}

@Composable
private fun getBackgroundBrush(style: ScoreCardStyle, state: ScoreCardState): Brush {
    return when (state) {
        ScoreCardState.Winner -> Brush.verticalGradient(
            colors = listOf(
                gameShowColors.ScorePositive.copy(alpha = 0.1f),
                Color.Transparent
            )
        )
        else -> Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Transparent)
        )
    }
}

@Composable
private fun getRankBadgeColor(rank: Int, state: ScoreCardState): Color {
    return when {
        rank == 1 && state == ScoreCardState.Winner -> gameShowColors.ScorePositive
        rank == 1 -> MaterialTheme.colorScheme.primary
        rank <= 3 -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.outline
    }
}

@Composable
private fun getRankBadgeTextColor(rank: Int, state: ScoreCardState): Color {
    return when {
        rank == 1 && state == ScoreCardState.Winner -> Color.White
        rank == 1 -> MaterialTheme.colorScheme.onPrimary
        rank <= 3 -> MaterialTheme.colorScheme.onSecondary
        else -> MaterialTheme.colorScheme.surface
    }
}

private fun formatScore(score: Int): String {
    val locale = Locale("tr", "TR")
    return NumberFormat.getNumberInstance(locale).format(score)
}

private fun formatScoreDifference(difference: Int): String {
    val prefix = if (difference > 0) "+" else ""
    return "$prefix${formatScore(difference)}"
}

/**
 * Preview composables
 */
@Preview(name = "ScoreCard Layouts")
@Composable
private fun ScoreCardLayoutsPreview() {
    PreviewThemes.LightPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScoreCard(
                playerName = "Ayşe Yılmaz",
                score = 1250,
                style = ScoreCardStyle(layout = ScoreCardLayout.Compact),
                rank = 1,
                avatar = { DefaultPlayerAvatar() }
            )
            
            ScoreCard(
                playerName = "Mehmet Özkan",
                score = 980,
                previousScore = 850,
                style = ScoreCardStyle(layout = ScoreCardLayout.Detailed),
                state = ScoreCardState.Leading,
                rank = 2,
                avatar = { DefaultPlayerAvatar() }
            )
            
            ScoreCard(
                playerName = "Fatma Şahin",
                score = 750,
                style = ScoreCardStyle(layout = ScoreCardLayout.Leaderboard),
                rank = 3,
                avatar = { DefaultPlayerAvatar() }
            )
        }
    }
}

@Preview(name = "ScoreCard States")
@Composable
private fun ScoreCardStatesPreview() {
    PreviewThemes.LightPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScoreCard(
                playerName = "Kazanan",
                score = 2500,
                state = ScoreCardState.Winner,
                rank = 1,
                avatar = { DefaultPlayerAvatar() }
            )
            
            ScoreCard(
                playerName = "Önde Giden",
                score = 1800,
                state = ScoreCardState.Leading,
                rank = 2,
                avatar = { DefaultPlayerAvatar() }
            )
            
            ScoreCard(
                playerName = "Elenen",
                score = 200,
                state = ScoreCardState.Eliminated,
                rank = 4,
                avatar = { DefaultPlayerAvatar() }
            )
        }
    }
}
