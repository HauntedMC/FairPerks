package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.DamageSourceUtil;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.TNTPrimeEvent;

/**
 * Prevents TNT priming by players in restricted perk states.
 */
public class TntPrimeListener implements Listener {

    private final FairPerks plugin;

    public TntPrimeListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTntPrime(TNTPrimeEvent event) {
        if (event.getPrimingEntity() == null) {
            return;
        }

        Player player = DamageSourceUtil.resolvePlayerDamager(event.getPrimingEntity());
        if (player == null) {
            return;
        }

        PlayerRestrictionUtil.denyWhenGodModeOrFlying(
                this.plugin,
                player,
                event,
                "actionbar.deny.tnt-prime.god-mode",
                "actionbar.deny.tnt-prime.flying"
        );
    }
}
