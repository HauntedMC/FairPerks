package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.MetadataValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MeleeListenerTest {

    @Test
    void onMeleeDamageCancelsWhenEnemyIsNotSpawnerMobAndPlayerIsFlying() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity enemy = mock(Entity.class);
        MetadataValue metadataValue = mock(MetadataValue.class);
        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        when(metadataValue.asBoolean()).thenReturn(false);
        when(enemy.getType()).thenReturn(EntityType.CREEPER);
        when(enemy.getMetadata("spawnermob")).thenReturn(List.of(metadataValue));
        when(event.getEntity()).thenReturn(enemy);
        when(event.getDamager()).thenReturn(player);

        MeleeListener listener = new MeleeListener(plugin);
        listener.onMeleeDamage(event);

        verify(event).setCancelled(true);
    }
}
