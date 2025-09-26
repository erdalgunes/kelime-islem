#!/usr/bin/env python3
"""
Spawn Codegen.sh agents to fix CI/CD issues automatically.
"""

import json
import os
import sys
import time
import requests
from typing import Optional, Dict, Any

# Configuration
ORG_ID = "4969"
REPO_ID = "140699"  # kelime-islem repository
API_BASE_URL = "https://codegen-sh--rest-api.modal.run/v1"

def spawn_agent(prompt: str, metadata: Optional[Dict[str, Any]] = None) -> Dict[str, Any]:
    """
    Spawn a Codegen agent with the given prompt.

    Returns the agent run details including ID and web URL.
    """
    token = os.environ.get("CODEGEN_API_KEY")
    if not token:
        raise ValueError("CODEGEN_API_KEY environment variable not set")

    url = f"{API_BASE_URL}/organizations/{ORG_ID}/agent/run"
    headers = {
        "Authorization": f"Bearer {token}",
        "Content-Type": "application/json"
    }

    # Get repo context from GitHub Actions
    repo = os.environ.get("GITHUB_REPOSITORY", "unknown")
    run_id = os.environ.get("GITHUB_RUN_ID", "")

    # Enhanced metadata
    full_metadata = {
        "repository": repo,
        "workflow_run_id": run_id,
        "timestamp": int(time.time()),
        **(metadata or {})
    }

    data = {
        "prompt": prompt,
        "repo_id": int(REPO_ID),  # Include repo_id for context
        "metadata": full_metadata
    }

    try:
        response = requests.post(url, headers=headers, json=data, timeout=30)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.RequestException as e:
        print(f"Error spawning agent: {e}", file=sys.stderr)
        sys.exit(1)

def main():
    """Main entry point for CLI usage."""
    if len(sys.argv) < 2:
        print("Usage: spawn_agent.py <issue_type> [details]", file=sys.stderr)
        print("Issue types: lint, test, build, security, coderabbit", file=sys.stderr)
        sys.exit(1)

    issue_type = sys.argv[1]
    details = sys.argv[2] if len(sys.argv) > 2 else ""

    # Get context from environment
    repo = os.environ.get("GITHUB_REPOSITORY", "")
    pr_number = os.environ.get("PR_NUMBER", "")
    commit_sha = os.environ.get("GITHUB_SHA", "")[:7]

    # Build appropriate prompt based on issue type
    prompts = {
        "lint": f"""Fix linting errors in repository {repo}.
                    Details: {details}
                    Make code comply with project style guide.""",

        "test": f"""Fix failing tests in repository {repo}.
                    Details: {details}
                    Ensure all tests pass without breaking existing functionality.""",

        "build": f"""Fix build errors in repository {repo}.
                     Details: {details}
                     Resolve compilation/build issues.""",

        "security": f"""Fix security vulnerabilities in repository {repo}.
                        Details: {details}
                        Apply security best practices and update dependencies.""",

        "coderabbit": f"""Implement CodeRabbit suggestions for PR #{pr_number} in {repo}.
                          Suggestions: {details}
                          Apply all recommended improvements.""",

        "performance": f"""Optimize performance issues in repository {repo}.
                           Details: {details}
                           Improve code efficiency and reduce bottlenecks.""",

        "coverage": f"""Improve test coverage in repository {repo}.
                        Current coverage: {details}
                        Add comprehensive tests to reach 80% coverage."""
    }

    prompt = prompts.get(issue_type, f"Fix {issue_type} issue in {repo}: {details}")

    # Add PR context if available
    if pr_number:
        prompt += f"\n\nContext: Pull Request #{pr_number}"

    prompt += f"\nCommit: {commit_sha}"

    # Spawn the agent
    print(f"🤖 Spawning Codegen agent for {issue_type} issue...")

    result = spawn_agent(prompt, metadata={
        "issue_type": issue_type,
        "pr_number": pr_number,
        "commit_sha": commit_sha
    })

    # Output results
    print(f"✅ Agent spawned successfully!")
    print(f"   ID: {result.get('id')}")
    print(f"   Status: {result.get('status')}")
    print(f"   Web URL: {result.get('web_url')}")

    # Set GitHub Actions output
    if "GITHUB_OUTPUT" in os.environ:
        with open(os.environ["GITHUB_OUTPUT"], "a") as f:
            f.write(f"agent_id={result.get('id')}\n")
            f.write(f"agent_url={result.get('web_url')}\n")
            f.write(f"agent_status={result.get('status')}\n")

if __name__ == "__main__":
    main()