#!/bin/bash
# Validate GitHub Action versions in workflow files
# Uses git ls-remote for fast, rate-limit-free validation

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Cache configuration
CACHE_FILE=".git/action-ref-cache.json"
CACHE_TTL_TAGS=86400  # 24 hours for tags/SHAs
CACHE_TTL_BRANCHES=3600  # 1 hour for branches

# Initialize cache file if it doesn't exist
if [ ! -f "$CACHE_FILE" ]; then
    echo "{}" > "$CACHE_FILE"
fi

# Function to check if cache entry is valid
is_cache_valid() {
    local key=$1
    local ttl=$2
    local now=$(date +%s)

    if command -v jq &> /dev/null; then
        local cached=$(jq -r ".\"$key\"" "$CACHE_FILE" 2>/dev/null)
        if [ "$cached" != "null" ] && [ "$cached" != "" ]; then
            local cached_time=$(echo "$cached" | jq -r '.timestamp')
            local cached_result=$(echo "$cached" | jq -r '.result')
            local age=$((now - cached_time))

            if [ "$age" -lt "$ttl" ]; then
                if [ "$cached_result" == "true" ]; then
                    echo "valid"
                else
                    echo "invalid"
                fi
                return 0
            fi
        fi
    fi
    echo "miss"
}

# Function to update cache
update_cache() {
    local key=$1
    local result=$2
    local now=$(date +%s)

    if command -v jq &> /dev/null; then
        local temp_file=$(mktemp)
        jq ".\"$key\" = {\"result\": $result, \"timestamp\": $now}" "$CACHE_FILE" > "$temp_file"
        mv "$temp_file" "$CACHE_FILE"
    fi
}

# Function to validate action reference using git ls-remote
validate_action_ref() {
    local action=$1
    # Strip any carriage returns or whitespace
    action=$(echo "$action" | tr -d '\r' | xargs)

    # Handle local actions and docker actions
    if [[ "$action" == "./"* ]] || [[ "$action" == "docker://"* ]]; then
        return 0
    fi

    # Parse action components using parameter expansion
    local owner="${action%%/*}"
    local repo_and_ref="${action#*/}"

    # Handle actions with subpaths (e.g., github/codeql-action/upload-sarif@v3)
    if [[ "$repo_and_ref" == *"@"* ]]; then
        local repo_with_path="${repo_and_ref%%@*}"
        local ref="${repo_and_ref##*@}"
        # Extract just the repo name (before any slash for subpath)
        local repo="${repo_with_path%%/*}"
    else
        echo -e "${RED}✗${NC} $action - invalid format (missing @ref)"
        return 1
    fi


    local repo_url="https://github.com/${owner}/${repo}.git"
    local cache_key="${owner}/${repo}@${ref}"

    # Determine TTL based on ref type
    local ttl=$CACHE_TTL_TAGS
    if [[ "$ref" == "main" ]] || [[ "$ref" == "master" ]] || [[ "$ref" == "develop" ]]; then
        ttl=$CACHE_TTL_BRANCHES
    fi

    # Check cache first
    local cache_result=$(is_cache_valid "$cache_key" "$ttl")
    if [ "$cache_result" == "valid" ]; then
        echo -e "${GREEN}✓${NC} $action (cached)"
        return 0
    elif [ "$cache_result" == "invalid" ]; then
        echo -e "${RED}✗${NC} $action (cached - not found)"
        return 1
    fi

    # Perform actual validation
    local found=false

    # Try as tag
    if git ls-remote --exit-code --refs "$repo_url" "refs/tags/$ref" >/dev/null 2>&1; then
        found=true
    # Try as branch
    elif git ls-remote --exit-code --refs "$repo_url" "refs/heads/$ref" >/dev/null 2>&1; then
        found=true
    # Try as commit SHA
    elif git ls-remote --exit-code "$repo_url" "$ref" >/dev/null 2>&1; then
        found=true
    fi

    if [ "$found" = true ]; then
        echo -e "${GREEN}✓${NC} $action"
        update_cache "$cache_key" "true"
        return 0
    else
        echo -e "${RED}✗${NC} $action - ref '$ref' not found in $owner/$repo"
        update_cache "$cache_key" "false"
        return 1
    fi
}

# Main validation
echo "🔍 Validating GitHub Action versions..."

# Find all workflow files
WORKFLOW_FILES=$(find .github/workflows -name "*.yml" -o -name "*.yaml" 2>/dev/null || true)

if [ -z "$WORKFLOW_FILES" ]; then
    echo "No workflow files found."
    exit 0
fi

# Track validation results
VALIDATION_FAILED=false
TOTAL_ACTIONS=0
FAILED_ACTIONS=0

# Extract and validate action references
for workflow in $WORKFLOW_FILES; do
    echo -e "\n📄 Checking $(basename $workflow)..."

    # Extract action references (uses: statements)
    # Using grep and sed for compatibility, strip carriage returns
    ACTIONS=$(grep -E '^\s*uses:\s*' "$workflow" | sed 's/.*uses:\s*//; s/\s*$//' | tr -d '"' | tr -d "'" | tr -d '\r' || true)

    if [ -z "$ACTIONS" ]; then
        continue
    fi

    while IFS= read -r action; do
        # Skip empty lines and comments
        [ -z "$action" ] && continue
        [[ "$action" == "#"* ]] && continue

        TOTAL_ACTIONS=$((TOTAL_ACTIONS + 1))

        if ! validate_action_ref "$action"; then
            VALIDATION_FAILED=true
            FAILED_ACTIONS=$((FAILED_ACTIONS + 1))
        fi
    done <<< "$ACTIONS"
done

# Summary
echo -e "\n📊 Summary:"
echo "  Total actions checked: $TOTAL_ACTIONS"
echo "  Failed validations: $FAILED_ACTIONS"

if [ "$VALIDATION_FAILED" = true ]; then
    echo -e "\n${RED}❌ Action version validation failed!${NC}"
    echo "Please fix the invalid action references before pushing."
    exit 1
else
    echo -e "\n${GREEN}✅ All action versions are valid!${NC}"
    exit 0
fi