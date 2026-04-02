package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PvpDamageListenerTest {

    @Test
    void onPvpDamageCancelsWhenDamagerIsFlying() {
        FairPerks plugin = mock(FairPerks.class);
        Player damager = TestFixtures.mockPlayerWithSpigot(true);
        Player damaged = mock(Player.class);
        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);

        TestFixtures.stubGodMode(plugin, damager, false);
        when(damager.getUniqueId()).thenReturn(UUID.randomUUID());
        when(damaged.getUniqueId()).thenReturn(UUID.randomUUID());
        when(event.getDamager()).thenReturn(damager);
        when(event.getEntity()).thenReturn(damaged);

        PvpDamageListener listener = new PvpDamageListener(plugin);
        listener.onPvpDamage(event);

        verify(event).setCancelled(true);
    }

    @Test
    void onPvpDamageCancelsWhenProjectileShooterIsInGodMode() {
        FairPerks plugin = mock(FairPerks.class);
        Player damager = TestFixtures.mockPlayerWithSpigot(false);
        Player damaged = mock(Player.class);
        Projectile projectile = mock(Projectile.class);
        EntityDamageByEntityEvent event = mock(EntityDamageByEntityEvent.class);

        TestFixtures.stubGodMode(plugin, damager, true);
        when(damager.getUniqueId()).thenReturn(UUID.randomUUID());
        when(damaged.getUniqueId()).thenReturn(UUID.randomUUID());
        when(projectile.getShooter()).thenReturn(damager);
        when(event.getDamager()).thenReturn(projectile);
        when(event.getEntity()).thenReturn(damaged);

        PvpDamageListener listener = new PvpDamageListener(plugin);
        listener.onPvpDamage(event);

        verify(event).setCancelled(true);
    }
}
