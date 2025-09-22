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

package com.erdalgunes.kelimeislem

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import currentTimeMillis

data class AppUiState(
    val greetingMessage: String = "",
    val appTitle: String = "Bir Kelime Bir İşlem",
    val isLoading: Boolean = false
)

class AppViewModel {
    private val _uiState = MutableStateFlow(AppUiState())
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()
    
    private val contentManager = ContentManager()
    private var lastUpdateTime = 0L
    
    init {
        loadGreeting()
    }
    
    private fun loadGreeting() {
        val greeting = Greeting()
        val platform = getPlatform()
        val formattedGreeting = contentManager.formatGreeting(
            greeting.greet(), 
            platform.name
        )
        _uiState.value = _uiState.value.copy(
            greetingMessage = formattedGreeting
        )
        lastUpdateTime = getCurrentTime()
    }
    
    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadGreeting()
        _uiState.value = _uiState.value.copy(isLoading = false)
    }
    
    fun shouldAutoRefresh(): Boolean {
        return contentManager.shouldRefreshContent(
            lastUpdateTime,
            getCurrentTime()
        )
    }
    
    fun getContentResult(): ContentResult {
        return contentManager.processContent(
            _uiState.value.greetingMessage,
            _uiState.value.appTitle
        )
    }
    
    fun getDebugInfo(config: AppConfiguration): String {
        return contentManager.generateDebugInfo(_uiState.value, config)
    }
    
    private fun getCurrentTime(): Long = currentTimeMillis()
}