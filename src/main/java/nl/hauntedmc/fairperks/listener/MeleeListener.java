package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.LegacyUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static nl.hauntedmc.fairperks.util.SpawnerUtil.isSpawnermob;

public class MeleeListener implements Listener {

    private final FairPerks plugin;

    public MeleeListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMeleeDamage(EntityDamageByEntityEvent event) {
        Entity damagedEntity = event.getEntity();

        if (LegacyUtil.ENEMY.contains(damagedEntity.getType())) {
            if (!isSpawnermob(damagedEntity)) {

                Player player;

                if (event.getDamager() instanceof Player) {
                    player = (Player) event.getDamager();
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
