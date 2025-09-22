/*
 * Turkish Game Show Design System - Sample App
 * Demonstration of all design system components
 * Shows "Bir Kelime Bir İşlem" game interface examples
 */

package com.erdalgunes.kelimeislem.designsystem.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.erdalgunes.kelimeislem.designsystem.components.*
import com.erdalgunes.kelimeislem.designsystem.theme.GameTypography
import com.erdalgunes.kelimeislem.designsystem.theme.PreviewThemes
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowTheme
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowThemeExtensions.gameShowColors
import com.erdalgunes.kelimeislem.designsystem.tokens.GameShowSpacing

/**
 * Main sample app demonstrating the Turkish Game Show design system
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameShowSampleApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    
    val tabs = listOf("Oyun", "Skorlar", "Bileşenler", "Temalar")
    
    TurkishGameShowTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Bir Kelime Bir İşlem",
                            style = GameTypography.PlayerName.copy(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        )
                    }
                }
                
                when (selectedTab) {
                    0 -> GamePlayScreen()
                    1 -> ScoreboardScreen()
                    2 -> ComponentsScreen()
                    3 -> ThemesScreen()
                }
            }
        }
    }
}

/**
 * Game play screen demonstration
 */
@Composable
private fun GamePlayScreen() {
    var currentWord by remember { mutableStateOf("KELİME") }
    var currentScore by remember { mutableIntStateOf(1250) }
    var timerSeconds by remember { mutableIntStateOf(45) }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SectionTitle("Ana Oyun Ekranı")
        }
        
        item {
            // Timer section
            TimerBar(
                totalTimeSeconds = 60,
                currentTimeSeconds = timerSeconds,
                style = TimerStyle.Card,
                showWarningEffects = true,
                warningThresholdSeconds = 15,
                criticalThresholdSeconds = 5
            )
        }
        
        item {
            // Main word display
            WordBoard(
                word = currentWord,
                state = WordBoardState.Normal,
                style = WordBoardStyle(
                    variant = WordBoardVariant.Primary,
                    fontSize = 42.sp
                )
            )
        }
        
        item {
            // Game controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                BuzzerButton(
                    text = "BUZZER",
                    onClick = { 
                        currentWord = "DOĞRU"
                        currentScore += 100
                    },
                    size = BuzzerSize.Large,
                    style = BuzzerStyle.Primary,
                    icon = Icons.Default.NotificationsActive
                )
                
                BuzzerButton(
                    text = "İPUCU",
                    onClick = { currentWord = "İ_UCU" },
                    size = BuzzerSize.Medium,
                    style = BuzzerStyle.Secondary,
                    icon = Icons.Default.Lightbulb
                )
            }
        }
        
        item {
            // Player score
            ScoreCard(
                playerName = "Ayşe Yılmaz",
                score = currentScore,
                previousScore = currentScore - 100,
                style = ScoreCardStyle(layout = ScoreCardLayout.Detailed),
                state = ScoreCardState.Leading,
                rank = 1,
                avatar = { DefaultPlayerAvatar() }
            )
        }
        
        item {
            // Game controls row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CompactBuzzerButton(
                    text = "BAŞLA",
                    onClick = { timerSeconds = 60 },
                    style = BuzzerStyle.Success,
                    icon = Icons.Default.PlayArrow,
                    modifier = Modifier.weight(1f)
                )
                
                CompactBuzzerButton(
                    text = "DUR",
                    onClick = { timerSeconds = 0 },
                    style = BuzzerStyle.Danger,
                    icon = Icons.Default.Stop,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Scoreboard screen demonstration
 */
@Composable
private fun ScoreboardScreen() {
    val players = remember {
        listOf(
            Triple("Ayşe Yılmaz", 2500, ScoreCardState.Winner),
            Triple("Mehmet Özkan", 1800, ScoreCardState.Leading),
            Triple("Fatma Şahin", 1200, ScoreCardState.Normal),
            Triple("Ali Demir", 950, ScoreCardState.Normal),
            Triple("Zeynep Kaya", 400, ScoreCardState.Eliminated)
        )
    }
    
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            SectionTitle("Skor Tablosu")
        }
        
        item {
            Text(
                text = "Mevcut Durum",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        itemsIndexed(players) { index, (name, score, state) ->
            ScoreCard(
                playerName = name,
                score = score,
                previousScore = score - (50..200).random(),
                style = ScoreCardStyle(layout = ScoreCardLayout.Leaderboard),
                state = state,
                rank = index + 1,
                avatar = { DefaultPlayerAvatar() },
                showTrend = true
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Kompakt Görünüm",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        items(players.take(3)) { (name, score, state) ->
            ScoreCard(
                playerName = name,
                score = score,
                style = ScoreCardStyle(layout = ScoreCardLayout.Compact),
                state = state,
                rank = players.indexOf(Triple(name, score, state)) + 1,
                avatar = { DefaultPlayerAvatar() }
            )
        }
    }
}

/**
 * Components demonstration screen
 */
@Composable
private fun ComponentsScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SectionTitle("Bileşenler")
        }
        
        item {
            ComponentSection("Kelime Panoları") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    WordBoard(
                        word = "MERHABA",
                        state = WordBoardState.Normal,
                        style = WordBoardStyle(variant = WordBoardVariant.Primary)
                    )
                    
                    WordBoard(
                        word = "DOĞRU",
                        state = WordBoardState.Correct,
                        style = WordBoardStyle(variant = WordBoardVariant.Answer)
                    )
                    
                    WordBoard(
                        word = "YANLIŞ",
                        state = WordBoardState.Incorrect,
                        style = WordBoardStyle(variant = WordBoardVariant.Primary)
                    )
                }
            }
        }
        
        item {
            ComponentSection("Buzzer Düğmeleri") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        BuzzerButton(
                            text = "BUZZER",
                            onClick = {},
                            size = BuzzerSize.Large,
                            style = BuzzerStyle.Primary
                        )
                        
                        BuzzerButton(
                            text = "İPUCU",
                            onClick = {},
                            size = BuzzerSize.Medium,
                            style = BuzzerStyle.Secondary
                        )
                    }
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        CompactBuzzerButton(
                            text = "DOĞRU",
                            onClick = {},
                            style = BuzzerStyle.Success
                        )
                        
                        CompactBuzzerButton(
                            text = "YANLIŞ",
                            onClick = {},
                            style = BuzzerStyle.Danger
                        )
                        
                        OutlineBuzzerButton(
                            text = "GEÇME",
                            onClick = {},
                            style = BuzzerStyle.Secondary
                        )
                    }
                }
            }
        }
        
        item {
            ComponentSection("Zamanlayıcılar") {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TimerBar(
                        totalTimeSeconds = 60,
                        currentTimeSeconds = 30,
                        style = TimerStyle.Linear,
                        state = TimerState.Running
                    )
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
                    }
                }
            }
        }
        
        item {
            ComponentSection("Harf Kutucukları") {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val letters = "KELİME".toCharArray()
                    items(letters.size) { index ->
                        LetterTile(
                            letter = letters[index],
                            state = when (index) {
                                0 -> LetterTileState.Selected
                                1 -> LetterTileState.Highlighted
                                else -> LetterTileState.Normal
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Themes demonstration screen
 */
@Composable
private fun ThemesScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            SectionTitle("Tema Örnekleri")
        }
        
        item {
            ComponentSection("Renk Paleti") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ColorSwatch("Ana Renk", MaterialTheme.colorScheme.primary)
                    ColorSwatch("İkincil Renk", MaterialTheme.colorScheme.secondary)
                    ColorSwatch("Üçüncül Renk", MaterialTheme.colorScheme.tertiary)
                    ColorSwatch("Doğru Cevap", gameShowColors.CorrectAnswer)
                    ColorSwatch("Yanlış Cevap", gameShowColors.IncorrectAnswer)
                    ColorSwatch("Zamanlayıcı", gameShowColors.TimerNormal)
                }
            }
        }
        
        item {
            ComponentSection("Tipografi") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Kelime Gösterimi",
                        style = GameTypography.WordDisplay.copy(fontSize = 32.sp)
                    )
                    Text(
                        text = "Skor Gösterimi: 1.250",
                        style = GameTypography.ScoreDisplay.copy(fontSize = 24.sp)
                    )
                    Text(
                        text = "Oyuncu Adı",
                        style = GameTypography.PlayerName
                    )
                    Text(
                        text = "BUZZER METNİ",
                        style = GameTypography.BuzzerText
                    )
                    Text(
                        text = "Süre: 01:30",
                        style = GameTypography.TimerDisplay
                    )
                }
            }
        }
        
        item {
            ComponentSection("Yükseklik Seviyeleri") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ElevationCard("Seviye 1", 2.dp)
                    ElevationCard("Seviye 2", 4.dp)
                    ElevationCard("Seviye 3", 8.dp)
                    ElevationCard("Seviye 4", 16.dp)
                }
            }
        }
    }
}

/**
 * Helper composables for sample app
 */
@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ComponentSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            content()
        }
    }
}

@Composable
private fun ColorSwatch(name: String, color: androidx.compose.ui.graphics.Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(32.dp)
                .background(
                    color = color,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                )
        )
        
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ElevationCard(text: String, elevation: androidx.compose.ui.unit.Dp) {
    Card(
        modifier = Modifier.width(80.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Preview composables
 */
@Preview(name = "Game Show Sample App")
@Composable
fun GameShowSampleAppPreview() {
    GameShowSampleApp()
}

@Preview(name = "Game Play Screen")
@Composable
private fun GamePlayScreenPreview() {
    PreviewThemes.LightPreview {
        GamePlayScreen()
    }
}

@Preview(name = "Components Screen")
@Composable
private fun ComponentsScreenPreview() {
    PreviewThemes.LightPreview {
        ComponentsScreen()
    }
}