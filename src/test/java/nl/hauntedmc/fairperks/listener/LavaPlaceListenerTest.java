package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class LavaPlaceListenerTest {

    @Test
    void onLavaBucketInteractCancelsWhenEnemyIsNearbyAndPlayerIsFlying() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity enemy = TestFixtures.mockEntityOfType(EntityType.CREEPER);
        PlayerBucketEmptyEvent event = mock(PlayerBucketEmptyEvent.class);
        FileConfiguration config = mock(FileConfiguration.class);

        TestFixtures.stubGodMode(plugin, player, false);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("lava_entityrange")).thenReturn(5);
        when(event.getBucket()).thenReturn(Material.LAVA_BUCKET);
        when(event.getPlayer()).thenReturn(player);
        when(player.getNearbyEntities(5, 5, 5)).thenReturn(List.of(enemy));

        LavaPlaceListener listener = new LavaPlaceListener(plugin);
        listener.onLavaBucketInteract(event);

        verify(event).setCancelled(true);
    }
}
