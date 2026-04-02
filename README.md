# FairPerks

[![CI Lint](https://github.com/HauntedMC/FairPerks/actions/workflows/ci-lint.yml/badge.svg?branch=main)](https://github.com/HauntedMC/FairPerks/actions/workflows/ci-lint.yml)
[![CI Tests and Coverage](https://github.com/HauntedMC/FairPerks/actions/workflows/ci-tests-and-coverage.yml/badge.svg?branch=main)](https://github.com/HauntedMC/FairPerks/actions/workflows/ci-tests-and-coverage.yml)
[![Latest Release](https://img.shields.io/github/v/release/HauntedMC/FairPerks?sort=semver)](https://github.com/HauntedMC/FairPerks/releases/latest)
[![Java 21](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)](https://adoptium.net/)
[![License](https://img.shields.io/github/license/HauntedMC/FairPerks)](LICENSE)

EULA-friendly PvP safety perks for Paper servers, including controlled god-mode combat protections and anti-exploit interaction rules.

## Quick Start

1. Place `FairPerks.jar` in your server `plugins/` directory.
2. Install dependencies: EssentialsX and CombatLogX.
3. Start the server once to generate `config.yml`.
4. Enable only the features you want under `enabled`.
5. Restart and validate behavior in-game.

## Requirements

- Java 21
- Paper 1.21.x
- EssentialsX (required)
- CombatLogX API provider (required)

## Build From Source

```bash
mvn -B package
```

Output jar: `target/FairPerks.jar`

## Version Bump Workflow

Use the helper script to bump semver, commit, and tag:

```bash
scripts/bump-version.sh patch
scripts/bump-version.sh minor --push
```

Options:

- `major|minor|patch`: required bump type
- `--push`: push branch + tag after creating them
- `--remote <name>`: push/check against a remote (default: `origin`)

## Learn More

- [Configuration Guide](docs/CONFIGURATION.md)
- [Documentation Index](docs/README.md)
- [Architecture](docs/ARCHITECTURE.md)
- [Development Notes](docs/DEVELOPMENT.md)
- [Testing and Quality](docs/TESTING.md)
- [Contributing](CONTRIBUTING.md)

## Community

- [Support](SUPPORT.md)
- [Security Policy](SECURITY.md)
- [Code of Conduct](CODE_OF_CONDUCT.md)
