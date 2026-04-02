package nl.hauntedmc.fairperks.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class InventoryUtilTest {

    @Test
    void holdsIgniterReturnsTrueWhenMainHandContainsFlintAndSteel() {
        Player player = mock(Player.class);
        PlayerInventory inventory = mock(PlayerInventory.class);
        ItemStack mainHand = mock(ItemStack.class);
        ItemStack offHand = mock(ItemStack.class);

        when(player.getInventory()).thenReturn(inventory);
        when(inventory.getItemInMainHand()).thenReturn(mainHand);
        when(inventory.getItemInOffHand()).thenReturn(offHand);
        when(mainHand.getType()).thenReturn(Material.FLINT_AND_STEEL);
        when(offHand.getType()).thenReturn(Material.AIR);

        assertTrue(InventoryUtil.holdsIgniter(player));
    }

    @Test
    void holdsIgniterReturnsFalseWhenNeitherHandContainsIgniter() {
        Player player = mock(Player.class);
        PlayerInventory inventory = mock(PlayerInventory.class);
        ItemStack mainHand = mock(ItemStack.class);
        ItemStack offHand = mock(ItemStack.class);

        when(player.getInventory()).thenReturn(inventory);
        when(inventory.getItemInMainHand()).thenReturn(mainHand);
        when(inventory.getItemInOffHand()).thenReturn(offHand);
        when(mainHand.getType()).thenReturn(Material.DIRT);
        when(offHand.getType()).thenReturn(Material.AIR);

        assertFalse(InventoryUtil.holdsIgniter(player));
    }
}
