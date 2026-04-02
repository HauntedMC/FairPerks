package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.GodMacroState;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static nl.hauntedmc.fairperks.util.CombatUtil.isInCombat;

public class GodMacroListener implements Listener {

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

        if (isInCombat(player, this.plugin) || !GodMacroState.isEnabled(player)) {
            return;
        }

        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();
        Long lastShiftTime = shiftTimestamps.get(playerId);

        int godMacroInterval = this.plugin.getConfig().getInt("godmacrointerval");
        if (lastShiftTime != null && currentTime - lastShiftTime < godMacroInterval) {
            player.performCommand("god");
            shiftTimestamps.remove(playerId);
            return;
        }

        shiftTimestamps.put(playerId, currentTime);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        shiftTimestamps.remove(event.getPlayer().getUniqueId());
    }
}
