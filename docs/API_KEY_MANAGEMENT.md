# API Key Management Guide

## Overview

This document outlines secure API key management practices for the Kelime İşlem project. **Never commit API keys or other sensitive credentials to version control.**

## ✅ Security Best Practices Implemented

### 1. No Hardcoded API Keys
- ❌ **NEVER DO THIS:**
  ```kotlin
  class CodeRabbitTest {
      private val apiKey = "cr_live_1234567890abcdef" // SECURITY RISK!
  }
  ```

- ✅ **DO THIS INSTEAD:**
  ```kotlin
  class CodeRabbitTest {
      private val apiKeyProvider: ApiKeyProvider = EnvironmentApiKeyProvider()
  }
  ```

### 2. Environment-Based Configuration
- API keys are loaded from environment variables or secure configuration files
- Different configurations for debug/release builds
- Local development uses `local.properties` (git-ignored)

### 3. Dependency Injection Pattern
- `ApiKeyProvider` interface allows for easy testing and mocking
- Production code uses `EnvironmentApiKeyProvider`
- Tests use `MockApiKeyProvider`

## Configuration Methods

### Method 1: Environment Variables (Recommended for CI/CD)

```bash
# Set environment variable
export CODERABBIT_API_KEY="your_api_key_here"

# Or in your CI/CD pipeline
CODERABBIT_API_KEY: ${{ secrets.CODERABBIT_API_KEY }}
```

### Method 2: Local Properties (Development)

1. Copy the template:
   ```bash
   cp local.properties.template local.properties
   ```

2. Edit `local.properties`:
   ```properties
   CODERABBIT_API_KEY=your_actual_api_key_here
   ```

3. **IMPORTANT:** `local.properties` is git-ignored and should never be committed!

### Method 3: Build Configuration

The project automatically reads environment variables during build:

```kotlin
// In build.gradle.kts
buildConfigField("String", "CODERABBIT_API_KEY", 
    "\"${System.getenv("CODERABBIT_API_KEY") ?: ""}\""
)
```

## Usage Examples

### In Production Code
```kotlin
class CodeRabbitService {
    private val apiKeyProvider = EnvironmentApiKeyProvider()
    
    fun initialize() {
        if (!apiKeyProvider.isConfigured()) {
            logger.warn("CodeRabbit API key not configured")
            return
        }
        
        val apiKey = apiKeyProvider.getCodeRabbitApiKey()
        // Use apiKey safely...
    }
}
```

### In Tests
```kotlin
class CodeRabbitTest : DescribeSpec({
    describe("API integration") {
        it("should work with mock API key") {
            val mockProvider = MockApiKeyProvider("test_key")
            val service = CodeRabbitService(mockProvider)
            
            service.testConnection() shouldBe true
        }
    }
})
```

## File Structure

```
├── composeApp/src/commonMain/kotlin/com/erdalgunes/kelimeislem/
│   ├── config/
│   │   └── ApiConfig.kt                 # Configuration constants
├── composeApp/src/commonTest/kotlin/com/erdalgunes/kelimeislem/
│   └── integration/
│       └── CodeRabbitTest.kt            # Secure test implementation
├── local.properties.template            # Template for local config
├── local.properties                     # Your actual config (git-ignored)
└── docs/
    └── API_KEY_MANAGEMENT.md            # This documentation
```

## Security Checklist

Before committing code, ensure:

- [ ] No hardcoded API keys in source code
- [ ] `local.properties` is in `.gitignore`
- [ ] Environment variables are used for sensitive data
- [ ] Tests use mock providers, not real API keys
- [ ] Release builds don't include API keys in APK
- [ ] CI/CD uses GitHub Secrets for API keys

## Common Mistakes to Avoid

### ❌ Hardcoded Keys
```kotlin
// DON'T DO THIS!
const val API_KEY = "cr_live_abc123def456"
```

### ❌ Committing Local Config
```bash
# DON'T DO THIS!
git add local.properties
```

### ❌ API Keys in Logs
```kotlin
// DON'T DO THIS!
logger.info("Using API key: $apiKey")
```

### ❌ Keys in Test Resources
```
# DON'T PUT IN src/test/resources/config.properties
api.key=real_api_key_here
```

## Integration Test Configuration

Integration tests that require real API keys are disabled by default:

```kotlin
it("should connect to CodeRabbit API").config(
    enabled = false // Disabled by default
) {
    // Integration test code...
}
```

To enable them locally:
1. Set `CODERABBIT_API_KEY` environment variable
2. Enable the test manually or through configuration

## CI/CD Configuration

In GitHub Actions, add API keys as secrets:

```yaml
env:
  CODERABBIT_API_KEY: ${{ secrets.CODERABBIT_API_KEY }}
```

## Getting API Keys

### CodeRabbit API Key
1. Go to [CodeRabbit Settings](https://app.coderabbit.ai/settings/api)
2. Generate a new API key
3. Store it securely using one of the methods above

## Troubleshooting

### "API key not configured" Error
1. Check environment variable: `echo $CODERABBIT_API_KEY`
2. Verify `local.properties` exists and contains the key
3. Ensure the key is not empty or malformed

### Tests Failing Due to Missing API Key
1. For unit tests: Ensure you're using `MockApiKeyProvider`
2. For integration tests: Set the environment variable or disable the test

### Build Configuration Issues
1. Clean and rebuild: `./gradlew clean build`
2. Check that `buildConfig = true` is enabled
3. Verify environment variables are available during build

## Questions?

If you have questions about API key management, please:
1. Check this documentation first
2. Review the example implementations in the codebase
3. Ask in the team chat or create an issue

Remember: **Security first!** 🔐