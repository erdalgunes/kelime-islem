#!/bin/bash

# Script to create comprehensive PR labels for the kelime-islem repository
# This creates a verbose labeling system to attract specialist reviewers

# Color codes for labels
COLOR_STATUS="0052CC"     # Blue - Status labels
COLOR_SIZE="C5DEF5"       # Light blue - Size labels
COLOR_NEEDS="D93F0B"      # Red - Needs review labels
COLOR_PRIORITY="FFA500"   # Orange - Priority labels
COLOR_APPROVED="0E8A16"   # Green - Approval labels

echo "Creating comprehensive label system for kelime-islem repository..."
echo "This will create labels to better communicate review needs to humans."
echo ""

# Status Labels
echo "Creating Status Labels..."
gh label create "status/ci-passing" --description "All CI checks pass" --color "$COLOR_STATUS" --force
gh label create "status/quality-gate-pass" --description "SonarCloud quality gate passes" --color "$COLOR_STATUS" --force
gh label create "status/ai-reviewed" --description "CodeRabbit AI review completed" --color "$COLOR_STATUS" --force
gh label create "status/ready-for-human" --description "All automated checks pass, needs human review" --color "$COLOR_STATUS" --force

# Size Labels
echo "Creating Size Labels..."
gh label create "size/XS" --description "Less than 10 lines changed" --color "$COLOR_SIZE" --force
gh label create "size/S" --description "10-100 lines changed" --color "$COLOR_SIZE" --force
gh label create "size/M" --description "100-500 lines changed" --color "$COLOR_SIZE" --force
gh label create "size/L" --description "500-1000 lines changed" --color "$COLOR_SIZE" --force
gh label create "size/XL" --description "More than 1000 lines changed" --color "$COLOR_SIZE" --force

# Review Type Labels (Needs)
echo "Creating Review Type Labels..."
gh label create "needs/architecture-review" --description "Core system architecture changes - needs architect review" --color "$COLOR_NEEDS" --force
gh label create "needs/performance-review" --description "Performance-critical code changed - needs optimization review" --color "$COLOR_NEEDS" --force
gh label create "needs/security-review" --description "Auth/crypto/permissions changed - needs security review" --color "$COLOR_NEEDS" --force
gh label create "needs/database-review" --description "Schema/migrations changed - needs database review" --color "$COLOR_NEEDS" --force
gh label create "needs/kmp-review" --description "Multiplatform code changed - needs KMP expertise" --color "$COLOR_NEEDS" --force
gh label create "needs/ui-review" --description "UI/UX changes - needs design review" --color "$COLOR_NEEDS" --force
gh label create "needs/test-review" --description "Test coverage or test changes - needs QA review" --color "$COLOR_NEEDS" --force

# Priority Labels
echo "Creating Priority Labels..."
gh label create "priority/urgent" --description "Breaking changes or critical fixes - review ASAP" --color "$COLOR_PRIORITY" --force
gh label create "priority/normal" --description "Regular feature or bug fix - standard review" --color "$COLOR_PRIORITY" --force
gh label create "priority/low" --description "Documentation or minor changes - review when available" --color "$COLOR_PRIORITY" --force

# Approval Labels
echo "Creating Approval Labels..."
gh label create "approved/architecture" --description "Architecture review completed and approved" --color "$COLOR_APPROVED" --force
gh label create "approved/performance" --description "Performance review completed and approved" --color "$COLOR_APPROVED" --force
gh label create "approved/security" --description "Security review completed and approved" --color "$COLOR_APPROVED" --force
gh label create "approved/kmp" --description "KMP review completed and approved" --color "$COLOR_APPROVED" --force
gh label create "ready-to-merge" --description "All reviews complete, PR is ready to merge" --color "$COLOR_APPROVED" --force

echo ""
echo "✅ Label creation complete!"
echo ""
echo "Next steps:"
echo "1. Run this script: chmod +x scripts/create-labels.sh && ./scripts/create-labels.sh"
echo "2. The workflow will automatically apply these labels based on PR content"
echo "3. Reviewers can manually add approved/* labels after review"