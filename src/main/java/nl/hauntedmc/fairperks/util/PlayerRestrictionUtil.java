package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Centralized checks for whether perk-related actions must be blocked.
 */
public final class PlayerRestrictionUtil {

    private PlayerRestrictionUtil() {
    }

    /**
     * Cancels the event when the player currently has god mode or active flight.
     * <p>
     * This helper is shared by many listeners so they all enforce identical behavior
     * and messaging order.
     *
     * @return {@code true} when the event was cancelled by this method
     */
    public static boolean denyWhenGodModeOrFlying(
            FairPerks plugin,
            Player player,
            Cancellable event,
            String godModeMessageKey,
            String flyingMessageKey
    ) {
        if (isInGodMode(player, plugin)) {
            event.setCancelled(true);
            plugin.getMessageService().sendActionBar(player, godModeMessageKey);
            return true;
        }

        if (player.isFlying()) {
            event.setCancelled(true);
            plugin.getMessageService().sendActionBar(player, flyingMessageKey);
            return true;
        }

        return false;
    }

    /**
     * Returns whether active restrictions apply right now.
     * <p>
     * Flight checks use {@link Player#isFlying()} intentionally. A player having
     * {@code allowFlight=true} but not actually flying should not be blocked.
     */
    public static boolean isGodModeOrFlying(Player player, FairPerks plugin) {
        return isInGodMode(player, plugin) || player.isFlying();
    }

    /**
     * Reads Essentials' current god-mode state for this player.
     */
    public static boolean isInGodMode(Player player, FairPerks plugin) {
        Essentials essentialsHook = plugin.getEssentialsHook();
        if (essentialsHook == null) {
            return false;
        }

        User essentialsUser = essentialsHook.getUser(player);
        return essentialsUser != null && essentialsUser.isGodModeEnabled();
    }
}
