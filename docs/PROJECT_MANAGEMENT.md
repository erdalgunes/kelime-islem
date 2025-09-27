# Project Management Guide

## Overview
This document outlines the project management processes for Kelime İşlem development.

## Development Methodology
We follow a hybrid Agile approach combining Scrum sprints with Kanban flow:
- **Sprint Duration**: 2 weeks
- **Release Cycle**: Monthly
- **Planning**: Every other Monday
- **Review/Retro**: Every other Friday

## Sprint Process

### Sprint Planning (Day 1)
1. **Review Previous Sprint**
   - Completed items
   - Carried over items
   - Blockers encountered

2. **Define Sprint Goals**
   - 3-5 clear objectives
   - Aligned with quarterly roadmap
   - Measurable outcomes

3. **Select Sprint Backlog**
   - Priority issues from product backlog
   - Technical debt items (20% allocation)
   - Bug fixes

4. **Estimate Effort**
   - Story points (1, 2, 3, 5, 8, 13)
   - Team capacity planning
   - Buffer for unknowns (15%)

### Daily Standups
- **When**: 10:00 AM daily (async via GitHub Discussions)
- **Format**:
  - What I completed yesterday
  - What I'm working on today
  - Any blockers

### Sprint Review (Day 14)
- Demo completed features
- Gather stakeholder feedback
- Update product backlog
- Celebrate achievements

### Sprint Retrospective
- What went well?
- What could improve?
- Action items for next sprint
- Team health check

## Issue Management

### Issue Types
- **Epic**: Large feature spanning multiple sprints
- **Story**: User-facing feature
- **Task**: Technical work item
- **Bug**: Defect in existing functionality
- **Spike**: Research/investigation task

### Issue Workflow
```text
📝 Backlog → 🎯 Sprint Ready → 💻 In Progress → 👀 Review → ✅ Done
```

### Priority Levels
1. **P0 - Critical**: Production down, security issue
2. **P1 - High**: Major feature blocked, significant bug
3. **P2 - Medium**: Important but not urgent
4. **P3 - Low**: Nice to have, minor improvements

### Definition of Ready
- [ ] Clear acceptance criteria
- [ ] Estimated (story points)
- [ ] Dependencies identified
- [ ] Design/mockups attached (if UI)
- [ ] Technical approach documented

### Definition of Done
- [ ] Code complete and pushed
- [ ] Unit tests written (>80% coverage)
- [ ] Integration tests passing
- [ ] Code reviewed and approved
- [ ] Documentation updated
- [ ] Deployed to staging
- [ ] QA tested
- [ ] Product owner accepted

## Release Management

### Version Numbering
Following Semantic Versioning (MAJOR.MINOR.PATCH):
- **MAJOR**: Breaking changes
- **MINOR**: New features, backward compatible
- **PATCH**: Bug fixes

### Release Process
1. **Feature Freeze** (3 days before release)
   - No new features
   - Only bug fixes
   - Update changelog

2. **Release Candidate**
   - Tag RC version
   - Deploy to staging
   - Full regression testing

3. **Production Release**
   - Tag release version
   - Deploy to production
   - Monitor metrics
   - Announce to users

### Hotfix Process
1. Create hotfix branch from main
2. Fix issue with tests
3. Deploy to staging
4. Quick QA validation
5. Deploy to production
6. Merge back to main and develop

## GitHub Project Setup

### Board Columns
1. **📋 Backlog**: All unscheduled work
2. **🎯 Sprint Ready**: Refined and ready for sprint
3. **📅 Current Sprint**: This sprint's commitment
4. **💻 In Progress**: Actively being worked on
5. **👀 In Review**: PR created, awaiting review
6. **✅ Done**: Completed this sprint

### Custom Fields
- **Story Points**: Effort estimation (1-13)
- **Priority**: P0, P1, P2, P3
- **Sprint**: Current sprint number
- **Epic**: Parent epic link
- **Platform**: Android, iOS, Desktop, Web
- **Component**: UI, Backend, Game Logic, etc.

### Views
1. **Sprint Board**: Current sprint Kanban
2. **Product Backlog**: All items by priority
3. **Roadmap**: Timeline view by milestone
4. **Team Workload**: Items by assignee
5. **Bug Tracker**: All bugs by status

## Metrics & Reporting

### Sprint Metrics
- **Velocity**: Average story points completed
- **Burndown**: Daily progress tracking
- **Cycle Time**: Time from start to done
- **Throughput**: Items completed per sprint

### Quality Metrics
- **Bug Rate**: Bugs found per sprint
- **Escape Rate**: Bugs found in production
- **Test Coverage**: Percentage of code tested
- **Code Review Time**: Average PR review duration

### Team Health Metrics
- **Sprint Goal Success**: % of sprints meeting goals
- **Commitment Accuracy**: Planned vs actual
- **Team Satisfaction**: Monthly survey
- **Knowledge Sharing**: Cross-functional work

### Reporting Schedule
- **Daily**: Standup updates
- **Weekly**: Progress summary
- **Sprint**: Review and retrospective
- **Monthly**: Stakeholder report
- **Quarterly**: Roadmap review

## Communication Channels

### GitHub Discussions
- **Announcements**: Project updates
- **Planning**: Sprint planning discussions
- **Architecture**: Technical decisions
- **Ideas**: Feature proposals
- **Q&A**: Team questions

### Documentation
- **README.md**: Project overview
- **CONTRIBUTING.md**: Contribution guide
- **PROJECT_ROADMAP.md**: Long-term vision
- **CHANGELOG.md**: Release history
- **/docs**: Technical documentation

### Meetings
- **Sprint Planning**: Every 2 weeks
- **Daily Standup**: Async via Discussions
- **Sprint Review**: End of sprint
- **Retrospective**: After review
- **Architecture Review**: Monthly

## Tools & Integrations

### Development Tools
- **IDE**: Android Studio / IntelliJ IDEA
- **Version Control**: Git / GitHub
- **CI/CD**: GitHub Actions
- **Code Quality**: SonarCloud, Detekt
- **Testing**: Kotest, Kover

### Project Management
- **Issues**: GitHub Issues
- **Projects**: GitHub Projects
- **Discussions**: GitHub Discussions
- **Wiki**: GitHub Wiki
- **Time Tracking**: Integrated in issues

### Communication
- **Async**: GitHub Discussions
- **Sync**: Video calls as needed
- **Documentation**: Markdown in repo
- **Notifications**: GitHub + email

## Roles & Responsibilities

### Product Owner
- Define product vision
- Prioritize backlog
- Accept completed work
- Stakeholder communication

### Tech Lead
- Technical decisions
- Architecture guidance
- Code review
- Mentoring

### Developer
- Implement features
- Write tests
- Fix bugs
- Document code

### QA Engineer
- Test planning
- Manual testing
- Automation
- Bug reporting

### DevOps Engineer
- CI/CD pipeline
- Infrastructure
- Monitoring
- Deployment

## Best Practices

### Code Management
- Feature branches
- Small, focused PRs
- Descriptive commit messages
- Regular rebasing

### Documentation
- Document decisions
- Update as you go
- Include examples
- Keep it simple

### Testing
- Test-driven development
- Automated testing
- Manual exploratory testing
- Performance testing

### Continuous Improvement
- Regular retrospectives
- Experiment with process
- Measure outcomes
- Adapt based on learning

---

*Last Updated: September 27, 2025*
*Next Review: End of Q1 2026*
