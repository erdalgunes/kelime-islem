# CLAUDE.md: AI Assistant Guidelines

## Project Context
Kotlin Multiplatform project (Android, iOS, Desktop, Web) using Compose Multiplatform.

## Required Tool Usage

### 1. Use Grok for reasoning and complex decisions
```bash
uvx llm -m openrouter/x-ai/grok-code-fast-1 "your question here"
```
Use before making architectural decisions, optimizing workflows, or solving complex problems.

### 2. Use Tavily MCP for web research
```python
tavily-search("search query")
```
Research best practices, find solutions, validate approaches.

### 3. Use Context7 for documentation
```python
context7.resolve-library-id("library name")
context7.get-library-docs("/org/project", topic="specific topic")
```
Get official docs for libraries and tools.

## Problem Solving Approach
1. **Research first** - Use Tavily to find existing solutions
2. **Validate** - Use Grok to analyze and validate approach
3. **Check docs** - Use Context7 for official documentation
4. **Test locally** - Run `./gradlew build` before committing

## Current Focus Areas
- Look for open PRs with `ci-cd` or `claude-review` labels
- Simplify overly complex implementations when possible
- Ensure CI workflows are practical and maintainable
- Focus on build stability and developer experience

## Codegen Integration Status ✅ VERIFIED WORKING
- **Repository**: kelime-islem operational in org 4969
- **Real Success Evidence**: 11+ commits from `codegen-sh[bot]` user ✅
- **Active Branches**: feature/demo-cicd-improvements, fix-pr-10, main ✅
- **Auto-trigger**: @codegen-sh mentions via CodeRabbit workflow ✅
- **Manual backup**: GitHub Actions → workflow_dispatch ✅
- **Verification**: `git log --author=codegen-sh --oneline` shows real commits
- **Integration**: Working despite not showing in `codegen repo --list-repos`

## Quick Commands
```bash
# Test all platforms
./gradlew build

# Test specific platform
./gradlew :composeApp:testDebugUnitTest  # Android
./gradlew :composeApp:iosSimulatorArm64Test  # iOS
./gradlew :composeApp:desktopTest  # Desktop
./gradlew :composeApp:jsTest  # Web

# Code quality
./gradlew detekt
./gradlew koverVerify
```

**Remember:** These tools are free - use them extensively for better solutions.