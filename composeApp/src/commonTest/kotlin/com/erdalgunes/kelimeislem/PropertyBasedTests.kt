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

import androidx.compose.ui.unit.dp
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import kotlin.random.Random

class PropertyBasedTests : FreeSpec({
    
    "Property-based testing" - {
        
        "AppConfiguration properties" - {
            "padding should always be non-negative after creation" {
                repeat(100) {
                    val padding = Random.nextInt(0, 100)
                    val config = AppConfiguration(padding = padding.dp)
                    config.padding.value shouldBeGreaterThanOrEqualTo 0f
                }
            }
            
            "total padding should be double the single padding" {
                repeat(50) {
                    val padding = Random.nextInt(1, 50)
                    val config = AppConfiguration(padding = padding.dp)
                    val total = AppConfigUtils.calculateTotalPadding(config)
                    
                    total.value shouldBe (padding * 2).toFloat()
                }
            }
            
            "tablet detection should be consistent" {
                repeat(100) {
                    val padding = Random.nextInt(0, 100)
                    val config = AppConfiguration(padding = padding.dp)
                    val isTablet = AppConfigUtils.isTabletConfiguration(config)
                    
                    if (padding > 20) {
                        isTablet shouldBe true
                    } else {
                        isTablet shouldBe false
                    }
                }
            }
        }
        
        "ContentManager properties" - {
            "title validation should be consistent" {
                val manager = ContentManager()
                
                // Test various string lengths
                repeat(100) {
                    val length = Random.nextInt(0, 150)
                    val title = "a".repeat(length)
                    val isValid = manager.validateTitle(title)
                    
                    if (length in 1..100) {
                        isValid shouldBe true
                    } else {
                        isValid shouldBe false
                    }
                }
            }
            
            "word counting should handle various inputs" {
                val testCases = listOf(
                    "single" to 1,
                    "two words" to 2,
                    "  spaces  between  " to 2,
                    "one\ttab" to 2,
                    "new\nline" to 2,
                    "" to 0,
                    "   " to 0
                )
                
                testCases.forEach { (text, expected) ->
                    ContentUtils.countWords(text) shouldBe expected
                }
            }
            
            "text truncation should maintain max length" {
                repeat(50) {
                    val length = Random.nextInt(1, 200)
                    val text = "x".repeat(length)
                    val maxLength = Random.nextInt(10, 100)
                    val truncated = ContentUtils.truncateText(text, maxLength)
                    
                    if (length > maxLength) {
                        truncated.length shouldBe (maxLength + 3) // +3 for "..."
                    } else {
                        truncated.length shouldBe length
                    }
                }
            }
            
            "refresh timing should be monotonic" {
                val manager = ContentManager()
                
                repeat(50) {
                    val lastUpdate = Random.nextLong(0, 100000)
                    val current = lastUpdate + Random.nextLong(0, 200000)
                    val interval = Random.nextLong(1000, 60000)
                    
                    val shouldRefresh = manager.shouldRefreshContent(lastUpdate, current, interval)
                    
                    if (current - lastUpdate > interval) {
                        shouldRefresh shouldBe true
                    } else {
                        shouldRefresh shouldBe false
                    }
                }
            }
        }
        
        "ContentResult properties" - {
            "hasContent should be true if any field is non-blank" {
                repeat(50) {
                    val greeting = if (Random.nextBoolean()) "Hello" else ""
                    val title = if (Random.nextBoolean()) "Title" else ""
                    
                    val result = ContentResult(
                        greeting = greeting,
                        title = title,
                        isValid = true,
                        configuration = TextConfiguration.default()
                    )
                    
                    val expected = greeting.isNotBlank() || title.isNotBlank()
                    result.hasContent() shouldBe expected
                }
            }
            
            "toDisplayString should always contain newline" {
                repeat(20) {
                    val result = ContentResult(
                        greeting = "Line1",
                        title = "Line2",
                        isValid = true,
                        configuration = TextConfiguration.default()
                    )
                    
                    val display = result.toDisplayString()
                    display shouldBe "Line1\nLine2"
                }
            }
        }
        
        "Platform extraction regex" - {
            "should handle various greeting formats" {
                val testCases = mapOf(
                    "Hello from Android!" to "Android",
                    "Greetings from iOS" to "iOS", 
                    "Message from Desktop 2.0!" to "Desktop 2.0",
                    "Welcome from Web Browser" to "Web Browser",
                    "No platform here" to null,
                    "Ending from" to null
                )
                
                testCases.forEach { (input, expected) ->
                    val result = ContentUtils.extractPlatformFromGreeting(input)
                    result shouldBe expected
                }
            }
        }
        
        "TextConfiguration combinations" - {
            "all TextStyle values should be distinct" {
                val styles = TextConfiguration.TextStyle.values()
                styles.toSet().size shouldBe styles.size
                
                // Each style should not equal others
                styles.forEach { style1 ->
                    styles.forEach { style2 ->
                        if (style1 != style2) {
                            style1 shouldNotBe style2
                        }
                    }
                }
            }
        }
        
        "AppUiState invariants" - {
            "copy should preserve unmodified fields" {
                repeat(30) {
                    val original = AppUiState(
                        greetingMessage = "Original",
                        appTitle = "Title",
                        isLoading = Random.nextBoolean()
                    )
                    
                    val copied = original.copy(greetingMessage = "Modified")
                    
                    copied.greetingMessage shouldBe "Modified"
                    copied.appTitle shouldBe original.appTitle
                    copied.isLoading shouldBe original.isLoading
                }
            }
        }
    }
})