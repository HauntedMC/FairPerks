# Configuration Guide

This guide focuses on practical setup and safe operation of FairPerks in production-like environments.

## Runtime File Layout

FairPerks uses one main runtime file:

- `plugins/FairPerks/config.yml`
- `plugins/FairPerks/messages.yml` (default English messages)
- `plugins/FairPerks/messages_NL.yml` (Dutch translation)

## Automatic File Synchronization

On startup, FairPerks synchronizes `config.yml` and `messages.yml` with the bundled defaults:

- missing keys are added
- keys that no longer exist in the bundled defaults are removed
- existing values for known keys are preserved

This keeps server files current across plugin updates while retaining your configured values.

## Language Selection

Set `language` in `config.yml`:

- `default`: use `messages.yml`
- `NL`: use `messages_NL.yml`
- any custom code (for example `DE`): attempts `messages_DE.yml`

If the selected language file does not exist, FairPerks automatically falls back to `messages.yml`.

## Feature Toggles

Most behavior is controlled by the `enabled` section:

- `anchor`: respawn-anchor interaction protection.
- `bed`: bed explosion interaction protection.
- `blockignite`: ignition-near-mobs protection by cause list.
- `spawnermobs`: marks eligible spawner mobs with metadata.
- `creeperignite`: flint/fire-charge creeper ignite protection.
- `endcrystal`: end crystal damage protection.
- `godmacro`: double-shift god command macro.
- `lava`: lava bucket placement protection.
- `melee`: melee combat restriction logic.
- `projectile`: projectile combat restriction logic.
- `target`: hostile targeting suppression logic.
- `tntignite`: TNT ignite protection.

## Key Numeric Settings

- `ignite_entityrange`: search range for `blockignite`.
- `lava_entityrange`: search range for `lava`.
- `tnt_entityrange`: search range for `tntignite`.
- `godmacrointerval`: max milliseconds between two shift presses.

## Recommended Rollout Workflow

1. Start with all non-essential features disabled.
2. Enable one feature (or one related group) at a time.
3. Restart and validate expected behavior in-game.
4. Move to the next feature only after validation.

This keeps incidents small and rollback simple.

## Troubleshooting Tips

- If protections do not trigger, verify the related `enabled` toggle first.
- If a range-based rule seems inactive, validate numeric range values and nearby hostile mobs.
- If god macro behavior is inconsistent, validate permissions and `godmacrointerval`.
- Ensure EssentialsX is present and loaded before FairPerks.
- CombatLogX is optional. If installed, god macro combat checks will be enabled automatically.
