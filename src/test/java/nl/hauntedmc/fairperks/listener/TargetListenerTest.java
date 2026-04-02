package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TargetListenerTest {

    @Test
    void onEntityTargetClearsTargetWhenPlayerIsFlying() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity enemy = mock(Entity.class);
        EntityTargetLivingEntityEvent event = mock(EntityTargetLivingEntityEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        when(enemy.getType()).thenReturn(EntityType.CREEPER);
        when(event.getTarget()).thenReturn(player);
        when(event.getEntity()).thenReturn(enemy);

        TargetListener listener = new TargetListener(plugin);
        listener.onEntityTarget(event);

        verify(event).setTarget(null);
        verify(event).setCancelled(true);
    }
}
