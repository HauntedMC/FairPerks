package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;

import java.util.List;

public class BlockIgniteListener implements Listener {

    private final FairPerks plugin;

    public BlockIgniteListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockIgniteNearMobs(BlockIgniteEvent event) {
        @SuppressWarnings("unchecked")
        List<IgniteCause> list = (List<IgniteCause>) this.plugin.getConfig().getList("blockignite");

        if (list != null && list.contains(event.getCause())) {
            Player damager = event.getPlayer();

            if (damager == null) {
                return;
            }

            final int entityRange = this.plugin.getConfig().getInt("ignite_entityrange");
            List<Entity> nearbyEntities = damager.getNearbyEntities(entityRange, entityRange, entityRange);

            final String denyMessage = ChatColor.RED + "Je kunt geen vuur aansteken bij mobs %s.";

            if (nearbyEntities.stream().anyMatch(entity -> entity instanceof Enemy)) {
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
    }
}
