package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BlockIgniteListenerTest {

    @Test
    void onBlockIgniteNearMobsCancelsWhenPlayerIsFlying() {
        FairPerks plugin = mock(FairPerks.class);
        FileConfiguration config = mock(FileConfiguration.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity nearbyEnemy = TestFixtures.mockEntityOfType(EntityType.CREEPER);
        BlockIgniteEvent event = mock(BlockIgniteEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        when(plugin.getConfig()).thenReturn(config);
        doReturn(java.util.List.of("FIREBALL")).when(config).getList("blockignite");
        when(config.getInt("ignite_entityrange")).thenReturn(5);
        when(event.getCause()).thenReturn(IgniteCause.FIREBALL);
        when(event.getPlayer()).thenReturn(player);
        when(player.getNearbyEntities(5, 5, 5)).thenReturn(java.util.List.of(nearbyEnemy));

        BlockIgniteListener listener = new BlockIgniteListener(plugin);
        listener.onBlockIgniteNearMobs(event);

        verify(event).setCancelled(true);
    }

    @Test
    void onBlockIgniteNearMobsUsesDefaultRangeWhenConfigIsNonPositive() {
        FairPerks plugin = mock(FairPerks.class);
        FileConfiguration config = mock(FileConfiguration.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity nearbyEnemy = TestFixtures.mockEntityOfType(EntityType.CREEPER);
        BlockIgniteEvent event = mock(BlockIgniteEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        when(plugin.getConfig()).thenReturn(config);
        doReturn(java.util.List.of("FIREBALL")).when(config).getList("blockignite");
        when(config.getInt("ignite_entityrange")).thenReturn(0);
        when(event.getCause()).thenReturn(IgniteCause.FIREBALL);
        when(event.getPlayer()).thenReturn(player);
        when(player.getNearbyEntities(5, 5, 5)).thenReturn(java.util.List.of(nearbyEnemy));

        BlockIgniteListener listener = new BlockIgniteListener(plugin);
        listener.onBlockIgniteNearMobs(event);

        verify(event).setCancelled(true);
    }
}
