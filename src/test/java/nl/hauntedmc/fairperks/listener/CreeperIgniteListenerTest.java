package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreeperIgniteListenerTest {

    @Test
    void onCreeperIgniteCancelsWhenPlayerIsFlyingWithIgniter() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = TestFixtures.mockPlayerWithSpigot(true);
        Entity creeper = mock(Entity.class);
        PlayerInteractEntityEvent event = mock(PlayerInteractEntityEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        TestFixtures.stubMainHandMaterial(player, Material.FLINT_AND_STEEL);
        when(event.getPlayer()).thenReturn(player);
        when(event.getHand()).thenReturn(EquipmentSlot.HAND);
        when(event.getRightClicked()).thenReturn(creeper);
        when(creeper.getType()).thenReturn(EntityType.CREEPER);

        CreeperIgniteListener listener = new CreeperIgniteListener(plugin);
        listener.onCreeperIgnite(event);

        verify(event).setCancelled(true);
    }
}
