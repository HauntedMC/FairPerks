# Architecture Overview

FairPerks is a Paper plugin focused on controlled PvP safety behavior while respecting permissions and combat state.

## Design Goals

- Keep each protection concern in a dedicated listener.
- Minimize hidden behavior by relying on explicit config toggles.
- Integrate cleanly with EssentialsX, and optionally with CombatLogX for combat-state decisions.
- Keep shared logic in utility helpers rather than duplicating checks across listeners.

## Core Components

- `FairPerks`: plugin bootstrap, hook registration, listener wiring, and command registration.
- `GodMacroCommand`: enables/disables per-player god macro state.
- Listener package: event-driven protections for anchors, beds, ignition, combat, targeting, and macro behavior.
- Utility package:
  - `CombatUtil`: optional CombatLogX combat-state adapter (safe no-op when unavailable).
  - `InventoryUtil`: igniter item detection.
  - `MessageService`: language-file loading and MiniMessage/Adventure component rendering.
  - `SpawnerUtil`: spawner metadata read/write helpers.
  - `LegacyUtil`: hostile entity allowlist used by multiple listeners.

## Runtime Flow

Startup:

1. Config defaults are initialized.
2. EssentialsX hook is resolved; CombatLogX hook is resolved only when available.
3. Enabled listeners and command executors are registered.

Event handling:

1. Listener validates event type and relevant config scope.
2. Listener resolves actor state (god mode, flying, in-combat, metadata).
3. Event is cancelled when restricted conditions are met.
4. Player receives immediate action-bar feedback when applicable.

Shutdown:

1. Plugin-owned scheduler tasks are cancelled.

## Why This Matters

For operators, this structure keeps feature activation explicit and operationally predictable.

For contributors, change boundaries stay clear: event policy in listeners, shared primitives in utils, and bootstrapping in the main plugin class.
