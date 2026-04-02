package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static nl.hauntedmc.fairperks.util.InventoryUtil.holdsIgniter;

public class CreeperIgniteListener implements Listener {

    private final FairPerks plugin;

    public CreeperIgniteListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreeperIgnite(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.CREEPER) {
            Player player = event.getPlayer();

            if (holdsIgniter(player)) {
                if (this.plugin.getEssentialsHook().getUser(player).isGodModeEnabled()) {
                    event.setCancelled(true);
                    this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.creeper-ignite.god-mode");
                } else if (player.isFlying()) {
                    event.setCancelled(true);
                    this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.creeper-ignite.flying");
                }
            }
        }
    }
}
