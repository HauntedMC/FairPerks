package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SpawnerUtilTest {

    @Test
    void isSpawnermobReturnsTrueWhenAnyMetadataEntryIsTrue() {
        Entity entity = mock(Entity.class);
        MetadataValue falseValue = mock(MetadataValue.class);
        MetadataValue trueValue = mock(MetadataValue.class);

        when(falseValue.asBoolean()).thenReturn(false);
        when(trueValue.asBoolean()).thenReturn(true);
        when(entity.getMetadata("spawnermob")).thenReturn(List.of(falseValue, trueValue));

        assertTrue(SpawnerUtil.isSpawnermob(entity));
    }

    @Test
    void setSpawnermobWritesMetadataToEntity() {
        Entity entity = mock(Entity.class);
        MetadataValue metadataValue = mock(MetadataValue.class);

        SpawnerUtil.setSpawnermob(entity, metadataValue);

        verify(entity).setMetadata(eq("spawnermob"), eq(metadataValue));
    }

    @Test
    void createCustomMetadataValueReturnsPluginScopedTrueMetadata() {
        FairPerks plugin = mock(FairPerks.class);

        MetadataValue metadataValue = SpawnerUtil.createCustomMetadataValue(plugin);

        assertTrue(metadataValue.asBoolean());
        assertSame(plugin, metadataValue.getOwningPlugin());
    }
}
