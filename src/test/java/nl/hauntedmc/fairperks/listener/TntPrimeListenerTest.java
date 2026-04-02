package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.block.TNTPrimeEvent;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TntPrimeListenerTest {

    @Test
    void onTntPrimeCancelsWhenPrimingEntityIsFlyingPlayer() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        TNTPrimeEvent event = mock(TNTPrimeEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        when(event.getPrimingEntity()).thenReturn(player);

        TntPrimeListener listener = new TntPrimeListener(plugin);
        listener.onTntPrime(event);

        verify(event).setCancelled(true);
    }

    @Test
    void onTntPrimeCancelsWhenPrimedTntSourceIsPlayerInGodMode() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(false);
        TNTPrimed primedTnt = mock(TNTPrimed.class);
        TNTPrimeEvent event = mock(TNTPrimeEvent.class);

        TestFixtures.stubGodMode(plugin, player, true);
        when(primedTnt.getSource()).thenReturn(player);
        when(event.getPrimingEntity()).thenReturn(primedTnt);

        TntPrimeListener listener = new TntPrimeListener(plugin);
        listener.onTntPrime(event);

        verify(event).setCancelled(true);
    }
}
