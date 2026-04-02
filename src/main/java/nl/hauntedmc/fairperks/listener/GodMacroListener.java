package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static nl.hauntedmc.fairperks.util.CombatUtil.isInCombat;

public class GodMacroListener implements Listener {

    private static final NamespacedKey GOD_MACRO_KEY = new NamespacedKey("fairperks", "godmacro");
    private final FairPerks plugin;
    private final Map<UUID, Long> shiftTimestamps;

    public GodMacroListener(FairPerks plugin) {
        this.plugin = plugin;
        this.shiftTimestamps = new HashMap<>();
    }

    @EventHandler
    public void onPlayerShiftToggleGod(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (player.hasPermission("essentials.god") && player.hasPermission("fairperks.godmacro")) {
            if (!isInCombat(player, this.plugin)) {
                PersistentDataContainer playerMeta = player.getPersistentDataContainer();

                if (playerMeta.has(GOD_MACRO_KEY, PersistentDataType.STRING)) {
                    String macroStatus = playerMeta.get(GOD_MACRO_KEY, PersistentDataType.STRING);

                    if (macroStatus != null && macroStatus.equals("true")) {
                        if (event.isSneaking()) {
                            long currentTime = System.currentTimeMillis();

                            if (shiftTimestamps.containsKey(playerId)) {
                                long lastShiftTime = shiftTimestamps.get(playerId);

                                final int godMacroInterval = this.plugin.getConfig().getInt("godmacrointerval");

                                if (currentTime - lastShiftTime < godMacroInterval) {
                                    player.performCommand("god");
                                    shiftTimestamps.remove(playerId);
                                } else {
                                    shiftTimestamps.remove(playerId);
                                    shiftTimestamps.put(playerId, currentTime);
                                }

                            } else {
                                shiftTimestamps.put(playerId, currentTime);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        shiftTimestamps.remove(event.getPlayer().getUniqueId());
    }
}
