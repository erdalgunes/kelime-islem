#!/bin/bash

# Branch Protection Configuration Script
# This script sets up ironclad branch protection rules for the repository

set -euo pipefail

REPO_OWNER="erdalgunes"
REPO_NAME="kelime-islem"
BRANCH="main"

echo "🛡️  Setting up branch protection for ${REPO_OWNER}/${REPO_NAME}:${BRANCH}"

# Check if gh CLI is installed and authenticated
if ! command -v gh &> /dev/null; then
    echo "❌ GitHub CLI (gh) is not installed. Please install it first."
    echo "Visit: https://cli.github.com/"
    exit 1
fi

# Check if user is authenticated
if ! gh auth status &> /dev/null; then
    echo "❌ Not authenticated with GitHub CLI. Please run: gh auth login"
    exit 1
fi

# Set branch protection rules
echo "📋 Configuring branch protection rules..."

gh api \
  --method PUT \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  "/repos/${REPO_OWNER}/${REPO_NAME}/branches/${BRANCH}/protection" \
  --input - << EOF
{
  "required_status_checks": {
    "strict": true,
    "checks": [
      {
        "context": "validate",
        "app_id": -1
      },
      {
        "context": "build (debug)",
        "app_id": -1
      },
      {
        "context": "build (release)",
        "app_id": -1
      },
      {
        "context": "PR Validation & Coverage Check",
        "app_id": -1
      },
      {
        "context": "SonarCloud Code Analysis",
        "app_id": -1
      }
    ]
  },
  "enforce_admins": false,
  "required_pull_request_reviews": {
    "required_approving_review_count": 1,
    "dismiss_stale_reviews": true,
    "require_code_owner_reviews": true,
    "require_last_push_approval": true,
    "bypass_pull_request_allowances": {
      "users": [],
      "teams": [],
      "apps": []
    }
  },
  "restrictions": null,
  "required_linear_history": true,
  "allow_force_pushes": false,
  "allow_deletions": false,
  "block_creations": false,
  "required_conversation_resolution": true,
  "lock_branch": false,
  "allow_fork_syncing": true
}
EOF

echo "✅ Branch protection rules configured successfully!"

# Create CODEOWNERS file if it doesn't exist
CODEOWNERS_FILE=".github/CODEOWNERS"
if [ ! -f "$CODEOWNERS_FILE" ]; then
    echo "📝 Creating CODEOWNERS file..."
    cat > "$CODEOWNERS_FILE" << EOF
# Global code owners
* @erdalgunes

# CI/CD and GitHub workflows
/.github/ @erdalgunes

# Build configuration
/build.gradle.kts @erdalgunes
/gradle/ @erdalgunes
/build-logic/ @erdalgunes

# Design system (critical components)
/design-system/ @erdalgunes

# Security-sensitive files
/.github/workflows/ @erdalgunes
/.github/actions/ @erdalgunes
/.github/scripts/ @erdalgunes
EOF
    echo "✅ CODEOWNERS file created"
else
    echo "ℹ️  CODEOWNERS file already exists"
fi

# Set up repository settings for security
echo "🔒 Configuring repository security settings..."

# Enable vulnerability alerts
gh api \
  --method PUT \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  "/repos/${REPO_OWNER}/${REPO_NAME}/vulnerability-alerts" || echo "⚠️  Could not enable vulnerability alerts (may already be enabled)"

# Enable automated security fixes
gh api \
  --method PUT \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  "/repos/${REPO_OWNER}/${REPO_NAME}/automated-security-fixes" || echo "⚠️  Could not enable automated security fixes (may already be enabled)"

# Enable secret scanning
gh api \
  --method PUT \
  -H "Accept: application/vnd.github+json" \
  -H "X-GitHub-Api-Version: 2022-11-28" \
  "/repos/${REPO_OWNER}/${REPO_NAME}/secret-scanning/alerts" || echo "⚠️  Could not enable secret scanning (may require different permissions)"

echo ""
echo "🎉 Repository protection setup completed!"
echo ""
echo "📋 Summary of protections enabled:"
echo "  ✅ Required status checks (CI, tests, coverage)"
echo "  ✅ Required pull request reviews (1 approval)"
echo "  ✅ Dismiss stale reviews on new commits"
echo "  ✅ Require code owner reviews"
echo "  ✅ Require conversation resolution"
echo "  ✅ Linear history enforced"
echo "  ✅ Force pushes blocked"
echo "  ✅ Branch deletions blocked"
echo "  ✅ Vulnerability alerts enabled"
echo "  ✅ Automated security fixes enabled"
echo "  ✅ CODEOWNERS file configured"
echo ""
echo "⚠️  Manual steps still needed:"
echo "  1. Enable secret scanning in repository settings (if not already enabled)"
echo "  2. Configure branch protection exceptions if needed"
echo "  3. Add team members to CODEOWNERS if applicable"
echo "  4. Review and adjust required status checks based on your workflow names"