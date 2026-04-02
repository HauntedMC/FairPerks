package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import static nl.hauntedmc.fairperks.util.SpawnerUtil.isSpawnermob;

public class ProjectileListener implements Listener {

    private final FairPerks plugin;

    public ProjectileListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity damagedEntity = event.getHitEntity();
        if (damagedEntity == null || !LegacyUtil.isEnemy(damagedEntity.getType()) || isSpawnermob(damagedEntity)) {
            return;
        }

        Projectile projectile = event.getEntity();
        if (!(projectile.getShooter() instanceof Player player)) {
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
