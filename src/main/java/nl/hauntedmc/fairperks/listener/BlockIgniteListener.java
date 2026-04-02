package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

import java.util.List;
import java.util.Locale;

public class BlockIgniteListener implements Listener {

    private final FairPerks plugin;

    public BlockIgniteListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgniteNearMobs(BlockIgniteEvent event) {
        if (!isConfiguredCause(event.getCause())) {
            return;
        }

        Player damager = event.getPlayer();

        if (damager == null) {
            return;
        }

        final int entityRange = this.plugin.getConfig().getInt("ignite_entityrange");
        List<Entity> nearbyEntities = damager.getNearbyEntities(entityRange, entityRange, entityRange);

        if (nearbyEntities.stream().anyMatch(entity -> LegacyUtil.ENEMY.contains(entity.getType()))) {
            if (this.plugin.getEssentialsHook().getUser(damager).isGodModeEnabled()) {
                event.setCancelled(true);
                this.plugin.getMessageService().sendActionBar(damager, "actionbar.deny.blockignite.god-mode");
            } else if (damager.isFlying()) {
                event.setCancelled(true);
                this.plugin.getMessageService().sendActionBar(damager, "actionbar.deny.blockignite.flying");
            }
        }
    }

    private boolean isConfiguredCause(IgniteCause cause) {
        List<?> configuredCauses = this.plugin.getConfig().getList("blockignite");
        if (configuredCauses == null) {
            return false;
        }

        for (Object configuredCause : configuredCauses) {
            if (configuredCause instanceof IgniteCause && configuredCause == cause) {
                return true;
            }

            if (configuredCause instanceof String) {
                try {
                    IgniteCause parsed = IgniteCause.valueOf(((String) configuredCause).trim().toUpperCase(Locale.ROOT));
                    if (parsed == cause) {
                        return true;
                    }
                } catch (IllegalArgumentException ignored) {
                }
            }
        }

        return false;
    }
}
