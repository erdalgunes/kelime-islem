#!/bin/bash
# Git Hooks Management Script for Kelime İşlem
# Provides utilities for hook maintenance and monitoring

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
PURPLE='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
HOOKS_DIR=".git/hooks"
HOOK_CACHE_DIR=".git/hooks-cache"
PROJECT_ROOT=$(git rev-parse --show-toplevel 2>/dev/null || pwd)

# Function to show usage
show_usage() {
    echo -e "${BLUE}Git Hooks Management for Kelime İşlem${NC}"
    echo ""
    echo "Usage: $0 <command> [options]"
    echo ""
    echo -e "${YELLOW}Commands:${NC}"
    echo "  status          Show current hooks status"
    echo "  performance     Show performance metrics"
    echo "  skip <check>    Add check to skip list"
    echo "  unskip <check>  Remove check from skip list"
    echo "  reset           Reset all configurations to defaults"
    echo "  disable         Temporarily disable all hooks"
    echo "  enable          Re-enable all hooks"
    echo "  test            Test hooks without executing full workflow"
    echo "  logs            Show recent hook execution logs"
    echo "  clean           Clean cache and temporary files"
    echo "  update          Update hooks to latest version"
    echo ""
    echo -e "${YELLOW}Examples:${NC}"
    echo "  $0 status"
    echo "  $0 skip unit-tests"
    echo "  $0 performance"
    echo "  $0 test"
    echo ""
}

# Function to check hook status
show_status() {
    echo -e "${BLUE}🔍 Git Hooks Status${NC}"
    echo ""

    # Check if hooks exist and are executable
    echo -e "${PURPLE}Hook Files:${NC}"
    for hook in pre-commit pre-push; do
        if [ -f "$HOOKS_DIR/$hook" ]; then
            if [ -x "$HOOKS_DIR/$hook" ]; then
                size=$(wc -c < "$HOOKS_DIR/$hook")
                echo -e "  ✅ $hook: ${GREEN}installed${NC} (${size} bytes)"
            else
                echo -e "  ⚠️  $hook: ${YELLOW}not executable${NC}"
            fi
        else
            echo -e "  ❌ $hook: ${RED}missing${NC}"
        fi
    done

    echo ""

    # Show configuration
    echo -e "${PURPLE}Configuration:${NC}"
    if [ -f ".git/hooks-config" ]; then
        echo -e "  ✅ Configuration: ${GREEN}present${NC}"
        # Show key settings
        if grep -q "ENABLE_AUTO_FIX=true" ".git/hooks-config"; then
            echo -e "    Auto-fix: ${GREEN}enabled${NC}"
        else
            echo -e "    Auto-fix: ${YELLOW}disabled${NC}"
        fi
    else
        echo -e "  ❌ Configuration: ${RED}missing${NC}"
    fi

    # Show skip list
    echo ""
    echo -e "${PURPLE}Skip Configuration:${NC}"
    if [ -f ".git/skip-checks" ]; then
        skip_count=$(wc -l < ".git/skip-checks" | tr -d ' ')
        if [ "$skip_count" -gt 0 ]; then
            echo -e "  ⚠️  Skipping ${skip_count} checks:"
            while read -r check; do
                [[ "$check" =~ ^[[:space:]]*# ]] && continue
                [[ -z "$check" ]] && continue
                echo -e "    - ${YELLOW}$check${NC}"
            done < ".git/skip-checks"
        else
            echo -e "  ✅ No checks skipped"
        fi
    else
        echo -e "  ✅ No checks skipped"
    fi

    # Show cache status
    echo ""
    echo -e "${PURPLE}Cache Status:${NC}"
    if [ -d "$HOOK_CACHE_DIR" ]; then
        cache_size=$(du -sh "$HOOK_CACHE_DIR" 2>/dev/null | cut -f1)
        echo -e "  📁 Cache directory: ${GREEN}present${NC} (${cache_size})"

        if [ -f "$HOOK_CACHE_DIR/gradle-tasks" ]; then
            # Cross-platform stat command for file modification time
            if [[ "$OSTYPE" == "darwin"* ]]; then
                cache_mtime=$(stat -f %m "$HOOK_CACHE_DIR/gradle-tasks" 2>/dev/null || echo 0)
            else
                cache_mtime=$(stat -c %Y "$HOOK_CACHE_DIR/gradle-tasks" 2>/dev/null || echo 0)
            fi
            cache_age=$((($(date +%s) - cache_mtime) / 3600))
            echo -e "    Gradle tasks cache: ${cache_age}h old"
        fi

        if [ -f "$HOOK_CACHE_DIR/performance.log" ]; then
            perf_entries=$(wc -l < "$HOOK_CACHE_DIR/performance.log" | tr -d ' ')
            echo -e "    Performance entries: $perf_entries"
        fi
    else
        echo -e "  ❌ Cache directory: ${RED}missing${NC}"
    fi
}

# Function to show performance metrics
show_performance() {
    echo -e "${BLUE}📊 Hook Performance Metrics${NC}"
    echo ""

    if [ ! -f "$HOOK_CACHE_DIR/performance.log" ]; then
        echo -e "${YELLOW}No performance data available${NC}"
        return
    fi

    # Recent performance
    echo -e "${PURPLE}Recent Executions (last 10):${NC}"
    tail -10 "$HOOK_CACHE_DIR/performance.log" | while read -r line; do
        if [[ "$line" =~ ^[0-9] ]]; then
            date_part=$(echo "$line" | cut -d: -f1)
            time_part=$(echo "$line" | cut -d: -f2 | tr -d ' ')
            echo "  $date_part: $time_part"
        fi
    done

    echo ""

    # Statistics
    echo -e "${PURPLE}Statistics:${NC}"
    avg_time=$(grep -E "^[0-9]" "$HOOK_CACHE_DIR/performance.log" | \
        sed 's/.*: \([0-9]*\)s/\1/' | \
        awk '{sum+=$1; count++} END {if(count>0) printf "%.1f", sum/count; else print "0"}')

    max_time=$(grep -E "^[0-9]" "$HOOK_CACHE_DIR/performance.log" | \
        sed 's/.*: \([0-9]*\)s/\1/' | \
        sort -n | tail -1)

    total_runs=$(grep -c "^[0-9]" "$HOOK_CACHE_DIR/performance.log" || echo 0)

    echo "  Average execution time: ${avg_time}s"
    echo "  Maximum execution time: ${max_time}s"
    echo "  Total runs recorded: $total_runs"

    # Performance trend
    if [ "$total_runs" -gt 5 ]; then
        recent_avg=$(tail -5 "$HOOK_CACHE_DIR/performance.log" | \
            grep -E "^[0-9]" | \
            sed 's/.*: \([0-9]*\)s/\1/' | \
            awk '{sum+=$1; count++} END {if(count>0) printf "%.1f", sum/count; else print "0"}')

        if (( $(echo "$recent_avg < $avg_time" | bc -l) )); then
            echo -e "  Trend: ${GREEN}improving${NC} (recent: ${recent_avg}s)"
        else
            echo -e "  Trend: ${YELLOW}stable${NC} (recent: ${recent_avg}s)"
        fi
    fi
}

# Function to skip a check
skip_check() {
    local check_name="$1"
    if [ -z "$check_name" ]; then
        echo -e "${RED}Error: Check name required${NC}"
        return 1
    fi

    # Create skip file if it doesn't exist
    touch ".git/skip-checks"

    # Check if already skipped
    if grep -q "^$check_name$" ".git/skip-checks" 2>/dev/null; then
        echo -e "${YELLOW}Check '$check_name' is already skipped${NC}"
        return 0
    fi

    # Add to skip list
    echo "$check_name" >> ".git/skip-checks"
    echo -e "${GREEN}✅ Added '$check_name' to skip list${NC}"

    # Show available checks hint
    echo ""
    echo -e "${YELLOW}Available checks:${NC}"
    echo "Pre-commit: security, platform, kotlin-lint, compose, yaml, workflows, commit-msg, file-size, debug"
    echo "Pre-push: yaml-validation, gradle-tasks, platform-check, lint-checks, compilation, unit-tests"
}

# Function to unskip a check
unskip_check() {
    local check_name="$1"
    if [ -z "$check_name" ]; then
        echo -e "${RED}Error: Check name required${NC}"
        return 1
    fi

    if [ ! -f ".git/skip-checks" ]; then
        echo -e "${YELLOW}No checks are currently skipped${NC}"
        return 0
    fi

    # Remove from skip list
    if grep -q "^$check_name$" ".git/skip-checks"; then
        grep -v "^$check_name$" ".git/skip-checks" > ".git/skip-checks.tmp"
        mv ".git/skip-checks.tmp" ".git/skip-checks"
        echo -e "${GREEN}✅ Removed '$check_name' from skip list${NC}"
    else
        echo -e "${YELLOW}Check '$check_name' was not in skip list${NC}"
    fi
}

# Function to reset configuration
reset_config() {
    echo -e "${YELLOW}🔄 Resetting hook configuration...${NC}"

    # Remove skip list
    if [ -f ".git/skip-checks" ]; then
        rm ".git/skip-checks"
        echo "  ✓ Cleared skip list"
    fi

    # Reset cache
    if [ -d "$HOOK_CACHE_DIR" ]; then
        rm -rf "$HOOK_CACHE_DIR"
        echo "  ✓ Cleared cache"
    fi

    # Recreate configuration
    if [ -x "./scripts/install-hooks.sh" ]; then
        echo "  ✓ Recreating configuration..."
        ./scripts/install-hooks.sh > /dev/null 2>&1
        echo -e "${GREEN}✅ Configuration reset complete${NC}"
    else
        echo -e "${YELLOW}⚠️  Cannot recreate config - install script not found${NC}"
    fi
}

# Function to disable hooks
disable_hooks() {
    echo -e "${YELLOW}🔇 Disabling hooks temporarily...${NC}"

    for hook in pre-commit pre-push; do
        if [ -f "$HOOKS_DIR/$hook" ]; then
            mv "$HOOKS_DIR/$hook" "$HOOKS_DIR/$hook.disabled"
            echo "  ✓ Disabled $hook"
        fi
    done

    echo -e "${GREEN}✅ Hooks disabled${NC}"
    echo -e "${YELLOW}Use '$0 enable' to re-enable${NC}"
}

# Function to enable hooks
enable_hooks() {
    echo -e "${GREEN}🔊 Re-enabling hooks...${NC}"

    for hook in pre-commit pre-push; do
        if [ -f "$HOOKS_DIR/$hook.disabled" ]; then
            mv "$HOOKS_DIR/$hook.disabled" "$HOOKS_DIR/$hook"
            chmod +x "$HOOKS_DIR/$hook"
            echo "  ✓ Enabled $hook"
        fi
    done

    echo -e "${GREEN}✅ Hooks enabled${NC}"
}

# Function to test hooks
test_hooks() {
    echo -e "${BLUE}🧪 Testing hooks...${NC}"

    # Test pre-commit
    if [ -x "$HOOKS_DIR/pre-commit" ]; then
        echo -e "${PURPLE}Testing pre-commit hook:${NC}"
        if bash -n "$HOOKS_DIR/pre-commit"; then
            echo -e "  ✅ ${GREEN}Syntax valid${NC}"
        else
            echo -e "  ❌ ${RED}Syntax errors found${NC}"
        fi

        # Check for required functions
        if grep -q "should_skip_check" "$HOOKS_DIR/pre-commit"; then
            echo -e "  ✅ ${GREEN}Skip functionality present${NC}"
        fi
    fi

    # Test pre-push
    if [ -x "$HOOKS_DIR/pre-push" ]; then
        echo -e "${PURPLE}Testing pre-push hook:${NC}"
        if bash -n "$HOOKS_DIR/pre-push"; then
            echo -e "  ✅ ${GREEN}Syntax valid${NC}"
        else
            echo -e "  ❌ ${RED}Syntax errors found${NC}"
        fi

        # Check for parallel execution
        if grep -q "run_parallel" "$HOOKS_DIR/pre-push"; then
            echo -e "  ✅ ${GREEN}Parallel execution available${NC}"
        fi
    fi
}

# Function to show logs
show_logs() {
    echo -e "${BLUE}📋 Hook Execution Logs${NC}"
    echo ""

    if [ -f ".git/hooks.log" ]; then
        echo -e "${PURPLE}Recent log entries:${NC}"
        tail -20 ".git/hooks.log"
    else
        echo -e "${YELLOW}No log file found${NC}"
    fi

    echo ""

    # Show git log for hook-related commits
    echo -e "${PURPLE}Recent hook-related commits:${NC}"
    git log --oneline --grep="hook" --grep="lint" --grep="detekt" -10 2>/dev/null || echo "No hook-related commits found"
}

# Function to clean cache
clean_cache() {
    echo -e "${BLUE}🧹 Cleaning hook cache and temporary files...${NC}"

    # Clean cache directory
    if [ -d "$HOOK_CACHE_DIR" ]; then
        cache_size=$(du -sh "$HOOK_CACHE_DIR" 2>/dev/null | cut -f1)
        rm -rf "$HOOK_CACHE_DIR"/*
        echo "  ✓ Cleared cache directory ($cache_size freed)"
    fi

    # Clean temporary files (using claude temp directory per project setup)
    rm -f /tmp/claude/*_result /tmp/claude/*_compile /tmp/claude/*_test 2>/dev/null || true
    echo "  ✓ Cleaned temporary files"

    # Recreate cache structure
    mkdir -p "$HOOK_CACHE_DIR"
    echo "# Hook performance log" > "$HOOK_CACHE_DIR/performance.log"
    echo "  ✓ Recreated cache structure"

    echo -e "${GREEN}✅ Cache cleanup complete${NC}"
}

# Function to update hooks
update_hooks() {
    echo -e "${BLUE}🔄 Updating hooks to latest version...${NC}"

    # This would ideally pull from a central repository or update mechanism
    # For now, we'll just reinstall using the current scripts

    if [ -x "./scripts/install-hooks.sh" ]; then
        echo "  Running installation script..."
        ./scripts/install-hooks.sh
        echo -e "${GREEN}✅ Hooks updated${NC}"
    else
        echo -e "${RED}❌ Installation script not found${NC}"
        echo "  Please ensure ./scripts/install-hooks.sh exists and is executable"
    fi
}

# Main function
main() {
    cd "$PROJECT_ROOT"

    case "${1:-}" in
        status)
            show_status
            ;;
        performance|perf)
            show_performance
            ;;
        skip)
            skip_check "$2"
            ;;
        unskip)
            unskip_check "$2"
            ;;
        reset)
            reset_config
            ;;
        disable)
            disable_hooks
            ;;
        enable)
            enable_hooks
            ;;
        test)
            test_hooks
            ;;
        logs)
            show_logs
            ;;
        clean)
            clean_cache
            ;;
        update)
            update_hooks
            ;;
        help|--help|-h|"")
            show_usage
            ;;
        *)
            echo -e "${RED}Unknown command: $1${NC}"
            echo ""
            show_usage
            exit 1
            ;;
    esac
}

# Run main function
main "$@"