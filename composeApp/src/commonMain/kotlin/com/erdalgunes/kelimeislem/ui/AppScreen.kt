/*
 * Copyright 2025 Bir Kelime Bir İşlem Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.erdalgunes.kelimeislem.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.erdalgunes.kelimeislem.presentation.AppPresenter
import com.erdalgunes.kelimeislem.presentation.AppState
import com.erdalgunes.kelimeislem.designsystem.components.LetterTile
import com.erdalgunes.kelimeislem.designsystem.components.LetterTileState
import com.erdalgunes.kelimeislem.designsystem.components.MinimalCountdown
import com.erdalgunes.kelimeislem.designsystem.components.SimpleGameButton
import com.erdalgunes.kelimeislem.designsystem.components.WordBoard
import com.erdalgunes.kelimeislem.designsystem.theme.TurkishGameShowTheme

/**
 * Main application screen using Circuit pattern.
 * This is a minimal Composable that only renders UI based on state.
 * All business logic is in the Presenter.
 * 
 * This file is excluded from code coverage as it's pure UI.
 */
@Composable
fun AppScreen(
    modifier: Modifier = Modifier
) {
    val presenter = remember { AppPresenter() }
    val state by presenter.state.collectAsState()
    
    AppContent(
        state = state,
        modifier = modifier
    )
}

/**
 * Pure UI content that renders based on state.
 * Separated for easier testing with Paparazzi.
 * No business logic, just UI rendering.
 */
@Composable
fun AppContent(
    state: AppState,
    modifier: Modifier = Modifier
) {
    TurkishGameShowTheme {
        if (state.isLoading) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Text(
                        text = state.greetingMessage,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center
                    )
                }
                
                item {
                    Text(
                        text = "Modern Turkish Word Game Design System",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
                
                item {
                    WordBoard(word = "TÜRKÇE")
                }
                
                item {
                    androidx.compose.foundation.layout.Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        "KELIME".forEach { char ->
                            LetterTile(
                                letter = char,
                                state = LetterTileState.Available,
                                points = (1..5).random()
                            )
                        }
                    }
                }
                
                item {
                    MinimalCountdown(
                        totalSeconds = 60,
                        currentSeconds = 45
                    )
                }
                
                item {
                    SimpleGameButton(
                        text = "Kelime Gönder",
                        onClick = { }
                    )
                }
            }
        }
    }
}