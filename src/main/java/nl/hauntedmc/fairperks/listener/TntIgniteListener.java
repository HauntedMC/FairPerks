package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static nl.hauntedmc.fairperks.util.InventoryUtil.holdsIgniter;

public class TntIgniteListener implements Listener {

    private final FairPerks plugin;

    public TntIgniteListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTNTIgnite(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.TNT) {
            return;
        }

        if (event.getHand() == null || !holdsIgniter(event.getPlayer(), event.getHand())) {
            return;
        }

        int entityRange = this.plugin.getConfig().getInt("tnt_entityrange");
        List<Entity> nearbyEntities = event.getPlayer().getNearbyEntities(entityRange, entityRange, entityRange);
        if (nearbyEntities.stream().noneMatch(entity -> LegacyUtil.isEnemy(entity.getType()))) {
            return;
        }

        PlayerRestrictionUtil.denyWhenGodModeOrFlying(
                this.plugin,
                event.getPlayer(),
                event,
                "actionbar.deny.tnt-ignite.god-mode",
                "actionbar.deny.tnt-ignite.flying"
        );
    }
}
