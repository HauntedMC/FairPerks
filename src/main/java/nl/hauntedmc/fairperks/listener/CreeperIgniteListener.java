package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static nl.hauntedmc.fairperks.util.InventoryUtil.holdsIgniter;

public class CreeperIgniteListener implements Listener {

    private final FairPerks plugin;

    public CreeperIgniteListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreeperIgnite(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() != EntityType.CREEPER) {
            return;
        }

        if (event.getHand() == null || !holdsIgniter(event.getPlayer(), event.getHand())) {
            return;
        }

        PlayerRestrictionUtil.denyWhenGodModeOrFlying(
                this.plugin,
                event.getPlayer(),
                event,
                "actionbar.deny.creeper-ignite.god-mode",
                "actionbar.deny.creeper-ignite.flying"
        );
    }
}
