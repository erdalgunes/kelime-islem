# Contributing to Kelime İşlem

First off, thank you for considering contributing to Kelime İşlem! 🎉

## Table of Contents
- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Development Workflow](#development-workflow)
- [Style Guidelines](#style-guidelines)
- [Testing Guidelines](#testing-guidelines)
- [Commit Guidelines](#commit-guidelines)
- [Pull Request Process](#pull-request-process)

## Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to the project maintainers.

## How Can I Contribute?

### Reporting Bugs
- Use the [Bug Report template](.github/ISSUE_TEMPLATE/01-bug-report.yml)
- Check if the issue has already been reported
- Include as many details as possible

### Suggesting Features
- Use the [Feature Request template](.github/ISSUE_TEMPLATE/02-feature-request.yml)
- Explain the problem your feature would solve
- Consider the project's scope and goals

### Improving Documentation
- Use the [Documentation template](.github/ISSUE_TEMPLATE/03-documentation.yml)
- Even fixing typos is appreciated!

### Submitting Code
- Fix bugs
- Implement new features
- Improve performance
- Refactor code for better maintainability

## Development Setup

### Prerequisites
- JDK 17 or higher
- Android SDK
- Android Studio or IntelliJ IDEA
- Git

### Getting Started

1. **Fork the repository**
   ```bash
   gh repo fork erdalgunes/kelime-islem --clone
   ```

2. **Set up the project**
   ```bash
   cd kelime-islem
   ./gradlew build
   ```

3. **Run tests**
   ```bash
   ./gradlew testDebugUnitTest
   ```

4. **Run the app**
   ```bash
   ./gradlew installDebug
   ```

## Development Workflow

### 1. Create a Feature Branch
```bash
git checkout -b feature/your-feature-name
# or
git checkout -b fix/your-bug-fix
```

### 2. Make Your Changes
- Follow the [Architecture Guidelines](docs/ARCHITECTURE.md)
- Use the Circuit pattern for new features
- Keep UI logic minimal in Composables
- Put business logic in Presenters/ViewModels

### 3. Write Tests
- **Minimum 80% coverage** for business logic
- Use Kotest for unit tests
- Follow existing test patterns
- Run tests locally:
  ```bash
  ./gradlew testDebugUnitTest
  ./gradlew koverHtmlReport
  # Open composeApp/build/reports/kover/html/index.html
  ```

### 4. Check Code Quality
```bash
# Run detekt for static analysis
./gradlew detekt

# Run SonarQube analysis (requires SONAR_TOKEN)
./gradlew sonarqube
```

## Style Guidelines

### Kotlin Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Keep functions small and focused
- Prefer immutability

### Code Organization
```kotlin
// Good
class AppPresenter {
    fun refresh(forceRefresh: Boolean = false): Boolean {
        // Business logic here
    }
}

// Bad - mixing UI and business logic
@Composable
fun AppScreen() {
    // Don't put business logic in Composables
}
```

### File Structure
- Group related files together
- Use clear package names
- Follow the existing structure

## Testing Guidelines

### Test Naming
```kotlin
// Use descriptive test names
"should return true when content needs refresh" {
    // test implementation
}

// Not just
"test refresh" {
    // test implementation
}
```

### Test Coverage
- Business logic: **80% minimum**
- UI components: Tested separately with Paparazzi
- Convention: Test behavior, not implementation

### Property-Based Testing
```kotlin
// Use property-based tests for complex logic
checkAll(Arb.string(), Arb.int(1..100)) { text, number ->
    // Property assertions
}
```

## Commit Guidelines

### Commit Message Format
```
<type>: <subject>

<body>

<footer>
```

### Types
- **feat**: New feature
- **fix**: Bug fix
- **docs**: Documentation changes
- **style**: Code style changes (formatting, etc.)
- **refactor**: Code refactoring
- **perf**: Performance improvements
- **test**: Test additions or changes
- **build**: Build system changes
- **ci**: CI/CD changes
- **chore**: Other changes

### Examples
```bash
# Good
git commit -m "feat: Add dark mode support

- Implement theme switcher
- Add dark color palette
- Update all screens for dark mode"

# Bad
git commit -m "updated stuff"
```

## Pull Request Process

### Before Submitting

1. **Update documentation** if needed
2. **Add tests** for new functionality
3. **Ensure all tests pass**
   ```bash
   ./gradlew testDebugUnitTest
   ```
4. **Check coverage**
   ```bash
   ./gradlew koverVerify
   ```
5. **Run linting**
   ```bash
   ./gradlew detekt
   ```

### PR Template
Your PR should include:
- Clear description of changes
- Related issue numbers
- Screenshots (for UI changes)
- Test coverage report
- Breaking changes (if any)

### Review Process
1. Automated checks must pass
2. Code review by maintainers
3. Address feedback
4. Approval and merge

### After Merge
- Delete your feature branch
- Pull latest main
- Celebrate! 🎉

## Questions?

- Check the [Documentation](docs/)
- Ask in [Discussions](https://github.com/erdalgunes/kelime-islem/discussions)
- Open a [Question issue](.github/ISSUE_TEMPLATE/05-question.yml)

## Recognition

Contributors will be recognized in:
- The project README
- Release notes
- Special thanks in the app

Thank you for contributing to Kelime İşlem! 🚀