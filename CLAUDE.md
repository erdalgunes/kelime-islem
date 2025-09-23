# CLAUDE.md: AI Assistant Guidelines for Kelime İşlem KMP Project

## 🎯 Project Overview
This is a Kotlin Multiplatform (KMP) project targeting Android, iOS, Desktop, and Web platforms using Compose Multiplatform. The project uses Gradle build system with detekt for linting, kover for coverage (baseline 52.5%), and SonarCloud for quality analysis.

## 🚨 CRITICAL: Tool Usage Requirements

### ALWAYS Use These Free Tools Extensively:

#### 1. Grok AI Model for Complex Reasoning
```bash
uvx llm -m openrouter/x-ai/grok-code-fast-1 "<your prompt>"
```
**Use for:**
- Complex code generation and reasoning
- CI/CD workflow optimization
- Troubleshooting build failures
- Architecture decisions
- Performance optimization strategies

**Example Prompts:**
```bash
# For CI/CD optimization
uvx llm -m openrouter/x-ai/grok-code-fast-1 "Optimize this GitHub Actions workflow for a KMP project with Android, iOS, Desktop, and Web targets. Focus on caching, parallelization, and reducing duplicate steps"

# For build issues
uvx llm -m openrouter/x-ai/grok-code-fast-1 "Debug this Gradle build error in a KMP project: [error message]. Consider configuration cache, dependency resolution, and platform-specific issues"

# For code generation
uvx llm -m openrouter/x-ai/grok-code-fast-1 "Generate a reusable GitHub Actions composite action for setting up Gradle with proper caching for KMP projects"
```

#### 2. Tavily MCP for Web Research
**Use liberally for:**
- Best practices research
- Finding solutions to specific errors
- Validating approaches
- Industry standards and trends

**Example Uses:**
```python
# Search for KMP CI/CD best practices
tavily-search("Kotlin Multiplatform GitHub Actions best practices 2024 caching optimization")

# Research specific errors
tavily-search("GitHub Actions SonarCloud permission error SONAR_TOKEN")

# Find working examples
tavily-search("working example KMP project GitHub Actions matrix build all platforms")
```

#### 3. Context7 for Documentation
**Use for:**
- Official API documentation
- Tool-specific configurations
- Library usage examples

**Example Uses:**
```python
# GitHub Actions documentation
context7.get-library-docs("/actions/toolkit", topic="caching gradle dependencies")

# Gradle documentation
context7.get-library-docs("/gradle/actions", topic="setup-gradle kotlin multiplatform")
```

## 🔄 Systematic CI/CD Problem-Solving Process

### Step 1: Research First
```bash
# 1. Search for best practices
tavily-search("problem description + KMP + GitHub Actions")

# 2. Get official documentation
context7.resolve-library-id("github actions")
context7.get-library-docs("<library-id>", topic="relevant topic")

# 3. Analyze with Grok
uvx llm -m openrouter/x-ai/grok-code-fast-1 "Analyze this CI/CD issue: [details]"
```

### Step 2: Validate Solutions
```bash
# Cross-reference findings
tavily-search("solution validation + real world examples")

# Get expert analysis
uvx llm -m openrouter/x-ai/grok-code-fast-1 "Validate this approach: [solution]"
```

### Step 3: Implement with Testing
- Make incremental changes
- Test locally first: `./gradlew build`
- Verify CI passes before committing

## 📋 CI/CD Workflow Optimization Checklist

### Gradle Optimizations (gradle.properties)
```properties
org.gradle.jvmargs=-Xmx2048M -Dfile.encoding=UTF-8
org.gradle.caching=true
org.gradle.configuration-cache=true
org.gradle.parallel=true
org.gradle.configureondemand=true
org.gradle.daemon=true
```

### GitHub Actions Best Practices
1. **Use Composite Actions** to reduce duplication
2. **Enable Caching**:
   - Gradle wrapper
   - Gradle caches
   - Kotlin/Native dependencies (~/.konan)
3. **Parallelize Tasks** with matrix builds
4. **Optimize Job Dependencies** to run independent tasks concurrently

### Essential Workflow Structure
```yaml
name: CI
on:
  push:
    branches: [main]
  pull_request:

concurrency:
  group: ${{ github.workflow }}-${{ github.ref }}
  cancel-in-progress: true

jobs:
  setup:
    uses: ./.github/actions/setup-gradle

  test:
    needs: setup
    strategy:
      matrix:
        target: [android, iosSimulatorArm64, desktop, js]
    steps:
      - uses: ./.github/actions/setup-gradle
      - run: ./gradlew :${{ matrix.target }}:test --parallel --build-cache
```

## 🐛 Common Issues and Solutions

### Issue: Slow Builds
```bash
# Research optimization
tavily-search("KMP Gradle build optimization configuration cache")

# Generate solution
uvx llm -m openrouter/x-ai/grok-code-fast-1 "Optimize Gradle build for KMP with these targets: Android, iOS, Desktop, Web"
```

### Issue: Coverage Below Threshold
```bash
# Current: 52.5%, Required: 80%
# Research incremental improvement
tavily-search("gradually increasing code coverage KMP project strategies")

# Generate test improvements
uvx llm -m openrouter/x-ai/grok-code-fast-1 "Generate unit tests for KMP shared code to increase coverage from 52% to 60%"
```

### Issue: SonarCloud Integration Failures
```bash
# Debug permissions
tavily-search("SonarCloud GitHub Actions permission denied SONAR_TOKEN")

# Get proper configuration
context7.get-library-docs("/sonarsource/sonarcloud-github-action", topic="permissions setup")
```

## 🎯 Specific Instructions for Current PRs

### For PR #5 (Practical CI Fixes)
1. Research why Claude review is failing:
   ```bash
   tavily-search("GitHub Actions Claude review workflow permission issues")
   ```
2. Fix with validated solution:
   ```bash
   uvx llm -m openrouter/x-ai/grok-code-fast-1 "Fix Claude review workflow permissions for PR comments"
   ```

### For PR #9 (Security-focused Overhaul)
1. Simplify overly complex security features:
   ```bash
   uvx llm -m openrouter/x-ai/grok-code-fast-1 "Simplify this security-focused CI/CD to essential features only"
   ```
2. Research actual requirements:
   ```bash
   tavily-search("essential security practices GitHub Actions KMP project 2024")
   ```

## 📝 Workflow Templates

### Optimized CI Workflow
Always use Grok to generate optimized workflows:
```bash
uvx llm -m openrouter/x-ai/grok-code-fast-1 "Generate optimized GitHub Actions CI workflow for KMP project with:
- Targets: Android, iOS, Desktop, Web
- Tools: detekt, kover, SonarCloud
- Requirements: Fast builds, proper caching, parallel execution
- Coverage threshold: 80% (current: 52.5%)"
```

### Reusable Setup Action
Create once, use everywhere:
```bash
uvx llm -m openrouter/x-ai/grok-code-fast-1 "Create reusable GitHub Actions composite action for Gradle setup with:
- JDK 17
- Gradle caching
- Kotlin/Native caching
- Configuration cache enabled"
```

## 🔍 Quality Gates

### Coverage Requirements
- **Current Baseline**: 52.5% (21/40 lines)
- **Target**: Progressive improvement → 60% → 70% → 80%
- **Strategy**: Focus on shared code in commonMain

### Linting (detekt)
- Must pass on all code
- Custom rules in `detekt.yml`

### SonarCloud
- Quality gate must pass
- No critical or blocker issues
- Maintain or improve code smells

## 💡 Best Practices

### Before Every Implementation:
1. **Research** with Tavily MCP
2. **Validate** with Context7 documentation
3. **Generate** with Grok AI
4. **Test** locally
5. **Verify** in CI

### Tool Usage Philosophy:
- **These tools are FREE** - use them extensively
- **More research = Better solutions**
- **Validate everything** before implementing
- **Cross-reference** multiple sources

### Commit Guidelines:
- Test all changes locally first
- Ensure CI passes before merging
- Document why changes were made
- Reference research sources

## 🚀 Quick Commands Reference

```bash
# Research best practices
tavily-search("your search query")

# Get documentation
context7.resolve-library-id("library name")
context7.get-library-docs("/org/project", topic="specific topic")

# Generate optimized code
uvx llm -m openrouter/x-ai/grok-code-fast-1 "your detailed prompt"

# Test locally
./gradlew build
./gradlew test
./gradlew koverVerify
./gradlew detekt

# Check specific platform
./gradlew :composeApp:testDebugUnitTest  # Android
./gradlew :composeApp:iosSimulatorArm64Test  # iOS
./gradlew :composeApp:desktopTest  # Desktop
./gradlew :composeApp:jsTest  # Web
```

## ⚠️ Remember:
- **ALWAYS** use the tools mentioned above - they're free and essential
- **NEVER** implement without researching first
- **ALWAYS** validate with multiple sources
- **NEVER** commit without testing
- **ALWAYS** focus on maintainability over complexity

This document is the source of truth for all AI assistants working on this project. Follow these guidelines strictly for optimal results.