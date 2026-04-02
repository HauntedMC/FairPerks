package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.util.LegacyUtil;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import static nl.hauntedmc.fairperks.util.SpawnerUtil.setSpawnermob;

public class CreatureSpawnListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.SPAWNER) {
            return;
        }

        Entity spawnerMob = event.getEntity();
        if (LegacyUtil.isEnemy(spawnerMob.getType())) {
            setSpawnermob(spawnerMob);
        }
    }
}
