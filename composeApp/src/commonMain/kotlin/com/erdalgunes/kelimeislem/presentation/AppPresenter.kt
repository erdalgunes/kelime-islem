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

package com.erdalgunes.kelimeislem.presentation

import com.erdalgunes.kelimeislem.ContentManager
import com.erdalgunes.kelimeislem.Greeting
import com.erdalgunes.kelimeislem.getPlatform
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Application state following Circuit pattern.
 * This is a pure data class with no behavior.
 */
data class AppState(
    val greetingMessage: String = "",
    val appTitle: String = "Bir Kelime Bir İşlem",
    val isLoading: Boolean = false,
    val lastRefreshTime: Long = 0L
)

/**
 * Presenter for the main application screen.
 * Contains all business logic and is fully testable.
 */
class AppPresenter {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()
    
    private val contentManager = ContentManager()
    private val greeting = Greeting()
    private val platform = getPlatform()
    
    init {
        loadInitialContent()
    }
    
    /**
     * Loads the initial greeting content.
     * This is business logic that should be tested.
     */
    private fun loadInitialContent() {
        val greetingText = greeting.greet()
        val formattedGreeting = contentManager.formatGreeting(
            greetingText,
            platform.name
        )
        
        _state.value = _state.value.copy(
            greetingMessage = formattedGreeting,
            lastRefreshTime = System.currentTimeMillis()
        )
    }
    
    /**
     * Refreshes the content.
     * Returns true if refresh was needed, false otherwise.
     */
    fun refresh(forceRefresh: Boolean = false): Boolean {
        val currentTime = System.currentTimeMillis()
        val shouldRefresh = forceRefresh || 
            contentManager.shouldRefreshContent(
                _state.value.lastRefreshTime,
                currentTime
            )
        
        if (shouldRefresh) {
            _state.value = _state.value.copy(isLoading = true)
            
            val greetingText = greeting.greet()
            val formattedGreeting = contentManager.formatGreeting(
                greetingText,
                platform.name
            )
            
            _state.value = _state.value.copy(
                greetingMessage = formattedGreeting,
                isLoading = false,
                lastRefreshTime = currentTime
            )
        }
        
        return shouldRefresh
    }
    
    /**
     * Validates the current state.
     * Business logic for state validation.
     */
    fun validateState(): StateValidation {
        val state = _state.value
        val hasGreeting = state.greetingMessage.isNotBlank()
        val hasTitle = contentManager.validateTitle(state.appTitle)
        val isStale = System.currentTimeMillis() - state.lastRefreshTime > 300_000L // 5 minutes
        
        return StateValidation(
            isValid = hasGreeting && hasTitle,
            hasContent = hasGreeting || hasTitle,
            isStale = isStale,
            errors = buildList {
                if (!hasGreeting) add("Missing greeting message")
                if (!hasTitle) add("Invalid title")
                if (isStale) add("Content is stale")
            }
        )
    }
    
    /**
     * Gets analytics data about the current state.
     * Pure business logic for analytics.
     */
    fun getAnalytics(): AppAnalytics {
        val state = _state.value
        return AppAnalytics(
            sessionDuration = System.currentTimeMillis() - state.lastRefreshTime,
            greetingLength = state.greetingMessage.length,
            titleLength = state.appTitle.length,
            platformName = platform.name,
            refreshCount = 0 // Would track this in real app
        )
    }
}

/**
 * State validation result.
 * Pure data class for validation results.
 */
data class StateValidation(
    val isValid: Boolean,
    val hasContent: Boolean,
    val isStale: Boolean,
    val errors: List<String>
)

/**
 * Analytics data for the application.
 * Pure data class for analytics.
 */
data class AppAnalytics(
    val sessionDuration: Long,
    val greetingLength: Int,
    val titleLength: Int,
    val platformName: String,
    val refreshCount: Int
)