package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
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
                final String denyMessage = ChatColor.RED + "Je kunt geen creepers igniten %s.";

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
