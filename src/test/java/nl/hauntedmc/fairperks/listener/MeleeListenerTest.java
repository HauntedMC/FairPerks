package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MeleeListenerTest {

    @Test
    void onMeleeDamageCancelsWhenEnemyIsNotSpawnerMobAndPlayerIsFlying() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity enemy = mock(Entity.class);
        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);
        Map<NamespacedKey, Byte> entityData = new HashMap<>();
        PersistentDataContainer dataContainer = TestFixtures.mapBackedByteDataContainer(entityData);

        TestFixtures.stubGodMode(plugin, player, false);
        when(enemy.getPersistentDataContainer()).thenReturn(dataContainer);
        when(enemy.getType()).thenReturn(EntityType.CREEPER);
        when(event.getEntity()).thenReturn(enemy);
        when(event.getDamager()).thenReturn(player);

        MeleeListener listener = new MeleeListener(plugin);
        listener.onMeleeDamage(event);

        verify(event).setCancelled(true);
    }
}
