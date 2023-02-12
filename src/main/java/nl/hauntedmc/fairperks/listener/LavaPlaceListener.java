package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
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

            final int entityRange = this.plugin.getConfig().getInt("lavs_entityrange");
            List<Entity> nearbyEntities = player.getNearbyEntities(entityRange, entityRange, entityRange);

            final String denyMessage = ChatColor.RED + "Je kunt geen lava plaatsen bij mobs %s.";

            if (nearbyEntities.stream().anyMatch(entity -> LegacyUtil.ENEMY.contains(entity.getType()))) {
                if (this.plugin.getEssentialsHook().getUser(player).isGodModeEnabled()) {
                    event.setCancelled(true);
                    //noinspection deprecation
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format(denyMessage, "in god mode")));
                } else if (player.isFlying()) {
                    event.setCancelled(true);
                    //noinspection deprecation
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(String.format(denyMessage, "terwijl je vliegt")));
                }
            }
        }
    }

}
