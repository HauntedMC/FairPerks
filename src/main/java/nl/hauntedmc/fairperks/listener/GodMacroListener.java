package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.GodMacroState;
import nl.hauntedmc.fairperks.util.PerkToggleGuardUtil;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles the optional god-macro behavior:
 * crouch twice within a short interval to run "/god".
 * <p>
 * When macro-triggered toggles would enable god mode, the same perk guard policy is
 * applied as direct /god and /fly commands. Disabling remains always allowed.
 */
public class GodMacroListener implements Listener {

    private static final int DEFAULT_GOD_MACRO_INTERVAL = 350;

    private final FairPerks plugin;
    private final Map<UUID, Long> shiftTimestamps;

    public GodMacroListener(FairPerks plugin) {
        this.plugin = plugin;
        this.shiftTimestamps = new HashMap<>();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerShiftToggleGod(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (!event.isSneaking()) {
            return;
        }

        if (!player.hasPermission("essentials.god") || !player.hasPermission("fairperks.godmacro")) {
            return;
        }

        if (!GodMacroState.isEnabled(player)) {
            return;
        }

        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        Long lastShiftTime = shiftTimestamps.get(playerId);

        int godMacroInterval = this.plugin.getConfig().getInt("godmacrointerval");
        if (godMacroInterval <= 0) {
            godMacroInterval = DEFAULT_GOD_MACRO_INTERVAL;
        }

        if (lastShiftTime != null && currentTime - lastShiftTime < godMacroInterval) {
            shiftTimestamps.remove(playerId);

            boolean enablingGodMode = !PlayerRestrictionUtil.isInGodMode(player, this.plugin);
            if (enablingGodMode && !PerkToggleGuardUtil.canEnablePerk(player, this.plugin)) {
                return;
            }

            player.performCommand("god");
            return;
        }

        shiftTimestamps.put(playerId, currentTime);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        shiftTimestamps.remove(event.getPlayer().getUniqueId());
    }
}
