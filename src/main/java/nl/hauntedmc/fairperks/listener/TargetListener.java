package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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
        if (!(event.getTarget() instanceof Player player)) {
            return;
        }

        Entity entity = event.getEntity();
        if (!LegacyUtil.isEnemy(entity.getType())) {
            return;
        }

        if (PlayerRestrictionUtil.isGodModeOrFlying(player, this.plugin)) {
            event.setTarget(null);
            event.setCancelled(true);
        }
    }
}
