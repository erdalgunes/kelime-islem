# Pull Request

## 📋 Summary
<!-- Provide a concise description of the changes in this PR -->

**What:** 
**Why:** 
**How:** 

## 🎯 Type of Change
<!-- Mark the relevant option with an 'x' -->
- [ ] 🐛 Bug fix (non-breaking change which fixes an issue)
- [ ] ✨ New feature (non-breaking change which adds functionality)
- [ ] 💥 Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] 📚 Documentation update
- [ ] 🎨 Code style/formatting change (no functional changes)
- [ ] ♻️ Refactoring (no functional changes, no api changes)
- [ ] ⚡ Performance improvement
- [ ] 🧪 Test addition/improvement
- [ ] 🔧 Build/CI configuration change

## 🏗️ Architecture & Circuit Impact
<!-- Describe impact on Circuit architecture patterns -->

**Affected Modules:**
- [ ] `:app` (Android Application)
- [ ] `:core:ui` (Shared UI Components) 
- [ ] `:core:domain` (Business Logic)
- [ ] `:core:data` (Data Layer)
- [ ] `:shared` (Multiplatform Shared Code)
- [ ] `:feature:*` (Feature modules)

**Circuit Boundaries:**
- [ ] New Presenter/Screen added
- [ ] State management changes
- [ ] UI-only changes (no business logic)
- [ ] Cross-module dependencies modified
- [ ] API surface changes

## 🧪 Testing Strategy
<!-- Describe testing approach and coverage -->

**Coverage Impact:**
- Current coverage: __%
- Expected coverage after changes: __%
- [ ] Coverage maintains ≥80% threshold
- [ ] New code has ≥90% coverage

**Test Types Added/Modified:**
- [ ] Unit tests (Kotest)
- [ ] Integration tests
- [ ] UI tests (planned for Paparazzi)
- [ ] Property-based tests

**Testing Checklist:**
- [ ] All tests pass locally (`./gradlew test`)
- [ ] Detekt checks pass (`./gradlew detekt`)
- [ ] Build succeeds (`./gradlew build`)
- [ ] Coverage report generated (`./gradlew koverHtmlReport`)

## 🔍 Quality Assurance

**Code Quality:**
- [ ] Follows Kotlin coding conventions
- [ ] No new Detekt violations
- [ ] SonarCloud quality gate passes
- [ ] No new security vulnerabilities
- [ ] Performance impact assessed

**Review Readiness:**
- [ ] Self-reviewed the PR
- [ ] Tested on device/emulator
- [ ] Breaking changes documented
- [ ] Migration guide provided (if needed)

## 📱 Device Testing
<!-- For UI changes, specify testing approach -->

**Tested On:**
- [ ] Emulator (API level: ___)
- [ ] Physical device (Model: ___)
- [ ] Different screen sizes
- [ ] Dark/Light themes

**Preview APK:** 
<!-- Will be auto-generated and linked by CI -->
_APK will be automatically built and distributed via Firebase App Distribution_

## 🔗 Related Issues
<!-- Link related issues using keywords -->

Closes #
Relates to #
Blocked by #

## 📸 Screenshots/Recordings
<!-- For UI changes, provide before/after screenshots -->

<details>
<summary>📸 Before/After Screenshots</summary>

| Before | After |
|--------|-------|
| <!-- screenshot --> | <!-- screenshot --> |

</details>

## 🚀 Deployment Notes
<!-- Any special deployment considerations -->

**Database Changes:**
- [ ] No database changes
- [ ] Migration required
- [ ] Backward compatible

**Feature Flags:**
- [ ] No feature flags needed
- [ ] New feature flag added: `feature_name`
- [ ] Existing feature flag modified

## ⚠️ Breaking Changes
<!-- List any breaking changes and migration steps -->

**API Changes:**
- [ ] No API changes
- [ ] Public API modified (describe impact)
- [ ] Internal API modified

**Migration Required:**
```kotlin
// Example migration code
```

## 📋 Reviewer Checklist
<!-- For reviewers to verify -->

**Code Review:**
- [ ] Code follows architectural patterns (Circuit)
- [ ] Business logic properly separated from UI
- [ ] Error handling implemented
- [ ] Resource management (lifecycle, memory)
- [ ] Thread safety considerations

**Quality Gates:**
- [ ] All CI checks pass
- [ ] SonarCloud analysis acceptable
- [ ] Coverage requirements met
- [ ] No performance regressions
- [ ] Security considerations addressed

## 📝 Additional Notes
<!-- Any additional context, concerns, or future work -->

**Future Work:**
- 

**Known Limitations:**
- 

**Dependencies:**
- 

---

<!-- 
📚 PR Best Practices Reminder:
- Keep PRs small and focused (< 400 lines preferred)
- Provide clear context in title and description  
- Test thoroughly before requesting review
- Respond to feedback promptly
- Squash commits before merging if needed
-->