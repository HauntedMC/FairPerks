package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.List;

public class LavaPlaceListener implements Listener {

    private static final int DEFAULT_LAVA_ENTITY_RANGE = 5;

    private final FairPerks plugin;

    public LavaPlaceListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLavaBucketInteract(PlayerBucketEmptyEvent event) {
        if (event.getBucket() != Material.LAVA_BUCKET) {
            return;
        }

        int entityRange = this.plugin.getConfig().getInt("lava_entityrange");
        if (entityRange <= 0) {
            entityRange = DEFAULT_LAVA_ENTITY_RANGE;
        }
        List<Entity> nearbyEntities = event.getPlayer().getNearbyEntities(entityRange, entityRange, entityRange);
        if (nearbyEntities.stream().noneMatch(entity -> LegacyUtil.isEnemy(entity.getType()))) {
            return;
        }

        PlayerRestrictionUtil.denyWhenGodModeOrFlying(
                this.plugin,
                event.getPlayer(),
                event,
                "actionbar.deny.lava.god-mode",
                "actionbar.deny.lava.flying"
        );
    }
}
