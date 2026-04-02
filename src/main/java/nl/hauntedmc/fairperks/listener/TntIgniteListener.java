package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static nl.hauntedmc.fairperks.util.InventoryUtil.holdsIgniter;

public class TntIgniteListener implements Listener {

    private final FairPerks plugin;

    public TntIgniteListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTNTIgnite(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        final int entityRange = this.plugin.getConfig().getInt("tnt_entityrange");
        List<Entity> nearbyEntities = player.getNearbyEntities(entityRange, entityRange, entityRange);

        if (nearbyEntities.stream().anyMatch(entity -> LegacyUtil.ENEMY.contains(entity.getType()))) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.TNT) {
                    if (holdsIgniter(player)) {
                        if (this.plugin.getEssentialsHook().getUser(player).isGodModeEnabled()) {
                            event.setCancelled(true);
                            this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.tnt-ignite.god-mode");
                        } else if (player.isFlying()) {
                            event.setCancelled(true);
                            this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.tnt-ignite.flying");
                        }
                    }
                }
            }
        }
    }
}
