package nl.hauntedmc.fairperks.util;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.projectiles.ProjectileSource;

import java.util.UUID;

/**
 * Resolves player-owned damage sources so listeners can apply the same policy to
 * direct and indirect attacks.
 */
public final class DamageSourceUtil {

    private DamageSourceUtil() {
    }

    /**
     * Returns the responsible player when damage is caused directly by a player, by a
     * projectile shot by a player, by primed TNT sourced from a player, or by a cloud
     * effect sourced from a player.
     */
    public static Player resolvePlayerDamager(Entity damager) {
        if (damager instanceof Player player) {
            return player;
        }

        if (damager instanceof Projectile projectile) {
            Player projectileOwner = resolveProjectileOwner(projectile);
            if (projectileOwner != null) {
                return projectileOwner;
            }
        }

        if (damager instanceof TNTPrimed primedTnt && primedTnt.getSource() instanceof Player player) {
            return player;
        }

        if (damager instanceof AreaEffectCloud effectCloud && effectCloud.getSource() instanceof Player player) {
            return player;
        }

        if (damager instanceof EvokerFangs evokerFangs) {
            LivingEntity owner = evokerFangs.getOwner();
            if (owner instanceof Player player) {
                return player;
            }
        }

        if (damager instanceof Tameable tameable) {
            if (tameable.getOwner() instanceof Player player) {
                return player;
            }
            Player tamedOwner = resolveOnlinePlayer(damager, tameable.getOwnerUniqueId());
            if (tamedOwner != null) {
                return tamedOwner;
            }
        }

        if (damager instanceof Firework firework) {
            Player fireworkOwner = resolveOnlinePlayer(damager, firework.getSpawningEntity());
            if (fireworkOwner != null) {
                return fireworkOwner;
            }
        }

        return null;
    }

    private static Player resolveProjectileOwner(Projectile projectile) {
        ProjectileSource shooter = projectile.getShooter();
        if (shooter instanceof Player player) {
            return player;
        }

        return resolveOnlinePlayer(projectile, projectile.getOwnerUniqueId());
    }

    private static Player resolveOnlinePlayer(Entity entity, UUID playerId) {
        if (playerId == null) {
            return null;
        }
        return entity.getServer().getPlayer(playerId);
    }
}
