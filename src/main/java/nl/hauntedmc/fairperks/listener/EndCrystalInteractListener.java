package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EndCrystalInteractListener implements Listener {

    private final FairPerks plugin;

    public EndCrystalInteractListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void crystalDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.END_CRYSTAL) {
            return;
        }

        Player player = resolvePlayerDamager(event.getDamager());
        if (player == null) {
            return;
        }

        PlayerRestrictionUtil.denyWhenGodModeOrFlying(
                this.plugin,
                player,
                event,
                "actionbar.deny.end-crystal.god-mode",
                "actionbar.deny.end-crystal.flying"
        );
    }

    private Player resolvePlayerDamager(Entity damager) {
        if (damager instanceof Player player) {
            return player;
        }

        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player player) {
            return player;
        }

        return null;
    }
}
