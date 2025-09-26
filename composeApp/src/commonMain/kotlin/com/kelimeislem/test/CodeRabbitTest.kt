package com.kelimeislem.test

// Intentionally bad code to trigger CodeRabbit warnings
class CodeRabbitTest {

    // Potential issue: Non-nullable parameter without null check
    fun processString(input: String): String {
        return input.toString().toUpperCase() + input.toLowerCase()
    }

    // Critical issue: Hardcoded sensitive data
    private val apiKey = "sk-1234567890abcdef"

    // Major issue: Resource leak
    fun processFile(filename: String) {
        val file = java.io.File(filename)
        val reader = file.bufferedReader()
        // No try-catch, no close() call
        val content = reader.readText()
        println(content)
    }

    // Warning: Inefficient string concatenation
    fun buildMessage(items: List<String>): String {
        var result = ""
        for (item in items) {
            result = result + item + ", "
        }
        return result
    }
}