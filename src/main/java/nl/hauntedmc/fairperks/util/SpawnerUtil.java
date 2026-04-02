package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class SpawnerUtil {

    public static boolean isSpawnermob(Entity damagedEntity) {
        return damagedEntity.getMetadata("spawnermob").stream().anyMatch(MetadataValue::asBoolean);
    }

    public static void setSpawnermob(Entity spawnerMob, MetadataValue metadataValue) {
        spawnerMob.setMetadata("spawnermob", metadataValue);
    }

    public static MetadataValue createCustomMetadataValue(FairPerks plugin) {
        return new FixedMetadataValue(plugin, true);
    }

}
