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

/**
 * Manages content display logic for the app
 */
class ContentManager {
    
    fun formatGreeting(message: String, platform: String): String {
        return when {
            platform.isEmpty() -> message
            message.contains(platform) -> message
            else -> "$message from $platform"
        }
    }
    
    fun validateTitle(title: String): Boolean {
        return title.isNotBlank() && title.length <= 100
    }
    
    fun processContent(
        greeting: String,
        title: String,
        config: TextConfiguration = TextConfiguration.default()
    ): ContentResult {
        val isValid = validateTitle(title) && greeting.isNotBlank()
        val processedGreeting = if (config.centerAlign) {
            greeting.trim()
        } else {
            greeting
        }
        
        return ContentResult(
            greeting = processedGreeting,
            title = title,
            isValid = isValid,
            configuration = config
        )
    }
    
    fun shouldRefreshContent(
        lastUpdateTime: Long,
        currentTime: Long,
        refreshInterval: Long = 60_000L // 1 minute
    ): Boolean {
        return currentTime - lastUpdateTime >= refreshInterval
    }
    
    fun generateDebugInfo(
        uiState: AppUiState,
        config: AppConfiguration
    ): String {
        return buildString {
            appendLine("=== Debug Info ===")
            appendLine("Greeting: ${uiState.greetingMessage}")
            appendLine("Title: ${uiState.appTitle}")
            appendLine("Loading: ${uiState.isLoading}")
            appendLine("Padding: ${config.padding}")
            appendLine("Animations: ${config.enableAnimations}")
        }
    }
}

/**
 * Result of content processing
 */
data class ContentResult(
    val greeting: String,
    val title: String,
    val isValid: Boolean,
    val configuration: TextConfiguration
) {
    fun toDisplayString(): String {
        return "$greeting\n$title"
    }
    
    fun hasContent(): Boolean {
        return greeting.isNotBlank() || title.isNotBlank()
    }
}

/**
 * Content utilities
 */
object ContentUtils {
    fun truncateText(text: String, maxLength: Int = 50): String {
        return if (text.length > maxLength) {
            "${text.take(maxLength)}..."
        } else {
            text
        }
    }
    
    fun countWords(text: String): Int {
        return text.split("\\s+".toRegex()).filter { it.isNotBlank() }.size
    }
    
    fun extractPlatformFromGreeting(greeting: String): String? {
        val regex = "from (.+?)(!|$)".toRegex()
        val match = regex.find(greeting)
        return match?.groupValues?.getOrNull(1)?.trim()
    }
    
    fun createPlaceholderContent(): ContentResult {
        return ContentResult(
            greeting = "Loading...",
            title = "Please wait",
            isValid = false,
            configuration = TextConfiguration.default()
        )
    }
}