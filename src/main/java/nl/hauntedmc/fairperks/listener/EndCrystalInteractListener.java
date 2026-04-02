package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

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

        if (entity.getType() == EntityType.END_CRYSTAL) {

            Player player;

            if (event.getDamager() instanceof Player) {
                player = (Player) event.getDamager();
            } else {
                return;
            }

            if (this.plugin.getEssentialsHook().getUser(player).isGodModeEnabled()) {
                event.setCancelled(true);
                this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.end-crystal.god-mode");
            } else if (player.isFlying()) {
                event.setCancelled(true);
                this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.end-crystal.flying");
            }
        }
    }
}
