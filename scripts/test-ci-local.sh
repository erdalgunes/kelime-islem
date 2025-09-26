#!/bin/bash
# Script to test GitHub Actions workflows locally using act

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "🔍 Testing GitHub Actions workflows locally..."

# Check if act is installed
if ! command -v act &> /dev/null; then
    echo -e "${RED}❌ act is not installed. Install it with: brew install act${NC}"
    exit 1
fi

# Validate workflow syntax first
echo -e "${YELLOW}📝 Validating workflow syntax...${NC}"
for workflow in .github/workflows/*.yml; do
    if [ -f "$workflow" ]; then
        echo "  Checking $(basename $workflow)..."
        # Use GitHub's workflow syntax checker
        if ! python3 -c "import yaml; yaml.safe_load(open('$workflow'))" 2>/dev/null; then
            echo -e "${RED}  ❌ Invalid YAML in $workflow${NC}"
            exit 1
        fi
    fi
done
echo -e "${GREEN}✅ All workflows have valid YAML syntax${NC}"

# Check if required Gradle tasks exist
echo -e "${YELLOW}🔨 Checking available Gradle tasks...${NC}"
if [ -f "./gradlew" ]; then
    # Extract tasks mentioned in workflows
    TASKS=$(grep -h "gradlew" .github/workflows/*.yml | grep -oE "gradlew [a-zA-Z:]+" | cut -d' ' -f2 | sort -u)

    echo "  Tasks found in workflows:"
    for task in $TASKS; do
        echo "    - $task"
        # Check if task exists (quick check without running)
        if ! ./gradlew tasks --all 2>/dev/null | grep -q "^$task "; then
            echo -e "${YELLOW}    ⚠️  Task '$task' may not exist (verify manually)${NC}"
        fi
    done
fi

# Run act with dry-run to check workflow structure
echo -e "${YELLOW}🎬 Running act dry-run...${NC}"
act -n pull_request --job lint-and-analysis 2>&1 | tee /tmp/act-test.log

if grep -q "Error" /tmp/act-test.log; then
    echo -e "${RED}❌ Errors found in workflow. Check /tmp/act-test.log for details${NC}"
    exit 1
fi

echo -e "${GREEN}✅ Workflow structure looks good!${NC}"

# Optional: Run a specific job locally
echo -e "${YELLOW}Would you like to run a specific job locally? (y/n)${NC}"
read -r response
if [[ "$response" =~ ^[Yy]$ ]]; then
    echo "Available jobs:"
    act -l | grep -E "^Stage|^Job" | cut -d' ' -f2 | sort -u
    echo "Enter job name to run:"
    read -r job_name
    echo -e "${YELLOW}🚀 Running job: $job_name${NC}"
    act -j "$job_name" --container-architecture linux/amd64
fi

echo -e "${GREEN}✨ Local CI testing complete!${NC}"