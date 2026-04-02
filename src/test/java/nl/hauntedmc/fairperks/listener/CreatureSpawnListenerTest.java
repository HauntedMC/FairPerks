package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreatureSpawnListenerTest {

    @Test
    void onCreatureSpawnMarksSpawnerMobsWithMetadata() {
        FairPerks plugin = mock(FairPerks.class);
        LivingEntity entity = mock(LivingEntity.class);
        CreatureSpawnEvent event = mock(CreatureSpawnEvent.class);

        when(event.getSpawnReason()).thenReturn(SpawnReason.SPAWNER);
        when(event.getEntity()).thenReturn(entity);
        when(entity.getType()).thenReturn(EntityType.CREEPER);

        CreatureSpawnListener listener = new CreatureSpawnListener(plugin);
        listener.onCreatureSpawn(event);

        verify(entity).setMetadata(eq("spawnermob"), any());
    }
}
