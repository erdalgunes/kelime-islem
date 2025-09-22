# Security Policy

## Supported Versions

We release patches for security vulnerabilities. Currently supported versions:

| Version | Supported          |
| ------- | ------------------ |
| 1.0.x   | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

We take the security of Kelime İşlem seriously. If you believe you have found a security vulnerability, please report it to us as described below.

### Please DO NOT:
- **Do not** create a public GitHub issue for security vulnerabilities
- **Do not** disclose the vulnerability publicly until it has been addressed

### Please DO:
1. **Email us directly** at: [Create a security advisory](https://github.com/erdalgunes/kelime-islem/security/advisories/new)
2. **Provide details**:
   - Type of issue (e.g., buffer overflow, SQL injection, cross-site scripting, etc.)
   - Full paths of source file(s) related to the issue
   - Location of the affected source code (tag/branch/commit or direct URL)
   - Step-by-step instructions to reproduce the issue
   - Proof-of-concept or exploit code (if possible)
   - Impact of the issue

### What to Expect

- **Acknowledgment**: We will acknowledge receipt of your vulnerability report within 48 hours
- **Initial Assessment**: Within 7 days, we will provide an initial assessment and expected resolution timeline
- **Updates**: We will keep you informed about the progress
- **Fix & Disclosure**: Once the issue is fixed, we will:
  - Release a patch
  - Publicly disclose the vulnerability (crediting you, unless you prefer to remain anonymous)
  - Update this security policy if needed

## Security Best Practices for Contributors

When contributing to this project, please follow these security best practices:

### Code Security
- Never commit sensitive information (API keys, passwords, tokens)
- Use environment variables for configuration
- Validate all user inputs
- Use parameterized queries for database operations
- Keep dependencies up to date

### Dependency Management
- Regularly update dependencies
- Check for known vulnerabilities using:
  ```bash
  ./gradlew dependencyCheckAnalyze
  ```
- Review dependency licenses

### API Security
- Use HTTPS for all network requests
- Implement proper authentication
- Rate limit API calls
- Validate SSL certificates

### Data Protection
- Encrypt sensitive data at rest
- Use secure communication channels
- Follow GDPR/privacy requirements
- Implement proper session management

## Security Tools

We use several tools to maintain security:

### Static Analysis
- **Detekt**: Kotlin static analysis
  ```bash
  ./gradlew detekt
  ```

### Dependency Scanning
- **Dependabot**: Automated dependency updates
- **SonarCloud**: Security vulnerability scanning

### Code Quality
- **SonarCloud**: Continuous code quality and security analysis
- View our [Security Dashboard](https://sonarcloud.io/project/security_hotspots?id=erdalgunes_kelime-islem)

## Security Configuration

### Gradle Security
```kotlin
// In gradle.properties
android.useAndroidX=true
android.enableJetifier=false
android.nonTransitiveRClass=true

// Disable debug logging in release
buildTypes {
    release {
        minifyEnabled true
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
    }
}
```

### ProGuard Rules
Ensure sensitive code is properly obfuscated in release builds.

## Disclosure Policy

When we receive a security report, we will:

1. Confirm the problem and determine affected versions
2. Audit code to find similar problems
3. Prepare fixes for all supported versions
4. Release new security fix versions

## Comments on this Policy

If you have suggestions on how this process could be improved, please submit a pull request or open an issue.

## Acknowledgments

We would like to thank the following for their responsible disclosure of security issues:

- _Your name could be here!_

---

**Remember**: Security is everyone's responsibility. If you see something, say something!