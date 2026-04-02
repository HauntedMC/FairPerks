package nl.hauntedmc.fairperks.util;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public final class SpawnerUtil {

    private static final NamespacedKey SPAWNER_MOB_KEY = new NamespacedKey("fairperks", "spawnermob");
    private static final byte MARKER_TRUE = 1;

    private SpawnerUtil() {
    }

    public static boolean isSpawnermob(Entity damagedEntity) {
        Byte value = damagedEntity.getPersistentDataContainer().get(SPAWNER_MOB_KEY, PersistentDataType.BYTE);
        return value != null && value == MARKER_TRUE;
    }

    public static void setSpawnermob(Entity spawnerMob) {
        spawnerMob.getPersistentDataContainer().set(SPAWNER_MOB_KEY, PersistentDataType.BYTE, MARKER_TRUE);
    }

}
