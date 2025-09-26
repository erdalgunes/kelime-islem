# Self-Healing CI/CD with Codegen.sh

This repository uses Codegen.sh to automatically fix CI/CD failures and code review issues.

## How It Works

1. **Detection**: GitHub Actions monitors for:
   - CI/CD workflow failures
   - CodeRabbit review comments
   - Failed checks
   - Security alerts

2. **Agent Spawning**: When issues are detected, Codegen.sh automatically:
   - Analyzes the problem
   - Spawns a Claude Code agent
   - Agent investigates and implements fixes

3. **Validation**: All auto-fixes are:
   - Tested comprehensively
   - Validated for security
   - Checked for regressions

4. **Review**: Fixed code is:
   - Submitted as a PR
   - Labeled for easy identification
   - Auto-merged if all checks pass (optional)

## Setup

1. Add your Codegen.sh API key to GitHub secrets:
   ```
   CODEGEN_API_KEY=your-api-key
   ANTHROPIC_API_KEY=your-anthropic-key
   ```

2. The workflows will automatically activate on the main branch

## Supported Auto-Fixes

- ✅ Test failures
- ✅ Lint errors
- ✅ Build failures
- ✅ Security vulnerabilities
- ✅ Performance issues
- ✅ CodeRabbit suggestions
- ✅ Dependency updates

## Configuration

Edit `.github/codegen.yml` to customize:
- Agent models and parameters
- Auto-merge settings
- Required checks
- Notification preferences

## Monitoring

Check the Actions tab for:
- `Self-Healing CI/CD` - Main orchestrator
- `Validate Auto-Fixes` - Safety validation
- Auto-fix PRs labeled with `auto-fix`

## Security

- All fixes are validated before merging
- Sensitive operations require human approval
- Security fixes follow OWASP guidelines
- No credentials are ever committed