package nl.hauntedmc.fairperks.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.PlayerInventory;

import java.util.EnumSet;
import java.util.Set;

public final class InventoryUtil {

    private static final Set<Material> IGNITER_ITEMS = EnumSet.of(
            Material.FLINT_AND_STEEL,
            Material.FIRE_CHARGE
    );

    private InventoryUtil() {
    }

    public static boolean holdsIgniter(Player player) {
        PlayerInventory inventory = player.getInventory();
        return IGNITER_ITEMS.contains(inventory.getItemInMainHand().getType())
                || IGNITER_ITEMS.contains(inventory.getItemInOffHand().getType());
    }

    public static boolean holdsIgniter(Player player, EquipmentSlot hand) {
        PlayerInventory inventory = player.getInventory();

        if (hand == EquipmentSlot.HAND) {
            return IGNITER_ITEMS.contains(inventory.getItemInMainHand().getType());
        }

        if (hand == EquipmentSlot.OFF_HAND) {
            return IGNITER_ITEMS.contains(inventory.getItemInOffHand().getType());
        }

        return false;
    }

}
