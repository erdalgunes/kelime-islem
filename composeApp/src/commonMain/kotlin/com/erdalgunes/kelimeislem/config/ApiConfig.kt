package com.erdalgunes.kelimeislem.config

/**
 * Configuration object for API keys and endpoints.
 * 
 * SECURITY NOTE: This class should NEVER contain hardcoded API keys.
 * All sensitive values should be loaded from environment variables or secure configuration files.
 */
object ApiConfig {
    
    /**
     * CodeRabbit API configuration
     * The actual API key should be provided through environment variables or dependency injection
     */
    object CodeRabbit {
        const val BASE_URL = "https://api.coderabbit.ai"
        const val API_VERSION = "v1"
        
        // Environment variable names for configuration
        const val API_KEY_ENV_VAR = "CODERABBIT_API_KEY"
        const val API_URL_ENV_VAR = "CODERABBIT_API_URL"
    }
    
    /**
     * Test configuration constants
     */
    object Test {
        const val MOCK_API_KEY = "test_key_for_mocking" // Only for mocked tests
        const val TEST_TIMEOUT_MS = 10_000L
    }
}

/**
 * Interface for accessing API configuration in a testable way
 */
interface ApiKeyProvider {
    fun getCodeRabbitApiKey(): String?
    fun isConfigured(): Boolean
}

/**
 * Production implementation that reads from environment variables
 */
class EnvironmentApiKeyProvider : ApiKeyProvider {
    override fun getCodeRabbitApiKey(): String? {
        return System.getenv(ApiConfig.CodeRabbit.API_KEY_ENV_VAR)
    }
    
    override fun isConfigured(): Boolean {
        return getCodeRabbitApiKey()?.isNotBlank() == true
    }
}

/**
 * Test implementation for mocking API keys
 */
class MockApiKeyProvider(private val mockApiKey: String = ApiConfig.Test.MOCK_API_KEY) : ApiKeyProvider {
    override fun getCodeRabbitApiKey(): String = mockApiKey
    
    override fun isConfigured(): Boolean = true
}