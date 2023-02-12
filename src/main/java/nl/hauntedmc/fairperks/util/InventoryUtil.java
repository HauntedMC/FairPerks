package nl.hauntedmc.fairperks.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class InventoryUtil {

    public static boolean holdsIgniter(Player player) {
        return player.getInventory().getItemInMainHand().getType() == Material.FLINT_AND_STEEL ||
                player.getInventory().getItemInOffHand().getType() == Material.FLINT_AND_STEEL ||
                player.getInventory().getItemInMainHand().getType() == Material.FIRE_CHARGE ||
                player.getInventory().getItemInOffHand().getType() == Material.FIRE_CHARGE;
    }

}
