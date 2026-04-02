package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.PerkToggleGuardUtil;

import net.ess3.api.IUser;
import net.ess3.api.events.FlyStatusChangeEvent;
import net.ess3.api.events.GodStatusChangeEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Intercepts Essentials status changes for perk controls (god/fly) and blocks
 * only enable actions when fairness guards fail.
 * <p>
 * Disable actions are always allowed so players can instantly drop the perk state.
 */
public class PerkToggleGuardListener implements Listener {

    private final FairPerks plugin;

    public PerkToggleGuardListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onGodToggle(GodStatusChangeEvent event) {
        if (!event.getValue()) {
            return;
        }

        Player player = resolveAffectedPlayer(event.getAffected());
        if (player == null) {
            return;
        }

        if (!PerkToggleGuardUtil.canEnablePerk(player, this.plugin)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFlyToggle(FlyStatusChangeEvent event) {
        if (!event.getValue()) {
            return;
        }

        Player player = resolveAffectedPlayer(event.getAffected());
        if (player == null) {
            return;
        }

        if (!PerkToggleGuardUtil.canEnablePerk(player, this.plugin)) {
            event.setCancelled(true);
        }
    }

    private Player resolveAffectedPlayer(IUser affectedUser) {
        if (affectedUser == null) {
            return null;
        }
        return affectedUser.getBase();
    }
}
