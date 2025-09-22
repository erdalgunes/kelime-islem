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
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.string.shouldEndWith

class GreetingTest : FreeSpec({
    
    "Greeting" - {
        val greeting = Greeting()
        
        "should return a greeting message" {
            val result = greeting.greet()
            result shouldContain "Merhaba"
            result shouldContain "Kelime İşlem"
        }
        
        "should include platform name" {
            val result = greeting.greet()
            result shouldContain "from"
            
            // The platform name will vary depending on where the test runs
            // So we just check the structure
            result shouldStartWith "Merhaba, Kelime İşlem from"
            result shouldEndWith "!"
        }
        
        "message format" - {
            "should have correct punctuation" {
                val result = greeting.greet()
                result shouldEndWith "!"
            }
            
            "should contain Turkish characters" {
                val result = greeting.greet()
                result shouldContain "İşlem"
            }
        }
    }
    
    "Platform" - {
        "should have a non-empty name" {
            val platform = getPlatform()
            platform.name.isNotEmpty() shouldBe true
        }
    }
})