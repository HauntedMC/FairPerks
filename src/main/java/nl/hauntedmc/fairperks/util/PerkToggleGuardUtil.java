package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.entity.Player;

import static nl.hauntedmc.fairperks.util.CombatUtil.isInCombat;

/**
 * Shared policy guard for enabling high-impact perks like god/fly.
 */
public final class PerkToggleGuardUtil {

    private static final int DEFAULT_TOGGLE_BLOCK_RANGE = 16;

    private PerkToggleGuardUtil() {
    }

    /**
     * Returns whether a player is allowed to enable a perk right now.
     * <p>
     * This intentionally sends feedback messages itself because the same decision is
     * reused by both command guards and god-macro toggles.
     */
    public static boolean canEnablePerk(Player player, FairPerks plugin) {
        if (isInCombat(player, plugin)) {
            plugin.getMessageService().sendActionBar(player, "actionbar.deny.perk-toggle.combat");
            return false;
        }

        int toggleRange = plugin.getConfig().getInt("perktoggle_entityrange");
        if (toggleRange <= 0) {
            toggleRange = DEFAULT_TOGGLE_BLOCK_RANGE;
        }

        if (player.getNearbyEntities(toggleRange, toggleRange, toggleRange)
                .stream()
                .anyMatch(entity -> LegacyUtil.isEnemy(entity.getType()))) {
            plugin.getMessageService().sendActionBar(player, "actionbar.deny.perk-toggle.hostile-nearby");
            return false;
        }

        return true;
    }
}
