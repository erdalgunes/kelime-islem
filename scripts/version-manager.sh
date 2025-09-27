#!/bin/bash
# Automatic Version Management Script
# Inspired by Obtanium's approach to version detection and management

set -eo pipefail

# Configuration
VERSION_FILE="gradle/version.properties"
ANDROID_BUILD_FILE="androidApp/build.gradle.kts"
METADATA_FILE="version-metadata.json"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Functions
print_usage() {
    echo "Usage: $0 [command] [options]"
    echo ""
    echo "Commands:"
    echo "  bump-major     Increment major version (1.0.0 -> 2.0.0)"
    echo "  bump-minor     Increment minor version (1.0.0 -> 1.1.0)"
    echo "  bump-patch     Increment patch version (1.0.0 -> 1.0.1)"
    echo "  bump-code      Increment version code only"
    echo "  get-version    Display current version"
    echo "  get-code       Display current version code"
    echo "  set-version    Set specific version (e.g., 2.1.3)"
    echo "  generate-metadata Generate version metadata for release"
    echo "  check-update   Check if update is available (Obtanium-style)"
    echo "  auto-bump      Automatically determine version bump based on commits"
    echo ""
    echo "Options:"
    echo "  --dry-run      Preview changes without applying them"
    echo "  --force        Force update even if no changes detected"
    echo "  --help         Show this help message"
}

# Load current version
load_version() {
    if [ ! -f "$VERSION_FILE" ]; then
        echo -e "${RED}Error: Version file not found at $VERSION_FILE${NC}"
        exit 1
    fi

    source "$VERSION_FILE"

    MAJOR=${version_major:-1}
    MINOR=${version_minor:-0}
    PATCH=${version_patch:-0}
    CODE=${version_code:-1}

    CURRENT_VERSION="$MAJOR.$MINOR.$PATCH"
}

# Save version to properties file
save_version() {
    cat > "$VERSION_FILE" << EOF
# Version configuration file
# This file is automatically updated by CI/CD
version_major=$MAJOR
version_minor=$MINOR
version_patch=$PATCH
version_code=$CODE

# Version format: major.minor.patch
# Version code is incremented with each build
EOF
    echo -e "${GREEN}✓ Updated $VERSION_FILE${NC}"
}

# Update Android build.gradle.kts
update_android_build() {
    local version="$1"
    local code="$2"

    if [ -f "$ANDROID_BUILD_FILE" ]; then
        # Update versionCode
        sed -i.bak "s/versionCode = [0-9]*/versionCode = $code/" "$ANDROID_BUILD_FILE"
        # Update versionName
        sed -i.bak "s/versionName = \"[^\"]*\"/versionName = \"$version\"/" "$ANDROID_BUILD_FILE"
        rm "${ANDROID_BUILD_FILE}.bak"
        echo -e "${GREEN}✓ Updated $ANDROID_BUILD_FILE${NC}"
    fi
}

# Generate version metadata (Obtanium-compatible)
generate_metadata() {
    local version="$1"
    local code="$2"
    local timestamp=$(date -u +"%Y-%m-%dT%H:%M:%SZ")
    local commit=$(git rev-parse --short HEAD 2>/dev/null || echo "unknown")
    local branch=$(git rev-parse --abbrev-ref HEAD 2>/dev/null || echo "unknown")

    cat > "$METADATA_FILE" << EOF
{
  "version": "$version",
  "versionCode": $code,
  "timestamp": "$timestamp",
  "commit": "$commit",
  "branch": "$branch",
  "changelog": "$(get_changelog)",
  "updateUrl": "https://github.com/erdalgunes/kelime-islem/releases/latest",
  "apkUrl": "https://github.com/erdalgunes/kelime-islem/releases/download/v$version/kelime-islem-$version.apk",
  "obtanium": {
    "appId": "com.erdalgunes.kelimeislem",
    "sourceUrl": "https://github.com/erdalgunes/kelime-islem",
    "preferredApkIndex": 0,
    "versionDetection": true
  }
}
EOF
    echo -e "${GREEN}✓ Generated $METADATA_FILE${NC}"
}

# Get changelog from recent commits
get_changelog() {
    git log --pretty=format:"- %s" -n 10 2>/dev/null | head -5 | tr '\n' '|' | sed 's/|$//'
}

# Bump version components
bump_major() {
    load_version
    MAJOR=$((MAJOR + 1))
    MINOR=0
    PATCH=0
    CODE=$((CODE + 1))
    echo -e "${BLUE}Bumping major version: $CURRENT_VERSION -> $MAJOR.$MINOR.$PATCH${NC}"
}

bump_minor() {
    load_version
    MINOR=$((MINOR + 1))
    PATCH=0
    CODE=$((CODE + 1))
    echo -e "${BLUE}Bumping minor version: $CURRENT_VERSION -> $MAJOR.$MINOR.$PATCH${NC}"
}

bump_patch() {
    load_version
    PATCH=$((PATCH + 1))
    CODE=$((CODE + 1))
    echo -e "${BLUE}Bumping patch version: $CURRENT_VERSION -> $MAJOR.$MINOR.$PATCH${NC}"
}

bump_code() {
    load_version
    CODE=$((CODE + 1))
    echo -e "${BLUE}Bumping version code: $CODE${NC}"
}

# Set specific version
set_version() {
    local new_version="$1"
    if [[ ! "$new_version" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
        echo -e "${RED}Error: Invalid version format. Use X.Y.Z${NC}"
        exit 1
    fi

    IFS='.' read -r MAJOR MINOR PATCH <<< "$new_version"
    load_version
    CODE=$((CODE + 1))
    echo -e "${BLUE}Setting version to: $new_version${NC}"
}

# Auto-determine version bump based on commit messages
auto_bump() {
    load_version
    local last_tag=$(git describe --tags --abbrev=0 2>/dev/null || echo "")
    local commits_since=""

    if [ -n "$last_tag" ]; then
        commits_since=$(git log "$last_tag"..HEAD --oneline)
    else
        commits_since=$(git log --oneline -n 20)
    fi

    # Check for breaking changes (major bump)
    if echo "$commits_since" | grep -iE "breaking|major|incompatible" > /dev/null; then
        bump_major
    # Check for features (minor bump)
    elif echo "$commits_since" | grep -iE "feat:|feature:|add:" > /dev/null; then
        bump_minor
    # Default to patch bump
    else
        bump_patch
    fi
}

# Check for updates (Obtanium-style)
check_update() {
    echo -e "${BLUE}Checking for updates...${NC}"

    # Check latest release from GitHub
    local latest_release=$(gh api repos/erdalgunes/kelime-islem/releases/latest --jq '.tag_name' 2>/dev/null || echo "")

    if [ -z "$latest_release" ]; then
        echo -e "${YELLOW}No releases found${NC}"
        return
    fi

    load_version
    local current="v$CURRENT_VERSION"

    if [ "$latest_release" != "$current" ]; then
        echo -e "${YELLOW}Update available: $current -> $latest_release${NC}"

        # Get release assets (APK files)
        echo -e "${BLUE}Available assets:${NC}"
        gh api repos/erdalgunes/kelime-islem/releases/latest --jq '.assets[].name'
    else
        echo -e "${GREEN}Already on latest version: $current${NC}"
    fi
}

# Main execution
main() {
    local dry_run=false
    local force=false

    # Parse options
    for arg in "$@"; do
        case $arg in
            --dry-run)
                dry_run=true
                echo -e "${YELLOW}DRY RUN MODE - No changes will be saved${NC}"
                ;;
            --force)
                force=true
                ;;
            --help|-h)
                print_usage
                exit 0
                ;;
        esac
    done

    # Execute command
    case "${1:-}" in
        bump-major)
            bump_major
            if [ "$dry_run" = false ]; then
                save_version
                update_android_build "$MAJOR.$MINOR.$PATCH" "$CODE"
                generate_metadata "$MAJOR.$MINOR.$PATCH" "$CODE"
            fi
            ;;
        bump-minor)
            bump_minor
            if [ "$dry_run" = false ]; then
                save_version
                update_android_build "$MAJOR.$MINOR.$PATCH" "$CODE"
                generate_metadata "$MAJOR.$MINOR.$PATCH" "$CODE"
            fi
            ;;
        bump-patch)
            bump_patch
            if [ "$dry_run" = false ]; then
                save_version
                update_android_build "$MAJOR.$MINOR.$PATCH" "$CODE"
                generate_metadata "$MAJOR.$MINOR.$PATCH" "$CODE"
            fi
            ;;
        bump-code)
            bump_code
            if [ "$dry_run" = false ]; then
                save_version
                update_android_build "$MAJOR.$MINOR.$PATCH" "$CODE"
            fi
            ;;
        get-version)
            load_version
            echo "$CURRENT_VERSION"
            ;;
        get-code)
            load_version
            echo "$CODE"
            ;;
        set-version)
            set_version "$2"
            if [ "$dry_run" = false ]; then
                save_version
                update_android_build "$MAJOR.$MINOR.$PATCH" "$CODE"
                generate_metadata "$MAJOR.$MINOR.$PATCH" "$CODE"
            fi
            ;;
        generate-metadata)
            load_version
            generate_metadata "$CURRENT_VERSION" "$CODE"
            ;;
        check-update)
            check_update
            ;;
        auto-bump)
            auto_bump
            if [ "$dry_run" = false ]; then
                save_version
                update_android_build "$MAJOR.$MINOR.$PATCH" "$CODE"
                generate_metadata "$MAJOR.$MINOR.$PATCH" "$CODE"
            fi
            ;;
        *)
            print_usage
            exit 1
            ;;
    esac
}

# Run main function
main "$@"