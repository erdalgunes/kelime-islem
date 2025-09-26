package com.test

// Test file with intentional errors for Codegen auto-fix
class TestCodegenFix {

    fun brokenFunction() {
        val unused = "This variable is never used"

        // Missing closing brace
        if (true) {
            println("Missing brace")
        // Oops forgot closing brace

    }

    // Function with wrong return type
    fun wrongReturnType(): String {
        return 42  // Returns Int instead of String
    }

    // Unreachable code
    fun unreachableCode() {
        return
        println("This will never execute")
    }
}