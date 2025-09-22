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

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Configuration for the app's UI layout and styling
 */
data class AppConfiguration(
    val padding: Dp = 16.dp,
    val spaceBetweenElements: Dp = 16.dp,
    val enableAnimations: Boolean = true,
    val debugMode: Boolean = false
) {
    companion object {
        fun default() = AppConfiguration()
        
        fun debug() = AppConfiguration(
            debugMode = true,
            enableAnimations = false
        )
        
        fun tablet() = AppConfiguration(
            padding = 32.dp,
            spaceBetweenElements = 24.dp
        )
    }
}

/**
 * Text configuration for the app
 */
data class TextConfiguration(
    val greetingStyle: TextStyle = TextStyle.Headline,
    val titleStyle: TextStyle = TextStyle.Title,
    val centerAlign: Boolean = true
) {
    enum class TextStyle {
        Headline,
        Title,
        Body,
        Caption
    }
    
    companion object {
        fun default() = TextConfiguration()
        
        fun largeText() = TextConfiguration(
            greetingStyle = TextStyle.Headline,
            titleStyle = TextStyle.Headline
        )
        
        fun leftAligned() = TextConfiguration(
            centerAlign = false
        )
    }
}

/**
 * Utility functions for app configuration
 */
object AppConfigUtils {
    fun calculateTotalPadding(config: AppConfiguration): Dp {
        return config.padding * 2
    }
    
    fun isTabletConfiguration(config: AppConfiguration): Boolean {
        return config.padding.value > 20f
    }
    
    fun shouldShowDebugInfo(config: AppConfiguration): Boolean {
        return config.debugMode && !config.enableAnimations
    }
    
    fun validateConfiguration(config: AppConfiguration): Boolean {
        return config.padding.value > 0 && 
               config.spaceBetweenElements.value >= 0
    }
    
    fun mergeConfigurations(
        base: AppConfiguration,
        override: AppConfiguration?
    ): AppConfiguration {
        return override ?: base
    }
}