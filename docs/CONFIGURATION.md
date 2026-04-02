# Configuration Guide

This guide focuses on practical setup and safe operation of FairPerks in production-like environments.

## Runtime File Layout

FairPerks uses one main runtime file:

- `plugins/FairPerks/config.yml`

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
- Ensure EssentialsX and CombatLogX are present and loaded before FairPerks.
