# CI/CD Guide for Kelime İşlem

## Overview

This document describes the CI/CD pipeline setup for the Kelime İşlem Kotlin Multiplatform project.

## Supported Platforms

This KMP project currently supports:
- **Android** - Native Android app
- **JS/Web** - Web application using Kotlin/JS

Note: iOS and Desktop platforms are not currently configured.

## Available Gradle Tasks

### Testing Tasks
- `:composeApp:testDebugUnitTest` - Run Android unit tests
- `:composeApp:jsNodeTest` - Run JavaScript tests in Node.js
- `:composeApp:jsBrowserTest` - Run JavaScript tests in browser
- `:composeApp:allTests` - Run all platform tests

### Build Tasks
- `:composeApp:assembleDebug` - Build debug APK for Android
- `:composeApp:jsBrowserDistribution` - Build web distribution

### Code Quality Tasks
- `:composeApp:lint` - Run Android lint checks
- `:composeApp:koverVerify` - Verify code coverage thresholds
- `:composeApp:koverXmlReport` - Generate XML coverage report
- `:composeApp:koverHtmlReport` - Generate HTML coverage report

## GitHub Actions Workflows

### Main CI/CD Pipeline (`kmp-ci.yml`)

The main pipeline runs on:
- Push to `main`, `develop`, `release/**`, `hotfix/**` branches
- Pull requests to `main` or `develop`
- Weekly schedule for dependency checks

#### Jobs:

1. **lint-and-analysis** - Code quality checks
   - Runs Android lint

2. **test-matrix** - Platform-specific tests
   - Android tests on Ubuntu
   - JavaScript tests on Ubuntu

3. **coverage-and-sonar** - Code coverage analysis
   - Generates coverage reports
   - Runs SonarCloud analysis
   - Uploads to Codecov

4. **security-scan** - Security scanning
   - Trivy vulnerability scanning
   - Dependency vulnerability checks

5. **build-artifacts** - Build release artifacts
   - Android APK
   - Web distribution

## Local Testing

### Prerequisites

1. Install `act` for local GitHub Actions testing:
```bash
brew install act
```

2. Configure act (first time only):
```bash
echo "-P ubuntu-latest=catthehacker/ubuntu:act-latest" > ~/.actrc
```

### Testing Workflows Locally

1. **Validate workflow syntax**:
```bash
./scripts/test-ci-local.sh
```

2. **Test specific job with act**:
```bash
act -j lint-and-analysis --container-architecture linux/amd64
```

3. **Dry run (no execution)**:
```bash
act -n pull_request
```

### Pre-push Validation

A pre-push hook is configured to validate CI configuration before pushing:
- Validates YAML syntax
- Checks GitHub Actions versions
- Verifies Gradle tasks exist

The hook is automatically installed when you run any git operation in the project.

## Common Issues and Solutions

### Issue: Task not found
**Solution**: Ensure you're using the full task path `:composeApp:taskName`

### Issue: iOS/Desktop tests failing
**Solution**: These platforms are not configured. Remove them from the workflow.

### Issue: act fails with architecture warning
**Solution**: Use `--container-architecture linux/amd64` flag on Apple Silicon

## Gradle Task Reference

To see all available tasks:
```bash
./gradlew :composeApp:tasks --all
```

To run tests locally:
```bash
# Android tests
./gradlew :composeApp:testDebugUnitTest

# JavaScript tests
./gradlew :composeApp:jsNodeTest

# All tests
./gradlew :composeApp:allTests
```

To check code coverage:
```bash
./gradlew :composeApp:koverVerify
```

## GitHub Apps Integration

This project uses GitHub Apps for automated code review and dependency management:

- **CodeRabbit**: Automatically reviews PRs with AI-powered suggestions
- **Claude**: Responds to @claude mentions in issues and PRs
- **Renovate**: Manages dependency updates via `renovate.json` configuration

Note: Redundant workflow files have been moved to `.github/disabled-workflows/` to avoid duplication with the GitHub Apps.

## Contributing

When modifying workflows:
1. Test locally with `act` first
2. Run `./scripts/test-ci-local.sh` to validate
3. The pre-push hook will catch common issues
4. Create a PR to `develop` branch for review