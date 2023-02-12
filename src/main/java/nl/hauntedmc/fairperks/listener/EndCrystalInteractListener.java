package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EndCrystalInteractListener implements Listener {

    private final FairPerks plugin;

    public EndCrystalInteractListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void crystalDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (entity.getType() == EntityType.ENDER_CRYSTAL) {

            Player player;

            if (event.getDamager() instanceof Player) {
                player = (Player) event.getDamager();
            } else {
                return;
            }

            final String denyMessage = ChatColor.RED + "Je kunt geen end crystals opblazen %s.";

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
