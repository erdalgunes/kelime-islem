# CLAUDE.md: AI Assistant Guidelines

## Project Context
Kotlin Multiplatform project (Android, iOS, Desktop, Web) using Compose Multiplatform.

## Required Tool Usage
### 0. Check the current date
```bash
date
```

### 1. Use Grok for reasoning and complex decisions
```bash
uvx llm -m openrouter/x-ai/grok-code-fast-1 "your question here"
```
Use before making architectural decisions, optimizing workflows, or solving complex problems.

### 2. Use Tavily MCP for web research
use tavily mcp tools
Research best practices, find solutions, validate approaches.

### 3. Use Context7 for documentation
use context7 mcp tools
Get official docs for libraries and tools.

## Problem Solving Approach
1. **Research first** - Use Tavily to find existing solutions
2. **Validate** - Use Grok to analyze and validate approach
3. **Check docs** - Use Context7 for official documentation
4. **Test locally** - Run `./gradlew build` before committing

**Remember:** These tools are free - use them extensively for better solutions.

## Session learnings
