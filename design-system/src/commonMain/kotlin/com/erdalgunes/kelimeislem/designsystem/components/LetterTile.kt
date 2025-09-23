/*
 * Modern Turkish Word Game Design System - LetterTile Component
 * Interactive letter tiles optimized for Turkish character set
 * Core component for word formation games
 */

package com.erdalgunes.kelimeislem.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
// Preview import moved to Android source set
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erdalgunes.kelimeislem.designsystem.theme.PreviewThemes
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowThemeExtensions.gameShowColors
import com.erdalgunes.kelimeislem.designsystem.tokens.Duration
import com.erdalgunes.kelimeislem.designsystem.tokens.Elevation
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowEasing
// Locale moved to platform-specific implementation

/**
 * Converts Turkish characters to uppercase using Turkish locale rules.
 * This is a simplified version that handles the most common Turkish characters.
 */
private fun String.toTurkishUppercase(): String {
    return this.uppercase().replace("I", "İ").replace("i", "İ")
}

/**
 * Interactive letter tile for Turkish word games
 * 
 * Features:
 * - Full Turkish character support (ç, ğ, ı, İ, ö, ş, ü)
 * - Touch-optimized 48dp minimum size
 * - State-based visual feedback
 * - Haptic feedback for interactions
 * - Letter value scoring support
 * - Smooth press animations
 */
@Composable
fun LetterTile(
    letter: Char,
    modifier: Modifier = Modifier,
    state: LetterTileState = LetterTileState.Available,
    points: Int? = null,
    size: LetterTileSize = LetterTileSize.Standard,
    onClick: ((Char) -> Unit)? = null,
    hapticEnabled: Boolean = true
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    val scale by animateFloatAsState(
        targetValue = when {
            isPressed -> 0.95f
            state == LetterTileState.Selected -> 1.05f
            else -> 1f
        },
        animationSpec = tween(
            durationMillis = Duration.GameShowFast.toInt(),
            easing = GameShowEasing.GameShowSnap
        ),
        label = "Letter Tile Scale"
    )
    
    val containerColor by animateColorAsState(
        targetValue = when (state) {
            LetterTileState.Available -> MaterialTheme.colorScheme.surface
            LetterTileState.Selected -> MaterialTheme.colorScheme.primaryContainer
            LetterTileState.Used -> MaterialTheme.colorScheme.surfaceVariant
            LetterTileState.Correct -> gameShowColors.CorrectAnswerContainer
            LetterTileState.Incorrect -> gameShowColors.IncorrectAnswerContainer
            LetterTileState.Disabled -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        animationSpec = tween(
            durationMillis = Duration.Medium2.toInt(),
            easing = GameShowEasing.Standard
        ),
        label = "Container Color"
    )
    
    val contentColor by animateColorAsState(
        targetValue = when (state) {
            LetterTileState.Available -> MaterialTheme.colorScheme.onSurface
            LetterTileState.Selected -> MaterialTheme.colorScheme.onPrimaryContainer
            LetterTileState.Used -> MaterialTheme.colorScheme.onSurfaceVariant
            LetterTileState.Correct -> gameShowColors.OnCorrectAnswerContainer
            LetterTileState.Incorrect -> gameShowColors.OnIncorrectAnswerContainer
            LetterTileState.Disabled -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        },
        animationSpec = tween(
            durationMillis = Duration.Medium2.toInt(),
            easing = GameShowEasing.Standard
        ),
        label = "Content Color"
    )
    
    val borderColor = when (state) {
        LetterTileState.Selected -> MaterialTheme.colorScheme.primary
        LetterTileState.Correct -> gameShowColors.CorrectAnswer
        LetterTileState.Incorrect -> gameShowColors.IncorrectAnswer
        else -> Color.Transparent
    }
    
    val dimensions = when (size) {
        LetterTileSize.Small -> TileDimensions(40.dp, 18.sp, 12.sp)
        LetterTileSize.Standard -> TileDimensions(48.dp, 24.sp, 14.sp)
        LetterTileSize.Large -> TileDimensions(56.dp, 28.sp, 16.sp)
    }
    
    Card(
        modifier = modifier
            .scale(scale)
            .size(dimensions.size),
        onClick = if (onClick != null && state != LetterTileState.Disabled) {
            {
                if (hapticEnabled) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                }
                onClick(letter)
            }
        } else {
            {}
        },
        enabled = state != LetterTileState.Disabled,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = when (state) {
                LetterTileState.Selected -> Elevation.GameShowElement
                LetterTileState.Available -> Elevation.Level2
                else -> Elevation.Level1
            }
        ),
        border = if (borderColor != Color.Transparent) {
            BorderStroke(2.dp, borderColor)
        } else null,
        shape = RoundedCornerShape(8.dp),
        interactionSource = interactionSource
    ) {
        Box(
            modifier = Modifier.size(dimensions.size),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = letter.toString().toTurkishUppercase(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = dimensions.letterSize,
                        fontWeight = FontWeight.Bold
                    ),
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
                
                if (points != null && points > 0) {
                    Text(
                        text = points.toString(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = dimensions.pointSize,
                            fontWeight = FontWeight.Medium
                        ),
                        color = contentColor.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Letter tile states
 */
enum class LetterTileState {
    Available,   // Ready to be selected
    Selected,    // Currently selected
    Used,        // Already used in word
    Correct,     // Correctly placed
    Incorrect,   // Incorrectly placed
    Disabled     // Not interactive
}

/**
 * Letter tile sizes
 */
enum class LetterTileSize {
    Small,       // 40dp - for compact layouts
    Standard,    // 48dp - default size
    Large        // 56dp - for prominent display
}

/**
 * Internal data class for tile dimensions
 */
private data class TileDimensions(
    val size: androidx.compose.ui.unit.Dp,
    val letterSize: androidx.compose.ui.unit.TextUnit,
    val pointSize: androidx.compose.ui.unit.TextUnit
)

/**
 * Turkish alphabet helper
 */
object TurkishAlphabet {
    val letters = listOf(
        'A', 'B', 'C', 'Ç', 'D', 'E', 'F', 'G', 'Ğ', 'H', 'I', 'İ', 'J', 'K', 'L', 'M',
        'N', 'O', 'Ö', 'P', 'R', 'S', 'Ş', 'T', 'U', 'Ü', 'V', 'Y', 'Z'
    )
    
    val vowels = setOf('A', 'E', 'I', 'İ', 'O', 'Ö', 'U', 'Ü')
    val consonants = letters.filterNot { it in vowels }
    
    fun isVowel(char: Char): Boolean = char.toString().toTurkishUppercase().first() in vowels
}

/**
 * Preview composables
 */
// @Preview(name = "Letter Tile States") - Moved to Android source set
@Composable
private fun LetterTileStatesPreview() {
    PreviewThemes.LightPreview {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Tile States", style = MaterialTheme.typography.titleMedium)
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LetterTile(
                    letter = 'A',
                    state = LetterTileState.Available,
                    points = 1
                )
                LetterTile(
                    letter = 'Ç',
                    state = LetterTileState.Selected,
                    points = 4
                )
                LetterTile(
                    letter = 'K',
                    state = LetterTileState.Used,
                    points = 5
                )
                LetterTile(
                    letter = 'İ',
                    state = LetterTileState.Correct,
                    points = 1
                )
                LetterTile(
                    letter = 'Ş',
                    state = LetterTileState.Incorrect,
                    points = 4
                )
            }
            
            Text("Tile Sizes", style = MaterialTheme.typography.titleMedium)
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LetterTile(
                    letter = 'Ğ',
                    size = LetterTileSize.Small,
                    points = 5
                )
                LetterTile(
                    letter = 'Ö',
                    size = LetterTileSize.Standard,
                    points = 3
                )
                LetterTile(
                    letter = 'Ü',
                    size = LetterTileSize.Large,
                    points = 3
                )
            }
            
            Text("Turkish Characters", style = MaterialTheme.typography.titleMedium)
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                "ÇĞIİÖŞÜ".forEach { char ->
                    LetterTile(
                        letter = char,
                        points = (1..8).random(),
                        size = LetterTileSize.Standard
                    )
                }
            }
        }
    }
}