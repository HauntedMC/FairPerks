package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.List;

public class LavaPlaceListener implements Listener {
    private final FairPerks plugin;

    public LavaPlaceListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLavaBucketInteract(PlayerBucketEmptyEvent event) {
        if (event.getBucket() == Material.LAVA_BUCKET) {
            Player player = event.getPlayer();

            final int entityRange = this.plugin.getConfig().getInt("lava_entityrange");
            List<Entity> nearbyEntities = player.getNearbyEntities(entityRange, entityRange, entityRange);

            if (nearbyEntities.stream().anyMatch(entity -> LegacyUtil.ENEMY.contains(entity.getType()))) {
                if (this.plugin.getEssentialsHook().getUser(player).isGodModeEnabled()) {
                    event.setCancelled(true);
                    this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.lava.god-mode");
                } else if (player.isFlying()) {
                    event.setCancelled(true);
                    this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.lava.flying");
                }
            }
        }
    }

}
