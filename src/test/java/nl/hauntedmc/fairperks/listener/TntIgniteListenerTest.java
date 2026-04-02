package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TntIgniteListenerTest {

    @Test
    void onTntIgniteCancelsWhenEnemyIsNearbyAndPlayerIsFlying() {
        FairPerks plugin = mock(FairPerks.class);
        FileConfiguration config = mock(FileConfiguration.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity enemy = TestFixtures.mockEntityOfType(EntityType.CREEPER);
        Block clickedBlock = mock(Block.class);
        PlayerInteractEvent event = mock(PlayerInteractEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        TestFixtures.stubMainHandMaterial(player, Material.FLINT_AND_STEEL);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("tnt_entityrange")).thenReturn(10);
        when(player.getNearbyEntities(10, 10, 10)).thenReturn(List.of(enemy));
        when(clickedBlock.getType()).thenReturn(Material.TNT);
        when(event.getPlayer()).thenReturn(player);
        when(event.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
        when(event.getClickedBlock()).thenReturn(clickedBlock);

        TntIgniteListener listener = new TntIgniteListener(plugin);
        listener.onTNTIgnite(event);

        verify(event).setCancelled(true);
    }
}
