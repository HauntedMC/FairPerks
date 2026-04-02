# Development Notes

This page is for contributors who want a fast, reliable local workflow for FairPerks.

## Local Setup

```bash
mvn -q -DskipTests compile
```

Useful commands during development:

```bash
mvn -q test
mvn -B -DskipTests checkstyle:check
mvn -q test jacoco:report
mvn -B verify
mvn -B package
```

## Recommended Workflow

1. Create a branch for one focused change.
2. Implement the change with tests in the same pass.
3. Run local validation (`test` and Checkstyle at minimum).
4. Update docs when behavior or operator workflow changes.
5. Open a PR with context, impact, and migration notes (if any).

## Engineering Guidelines

- Keep listener logic concise and behavior-driven.
- Push repeated logic into utility helpers instead of copy/paste between listeners.
- Prefer predictable, explicit checks over broad implicit behavior.
- Validate hook and event state defensively to avoid runtime null issues.
- Keep operator-facing messaging clear and actionable.

## Before You Open a PR

- Build succeeds locally.
- Relevant tests pass.
- New behavior is covered by tests.
- Checkstyle passes.
- Config or operational impact is documented.
