package nl.hauntedmc.fairperks.util;

import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;

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

        if (damager instanceof Projectile projectile && projectile.getShooter() instanceof Player player) {
            return player;
        }

        if (damager instanceof TNTPrimed primedTnt && primedTnt.getSource() instanceof Player player) {
            return player;
        }

        if (damager instanceof AreaEffectCloud effectCloud && effectCloud.getSource() instanceof Player player) {
            return player;
        }

        return null;
    }
}
