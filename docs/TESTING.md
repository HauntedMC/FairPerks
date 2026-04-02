# Testing and Quality

Testing in this project is designed to catch regressions in protection behavior while keeping contributor workflow practical.

## Test Structure

Tests are organized under `src/test/java` and mirror production package boundaries:

- command tests for command behavior and toggle state handling;
- listener tests for event cancellation and condition branching;
- utility tests for deterministic helper behavior;
- root plugin tests for class-level contracts.

## Local Commands

Run tests:

```bash
mvn -q test
```

Run full quality checks:

```bash
mvn -B verify
```

Run lint checks:

```bash
mvn -B -DskipTests checkstyle:check
```

Generate local coverage report:

```bash
mvn -q test jacoco:report
```

## What to Test

When changing behavior, add or update tests close to that behavior:

- listener changes: event-path branching, cancellation outcomes, and edge guards;
- command changes: permission checks and persistent toggle state transitions;
- utility changes: deterministic logic and data conversion behavior;
- bootstrap changes: registration and hook contracts that can be validated in unit scope.

Focus on user-visible and regression-prone behavior.

## Test Quality Bar

Use these rules when adding or reviewing tests:

- prefer behavior assertions over "does not throw" assertions;
- validate happy path and failure path for event-sensitive logic;
- avoid static mocking when regular dependency boundaries are available;
- assert observable outcomes (state changes, cancellation, emitted messages, interactions).

## Coverage Reports

After `jacoco:report`:

- HTML report: `target/site/jacoco/index.html`
- XML report: `target/site/jacoco/jacoco.xml`
- CSV summary: `target/site/jacoco/jacoco.csv`

## CI

CI validates Checkstyle, tests, and coverage report generation on pull requests and `main` updates.
