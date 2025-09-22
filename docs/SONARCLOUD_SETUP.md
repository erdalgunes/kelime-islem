# SonarCloud Setup Guide

## Overview

This project uses SonarCloud for continuous code quality and security analysis. SonarCloud automatically analyzes code on every push and pull request.

## Configuration

### 1. SonarCloud Account Setup

1. Go to [SonarCloud](https://sonarcloud.io)
2. Sign in with your GitHub account
3. Import the repository: `erdalgunes/kelime-islem`
4. Get your organization key (usually your GitHub username)

### 2. GitHub Repository Secrets

Add these secrets to your GitHub repository:

1. Go to Settings → Secrets and variables → Actions
2. Add `SONAR_TOKEN`:
   - Get from SonarCloud: My Account → Security → Generate Token
   - Name: `kelime-islem` or similar
   - Copy the token and add it as a GitHub secret

### 3. Local Analysis

To run SonarCloud analysis locally:

```bash
# Install SonarScanner CLI (macOS)
brew install sonar-scanner

# Run analysis (requires SONAR_TOKEN)
export SONAR_TOKEN="your_token_here"
./gradlew build sonarqube \
  -Dsonar.host.url=https://sonarcloud.io \
  -Dsonar.organization=erdalgunes \
  -Dsonar.projectKey=erdalgunes_kelime-islem
```

## Quality Gates

The project enforces these quality standards:

### Coverage
- **Overall Coverage**: 80% minimum
- **New Code Coverage**: 80% minimum
- **Business Logic**: Excluding UI components

### Code Quality
- **Reliability**: A rating (0 bugs)
- **Security**: A rating (0 vulnerabilities)
- **Maintainability**: A rating (< 5% technical debt)
- **Duplications**: < 3% duplicated lines

### Security
- **Security Hotspots**: Reviewed
- **Vulnerabilities**: 0 allowed

## CI/CD Integration

SonarCloud runs automatically on:

1. **Every Push to Main**: Full analysis
2. **Pull Requests**: Analyzes changed code
3. **Release Builds**: Enforces quality gates

### GitHub Actions Workflow

The analysis is triggered in `.github/workflows/ci.yml`:

```yaml
- name: SonarCloud Scan
  uses: SonarSource/sonarcloud-github-action@master
  env:
    GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
    SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
```

## Project Configuration

### sonar-project.properties

Key configurations:
- **Sources**: `composeApp/src/commonMain,composeApp/src/androidMain`
- **Tests**: `composeApp/src/commonTest,composeApp/src/androidUnitTest`
- **Coverage**: Uses Kover XML report
- **Exclusions**: UI files, generated code, test files

### Gradle Configuration

The project uses the SonarQube Gradle plugin:

```kotlin
plugins {
    id("org.sonarqube") version "5.1.0.4882"
}

sonarqube {
    properties {
        property("sonar.projectKey", "erdalgunes_kelime-islem")
        property("sonar.organization", "erdalgunes")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}
```

## Viewing Results

### SonarCloud Dashboard
- Project: https://sonarcloud.io/project/overview?id=erdalgunes_kelime-islem
- Pull Requests: Automatic comments with analysis results
- Quality Gate status in PR checks

### Badges
Add to README:
```markdown
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=erdalgunes_kelime-islem&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=erdalgunes_kelime-islem)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=erdalgunes_kelime-islem&metric=coverage)](https://sonarcloud.io/summary/new_code?id=erdalgunes_kelime-islem)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=erdalgunes_kelime-islem&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=erdalgunes_kelime-islem)
```

## Troubleshooting

### Common Issues

1. **"Not authorized" error**
   - Verify SONAR_TOKEN is set correctly
   - Check token hasn't expired

2. **Coverage not showing**
   - Ensure Kover report is generated: `./gradlew koverXmlReport`
   - Check path in sonar-project.properties

3. **Files not analyzed**
   - Review exclusion patterns
   - Check source paths are correct

### Local Testing

Test configuration without uploading:
```bash
sonar-scanner -Dsonar.dryRun=true
```

## Best Practices

1. **Fix issues immediately**: Don't let technical debt accumulate
2. **Review security hotspots**: Even if marked as safe
3. **Maintain coverage**: Especially for new code
4. **Use PR decoration**: Get feedback before merging
5. **Monitor trends**: Track improvement over time

## Integration with IDE

### IntelliJ IDEA
1. Install SonarLint plugin
2. Connect to SonarCloud project
3. Sync rules and see issues in IDE

### VS Code
1. Install SonarLint extension
2. Configure connection to SonarCloud
3. Real-time analysis as you code# Status

✅ SONAR_TOKEN configured in GitHub Secrets
