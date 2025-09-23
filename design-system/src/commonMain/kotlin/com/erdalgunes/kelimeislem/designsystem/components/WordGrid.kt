/*
 * Modern Turkish Word Game Design System - WordGrid Component
 * Interactive grid for word formation and display
 * Optimized for Turkish word game mechanics
 */

package com.erdalgunes.kelimeislem.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erdalgunes.kelimeislem.designsystem.theme.PreviewThemes
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowThemeExtensions.gameShowColors
import com.erdalgunes.kelimeislem.designsystem.tokens.Duration
import com.erdalgunes.kelimeislem.designsystem.tokens.Elevation
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowEasing
import java.util.Locale

/**
 * Interactive word formation grid for Turkish word games
 * 
 * Features:
 * - Drag and drop letter placement (conceptual - actual implementation would need gesture handling)
 * - Word validation visual feedback
 * - Turkish character support
 * - Responsive grid sizing
 * - Animation support for letter placement
 * - Multiple word display modes
 */
@Composable
fun WordGrid(
    words: List<WordEntry>,
    modifier: Modifier = Modifier,
    maxLettersPerWord: Int = 12,
    style: WordGridStyle = WordGridStyle.Vertical,
    showWordScores: Boolean = true,
    onWordClick: ((WordEntry) -> Unit)? = null
) {
    when (style) {
        WordGridStyle.Vertical -> {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(words) { word ->
                    WordRow(
                        word = word,
                        maxLetters = maxLettersPerWord,
                        showScore = showWordScores,
                        onClick = onWordClick
                    )
                }
            }
        }
        WordGridStyle.Horizontal -> {
            LazyRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(words) { word ->
                    WordColumn(
                        word = word,
                        maxLetters = maxLettersPerWord,
                        showScore = showWordScores,
                        onClick = onWordClick
                    )
                }
            }
        }
    }
}

/**
 * Single word formation area for building words
 */
@Composable
fun WordFormationArea(
    currentWord: String,
    maxLength: Int = 12,
    modifier: Modifier = Modifier,
    state: WordValidationState = WordValidationState.Building,
    onLetterRemove: ((Int) -> Unit)? = null
) {
    val scale by animateFloatAsState(
        targetValue = when (state) {
            WordValidationState.Valid -> 1.05f
            WordValidationState.Invalid -> 0.95f
            else -> 1f
        },
        animationSpec = tween(
            durationMillis = Duration.Medium2.toInt(),
            easing = GameShowEasing.GameShowBounce
        ),
        label = "Formation Area Scale"
    )
    
    val borderColor by animateColorAsState(
        targetValue = when (state) {
            WordValidationState.Valid -> gameShowColors.CorrectAnswer
            WordValidationState.Invalid -> gameShowColors.IncorrectAnswer
            else -> MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(
            durationMillis = Duration.Medium1.toInt(),
            easing = GameShowEasing.Standard
        ),
        label = "Border Color"
    )
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = when (state) {
                WordValidationState.Valid -> gameShowColors.CorrectAnswerContainer.copy(alpha = 0.1f)
                WordValidationState.Invalid -> gameShowColors.IncorrectAnswerContainer.copy(alpha = 0.1f)
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Elevation.Level2
        ),
        border = BorderStroke(2.dp, borderColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            itemsIndexed(currentWord.toList()) { index, letter ->
                LetterSlot(
                    letter = letter,
                    position = index,
                    onRemove = onLetterRemove
                )
            }
            
            // Empty slots for remaining letters
            items(maxLength - currentWord.length) {
                EmptyLetterSlot()
            }
        }
    }
}

/**
 * Individual letter slot in the formation area
 */
@Composable
private fun LetterSlot(
    letter: Char,
    position: Int,
    onRemove: ((Int) -> Unit)? = null
) {
    AnimatedVisibility(
        visible = true,
        enter = scaleIn(
            animationSpec = tween(
                durationMillis = Duration.Medium2.toInt(),
                easing = GameShowEasing.GameShowBounce
            )
        ) + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        LetterTile(
            letter = letter,
            state = LetterTileState.Selected,
            size = LetterTileSize.Standard,
            onClick = if (onRemove != null) { _ -> onRemove(position) } else null
        )
    }
}

/**
 * Empty slot for letter placement
 */
@Composable
private fun EmptyLetterSlot() {
    Card(
        modifier = Modifier.size(48.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Elevation.Level1
        ),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )
    ) {
        Box(
            modifier = Modifier.size(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add letter",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(16.dp)
                    .alpha(0.6f)
            )
        }
    }
}

/**
 * Word display in horizontal row format
 */
@Composable
private fun WordRow(
    word: WordEntry,
    maxLetters: Int,
    showScore: Boolean,
    onClick: ((WordEntry) -> Unit)?
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = if (onClick != null) { { onClick(word) } } else { {} },
        colors = CardDefaults.cardColors(
            containerColor = when (word.state) {
                WordValidationState.Valid -> gameShowColors.CorrectAnswerContainer.copy(alpha = 0.1f)
                WordValidationState.Invalid -> gameShowColors.IncorrectAnswerContainer.copy(alpha = 0.1f)
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = Elevation.Level1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(word.text.toList()) { letter ->
                    LetterTile(
                        letter = letter,
                        state = when (word.state) {
                            WordValidationState.Valid -> LetterTileState.Correct
                            WordValidationState.Invalid -> LetterTileState.Incorrect
                            else -> LetterTileState.Used
                        },
                        size = LetterTileSize.Small
                    )
                }
            }
            
            if (showScore && word.score > 0) {
                Text(
                    text = word.score.toString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

/**
 * Word display in vertical column format
 */
@Composable
private fun WordColumn(
    word: WordEntry,
    maxLetters: Int,
    showScore: Boolean,
    onClick: ((WordEntry) -> Unit)?
) {
    Card(
        onClick = if (onClick != null) { { onClick(word) } } else { {} },
        colors = CardDefaults.cardColors(
            containerColor = when (word.state) {
                WordValidationState.Valid -> gameShowColors.CorrectAnswerContainer.copy(alpha = 0.1f)
                WordValidationState.Invalid -> gameShowColors.IncorrectAnswerContainer.copy(alpha = 0.1f)
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            word.text.forEach { letter ->
                LetterTile(
                    letter = letter,
                    state = when (word.state) {
                        WordValidationState.Valid -> LetterTileState.Correct
                        WordValidationState.Invalid -> LetterTileState.Incorrect
                        else -> LetterTileState.Used
                    },
                    size = LetterTileSize.Small
                )
            }
            
            if (showScore && word.score > 0) {
                Text(
                    text = word.score.toString(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Word entry data class
 */
data class WordEntry(
    val text: String,
    val score: Int = 0,
    val state: WordValidationState = WordValidationState.Building
) {
    val length: Int get() = text.length
    val isValid: Boolean get() = state == WordValidationState.Valid
    
    companion object {
        fun fromString(text: String, score: Int = 0): WordEntry {
            return WordEntry(
                text = text.uppercase(Locale("tr", "TR")),
                score = score,
                state = WordValidationState.Building
            )
        }
    }
}

/**
 * Word validation states
 */
enum class WordValidationState {
    Building,    // Currently being formed
    Valid,       // Valid Turkish word
    Invalid      // Invalid or non-existent word
}

/**
 * Word grid display styles
 */
enum class WordGridStyle {
    Vertical,    // Words displayed in vertical list
    Horizontal   // Words displayed in horizontal scroll
}

/**
 * Preview composables
 */
@Preview(name = "Word Grid Components")
@Composable
private fun WordGridPreview() {
    PreviewThemes.LightPreview {
        var currentWord by remember { mutableStateOf("KELIME") }
        val sampleWords = listOf(
            WordEntry("MERHABA", 15, WordValidationState.Valid),
            WordEntry("OYUN", 8, WordValidationState.Valid),
            WordEntry("TÜRKİYE", 18, WordValidationState.Valid),
            WordEntry("ÇÖZÜM", 12, WordValidationState.Valid),
            WordEntry("ĞÜZEL", 0, WordValidationState.Invalid)
        )
        
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Word Formation Area", style = MaterialTheme.typography.titleMedium)
            WordFormationArea(
                currentWord = currentWord,
                state = WordValidationState.Valid,
                onLetterRemove = { index ->
                    currentWord = currentWord.removeRange(index, index + 1)
                }
            )
            
            Text("Word Grid - Vertical", style = MaterialTheme.typography.titleMedium)
            WordGrid(
                words = sampleWords,
                style = WordGridStyle.Vertical,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}