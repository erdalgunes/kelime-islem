#!/bin/bash
set -e

# Impact Analysis Script for Kelime İşlem PR Workflows
# Analyzes changed files and determines affected modules and test scope

echo "🔍 Starting Impact Analysis..."

# Get base branch (default to main)
BASE_BRANCH=${1:-"origin/main"}
echo "📊 Comparing against: $BASE_BRANCH"

# Get changed files
CHANGED_FILES=$(git diff --name-only $BASE_BRANCH...HEAD)
echo "📁 Changed files:"
echo "$CHANGED_FILES" | sed 's/^/  /'

# Initialize arrays
AFFECTED_MODULES=()
AFFECTED_LAYERS=()
GRADLE_TASKS=()
RISK_LEVEL="low"

# Function to add unique module
add_module() {
    local module="$1"
    if [[ ! " ${AFFECTED_MODULES[@]} " =~ " ${module} " ]]; then
        AFFECTED_MODULES+=("$module")
    fi
}

# Function to add unique layer
add_layer() {
    local layer="$1"
    if [[ ! " ${AFFECTED_LAYERS[@]} " =~ " ${layer} " ]]; then
        AFFECTED_LAYERS+=("$layer")
    fi
}

echo ""
echo "🏗️ Analyzing architectural impact..."

# Analyze each changed file
while IFS= read -r file; do
    if [[ -z "$file" ]]; then
        continue
    fi
    
    echo "  📄 Analyzing: $file"
    
    # App module
    if [[ "$file" =~ ^app/ ]]; then
        add_module ":app"
        add_layer "presentation"
        
        if [[ "$file" =~ MainActivity\.kt$ ]]; then
            RISK_LEVEL="medium"
            echo "    ⚠️  Main activity change detected"
        fi
        
        if [[ "$file" =~ /ui/ ]]; then
            add_layer "ui"
        fi
    fi
    
    # Core modules
    if [[ "$file" =~ ^core/ ]]; then
        add_layer "core"
        
        if [[ "$file" =~ ^core/ui/ ]]; then
            add_module ":core:ui"
            add_layer "shared-ui"
        elif [[ "$file" =~ ^core/domain/ ]]; then
            add_module ":core:domain"
            add_layer "business-logic"
            RISK_LEVEL="high"  # Domain changes are high impact
            echo "    🔴 Business logic change detected"
        elif [[ "$file" =~ ^core/data/ ]]; then
            add_module ":core:data"
            add_layer "data"
            RISK_LEVEL="high"  # Data layer changes are high impact
            echo "    🔴 Data layer change detected"
        elif [[ "$file" =~ ^core/testing/ ]]; then
            add_module ":core:testing"
            add_layer "testing"
        fi
    fi
    
    # Shared multiplatform
    if [[ "$file" =~ ^shared/ ]]; then
        add_module ":shared"
        add_layer "multiplatform"
        RISK_LEVEL="high"  # Shared code affects multiple platforms
        echo "    🔴 Multiplatform shared code change detected"
    fi
    
    # Feature modules
    if [[ "$file" =~ ^feature/ ]]; then
        add_layer "feature"
        
        if [[ "$file" =~ ^feature/game/ ]]; then
            add_module ":feature:game"
            add_layer "game-logic"
        fi
    fi
    
    # Build system
    if [[ "$file" =~ ^build-logic/ ]] || [[ "$file" =~ gradle\.properties$ ]] || \
       [[ "$file" =~ settings\.gradle\.kts$ ]] || [[ "$file" =~ build\.gradle\.kts$ ]] || \
       [[ "$file" =~ ^gradle/ ]] || [[ "$file" =~ libs\.versions\.toml$ ]]; then
        add_layer "build-system"
        RISK_LEVEL="high"  # Build changes affect everything
        echo "    🔴 Build system change detected"
    fi
    
    # Circuit architecture patterns
    if [[ "$file" =~ Presenter\.kt$ ]]; then
        add_layer "circuit-presenter"
        echo "    🎯 Circuit Presenter change detected"
    fi
    
    if [[ "$file" =~ Screen\.kt$ ]]; then
        add_layer "circuit-screen"
        echo "    🎯 Circuit Screen change detected"
    fi
    
    if [[ "$file" =~ State\.kt$ ]] || [[ "$file" =~ Event\.kt$ ]]; then
        add_layer "circuit-state"
        echo "    🎯 Circuit State/Event change detected"
    fi
    
    # Test files
    if [[ "$file" =~ Test\.kt$ ]] || [[ "$file" =~ Tests\.kt$ ]] || [[ "$file" =~ /test/ ]]; then
        add_layer "tests"
        echo "    🧪 Test file change detected"
    fi
    
    # Configuration files
    if [[ "$file" =~ detekt\.yml$ ]] || [[ "$file" =~ sonar-project\.properties$ ]]; then
        add_layer "quality-config"
        echo "    ⚙️  Quality configuration change detected"
    fi
    
    # GitHub workflows
    if [[ "$file" =~ ^\.github/ ]]; then
        add_layer "ci-cd"
        echo "    🔧 CI/CD configuration change detected"
    fi
    
done <<< "$CHANGED_FILES"

echo ""
echo "📋 Impact Analysis Results:"
echo "  🎯 Risk Level: $RISK_LEVEL"
echo "  📦 Affected Modules: ${AFFECTED_MODULES[*]:-"none"}"
echo "  🏗️  Affected Layers: ${AFFECTED_LAYERS[*]:-"none"}"

# Determine Gradle tasks based on affected modules
echo ""
echo "⚙️  Determining optimal Gradle tasks..."

if [[ ${#AFFECTED_MODULES[@]} -eq 0 ]]; then
    echo "  ℹ️  No specific modules affected, running basic validation"
    GRADLE_TASKS=(":app:compileDebugKotlin" "detekt")
else
    for module in "${AFFECTED_MODULES[@]}"; do
        GRADLE_TASKS+=("${module}:test")
        GRADLE_TASKS+=("${module}:detekt")
        
        # Add compile task for app module
        if [[ "$module" == ":app" ]]; then
            GRADLE_TASKS+=("${module}:compileDebugKotlin")
        fi
    done
    
    # Always run kover if we have test tasks
    if [[ "${GRADLE_TASKS[*]}" =~ "test" ]]; then
        GRADLE_TASKS+=("koverHtmlReport")
    fi
fi

# Add build task for high-risk changes
if [[ "$RISK_LEVEL" == "high" ]]; then
    GRADLE_TASKS+=("build")
    echo "  🔴 High risk detected - adding full build"
fi

echo "  🏃 Recommended Gradle tasks: ${GRADLE_TASKS[*]}"

# Output for GitHub Actions
if [[ -n "$GITHUB_OUTPUT" ]]; then
    echo "affected_modules=${AFFECTED_MODULES[*]}" >> $GITHUB_OUTPUT
    echo "affected_layers=${AFFECTED_LAYERS[*]}" >> $GITHUB_OUTPUT
    echo "gradle_tasks=${GRADLE_TASKS[*]}" >> $GITHUB_OUTPUT
    echo "risk_level=$RISK_LEVEL" >> $GITHUB_OUTPUT
    
    # Create JSON output for more complex workflows
    cat > impact-analysis.json << EOF
{
    "risk_level": "$RISK_LEVEL",
    "affected_modules": [$(printf '"%s",' "${AFFECTED_MODULES[@]}" | sed 's/,$//')],
    "affected_layers": [$(printf '"%s",' "${AFFECTED_LAYERS[@]}" | sed 's/,$//')],
    "gradle_tasks": [$(printf '"%s",' "${GRADLE_TASKS[@]}" | sed 's/,$//')],
    "requires_full_build": $([ "$RISK_LEVEL" == "high" ] && echo "true" || echo "false"),
    "requires_apk_build": $([ "${AFFECTED_MODULES[*]}" =~ ":app" ] && echo "true" || echo "false")
}
EOF
    echo "  📄 Generated impact-analysis.json"
fi

# Provide recommendations
echo ""
echo "💡 Recommendations:"

case "$RISK_LEVEL" in
    "high")
        echo "  🔴 HIGH RISK: Requires thorough testing and human review"
        echo "  📋 Suggested actions:"
        echo "    - Run full test suite"
        echo "    - Build and test APK"
        echo "    - Manual testing on device"
        echo "    - Architecture review required"
        echo "    - Consider gradual rollout"
        ;;
    "medium")
        echo "  🟡 MEDIUM RISK: Standard testing with focused validation"
        echo "  📋 Suggested actions:"
        echo "    - Run affected module tests"
        echo "    - Build and validate APK"
        echo "    - Basic smoke testing"
        ;;
    "low")
        echo "  🟢 LOW RISK: Minimal testing required"
        echo "  📋 Suggested actions:"
        echo "    - Run unit tests for affected areas"
        echo "    - Basic compilation check"
        ;;
esac

echo ""
echo "✅ Impact analysis complete!"