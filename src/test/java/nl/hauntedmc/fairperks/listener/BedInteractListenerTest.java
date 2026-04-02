package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BedInteractListenerTest {

    @Test
    void onBedInteractCancelsWhenPlayerIsFlyingOutsideOverworld() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        TestFixtures.stubGodMode(plugin, player, false);

        Block clickedBlock = mock(Block.class);
        Bed bedData = mock(Bed.class);
        World world = mock(World.class);
        PlayerInteractEvent event = mock(PlayerInteractEvent.class);

        when(event.getAction()).thenReturn(Action.RIGHT_CLICK_BLOCK);
        when(event.getHand()).thenReturn(EquipmentSlot.HAND);
        when(event.getClickedBlock()).thenReturn(clickedBlock);
        when(event.getPlayer()).thenReturn(player);
        when(clickedBlock.getBlockData()).thenReturn(bedData);
        when(clickedBlock.getWorld()).thenReturn(world);
        when(world.getEnvironment()).thenReturn(World.Environment.NETHER);

        BedInteractListener listener = new BedInteractListener(plugin);
        listener.onBedInteract(event);

        verify(event).setCancelled(true);
    }
}
