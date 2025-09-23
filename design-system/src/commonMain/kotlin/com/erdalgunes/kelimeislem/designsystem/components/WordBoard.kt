/*
 * Turkish Game Show Design System - WordBoard Component
 * Prominent word display component for "Bir Kelime Bir İşlem"
 * Supports Turkish characters and game show aesthetics
 */

package com.erdalgunes.kelimeislem.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erdalgunes.kelimeislem.designsystem.theme.GameTypography
import com.erdalgunes.kelimeislem.designsystem.theme.PreviewThemes
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowThemeExtensions.gameShowColors
import com.erdalgunes.kelimeislem.designsystem.tokens.Duration
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowEasing
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowElevation
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowSpacing

/**
 * WordBoard component for displaying game words prominently
 * Supports Turkish character display and game show aesthetics
 */
@Composable
fun WordBoard(
    word: String,
    modifier: Modifier = Modifier,
    style: WordBoardStyle = WordBoardStyle(),
    state: WordBoardState = WordBoardState.Normal,
    showLetters: Boolean = true,
    animateEntry: Boolean = true,
    onWordClick: (() -> Unit)? = null
) {
    val scale by animateFloatAsState(
        targetValue = when (state) {
            WordBoardState.Normal -> 1f
            WordBoardState.Highlighted -> 1.05f
            WordBoardState.Correct -> 1.1f
            WordBoardState.Incorrect -> 0.95f
        },
        animationSpec = tween(
            durationMillis = Duration.GameShowStandard.toInt(),
            easing = when (state) {
                WordBoardState.Correct -> GameShowEasing.CorrectBounce
                WordBoardState.Incorrect -> GameShowEasing.IncorrectShake
                else -> GameShowEasing.Standard
            }
        ),
        label = "WordBoard Scale"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (state == WordBoardState.Incorrect) 0.8f else 1f,
        animationSpec = tween(Duration.GameShowFast.toInt()),
        label = "WordBoard Alpha"
    )
    
    Card(
        modifier = modifier
            .scale(scale)
            .alpha(alpha),
        shape = RoundedCornerShape(style.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = getContainerColor(style, state)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = GameShowElevation.WordDisplay
        ),
        onClick = onWordClick ?: {}
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = getBackgroundBrush(style, state)
                )
                .padding(GameShowSpacing.WordDisplayPadding),
            contentAlignment = Alignment.Center
        ) {
            if (showLetters) {
                if (animateEntry) {
                    AnimatedWordDisplay(
                        word = word,
                        style = style,
                        state = state
                    )
                } else {
                    StaticWordDisplay(
                        word = word,
                        style = style,
                        state = state
                    )
                }
            } else {
                PlaceholderWordDisplay(style = style)
            }
        }
    }
}

/**
 * Animated word display with letter-by-letter reveal
 */
@Composable
private fun AnimatedWordDisplay(
    word: String,
    style: WordBoardStyle,
    state: WordBoardState
) {
    var displayedWord by remember(word) { mutableStateOf("") }
    
    AnimatedContent(
        targetState = word,
        transitionSpec = {
            slideInVertically(
                animationSpec = tween(
                    durationMillis = Duration.GameShowStandard.toInt(),
                    easing = GameShowEasing.GameShowReveal
                )
            ) { height -> height } togetherWith slideOutVertically(
                animationSpec = tween(
                    durationMillis = Duration.GameShowFast.toInt(),
                    easing = GameShowEasing.Standard
                )
            ) { height -> -height }
        },
        label = "WordBoard Animation"
    ) { targetWord ->
        Text(
            text = targetWord.uppercase(),
            style = getTextStyle(style, state),
            color = getTextColor(style, state),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * Static word display without animation
 */
@Composable
private fun StaticWordDisplay(
    word: String,
    style: WordBoardStyle,
    state: WordBoardState
) {
    Text(
        text = word.uppercase(),
        style = getTextStyle(style, state),
        color = getTextColor(style, state),
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

/**
 * Placeholder display when word is hidden
 */
@Composable
private fun PlaceholderWordDisplay(
    style: WordBoardStyle
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(6) { // Show 6 placeholder dashes
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}


/**
 * WordBoard style configuration
 */
data class WordBoardStyle(
    val cornerRadius: androidx.compose.ui.unit.Dp = 16.dp,
    val fontSize: androidx.compose.ui.unit.TextUnit = 48.sp,
    val fontWeight: FontWeight = FontWeight.ExtraBold,
    val variant: WordBoardVariant = WordBoardVariant.Primary
)

/**
 * WordBoard state
 */
enum class WordBoardState {
    Normal,
    Highlighted,
    Correct,
    Incorrect
}

/**
 * WordBoard variants
 */
enum class WordBoardVariant {
    Primary,    // Main game word
    Secondary,  // Hint or related word
    Answer,     // Correct answer display
    Question    // Question prompt
}

/**
 * Letter tile style configuration
 */
data class LetterTileStyle(
    val size: androidx.compose.ui.unit.Dp = 56.dp,
    val cornerRadius: androidx.compose.ui.unit.Dp = 8.dp,
    val fontSize: androidx.compose.ui.unit.TextUnit = 24.sp
)


/**
 * Helper functions for styling
 */
@Composable
private fun getContainerColor(style: WordBoardStyle, state: WordBoardState): Color {
    return when (state) {
        WordBoardState.Correct -> gameShowColors.CorrectAnswerContainer
        WordBoardState.Incorrect -> gameShowColors.IncorrectAnswerContainer
        else -> when (style.variant) {
            WordBoardVariant.Primary -> MaterialTheme.colorScheme.surface
            WordBoardVariant.Secondary -> MaterialTheme.colorScheme.surfaceVariant
            WordBoardVariant.Answer -> gameShowColors.CorrectAnswerContainer
            WordBoardVariant.Question -> MaterialTheme.colorScheme.primaryContainer
        }
    }
}

@Composable
private fun getTextColor(style: WordBoardStyle, state: WordBoardState): Color {
    return when (state) {
        WordBoardState.Correct -> gameShowColors.OnCorrectAnswerContainer
        WordBoardState.Incorrect -> gameShowColors.OnIncorrectAnswerContainer
        else -> when (style.variant) {
            WordBoardVariant.Primary -> MaterialTheme.colorScheme.onSurface
            WordBoardVariant.Secondary -> MaterialTheme.colorScheme.onSurfaceVariant
            WordBoardVariant.Answer -> gameShowColors.OnCorrectAnswerContainer
            WordBoardVariant.Question -> MaterialTheme.colorScheme.onPrimaryContainer
        }
    }
}

@Composable
private fun getTextStyle(style: WordBoardStyle, state: WordBoardState): TextStyle {
    return GameTypography.WordDisplay.copy(
        fontSize = style.fontSize,
        fontWeight = style.fontWeight
    )
}

@Composable
private fun getBackgroundBrush(style: WordBoardStyle, state: WordBoardState): Brush {
    return when (state) {
        WordBoardState.Highlighted -> Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
            )
        )
        else -> Brush.verticalGradient(
            colors = listOf(Color.Transparent, Color.Transparent)
        )
    }
}
