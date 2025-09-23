# GitHub Repository Secrets Documentation

This document describes all the secrets required for the CI/CD workflows in this repository.

## Required Secrets

### 1. SONAR_TOKEN ✅
- **Purpose**: Authentication token for SonarCloud analysis
- **Status**: Already configured
- **How to obtain**: 
  1. Log in to [SonarCloud](https://sonarcloud.io)
  2. Go to Account > Security
  3. Generate a new token with "Execute Analysis" permission
- **Used in**: `ci.yml`, `pr-check.yml`

### 2. PAT_COVERAGE_TOKEN ✅
- **Purpose**: Personal Access Token for coverage reporting
- **Status**: Already configured
- **Used in**: Coverage reporting workflows

### 3. CLAUDE_CODE_OAUTH_TOKEN ✅
- **Purpose**: OAuth token for Claude Code review integration
- **Status**: Already configured
- **Used in**: `claude-code-review.yml`

## Optional Secrets

### 4. CODECOV_TOKEN (Optional)
- **Purpose**: Token for uploading coverage to Codecov
- **Status**: Not configured
- **How to obtain**:
  1. Sign up at [Codecov](https://codecov.io)
  2. Add the repository
  3. Copy the upload token from repository settings
- **Used in**: `ci.yml` (conditionally)
- **Note**: If not provided, Codecov upload will be skipped

## Automatic Secrets

These secrets are automatically provided by GitHub:

- **GITHUB_TOKEN**: Automatically provided for GitHub Actions
  - Used for: PR comments, artifact uploads, SonarCloud integration
  - No configuration needed

## Adding Secrets

To add a new secret:
1. Go to Settings > Secrets and variables > Actions
2. Click "New repository secret"
3. Enter the name and value
4. Click "Add secret"

## Security Best Practices

1. **Rotate secrets regularly** - Update tokens every 90 days
2. **Use minimal permissions** - Only grant necessary access
3. **Never commit secrets** - Always use GitHub Secrets
4. **Monitor usage** - Check audit logs for unusual activity

## Troubleshooting

### SonarCloud Analysis Failing
- Verify `SONAR_TOKEN` is valid and not expired
- Check SonarCloud project exists and is configured
- Ensure organization matches in `sonar-project.properties`

### Coverage Not Reporting
- Verify tests are running successfully
- Check Kover report is generated at expected path
- If using Codecov, ensure `CODECOV_TOKEN` is configured

### PR Checks Not Running
- Verify `GITHUB_TOKEN` has proper permissions
- Check workflow permissions in repository settings
- Ensure branch protection rules allow checks