package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
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

        final String denyMessage = ChatColor.RED + "Je kunt geen vuur aansteken bij mobs %s.";

        if (nearbyEntities.stream().anyMatch(entity -> LegacyUtil.ENEMY.contains(entity.getType()))) {
            if (this.plugin.getEssentialsHook().getUser(damager).isGodModeEnabled()) {
                event.setCancelled(true);
                //noinspection deprecation
                damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format(denyMessage, "in god mode")));
            } else if (damager.isFlying()) {
                event.setCancelled(true);
                //noinspection deprecation
                damager.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format(denyMessage, "terwijl je vliegt")));
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
