package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.MetadataValue;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProjectileListenerTest {

    @Test
    void onProjectileHitCancelsWhenEnemyIsNotSpawnerMobAndShooterIsFlying() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity enemy = mock(Entity.class);
        MetadataValue metadataValue = mock(MetadataValue.class);
        Projectile projectile = mock(Projectile.class);
        ProjectileHitEvent event = mock(ProjectileHitEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        when(metadataValue.asBoolean()).thenReturn(false);
        when(enemy.getType()).thenReturn(EntityType.CREEPER);
        when(enemy.getMetadata("spawnermob")).thenReturn(List.of(metadataValue));
        when(projectile.getShooter()).thenReturn(player);
        when(event.getHitEntity()).thenReturn(enemy);
        when(event.getEntity()).thenReturn(projectile);

        ProjectileListener listener = new ProjectileListener(plugin);
        listener.onProjectileHit(event);

        verify(event).setCancelled(true);
    }
}
