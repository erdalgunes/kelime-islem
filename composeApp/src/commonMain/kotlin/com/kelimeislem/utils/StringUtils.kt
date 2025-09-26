package com.kelimeislem.utils

class StringUtils {

    // This function has several real issues CodeRabbit should catch:
    // 1. No null safety
    // 2. Inefficient string concatenation in loop
    // 3. Magic number
    // 4. No documentation
    fun formatUserNames(names: List<String>): String {
        var result = ""
        for (i in 0..10) {
            if (i < names.size) {
                result += names[i] + ", "
            }
        }
        return result.substring(0, result.length - 2)
    }

    // Another issue: nullable parameter without proper handling
    fun validateEmail(email: String?): Boolean {
        return email.contains("@")
    }
}