package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpawnerUtilTest {

    @Test
    void isSpawnermobReturnsTrueWhenMarkerIsPresent() {
        Entity entity = mock(Entity.class);
        Map<NamespacedKey, Byte> entityData = new HashMap<>();
        PersistentDataContainer dataContainer = TestFixtures.mapBackedByteDataContainer(entityData);

        entityData.put(new NamespacedKey("fairperks", "spawnermob"), (byte) 1);
        when(entity.getPersistentDataContainer()).thenReturn(dataContainer);

        assertTrue(SpawnerUtil.isSpawnermob(entity));
    }

    @Test
    void setSpawnermobWritesMarkerToPersistentDataContainer() {
        Entity entity = mock(Entity.class);
        Map<NamespacedKey, Byte> entityData = new HashMap<>();
        PersistentDataContainer dataContainer = TestFixtures.mapBackedByteDataContainer(entityData);

        when(entity.getPersistentDataContainer()).thenReturn(dataContainer);

        SpawnerUtil.setSpawnermob(entity);

        assertEquals(Byte.valueOf((byte) 1), entityData.get(new NamespacedKey("fairperks", "spawnermob")));
    }

    @Test
    void isSpawnermobReturnsFalseWhenMarkerIsMissing() {
        Entity entity = mock(Entity.class);
        PersistentDataContainer dataContainer = TestFixtures.mapBackedByteDataContainer(new HashMap<>());

        when(entity.getPersistentDataContainer()).thenReturn(dataContainer);

        assertFalse(SpawnerUtil.isSpawnermob(entity));
    }
}
