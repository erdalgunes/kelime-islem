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

class AppConfigurationTest : FreeSpec({
    
    "AppConfiguration" - {
        "default configuration" - {
            "should have standard values" {
                val config = AppConfiguration.default()
                
                config.padding shouldBe 16.dp
                config.spaceBetweenElements shouldBe 16.dp
                config.enableAnimations shouldBe true
                config.debugMode shouldBe false
            }
        }
        
        "debug configuration" - {
            "should disable animations and enable debug mode" {
                val config = AppConfiguration.debug()
                
                config.debugMode shouldBe true
                config.enableAnimations shouldBe false
                config.padding shouldBe 16.dp
            }
        }
        
        "tablet configuration" - {
            "should have larger padding" {
                val config = AppConfiguration.tablet()
                
                config.padding shouldBe 32.dp
                config.spaceBetweenElements shouldBe 24.dp
                config.enableAnimations shouldBe true
            }
        }
        
        "data class features" - {
            "should support copy" {
                val original = AppConfiguration()
                val copied = original.copy(padding = 20.dp)
                
                copied.padding shouldBe 20.dp
                copied.spaceBetweenElements shouldBe original.spaceBetweenElements
            }
            
            "should generate equals" {
                val config1 = AppConfiguration(padding = 10.dp)
                val config2 = AppConfiguration(padding = 10.dp)
                val config3 = AppConfiguration(padding = 20.dp)
                
                config1 shouldBe config2
                config1 shouldNotBe config3
            }
        }
    }
    
    "TextConfiguration" - {
        "default configuration" - {
            "should have standard text styles" {
                val config = TextConfiguration.default()
                
                config.greetingStyle shouldBe TextConfiguration.TextStyle.Headline
                config.titleStyle shouldBe TextConfiguration.TextStyle.Title
                config.centerAlign shouldBe true
            }
        }
        
        "large text configuration" - {
            "should use headline for both styles" {
                val config = TextConfiguration.largeText()
                
                config.greetingStyle shouldBe TextConfiguration.TextStyle.Headline
                config.titleStyle shouldBe TextConfiguration.TextStyle.Headline
                config.centerAlign shouldBe true
            }
        }
        
        "left aligned configuration" - {
            "should disable center alignment" {
                val config = TextConfiguration.leftAligned()
                
                config.centerAlign shouldBe false
                config.greetingStyle shouldBe TextConfiguration.TextStyle.Headline
            }
        }
        
        "TextStyle enum" - {
            "should have all expected values" {
                TextConfiguration.TextStyle.values().size shouldBe 4
                TextConfiguration.TextStyle.Headline shouldNotBe TextConfiguration.TextStyle.Body
            }
        }
    }
    
    "AppConfigUtils" - {
        "calculateTotalPadding" - {
            "should double the padding value" {
                val config = AppConfiguration(padding = 10.dp)
                val total = AppConfigUtils.calculateTotalPadding(config)
                
                total shouldBe 20.dp
            }
            
            "should work with zero padding" {
                val config = AppConfiguration(padding = 0.dp)
                val total = AppConfigUtils.calculateTotalPadding(config)
                
                total shouldBe 0.dp
            }
        }
        
        "isTabletConfiguration" - {
            "should identify tablet config by padding" {
                val tablet = AppConfiguration(padding = 32.dp)
                val phone = AppConfiguration(padding = 16.dp)
                val edge = AppConfiguration(padding = 21.dp)
                
                AppConfigUtils.isTabletConfiguration(tablet) shouldBe true
                AppConfigUtils.isTabletConfiguration(phone) shouldBe false
                AppConfigUtils.isTabletConfiguration(edge) shouldBe true
            }
        }
        
        "shouldShowDebugInfo" - {
            "should require both debug mode and disabled animations" {
                val debugNoAnim = AppConfiguration(debugMode = true, enableAnimations = false)
                val debugWithAnim = AppConfiguration(debugMode = true, enableAnimations = true)
                val noDebugNoAnim = AppConfiguration(debugMode = false, enableAnimations = false)
                
                AppConfigUtils.shouldShowDebugInfo(debugNoAnim) shouldBe true
                AppConfigUtils.shouldShowDebugInfo(debugWithAnim) shouldBe false
                AppConfigUtils.shouldShowDebugInfo(noDebugNoAnim) shouldBe false
            }
        }
        
        "validateConfiguration" - {
            "should validate padding and spacing" {
                val valid = AppConfiguration(padding = 10.dp, spaceBetweenElements = 5.dp)
                val zeroPadding = AppConfiguration(padding = 0.dp)
                val negativePadding = AppConfiguration(padding = (-5).dp)
                val negativeSpacing = AppConfiguration(spaceBetweenElements = (-10).dp)
                
                AppConfigUtils.validateConfiguration(valid) shouldBe true
                AppConfigUtils.validateConfiguration(zeroPadding) shouldBe false
                AppConfigUtils.validateConfiguration(negativePadding) shouldBe false
                AppConfigUtils.validateConfiguration(negativeSpacing) shouldBe false
            }
        }
        
        "mergeConfigurations" - {
            "should use override when provided" {
                val base = AppConfiguration(padding = 10.dp)
                val override = AppConfiguration(padding = 20.dp)
                
                val result = AppConfigUtils.mergeConfigurations(base, override)
                result shouldBe override
            }
            
            "should use base when override is null" {
                val base = AppConfiguration(padding = 10.dp)
                
                val result = AppConfigUtils.mergeConfigurations(base, null)
                result shouldBe base
            }
        }
    }
})