package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AnchorInteractListenerTest {

    @Test
    void onAnchorInteractCancelsWhenPlayerIsInGodMode() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(false);
        TestFixtures.stubGodMode(plugin, player, true);

        Block clickedBlock = mock(Block.class);
        World world = mock(World.class);
        PlayerInteractEvent event = mock(PlayerInteractEvent.class);

        when(event.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
        when(event.getHand()).thenReturn(EquipmentSlot.HAND);
        when(event.getClickedBlock()).thenReturn(clickedBlock);
        when(event.getPlayer()).thenReturn(player);
        when(clickedBlock.getType()).thenReturn(Material.RESPAWN_ANCHOR);
        when(clickedBlock.getWorld()).thenReturn(world);
        when(world.getEnvironment()).thenReturn(World.Environment.NORMAL);

        AnchorInteractListener listener = new AnchorInteractListener(plugin);
        listener.onAnchorInteract(event);

        verify(event).setCancelled(true);
    }
}
