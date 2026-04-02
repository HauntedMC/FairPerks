package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;

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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onProjectileHit(ProjectileHitEvent event) {
        Entity damagedEntity = event.getHitEntity();

        if (damagedEntity != null && LegacyUtil.ENEMY.contains(damagedEntity.getType())) {
            if (!isSpawnermob(damagedEntity)) {
                Projectile projectile = event.getEntity();

                Player player;

                if (projectile.getShooter() instanceof Player) {
                    player = (Player) projectile.getShooter();
                } else {
                    return;
                }

                if (this.plugin.getEssentialsHook().getUser(player).isGodModeEnabled()) {
                    event.setCancelled(true);
                    this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.mob-kill.god-mode");
                } else if (player.isFlying()) {
                    event.setCancelled(true);
                    this.plugin.getMessageService().sendActionBar(player, "actionbar.deny.mob-kill.flying");
                }
            }
        }
    }

}
