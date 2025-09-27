# Communication Guidelines

## Communication Principles

### Be Respectful
- Assume positive intent
- Use inclusive language
- Value diverse perspectives
- Disagree professionally

### Be Clear
- State your purpose upfront
- Use simple, direct language
- Provide context
- Include examples

### Be Responsive
- Acknowledge receipt
- Set expectations for response
- Follow up on commitments
- Close the loop

## Communication Channels

### GitHub Issues
**Purpose**: Track work, bugs, features
**Response Time**: 24 hours
**Best For**:
- Bug reports
- Feature requests
- Task tracking
- Technical discussions

**Guidelines**:
- Use templates
- Add relevant labels
- Link related issues
- Update status regularly

### GitHub Discussions
**Purpose**: Team collaboration, Q&A
**Response Time**: 24 hours
**Best For**:
- Daily standups
- Architecture decisions
- Team announcements
- Knowledge sharing

**Categories**:
- 📢 Announcements
- 🎯 Planning
- 💡 Ideas
- 🏗️ Architecture
- 📚 Learning
- 💬 General

### Pull Requests
**Purpose**: Code review and merge
**Response Time**: 4 hours (working hours)
**Best For**:
- Code changes
- Documentation updates
- Configuration changes

**Guidelines**:
- Complete PR template
- Add reviewers
- Respond to feedback
- Keep PRs focused

### GitHub Wiki
**Purpose**: Permanent documentation
**Update Frequency**: As needed
**Best For**:
- Architecture docs
- Setup guides
- Process documentation
- Knowledge base

## Async Communication Best Practices

### Writing Messages
```markdown
## Subject: Clear, Specific Title

**Priority**: High/Medium/Low
**Response Needed By**: Date/Time

### Context
Brief background information

### Question/Request
What you need specifically

### Options/Recommendations
1. Option A with pros/cons
2. Option B with pros/cons

### Next Steps
- What happens next
- Who does what
```

### Daily Standups
Post by 10 AM in GitHub Discussions:
```markdown
## @username - Date

### Yesterday
- Completed feature X
- Reviewed PR #123

### Today
- Working on issue #456
- Pairing with @teammate

### Blockers
- Need clarification on requirement Y
```

### Status Updates
```markdown
## Project: Feature Name
**Status**: 🟢 On Track / 🟡 At Risk / 🔴 Blocked

### Progress
- [x] Task 1 complete
- [ ] Task 2 in progress (60%)
- [ ] Task 3 not started

### Risks/Issues
- Risk: Description and mitigation

### Need Help With
- Specific assistance required
```

## Synchronous Communication

### When to Go Sync
- Complex technical discussions
- Conflict resolution
- Brainstorming sessions
- Emergency issues
- Onboarding/pairing

### Video Call Etiquette
- Test audio/video before joining
- Mute when not speaking
- Use virtual backgrounds if needed
- Share agenda beforehand
- Record if others can't attend

### Meeting Types

#### Sprint Planning
**Duration**: 2 hours
**Attendees**: Entire team
**Agenda**:
1. Review last sprint
2. Define goals
3. Select backlog items
4. Estimate effort

#### Architecture Review
**Duration**: 1 hour
**Attendees**: Tech lead + developers
**Agenda**:
1. Present proposal
2. Discuss trade-offs
3. Address concerns
4. Decide path forward

#### 1:1 Check-ins
**Duration**: 30 minutes
**Attendees**: Lead + team member
**Agenda**:
1. Progress update
2. Blockers/challenges
3. Growth/learning
4. Feedback

## Documentation Standards

### Code Comments
```kotlin
// Why: Explain the reasoning
// What: Complex algorithm explanation
// TODO: With assignee and date
// FIXME: With issue number
// WARNING: Critical information
```

### Commit Messages
```
type(scope): subject

Body explaining what and why

Fixes #123
```

### PR Descriptions
```markdown
## Description
What this PR does

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change

## Testing
How to test

## Screenshots
If applicable

## Checklist
- [ ] Tests pass
- [ ] Docs updated
```

## Escalation Path

### Level 1: Direct Discussion
- Try to resolve between individuals
- Focus on the issue, not personality
- Document the discussion

### Level 2: Team Discussion
- Bring to team meeting
- Get multiple perspectives
- Vote if necessary

### Level 3: Tech Lead Decision
- Present both sides
- Tech lead makes final call
- Document decision and rationale

### Level 4: External Mediation
- For serious conflicts
- Involve HR or management
- Focus on resolution

## Response Time Expectations

### Priority Levels
- **P0 - Critical**: 1 hour
- **P1 - High**: 4 hours
- **P2 - Medium**: 24 hours
- **P3 - Low**: 48 hours

### Outside Working Hours
- No expectation to respond
- Use GitHub's scheduled reminders
- Mark as urgent only if truly urgent
- Provide context for timezone differences

## Language & Tone

### Do's ✅
- "I think..."
- "Have you considered..."
- "What if we..."
- "I'd suggest..."
- "Great work on..."

### Don'ts ❌
- "You're wrong"
- "This is stupid"
- "Obviously..."
- "Everyone knows..."
- "Just do..."

### Giving Feedback
```
Situation: When X happened...
Behavior: You did/said Y...
Impact: Which resulted in Z...
Next: Going forward, consider...
```

### Receiving Feedback
- Listen without defending
- Ask clarifying questions
- Thank for the feedback
- Reflect and improve

## Cultural Considerations

### Time Zones
- Include timezone in meeting invites
- Rotate meeting times if global team
- Record important sessions
- Use async when possible

### Language
- Use simple English
- Avoid idioms and slang
- Clarify acronyms
- Be patient with non-native speakers

### Holidays
- Respect different cultures
- Plan around major holidays
- Update team calendar
- Communicate availability

## Emergency Communication

### Incident Response
1. Create incident channel/issue
2. Assign incident commander
3. Update status every 30 min
4. Document timeline
5. Post-mortem within 48 hours

### Contact List
Maintain in private repo:
- Team members + timezones
- Escalation contacts
- Emergency procedures
- On-call schedule

## Communication Metrics

### Monthly Review
- Response time average
- Discussion participation
- PR review turnaround
- Meeting effectiveness

### Quarterly Survey
- Communication satisfaction
- Channel effectiveness
- Process improvements
- Tool recommendations

---

*Last Updated: September 27, 2025*
*Next Review: Q1 2026*