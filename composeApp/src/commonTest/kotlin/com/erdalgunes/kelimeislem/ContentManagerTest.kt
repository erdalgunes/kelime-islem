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
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldStartWith

class ContentManagerTest : FreeSpec({
    
    "ContentManager" - {
        val manager = ContentManager()
        
        "formatGreeting" - {
            "should return message as-is if platform already included" {
                val message = "Hello from Android!"
                val result = manager.formatGreeting(message, "Android")
                
                result shouldBe message
            }
            
            "should append platform if not included" {
                val message = "Hello World!"
                val result = manager.formatGreeting(message, "iOS")
                
                result shouldBe "Hello World! from iOS"
            }
            
            "should handle empty platform" {
                val result = manager.formatGreeting("Hello", "")
                result shouldBe "Hello"
            }
        }
        
        "validateTitle" - {
            "should accept valid titles" {
                manager.validateTitle("Bir Kelime Bir İşlem") shouldBe true
                manager.validateTitle("A") shouldBe true
                manager.validateTitle("Title with spaces") shouldBe true
            }
            
            "should reject blank titles" {
                manager.validateTitle("") shouldBe false
                manager.validateTitle("   ") shouldBe false
            }
            
            "should reject titles over 100 characters" {
                val longTitle = "a".repeat(101)
                manager.validateTitle(longTitle) shouldBe false
                
                val exactTitle = "a".repeat(100)
                manager.validateTitle(exactTitle) shouldBe true
            }
        }
        
        "processContent" - {
            "should process valid content" {
                val result = manager.processContent(
                    greeting = "Hello",
                    title = "Test App"
                )
                
                result.greeting shouldBe "Hello"
                result.title shouldBe "Test App"
                result.isValid shouldBe true
            }
            
            "should trim greeting when center aligned" {
                val result = manager.processContent(
                    greeting = "  Hello  ",
                    title = "App",
                    config = TextConfiguration(centerAlign = true)
                )
                
                result.greeting shouldBe "Hello"
            }
            
            "should not trim when left aligned" {
                val result = manager.processContent(
                    greeting = "  Hello  ",
                    title = "App",
                    config = TextConfiguration(centerAlign = false)
                )
                
                result.greeting shouldBe "  Hello  "
            }
            
            "should mark invalid content" {
                val blankGreeting = manager.processContent("", "Title")
                blankGreeting.isValid shouldBe false
                
                val blankTitle = manager.processContent("Hello", "")
                blankTitle.isValid shouldBe false
            }
        }
        
        "shouldRefreshContent" - {
            "should refresh after interval" {
                val lastUpdate = 1000L
                val current = 61000L
                
                manager.shouldRefreshContent(lastUpdate, current) shouldBe true
            }
            
            "should not refresh within interval" {
                val lastUpdate = 1000L
                val current = 30000L
                
                manager.shouldRefreshContent(lastUpdate, current) shouldBe false
            }
            
            "should use custom interval" {
                val lastUpdate = 1000L
                val current = 2000L
                
                manager.shouldRefreshContent(lastUpdate, current, 500L) shouldBe true
                manager.shouldRefreshContent(lastUpdate, current, 2000L) shouldBe false
            }
        }
        
        "generateDebugInfo" - {
            "should include all state information" {
                val uiState = AppUiState(
                    greetingMessage = "Hello",
                    appTitle = "Test",
                    isLoading = true
                )
                val config = AppConfiguration(
                    padding = 10.dp,
                    enableAnimations = false
                )
                
                val debug = manager.generateDebugInfo(uiState, config)
                
                debug shouldContain "Debug Info"
                debug shouldContain "Greeting: Hello"
                debug shouldContain "Title: Test"
                debug shouldContain "Loading: true"
                debug shouldContain "Padding: 10.0.dp"
                debug shouldContain "Animations: false"
            }
        }
    }
    
    "ContentResult" - {
        "toDisplayString" - {
            "should combine greeting and title" {
                val result = ContentResult(
                    greeting = "Hello",
                    title = "World",
                    isValid = true,
                    configuration = TextConfiguration.default()
                )
                
                result.toDisplayString() shouldBe "Hello\nWorld"
            }
        }
        
        "hasContent" - {
            "should detect content presence" {
                val withContent = ContentResult("Hello", "", true, TextConfiguration.default())
                val noContent = ContentResult("", "", false, TextConfiguration.default())
                val titleOnly = ContentResult("", "Title", true, TextConfiguration.default())
                
                withContent.hasContent() shouldBe true
                noContent.hasContent() shouldBe false
                titleOnly.hasContent() shouldBe true
            }
        }
    }
    
    "ContentUtils" - {
        "truncateText" - {
            "should truncate long text" {
                val long = "a".repeat(60)
                val result = ContentUtils.truncateText(long, 50)
                
                result shouldEndWith "..."
                result.length shouldBe 53
            }
            
            "should not truncate short text" {
                val short = "Hello World"
                val result = ContentUtils.truncateText(short, 50)
                
                result shouldBe short
            }
            
            "should handle exact length" {
                val exact = "a".repeat(50)
                val result = ContentUtils.truncateText(exact, 50)
                
                result shouldBe exact
            }
        }
        
        "countWords" - {
            "should count words correctly" {
                ContentUtils.countWords("Hello World") shouldBe 2
                ContentUtils.countWords("One") shouldBe 1
                ContentUtils.countWords("  Multiple   spaces  ") shouldBe 2
                ContentUtils.countWords("") shouldBe 0
                ContentUtils.countWords("   ") shouldBe 0
            }
        }
        
        "extractPlatformFromGreeting" - {
            "should extract platform name" {
                val greeting = "Hello from Android!"
                ContentUtils.extractPlatformFromGreeting(greeting) shouldBe "Android"
            }
            
            "should handle no exclamation" {
                val greeting = "Hello from iOS"
                ContentUtils.extractPlatformFromGreeting(greeting) shouldBe "iOS"
            }
            
            "should return null when no platform" {
                val greeting = "Hello World"
                ContentUtils.extractPlatformFromGreeting(greeting) shouldBe null
            }
            
            "should handle complex platform names" {
                val greeting = "Greetings from Android 13 Beta!"
                ContentUtils.extractPlatformFromGreeting(greeting) shouldBe "Android 13 Beta"
            }
        }
        
        "createPlaceholderContent" - {
            "should create loading placeholder" {
                val placeholder = ContentUtils.createPlaceholderContent()
                
                placeholder.greeting shouldBe "Loading..."
                placeholder.title shouldBe "Please wait"
                placeholder.isValid shouldBe false
                placeholder.configuration shouldBe TextConfiguration.default()
            }
        }
    }
})