# Claude Configuration

## Core Principles
- be collaborative and show your thinking process
- prioritize accuracy over assumptions - verify before acting
- maintain atomic git commits with clear messages
- prefer existing solutions over creating new ones
- exhaust all options before pivoting to simpler approaches

## Environment Setup
- run `date` first to understand model cutoff limitations
- bash timeout: 10 minutes (600000ms)
- working directory: use `/tmp/` for temporary operations
- python: use `uv` for package management
- prototyping: use `python -c` for quick tests before implementation

## MCP Tools (Preferred Over Shell)
### Web Operations - Tavily MCP
- `tavily-search`: real-time web search with domain/time filtering
- `tavily-extract`: extract and process content from URLs
- `tavily-crawl`: systematically explore websites with depth control
- `tavily-map`: create detailed site structure maps

### Documentation - Context7 MCP
- resolve library names to IDs before fetching docs
- fetch up-to-date documentation with topic focus
- higher token limits provide more comprehensive context

## Development Workflow
### Planning & Tracking
- use TodoWrite for multi-step tasks (3+ steps)
- mark tasks as `in_progress` before starting
- complete tasks immediately after finishing
- keep only one task `in_progress` at a time

### Code Quality
- read existing code patterns before implementing
- follow project conventions and style guides
- run lint and typecheck before completing tasks
- never use `--no-verify` flag
- never expose or commit secrets/keys

### Git & GitHub
- use `gh` CLI for all GitHub operations
- clone repos to `/tmp/` for inspection
- atomic commits with descriptive messages
- check PR requirements with `gh pr checks`

## AI Model Stack
- `uvx llm -m gpt-5-nano`: cutting-edge reasoning for complex problems
- `uvx llm -m openrouter/x-ai/grok-code-fast-1`: be very specific

## File Management
- avoid creating unnecessary files
- prefer editing existing files over creating new ones
- never proactively create documentation unless requested
- use absolute paths for all file operations

## Error Handling & Validation
- verify success with actual checks, not assumptions
- handle errors gracefully with clear feedback
- test edge cases and error conditions
- validate inputs before processing

## Communication Style
- concise responses (< 4 lines unless detail requested)
- no unnecessary preambles or explanations
- one-word answers when appropriate
- show file paths with line numbers: `file.py:42`

## Restrictions
- simplifying approaches is not acceptable
- undercommitting to solutions is not acceptable
- creating files without necessity is not acceptable
- using expensive models when cheaper ones suffice is not acceptable

## CI/CD Ironclad Configuration

### Security-First Approach
- OIDC everywhere: eliminate long-lived secrets with short-lived tokens
- least-privilege permissions: explicit `permissions:` blocks in all workflows
- environment-based access: separate dev/staging/prod with protection rules
- supply chain security: SBOM generation, artifact signing, provenance attestation

### GitHub Actions Best Practices (2025)
```yaml
permissions:
  contents: read          # Repository access
  id-token: write        # OIDC token generation
  actions: read          # Workflow artifact access
  checks: write          # Status check creation
  security-events: write # Security scan results
```

### Mandatory Security Workflows
1. **Dependency Security Scan** - daily vulnerability checks
2. **Code Security Analysis** - CodeQL + Semgrep integration
3. **Secrets Scanning** - TruffleHog for credential detection
4. **Supply Chain Security** - SBOM generation + OSSF Scorecard

### Release Security Standards
- Multi-environment approval workflow (dev → staging → prod)
- Artifact signing with Cosign
- SLSA provenance attestation
- Automated security verification gates
- Zero-trust deployment model

### Branch Protection Rules
```bash
# Run: .github/scripts/setup-branch-protection.sh
# Enables: required reviews, status checks, conversation resolution
# Enforces: linear history, blocks force pushes, requires CODEOWNERS
```

### Monitoring & Alerting
- Real-time security event detection
- Failed deployment notifications
- Dependency vulnerability alerts
- Runtime integrity monitoring for mobile apps

### Quality Gates
- 80% minimum code coverage
- SonarCloud quality gate passage
- Android Lint compliance
- Security scan clearance
- All tests must pass before merge