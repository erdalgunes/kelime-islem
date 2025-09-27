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
import io.kotest.matchers.string.shouldNotContain
import kotlin.random.Random

/**
 * Comprehensive Turkish language support tests using property-based testing
 * Focuses on Turkish-specific character handling and word game functionality
 */
class TurkishLanguageTests : FreeSpec({
    
    "Turkish Character Handling" - {
        
        "Turkish character set validation" - {
            val turkishChars = listOf('ç', 'ğ', 'ı', 'İ', 'ö', 'ş', 'ü')
            val turkishSpecialChars = listOf('Ç', 'Ğ', 'Ö', 'Ş', 'Ü')
            
            "should preserve Turkish characters in greeting" {
                val greeting = Greeting()
                val result = greeting.greet()
                
                // Should contain Turkish characters from "Merhaba, Kelime İşlem"
                result shouldContain "ş" // from İşlem
                result shouldContain "İ" // from İşlem
            }
            
            "should handle Turkish characters in content processing" {
                val manager = ContentManager()
                val turkishText = "Merhaba çok güzel bir gün"
                
                val result = manager.processContent(
                    greeting = turkishText,
                    title = "Türkçe Başlık",
                    config = TextConfiguration.default()
                )
                
                result.greeting shouldBe turkishText
                result.title shouldBe "Türkçe Başlık"
                result.isValid shouldBe true
            }
            
            "should count Turkish words correctly" {
                val turkishSentences = listOf(
                    "Merhaba dünya" to 2,
                    "çok güzel bir gün" to 4,
                    "İstanbul'da yaşıyorum" to 2, // Apostrophe handling
                    "ğ ı ö ü ç ş" to 6,
                    "Ğ I Ö Ü Ç Ş" to 6
                )
                
                turkishSentences.forEach { (text, expected) ->
                    ContentUtils.countWords(text) shouldBe expected
                }
            }
        }
        
        "Property-based Turkish text processing" - {
            "should handle random Turkish character combinations" {
                val turkishChars = "çğıİöşüÇĞIÖŞÜabcdefghijk"
                
                repeat(50) {
                    val length = Random.nextInt(1, 11)
                    val turkishText = (1..length)
                        .map { turkishChars[Random.nextInt(turkishChars.length)] }
                        .joinToString("")
                    
                    val manager = ContentManager()
                    
                    // Should be able to process Turkish text without errors
                    val result = manager.processContent(
                        greeting = turkishText,
                        title = "Test"
                    )
                    
                    result.greeting shouldBe turkishText
                    result.hasContent() shouldBe true
                }
            }
            
            "should validate titles with Turkish characters" {
                val manager = ContentManager()
                val turkishTitles = listOf(
                    "Kelime İşleme Oyunu",
                    "Türkçe Metin Editörü",
                    "Çok Güzel Uygulama",
                    "Şahane Bir Proje",
                    "Ğ harfli başlık"
                )
                
                turkishTitles.forEach { title ->
                    if (title.length <= 100) {
                        manager.validateTitle(title) shouldBe true
                    }
                }
            }
        }
    }
    
    "Turkish Word Game Mechanics" - {
        
        "word processing for game logic" - {
            "should handle Turkish word patterns" {
                val turkishWords = listOf(
                    "kelime",
                    "işlem", 
                    "çözüm",
                    "ğıpta",
                    "üzücü"
                )
                
                turkishWords.forEach { word ->
                    ContentUtils.countWords(word) shouldBe 1
                    
                    // Should not be truncated if reasonable length
                    val truncated = ContentUtils.truncateText(word, 50)
                    truncated shouldBe word
                }
            }
            
            "should extract platform information from Turkish greetings" {
                val turkishGreetings = mapOf(
                    "Merhaba from Android!" to "Android",
                    "Selam from iOS 17" to "iOS 17",
                    "İyi günler from Web" to "Web",
                    "Hoşgeldiniz from Desktop" to "Desktop",
                    "Sadece selam" to null
                )
                
                turkishGreetings.forEach { (greeting, expectedPlatform) ->
                    ContentUtils.extractPlatformFromGreeting(greeting) shouldBe expectedPlatform
                }
            }
        }
        
        "Turkish text formatting" - {
            "should handle Turkish text alignment" {
                val manager = ContentManager()
                val turkishGreeting = "  Merhaba çok güzel  "
                
                // Center align should trim
                val centerAligned = manager.processContent(
                    greeting = turkishGreeting,
                    title = "Test",
                    config = TextConfiguration(centerAlign = true)
                )
                centerAligned.greeting shouldBe "Merhaba çok güzel"
                
                // Left align should preserve spaces
                val leftAligned = manager.processContent(
                    greeting = turkishGreeting,
                    title = "Test", 
                    config = TextConfiguration(centerAlign = false)
                )
                leftAligned.greeting shouldBe turkishGreeting
            }
            
            "should format Turkish greetings with platform" {
                val manager = ContentManager()
                
                val testCases = listOf(
                    Triple("Merhaba", "Android", "Merhaba from Android"),
                    Triple("İyi günler", "iOS", "İyi günler from iOS"),
                    Triple("Selam from Desktop", "Desktop", "Selam from Desktop") // Already contains platform
                )
                
                testCases.forEach { (greeting, platform, expected) ->
                    val result = manager.formatGreeting(greeting, platform)
                    result shouldBe expected
                }
            }
        }
    }
    
    "Turkish Application State" - {
        
        "app title validation" - {
            "should handle Turkish app titles" {
                val turkishTitles = listOf(
                    "Bir Kelime Bir İşlem",
                    "Türkçe Oyun",
                    "Çok Güzel Bir Uygulama",
                    "Şahane Proje"
                )
                
                turkishTitles.forEach { title ->
                    val state = AppUiState(appTitle = title)
                    state.appTitle shouldBe title
                    
                    // Should pass title validation
                    val manager = ContentManager()
                    manager.validateTitle(title) shouldBe true
                }
            }
        }
        
        "greeting message formatting" - {
            "should preserve Turkish characters in state" {
                val turkishGreetings = listOf(
                    "Merhaba güzel dünya",
                    "İyi akşamlar arkadaşlar", 
                    "Çok hoş bir gün",
                    "Şahane bir uygulamayla selam"
                )
                
                turkishGreetings.forEach { greeting ->
                    val state = AppUiState(greetingMessage = greeting)
                    state.greetingMessage shouldBe greeting
                    
                    // Should contain original Turkish characters
                    greeting.forEach { char ->
                        if (char in "çğıİöşüÇĞIÖŞÜ") {
                            state.greetingMessage shouldContain char.toString()
                        }
                    }
                }
            }
        }
    }
    
    "Turkish Configuration and Utils" - {
        
        "debug info with Turkish content" - {
            "should display Turkish text correctly in debug output" {
                val manager = ContentManager()
                val uiState = AppUiState(
                    greetingMessage = "Merhaba çok güzel işlem",
                    appTitle = "Türkçe Başlık",
                    isLoading = false
                )
                val config = AppConfiguration.debug()
                
                val debugInfo = manager.generateDebugInfo(uiState, config)
                
                debugInfo shouldContain "Merhaba çok güzel işlem"
                debugInfo shouldContain "Türkçe Başlık"
                debugInfo shouldContain "Loading: false"
            }
        }
        
        "content validation with Turkish text" - {
            "should validate complex Turkish content" {
                val manager = ContentManager()
                val turkishAlphabet = "abcçdefgğhıijklmnoöprsştuüvyz"
                
                repeat(30) {
                    val length = Random.nextInt(5, 51)
                    val turkishWord = (1..length)
                        .map { turkishAlphabet[Random.nextInt(turkishAlphabet.length)] }
                        .joinToString("")
                    
                    // Process Turkish content
                    val result = manager.processContent(
                        greeting = turkishWord,
                        title = "Türkçe Test"
                    )
                    
                    // Should preserve Turkish characters
                    result.greeting shouldBe turkishWord
                    result.title shouldBe "Türkçe Test"
                    result.isValid shouldBe true
                    result.hasContent() shouldBe true
                }
            }
        }
    }
    
    "Edge Cases with Turkish Characters" - {
        
        "empty and whitespace handling" - {
            "should handle Turkish whitespace correctly" {
                val manager = ContentManager()
                
                // Turkish text with various whitespace
                val testCases = listOf(
                    "\tMerhaba\n",
                    " \t Güzel gün \n ",
                    "İstanbul\tAnkara",
                    "ç\nğ\tı"
                )
                
                testCases.forEach { text ->
                    val wordCount = ContentUtils.countWords(text)
                    wordCount shouldNotBe 0 // Should detect words despite whitespace
                }
            }
        }
        
        "boundary value testing with Turkish" - {
            "should handle maximum length Turkish titles" {
                val manager = ContentManager()
                val turkishChars = "çğıİöşüabcdefghijklmnoprstuvyz"
                
                // Test exactly 100 character limit
                val maxTurkishTitle = turkishChars.take(25).repeat(4) // Exactly 100 chars
                manager.validateTitle(maxTurkishTitle) shouldBe true
                
                // Test over limit
                val overLimitTitle = maxTurkishTitle + "ç"
                manager.validateTitle(overLimitTitle) shouldBe false
            }
        }
    }
})