package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class TargetListener implements Listener {

    private final FairPerks plugin;

    public TargetListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        Player player;

        if (event.getTarget() instanceof Player) {
            player = (Player) event.getTarget();
        } else {
            return;
        }

        Entity entity = event.getEntity();

        if (entity instanceof Enemy) {
            if (this.plugin.getEssentialsHook().getUser(player).isGodModeEnabled() || player.isFlying()) {
                event.setTarget(null);
                event.setCancelled(true);
            }
        }
    }
}
