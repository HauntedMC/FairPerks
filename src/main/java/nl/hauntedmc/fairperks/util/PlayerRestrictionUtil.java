package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public final class PlayerRestrictionUtil {

    private PlayerRestrictionUtil() {
    }

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

    public static boolean isGodModeOrFlying(Player player, FairPerks plugin) {
        return isInGodMode(player, plugin) || player.isFlying();
    }

    private static boolean isInGodMode(Player player, FairPerks plugin) {
        Essentials essentialsHook = plugin.getEssentialsHook();
        if (essentialsHook == null) {
            return false;
        }

        User essentialsUser = essentialsHook.getUser(player);
        return essentialsUser != null && essentialsUser.isGodModeEnabled();
    }
}
