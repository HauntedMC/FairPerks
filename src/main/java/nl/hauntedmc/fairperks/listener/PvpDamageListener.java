package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.DamageSourceUtil;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Blocks player-vs-player damage attempts from attackers that currently use
 * restricted perk states (god mode / active flight).
 */
public class PvpDamageListener implements Listener {

    private final FairPerks plugin;

    public PvpDamageListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPvpDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer)) {
            return;
        }

        Player damager = DamageSourceUtil.resolvePlayerDamager(event.getDamager());
        if (damager == null || damager.getUniqueId().equals(damagedPlayer.getUniqueId())) {
            return;
        }

        PlayerRestrictionUtil.denyWhenGodModeOrFlying(
                this.plugin,
                damager,
                event,
                "actionbar.deny.pvp.god-mode",
                "actionbar.deny.pvp.flying"
        );
    }
}
