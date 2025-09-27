#!/bin/bash
# Git Hooks Installation Script for Kelime İşlem
# Ensures consistent hook setup across the team

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
NC='\033[0m' # No Color

# Configuration
HOOKS_DIR=".git/hooks"
HOOK_CACHE_DIR=".git/hooks-cache"
PROJECT_ROOT=$(git rev-parse --show-toplevel 2>/dev/null || pwd)

echo -e "${BLUE}🔧 Installing Git hooks for Kelime İşlem...${NC}"
echo -e "${PURPLE}📁 Project root: $PROJECT_ROOT${NC}"

# Function to check if we're in a git repository
check_git_repo() {
    if ! git rev-parse --git-dir > /dev/null 2>&1; then
        echo -e "${RED}❌ Error: Not in a Git repository!${NC}"
        echo "  Please run this script from the project root"
        exit 1
    fi
}

# Function to backup existing hooks
backup_existing_hooks() {
    local backup_dir=".git/hooks-backup-$(date +%Y%m%d-%H%M%S)"

    if [ -d "$HOOKS_DIR" ]; then
        local existing_hooks=$(find "$HOOKS_DIR" -type f ! -name "*.sample" 2>/dev/null || true)
        if [ -n "$existing_hooks" ]; then
            echo -e "${YELLOW}📦 Backing up existing hooks to $backup_dir${NC}"
            mkdir -p "$backup_dir"

            for hook in $existing_hooks; do
                hook_name=$(basename "$hook")
                if [[ ! "$hook_name" == *.sample ]]; then
                    cp "$hook" "$backup_dir/"
                    echo "  Backed up: $hook_name"
                fi
            done
        fi
    fi
}

# Function to create hook cache directory
setup_cache_dir() {
    echo -e "${BLUE}📂 Setting up hook cache directory...${NC}"
    mkdir -p "$HOOK_CACHE_DIR"

    # Initialize performance log
    if [ ! -f "$HOOK_CACHE_DIR/performance.log" ]; then
        echo "# Hook performance log" > "$HOOK_CACHE_DIR/performance.log"
        echo "# Format: YYYY-MM-DD: <execution_time>s" >> "$HOOK_CACHE_DIR/performance.log"
    fi

    echo "  ✓ Cache directory created: $HOOK_CACHE_DIR"
}

# Function to install pre-commit hook
install_pre_commit() {
    echo -e "${PURPLE}📝 Installing enhanced pre-commit hook...${NC}"

    if [ ! -f "$HOOKS_DIR/pre-commit" ]; then
        echo -e "${RED}❌ pre-commit hook not found in $HOOKS_DIR${NC}"
        echo "  Please ensure the hook file exists"
        return 1
    fi

    chmod +x "$HOOKS_DIR/pre-commit"
    echo "  ✓ pre-commit hook installed and made executable"

    # Verify hook content
    if grep -q "Enhanced pre-commit hook for Kelime İşlem" "$HOOKS_DIR/pre-commit"; then
        echo "  ✓ Enhanced pre-commit hook verified"
    else
        echo -e "${YELLOW}⚠️  Warning: Hook may not be the enhanced version${NC}"
    fi
}

# Function to install pre-push hook
install_pre_push() {
    echo -e "${PURPLE}🚀 Installing optimized pre-push hook...${NC}"

    if [ ! -f "$HOOKS_DIR/pre-push" ]; then
        echo -e "${RED}❌ pre-push hook not found in $HOOKS_DIR${NC}"
        echo "  Please ensure the hook file exists"
        return 1
    fi

    chmod +x "$HOOKS_DIR/pre-push"
    echo "  ✓ pre-push hook installed and made executable"

    # Verify hook content
    if grep -q "Optimized pre-push hook for Kelime İşlem" "$HOOKS_DIR/pre-push"; then
        echo "  ✓ Optimized pre-push hook verified"
    else
        echo -e "${YELLOW}⚠️  Warning: Hook may not be the optimized version${NC}"
    fi
}

# Function to create configuration files
create_config_files() {
    echo -e "${BLUE}⚙️  Creating configuration files...${NC}"

    # Create skip-checks template
    if [ ! -f ".git/skip-checks" ]; then
        cat > ".git/skip-checks.template" << 'EOF'
# Git Hooks Skip Configuration
# Add check names (one per line) to skip specific validations
#
# Available checks:
# Pre-commit: security, platform, kotlin-lint, compose, yaml, workflows, commit-msg, file-size, debug, build
# Pre-push: yaml-validation, gradle-tasks, platform-check, lint-checks, compilation, unit-tests, required-files, commit-analysis
#
# Examples:
# security
# unit-tests
# kotlin-lint
EOF
        echo "  ✓ Skip-checks template created: .git/skip-checks.template"
        echo "    Copy to .git/skip-checks and customize as needed"
    fi

    # Create hook configuration
    cat > ".git/hooks-config" << EOF
# Git Hooks Configuration for Kelime İşlem
# Installed on: $(date)
# Installation script version: 1.0

# Pre-commit settings
ENABLE_AUTO_FIX=true
MAX_FILE_SIZE_MB=5
MAX_COMPLEXITY_SCORE=15

# Pre-push settings
MAX_EXECUTION_TIME=300
PARALLEL_JOBS=4

# Team settings
PROJECT_NAME="Kelime İşlem"
SPRINT_PATTERN="(USTAD-[0-9]+|#[0-9]+)"
EOF
    echo "  ✓ Hook configuration created: .git/hooks-config"
}

# Function to validate requirements
validate_requirements() {
    echo -e "${BLUE}🔍 Validating requirements...${NC}"

    local missing_requirements=()

    # Check for Python 3 (for YAML validation)
    if ! command -v python3 &> /dev/null; then
        missing_requirements+=("python3 (for YAML validation)")
    fi

    # Check for Gradle wrapper
    if [ ! -f "./gradlew" ]; then
        missing_requirements+=("Gradle wrapper (./gradlew)")
    fi

    # Check for required directories
    if [ ! -d ".github/workflows" ]; then
        missing_requirements+=(".github/workflows directory")
    fi

    # Check for PyYAML
    if ! python3 -c "import yaml" 2>/dev/null; then
        echo -e "${YELLOW}⚠️  PyYAML not found. Installing...${NC}"
        if command -v pip3 &> /dev/null; then
            pip3 install PyYAML || echo -e "${YELLOW}  Failed to install PyYAML automatically${NC}"
        else
            missing_requirements+=("PyYAML (pip3 install PyYAML)")
        fi
    fi

    if [ ${#missing_requirements[@]} -gt 0 ]; then
        echo -e "${YELLOW}⚠️  Missing requirements:${NC}"
        for req in "${missing_requirements[@]}"; do
            echo "    - $req"
        done
        echo -e "${YELLOW}  Hooks will still be installed but may not work fully${NC}"
    else
        echo "  ✓ All requirements satisfied"
    fi
}

# Function to test hooks
test_hooks() {
    echo -e "${BLUE}🧪 Testing installed hooks...${NC}"

    # Test pre-commit hook with dry run
    if [ -x "$HOOKS_DIR/pre-commit" ]; then
        echo "  Testing pre-commit hook..."

        # Test if the hook can execute basic checks
        if bash -n "$HOOKS_DIR/pre-commit"; then
            echo "  ✓ pre-commit hook syntax is valid"
        else
            echo -e "${RED}  ❌ pre-commit hook has syntax errors${NC}"
        fi
    fi

    # Test pre-push hook functions (without full execution)
    if [ -x "$HOOKS_DIR/pre-push" ]; then
        echo "  Testing pre-push hook functions..."

        # Test if the hook can execute basic checks
        if bash -n "$HOOKS_DIR/pre-push"; then
            echo "  ✓ pre-push hook syntax is valid"
        else
            echo -e "${RED}  ❌ pre-push hook has syntax errors${NC}"
        fi
    fi
}

# Function to display usage information
show_usage_info() {
    echo ""
    echo -e "${GREEN}🎉 Git hooks installation completed!${NC}"
    echo ""
    echo -e "${BLUE}📚 Usage Information:${NC}"
    echo ""
    echo -e "${YELLOW}Skipping Checks:${NC}"
    echo "  • Copy .git/skip-checks.template to .git/skip-checks"
    echo "  • Add check names (one per line) to skip specific validations"
    echo "  • Use 'git commit --no-verify' to skip all pre-commit checks"
    echo "  • Use 'git push --no-verify' to skip all pre-push checks"
    echo ""
    echo -e "${YELLOW}Configuration:${NC}"
    echo "  • Edit .git/hooks-config to customize hook behavior"
    echo "  • View performance logs in .git/hooks-cache/performance.log"
    echo ""
    echo -e "${YELLOW}Troubleshooting:${NC}"
    echo "  • Run '$0 --test' to test hook functionality"
    echo "  • Check .git/hooks.log for skip audit trail"
    echo "  • Hooks are team-specific - ensure everyone runs this script"
    echo ""
    echo -e "${YELLOW}Available Checks:${NC}"
    echo "  Pre-commit: security, platform, kotlin-lint, compose, yaml, workflows, commit-msg, file-size, debug"
    echo "  Pre-push: yaml-validation, gradle-tasks, platform-check, lint-checks, compilation, unit-tests"
    echo ""
}

# Main installation function
main() {
    local test_mode=false

    # Parse arguments
    case "${1:-}" in
        --test)
            test_mode=true
            ;;
        --help|-h)
            echo "Usage: $0 [--test] [--help]"
            echo ""
            echo "Options:"
            echo "  --test    Test installed hooks without installation"
            echo "  --help    Show this help message"
            exit 0
            ;;
    esac

    # Change to project root
    cd "$PROJECT_ROOT"

    # Validate we're in a git repo
    check_git_repo

    if [ "$test_mode" = true ]; then
        echo -e "${BLUE}🧪 Testing hooks only...${NC}"
        test_hooks
        exit 0
    fi

    # Run installation steps
    validate_requirements
    backup_existing_hooks
    setup_cache_dir
    install_pre_commit
    install_pre_push
    create_config_files
    test_hooks
    show_usage_info

    echo -e "${GREEN}✅ Hook installation completed successfully!${NC}"
    echo -e "${PURPLE}🚀 Ready for enhanced development workflow${NC}"
}

# Run main function
main "$@"