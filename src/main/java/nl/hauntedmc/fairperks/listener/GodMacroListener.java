package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

import static nl.hauntedmc.fairperks.util.CombatUtil.isInCombat;

public class GodMacroListener implements Listener {

    private static final NamespacedKey GOD_MACRO_KEY = new NamespacedKey("fairperks", "godmacro");
    private final FairPerks plugin;
    private final Map<Player, Long> shiftTimestamps;

    public GodMacroListener(FairPerks plugin) {
        this.plugin = plugin;
        this.shiftTimestamps = new HashMap<>();
    }

    @EventHandler
    public void onPlayerShiftToggleGod(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("essentials.god") && player.hasPermission("fairperks.godmacro")) {
            if (!isInCombat(player, this.plugin)) {
                PersistentDataContainer playerMeta = player.getPersistentDataContainer();

                if (playerMeta.has(GOD_MACRO_KEY, PersistentDataType.STRING)) {
                    String macroStatus = playerMeta.get(GOD_MACRO_KEY, PersistentDataType.STRING);

                    if (macroStatus != null && macroStatus.equals("true")) {
                        if (player.isSneaking()) {
                            long currentTime = System.currentTimeMillis();

                            if (shiftTimestamps.containsKey(player)) {
                                long lastShiftTime = shiftTimestamps.get(player);

                                final int godMacroInterval = this.plugin.getConfig().getInt("godmacrointerval");

                                if (currentTime - lastShiftTime < godMacroInterval) {
                                    player.performCommand("god");
                                    shiftTimestamps.remove(player);
                                } else {
                                    shiftTimestamps.remove(player);
                                    shiftTimestamps.put(player, currentTime);
                                }

                            } else {
                                shiftTimestamps.put(player, currentTime);
                            }
                        }
                    }
                }
            }
        }
    }
}
