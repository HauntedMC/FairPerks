package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;

import org.bukkit.entity.Player;

public class CombatUtil {

    public static boolean isInCombat(Player player, FairPerks plugin) {
        ICombatLogX combatlogHook = plugin.getCombatlogHook();
        ICombatManager combatManager = combatlogHook.getCombatManager();
        return combatManager.isInCombat(player);
    }

}
