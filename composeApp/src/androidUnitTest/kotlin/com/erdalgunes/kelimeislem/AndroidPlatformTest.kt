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
import io.kotest.matchers.string.shouldStartWith

class AndroidPlatformTest : FreeSpec({
    
    "AndroidPlatform" - {
        "should have correct platform type" {
            val platform = AndroidPlatform()
            platform::class shouldBe AndroidPlatform::class
        }
        
        "name property" - {
            "should not be null or empty" {
                val platform = AndroidPlatform()
                platform.name shouldNotBe null
                platform.name.isNotEmpty() shouldBe true
            }
            
            "should contain Android prefix" {
                val platform = AndroidPlatform()
                platform.name shouldStartWith "Android"
            }
            
            "should contain a space separator" {
                val platform = AndroidPlatform()
                platform.name shouldContain " "
            }
            
            "should follow format 'Android <number>'" {
                val platform = AndroidPlatform()
                val parts = platform.name.split(" ")
                
                parts.size shouldBe 2
                parts[0] shouldBe "Android"
                // Check if second part is a number
                val sdkInt = parts[1].toIntOrNull()
                sdkInt shouldNotBe null
            }
        }
    }
    
    "getPlatform function" - {
        "should return AndroidPlatform instance" {
            val platform = getPlatform()
            platform::class shouldBe AndroidPlatform::class
        }
        
        "should return platform with valid name" {
            val platform = getPlatform()
            platform.name shouldStartWith "Android"
            platform.name shouldContain " "
        }
        
        "should always return AndroidPlatform type" {
            repeat(5) {
                val platform = getPlatform()
                platform::class shouldBe AndroidPlatform::class
                platform.name shouldStartWith "Android"
            }
        }
        
        "returned platform should have consistent format" {
            val platform = getPlatform() as AndroidPlatform
            val nameParts = platform.name.split(" ")
            
            nameParts.size shouldBe 2
            nameParts[0] shouldBe "Android"
            nameParts[1].toIntOrNull() shouldNotBe null
        }
    }
})