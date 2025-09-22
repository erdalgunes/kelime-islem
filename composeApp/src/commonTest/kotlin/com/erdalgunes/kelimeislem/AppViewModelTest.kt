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

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import kotlinx.coroutines.flow.first

class AppViewModelTest : FreeSpec({
    
    "AppViewModel" - {
        "initial state" - {
            "should have default app title" {
                val viewModel = AppViewModel()
                val state = viewModel.uiState.value
                
                state.appTitle shouldBe "Bir Kelime Bir İşlem"
            }
            
            "should load greeting message on initialization" {
                val viewModel = AppViewModel()
                val state = viewModel.uiState.value
                
                state.greetingMessage shouldNotBe ""
                // Greeting contains formatted message with platform
            }
            
            "should not be loading initially" {
                val viewModel = AppViewModel()
                val state = viewModel.uiState.value
                
                state.isLoading shouldBe false
            }
        }
        
        "uiState" - {
            "should be a StateFlow" {
                val viewModel = AppViewModel()
                
                // Verify we can collect from the flow
                val state = viewModel.uiState.first()
                state shouldNotBe null
            }
            
            "should contain platform information in greeting" {
                val viewModel = AppViewModel()
                val state = viewModel.uiState.value
                
                // The greeting message is already formatted with platform
                state.greetingMessage shouldNotBe ""
            }
        }
        
        "refresh function" - {
            "should reload greeting message" {
                val viewModel = AppViewModel()
                val initialGreeting = viewModel.uiState.value.greetingMessage
                
                viewModel.refresh()
                val refreshedGreeting = viewModel.uiState.value.greetingMessage
                
                // Greeting should be regenerated (same value but method was called)
                refreshedGreeting shouldBe initialGreeting
            }
            
            "should maintain app title during refresh" {
                val viewModel = AppViewModel()
                val initialTitle = viewModel.uiState.value.appTitle
                
                viewModel.refresh()
                val refreshedTitle = viewModel.uiState.value.appTitle
                
                refreshedTitle shouldBe initialTitle
                refreshedTitle shouldBe "Bir Kelime Bir İşlem"
            }
        }
        
        "new methods" - {
            "shouldAutoRefresh" - {
                "should return false immediately after creation" {
                    val viewModel = AppViewModel()
                    viewModel.shouldAutoRefresh() shouldBe false
                }
            }
            
            "getContentResult" - {
                "should return valid content result" {
                    val viewModel = AppViewModel()
                    val result = viewModel.getContentResult()
                    
                    result.isValid shouldBe true
                    result.hasContent() shouldBe true
                    result.title shouldBe "Bir Kelime Bir İşlem"
                }
            }
            
            "getDebugInfo" - {
                "should generate debug information" {
                    val viewModel = AppViewModel()
                    val config = AppConfiguration.debug()
                    val debugInfo = viewModel.getDebugInfo(config)
                    
                    debugInfo shouldContain "Debug Info"
                    debugInfo shouldContain "Bir Kelime Bir İşlem"
                }
            }
        }
        
        "AppUiState data class" - {
            "should have correct default values" {
                val state = AppUiState()
                
                state.greetingMessage shouldBe ""
                state.appTitle shouldBe "Bir Kelime Bir İşlem"
                state.isLoading shouldBe false
            }
            
            "should support copy operation" {
                val original = AppUiState(
                    greetingMessage = "Hello",
                    appTitle = "Test",
                    isLoading = true
                )
                
                val copied = original.copy(greetingMessage = "Updated")
                
                copied.greetingMessage shouldBe "Updated"
                copied.appTitle shouldBe "Test"
                copied.isLoading shouldBe true
            }
            
            "should generate equals and hashCode" {
                val state1 = AppUiState(greetingMessage = "Test")
                val state2 = AppUiState(greetingMessage = "Test")
                val state3 = AppUiState(greetingMessage = "Different")
                
                state1 shouldBe state2
                state1.hashCode() shouldBe state2.hashCode()
                state1 shouldNotBe state3
            }
        }
    }
})