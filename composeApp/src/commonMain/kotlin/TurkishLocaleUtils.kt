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

/**
 * Turkish locale utilities for proper character handling in multiplatform projects.
 * 
 * Critical for Turkish game show applications where proper character casing is essential:
 * - 'i' → 'İ' (not 'I')
 * - 'ı' → 'I' 
 * - Proper handling of ç, ğ, ö, ş, ü characters
 */

/**
 * Converts string to uppercase using Turkish locale rules.
 * 
 * This is critical for Turkish text because:
 * - Standard uppercase() converts 'i' to 'I' (wrong in Turkish)
 * - Turkish locale converts 'i' to 'İ' (correct)
 * - Turkish locale converts 'ı' to 'I' (correct)
 * 
 * Example:
 * - "kelime".turkishUppercase() → "KELİME" ✅
 * - "kelime".uppercase() → "KELIME" ❌ (wrong in Turkish)
 */
expect fun String.turkishUppercase(): String

/**
 * Converts string to lowercase using Turkish locale rules.
 * 
 * Critical for Turkish text because:
 * - Standard lowercase() converts 'I' to 'i' (wrong in Turkish)  
 * - Turkish locale converts 'I' to 'ı' (correct)
 * - Turkish locale converts 'İ' to 'i' (correct)
 */
expect fun String.turkishLowercase(): String

/**
 * Capitalizes first letter using Turkish locale rules.
 * 
 * Useful for proper names and UI text in Turkish game shows.
 */
expect fun String.turkishCapitalize(): String

/**
 * Returns true if the string contains Turkish-specific characters.
 * 
 * Turkish-specific characters: ç, ğ, ı, İ, ö, ş, ü and their uppercase variants
 */
fun String.containsTurkishCharacters(): Boolean {
    val turkishChars = "çğıİöşüÇĞIıÖŞÜ"
    return any { it in turkishChars }
}

/**
 * Validates Turkish character usage in words.
 * Useful for word games to ensure proper Turkish spelling.
 */
fun String.isValidTurkishWord(): Boolean {
    // Basic validation - contains only Turkish alphabet characters
    val turkishAlphabet = "abcçdefgğhıijklmnoöprsştuüvyzABCÇDEFGĞHIıİJKLMNOÖPRSŞTUÜVYZ"
    return all { it in turkishAlphabet || it.isWhitespace() }
}

/**
 * Gets the Turkish letter value for word games.
 * Based on frequency and difficulty in Turkish language.
 */
fun Char.getTurkishLetterValue(): Int {
    return when (this.lowercaseChar()) {
        'a', 'e', 'i', 'ı', 'o', 'u', 'ü', 'ö' -> 1  // Common vowels
        'l', 'n', 'r', 's', 't' -> 1  // Common consonants
        'd', 'k', 'm' -> 2
        'b', 'c', 'ç', 'g', 'h', 'p', 'y' -> 3
        'f', 'j', 'v', 'z' -> 4
        'ğ', 'ş' -> 5  // Turkish-specific characters
        'q', 'w', 'x' -> 10  // Rare letters
        else -> 1
    }
}