// Test file with intentional errors for Codegen auto-fix verification
// Codegen should automatically fix these issues when CI fails

const testFunction = () => {
    console.log("Missing semicolon")  // Missing semicolon

    const unusedVariable = "This is never used";

    // Missing closing brace
    if (true) {
        console.log("Test message")
    // Oops, forgot the closing brace

    return "done";
}

// Call without proper export
testFunction()