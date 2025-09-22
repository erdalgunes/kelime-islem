# Claude Code Integration Guide

This document explains how to effectively use Claude Code in the Kelime İşlem project for automated code review, issue management, and development assistance.

## Overview

We use Claude Code through GitHub Actions in two workflows:
1. **Interactive Claude** (`claude.yml`) - Triggered by @claude mentions
2. **Automatic PR Review** (`claude-code-review.yml`) - Runs on every PR

## Interactive Claude Usage

### Mentioning Claude

Tag `@claude` in any:
- Issue comments
- PR comments
- PR review comments
- Issue titles or descriptions

Claude will automatically respond with relevant assistance.

### Available Commands

Claude has access to the following tools for this project:

#### Build & Test Commands
```bash
./gradlew test          # Run all tests
./gradlew build         # Build the project
./gradlew koverHtmlReport # Generate coverage report
./gradlew detekt        # Run static analysis
```

#### GitHub Operations
```bash
gh pr:*      # All PR operations (view, merge, comment, etc.)
gh issue:*   # All issue operations (create, update, label, etc.)
gh label:*   # Label management
```

### Example Interactions

**Code Review Request:**
```
@claude please review the recent changes in MainActivity.kt and suggest improvements
```

**Test Coverage Analysis:**
```
@claude check our current test coverage and identify areas that need more tests
```

**Build Issue Investigation:**
```
@claude the build is failing, can you investigate and suggest fixes?
```

## Automatic PR Review

### What Gets Reviewed

Every PR automatically triggers a comprehensive review focusing on:

- **Code Quality**: Best practices, naming conventions, architecture patterns
- **Security**: Potential vulnerabilities, input validation, secure coding practices  
- **Performance**: Inefficient algorithms, memory usage, optimization opportunities
- **Testing**: Test coverage, test quality, missing test scenarios
- **Documentation**: Code comments, README updates, API documentation

### Review Labels

Based on the review, Claude will automatically add labels:

- `claude-approved` ✅ - PR looks good, ready for human review
- `needs-human-review` ⚠️ - Complex issues found, requires human attention
- `security-concern` 🔒 - Security issues detected
- `performance-issue` ⚡ - Performance problems identified

### Review Process

1. **Automatic Trigger**: PR opened/updated
2. **Code Analysis**: Claude analyzes all changed files
3. **CLAUDE.md Guidance**: Follows project conventions from CLAUDE.md
4. **Review Comment**: Detailed feedback posted as PR comment
5. **Label Assignment**: Appropriate labels added based on findings

## Project-Specific Guidelines

### Architecture Patterns

- **Circuit Pattern**: Use Presenter + State + UI separation
- **Testable Logic**: Extract business logic from Composables
- **Coverage Target**: 80% minimum for non-UI code
- **UI Testing**: Use Paparazzi for Composable testing

### Testing Standards

- **Framework**: Kotest with FreeSpec style
- **Coverage**: Kover with UI exclusions
- **Boy Scout Rule**: Always improve code when touching it
- **Property-Based**: Use Kotest property testing for edge cases

### Code Quality

- **Static Analysis**: Detekt with custom rules
- **SonarCloud**: Quality gates must pass
- **Formatting**: ktlint via Spotless
- **Convention Plugins**: Follow Now in Android patterns

## Best Practices for Claude Interaction

### Effective Prompts

**Good:**
```
@claude analyze the performance impact of this RecyclerView implementation
```

**Better:**
```
@claude this RecyclerView is causing ANRs on older devices. Please review the adapter implementation and suggest optimizations for better performance.
```

### Context Providing

When asking for help, provide:
- Specific file paths: `MainActivity.kt:125`
- Error messages or logs
- Expected vs actual behavior
- Device/environment details

### Code Review Requests

For manual review requests:
```
@claude please review this PR focusing on:
- Memory leak potential in the ViewModel
- Thread safety of the database operations  
- Test coverage for edge cases
```

## Workflow Customization

### Modifying Allowed Tools

To add new tools to Claude's capabilities, edit `.github/workflows/claude.yml`:

```yaml
claude_args: |
  --allowed-tools "Bash(./gradlew test),Bash(new-command),Bash(gh pr:*)"
```

### Custom Review Prompts

Edit `.github/workflows/claude-code-review.yml` to customize review focus:

```yaml
prompt: |
  Please review focusing on Kotlin-specific best practices:
  - Coroutine usage and scope management
  - Null safety patterns
  - Extension function design
```

## Troubleshooting

### Claude Not Responding

1. Check if the mention is in a supported location
2. Verify GitHub permissions are correct
3. Check workflow run logs in Actions tab

### Review Quality Issues

1. Update CLAUDE.md with specific project conventions
2. Customize the review prompt for your needs
3. Use more specific @claude requests for complex areas

### Permission Errors

Ensure the workflow has required permissions:
```yaml
permissions:
  contents: read
  pull-requests: write
  issues: write
```

## Contributing to Claude Integration

When modifying Claude workflows:

1. Test changes in a feature branch first
2. Update this documentation for any new capabilities
3. Follow the Boy Scout Rule - improve while changing
4. Ensure proper error handling and fallbacks

## Support

For Claude Code issues:
- Check [Claude Code Documentation](https://docs.anthropic.com/claude-code)
- Report issues at [claude-code GitHub](https://github.com/anthropics/claude-code/issues)
- Use `@claude help` for basic usage information