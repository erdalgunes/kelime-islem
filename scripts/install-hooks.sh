#!/bin/bash
# Install Git hooks for Kelime İşlem project

set -eo pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

show_usage_info() {
    echo "Available check types to skip:"
    echo "  Pre-commit: security, platform, kotlin-lint, compose, yaml, workflows, commit-msg, file-size, debug, build"
    echo "  Pre-push: yaml-validation, gradle-tasks, platform-check, lint-checks, compilation, unit-tests, required-files, commit-analysis"
    echo ""
    echo "To skip checks, create .git/skip-checks with check names (one per line)"
}

echo -e "${BLUE}🔧 Installing Git hooks for Kelime İşlem...${NC}"

# Get the script directory
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
HOOKS_DIR="$SCRIPT_DIR/hooks"
GIT_HOOKS_DIR="$PROJECT_ROOT/.git/hooks"

# Check if we're in a git repository
if [ ! -d "$PROJECT_ROOT/.git" ]; then
    echo -e "${RED}❌ Error: Not in a git repository!${NC}"
    echo "  Please run this from the project root"
    exit 1
fi

# Check if hooks directory exists
if [ ! -d "$HOOKS_DIR" ]; then
    echo -e "${RED}❌ Error: Hooks directory not found at $HOOKS_DIR${NC}"
    exit 1
fi

# Backup existing hooks if they exist
backup_hook() {
    local hook_name=$1
    local existing_hook="$GIT_HOOKS_DIR/$hook_name"

    if [ -f "$existing_hook" ] && [ ! -L "$existing_hook" ]; then
        # Check if it's our hook already
        if ! grep -q "Kelime İşlem" "$existing_hook" 2>/dev/null; then
            echo -e "${YELLOW}  Backing up existing $hook_name to $hook_name.backup${NC}"
            mv "$existing_hook" "$existing_hook.backup"
        fi
    fi
}

# Install a hook
install_hook() {
    local hook_name=$1
    local source_hook="$HOOKS_DIR/$hook_name"
    local target_hook="$GIT_HOOKS_DIR/$hook_name"

    if [ ! -f "$source_hook" ]; then
        echo -e "${YELLOW}  Skipping $hook_name (not found)${NC}"
        return
    fi

    # Backup existing hook
    backup_hook "$hook_name"

    # Copy the hook with correct permissions
    install -m 0755 "$source_hook" "$target_hook"

    echo -e "${GREEN}  ✓ Installed $hook_name${NC}"
}

# Create hooks directory if it doesn't exist
mkdir -p "$GIT_HOOKS_DIR"

# Install hooks
echo "Installing hooks..."
install_hook "pre-commit"
install_hook "pre-push"

# Make validation scripts executable
if [ -f "$PROJECT_ROOT/scripts/validate-action-versions.sh" ]; then
    chmod +x "$PROJECT_ROOT/scripts/validate-action-versions.sh"
    echo -e "${GREEN}  ✓ Made validate-action-versions.sh executable${NC}"
fi

if [ -f "$PROJECT_ROOT/scripts/test-ci-local.sh" ]; then
    chmod +x "$PROJECT_ROOT/scripts/test-ci-local.sh"
    echo -e "${GREEN}  ✓ Made test-ci-local.sh executable${NC}"
fi

# Verify installation
echo ""
echo -e "${BLUE}📋 Verification:${NC}"
if [ -x "$GIT_HOOKS_DIR/pre-commit" ]; then
    echo -e "${GREEN}  ✓ pre-commit hook installed and executable${NC}"
else
    echo -e "${RED}  ✗ pre-commit hook not installed properly${NC}"
fi

if [ -x "$GIT_HOOKS_DIR/pre-push" ]; then
    echo -e "${GREEN}  ✓ pre-push hook installed and executable${NC}"
else
    echo -e "${RED}  ✗ pre-push hook not installed properly${NC}"
fi

# Instructions
echo ""
echo -e "${GREEN}✅ Git hooks installed successfully!${NC}"
echo ""
echo "Hooks will now run automatically:"
echo "  • pre-commit: Fast checks on staged files before commit"
echo "  • pre-push: Full CI validation before pushing to remote"
echo ""
echo "To bypass hooks (emergency only):"
echo "  • git commit --no-verify"
echo "  • git push --no-verify"
echo ""
show_usage_info
echo ""
echo -e "${YELLOW}⚠️  Note: Hooks are local to your repository.${NC}"
echo "  Other contributors need to run this script too."