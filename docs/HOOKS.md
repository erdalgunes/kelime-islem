# Git Hooks Documentation

## Overview
This project uses Git hooks to catch CI/CD failures locally before they reach GitHub, preventing "ping-pong" of failed builds.

## Installation

Run the installation script from the project root:
```bash
./scripts/install-hooks.sh
```

This will install:
- **pre-commit**: Fast validation of staged changes (<5 seconds)
- **pre-push**: Comprehensive CI simulation before push (<2 minutes)

## Hook Descriptions

### Pre-Commit Hook
Runs on `git commit` and performs fast checks:
- ✅ Validates YAML syntax in workflow files
- ✅ Checks for unsupported platform references (Desktop/iOS)
- ✅ Validates Gradle tasks exist (prevents `detekt`/`ktlintCheck` errors)
- ✅ Basic Kotlin formatting checks
- ✅ Warns about debug statements and TODOs
- ✅ Alerts on large files (>1MB)

### Pre-Push Hook
Runs on `git push` and performs comprehensive validation:
- ✅ Full workflow YAML syntax validation
- ✅ GitHub Action version checking
- ✅ Validates ALL Gradle tasks referenced in workflows exist
- ✅ Checks for unsupported platform references
- ✅ Runs lint checks
- ✅ Executes Android unit tests
- ✅ Verifies required files exist
- ✅ Checks for merge conflict markers
- ✅ Reviews commit messages

## Supported Platforms
This project supports **Android and JS/Web only**. The hooks will reject:
- ❌ Desktop references (`testDesktopJvm`, `packageDistributionForCurrentOS`)
- ❌ iOS references (`iosX64Test`, `iosSimulatorArm64Test`)

## Available Gradle Tasks
The project uses these valid tasks:
- `lint` (NOT `detekt`)
- `:composeApp:testDebugUnitTest` (Android tests)
- `:composeApp:jsNodeTest` (JavaScript tests)
- `:composeApp:assembleDebug` (Android APK)
- `:composeApp:jsBrowserDistribution` (Web bundle)

## Bypassing Hooks (Emergency Only)
If you need to bypass hooks in an emergency:
```bash
# Skip pre-commit
git commit --no-verify

# Skip pre-push
git push --no-verify
```

⚠️ **Warning**: Bypassing hooks may cause CI failures on GitHub!

## Troubleshooting

### Hook not running
- Ensure hooks are executable: `chmod +x .git/hooks/pre-*`
- Re-run installation: `./scripts/install-hooks.sh`

### False positives
- Update the task list in `scripts/hooks/pre-push` if new tasks are added
- Check that workflow files use correct task names

### Performance issues
- Pre-commit should complete in <5 seconds
- Pre-push may take 1-2 minutes (runs tests)
- Use `--no-verify` if working offline

## Contributing
All contributors must install hooks before making changes:
```bash
git clone <repo>
cd kelime-islem
./scripts/install-hooks.sh
```

## Related Files
- `scripts/install-hooks.sh` - Installation script
- `scripts/hooks/pre-commit` - Pre-commit hook
- `scripts/hooks/pre-push` - Pre-push hook
- `scripts/validate-action-versions.sh` - GitHub Action version validator
- `scripts/test-ci-local.sh` - Local CI simulation script