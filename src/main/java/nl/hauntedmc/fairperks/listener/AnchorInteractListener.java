package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class AnchorInteractListener implements Listener {

    private final FairPerks plugin;

    public AnchorInteractListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnchorInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) {
                if (event.getClickedBlock().getWorld().getEnvironment() != World.Environment.NETHER) {
                    Player player = event.getPlayer();

                    if (this.plugin.getEssentialsHook().getUser(player).isGodModeEnabled()) {
                        event.setCancelled(true);
                        this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.anchor.god-mode");
                    } else if (player.isFlying()) {
                        event.setCancelled(true);
                        this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.anchor.flying");
                    }
                }
            }
        }
    }
}
