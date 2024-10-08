package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
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

                    final String denyMessage = ChatColor.RED + "Je kunt geen respawn anchor opblazen %s.";

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
}
