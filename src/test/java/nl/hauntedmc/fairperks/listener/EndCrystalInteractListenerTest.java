package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EndCrystalInteractListenerTest {

    @Test
    void crystalDamageCancelsWhenPlayerIsFlying() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity endCrystal = mock(Entity.class);
        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        when(event.getEntity()).thenReturn(endCrystal);
        when(event.getDamager()).thenReturn(player);
        when(endCrystal.getType()).thenReturn(EntityType.END_CRYSTAL);

        EndCrystalInteractListener listener = new EndCrystalInteractListener(plugin);
        listener.crystalDamage(event);

        verify(event).setCancelled(true);
    }
}
