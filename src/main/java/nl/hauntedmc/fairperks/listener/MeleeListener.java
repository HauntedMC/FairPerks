package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMeleeDamage(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();
        if (!LegacyUtil.isEnemy(damagedEntity.getType()) || isSpawnermob(damagedEntity)) {
            return;
        }

        if (!(event.getDamager() instanceof Player player)) {
            return;
        }

        PlayerRestrictionUtil.denyWhenGodModeOrFlying(
                this.plugin,
                player,
                event,
                "actionbar.deny.mob-kill.god-mode",
                "actionbar.deny.mob-kill.flying"
        );
    }
}
