package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BedInteractListener implements Listener {

    private final FairPerks plugin;

    public BedInteractListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBedInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();

            if (clickedBlock != null && clickedBlock.getBlockData() instanceof Bed) {
                if (event.getClickedBlock().getWorld().getEnvironment() != World.Environment.NORMAL) {
                    Player player = event.getPlayer();

                    if (this.plugin.getEssentialsHook().getUser(player).isGodModeEnabled()) {
                        event.setCancelled(true);
                        this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.bed.god-mode");
                    } else if (player.isFlying()) {
                        event.setCancelled(true);
                        this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.bed.flying");
                    }
                }
            }
        }
    }
}
