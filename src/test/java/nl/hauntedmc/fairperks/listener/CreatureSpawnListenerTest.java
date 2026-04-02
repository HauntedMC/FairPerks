package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreatureSpawnListenerTest {

    @Test
    void onCreatureSpawnMarksSpawnerMobsInPersistentData() {
        LivingEntity entity = mock(LivingEntity.class);
        CreatureSpawnEvent event = mock(CreatureSpawnEvent.class);
        Map<NamespacedKey, Byte> entityData = new HashMap<>();
        PersistentDataContainer dataContainer = TestFixtures.mapBackedByteDataContainer(entityData);

        when(event.getSpawnReason()).thenReturn(SpawnReason.SPAWNER);
        when(event.getEntity()).thenReturn(entity);
        when(entity.getType()).thenReturn(EntityType.CREEPER);
        when(entity.getPersistentDataContainer()).thenReturn(dataContainer);

        CreatureSpawnListener listener = new CreatureSpawnListener();
        listener.onCreatureSpawn(event);

        assertEquals(Byte.valueOf((byte) 1), entityData.get(new NamespacedKey("fairperks", "spawnermob")));
    }
}
