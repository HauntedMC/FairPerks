package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.entity.Enemy;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.MetadataValue;

import static nl.hauntedmc.fairperks.util.SpawnerUtil.createCustomMetadataValue;
import static nl.hauntedmc.fairperks.util.SpawnerUtil.setSpawnermob;

public class CreatureSpawnListener implements Listener {

    final private FairPerks plugin;

    public CreatureSpawnListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent event){
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
            Entity spawnerMob = event.getEntity();
            if (spawnerMob instanceof Enemy) {
                MetadataValue metadataValue = createCustomMetadataValue(this.plugin);
                setSpawnermob(spawnerMob, metadataValue);
            }
        }
    }
}
