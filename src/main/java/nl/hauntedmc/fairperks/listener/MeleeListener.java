package nl.hauntedmc.fairperks.listener;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import nl.hauntedmc.fairperks.FairPerks;
import org.bukkit.ChatColor;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static nl.hauntedmc.fairperks.util.SpawnerUtil.isSpawnermob;

public class MeleeListener implements Listener {

    private final FairPerks plugin;

    public MeleeListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMeleeDamage(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();

        if (damagedEntity instanceof Enemy) {
            if (!isSpawnermob(damagedEntity)) {

                Player player;

                if (event.getDamager() instanceof Player) {
                    player = (Player) event.getDamager();
                } else {
                    return;
                }

                final String denyMessage = ChatColor.RED + "Je kunt geen mobs killen %s.";

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
