package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

/**
 * Soft dependency bridge for CombatLogX.
 * <p>
 * FairPerks does not compile against CombatLogX internals, so this utility resolves
 * the combat manager reflectively and degrades safely when the hook is absent or
 * incompatible.
 */
public final class CombatUtil {

    private static boolean loggedCombatHookFailure;

    private CombatUtil() {
    }

    /**
     * Returns whether the player is currently combat tagged by CombatLogX.
     * <p>
     * When CombatLogX is missing, disabled, or its API shape differs, this method
     * returns {@code false} so perk checks remain functional without hard failure.
     */
    public static boolean isInCombat(Player player, FairPerks plugin) {
        Plugin combatlogHook = plugin.getCombatlogHook();
        if (combatlogHook == null || !combatlogHook.isEnabled()) {
            return false;
        }

        try {
            Method getCombatManagerMethod = combatlogHook.getClass().getMethod("getCombatManager");
            Object combatManager = getCombatManagerMethod.invoke(combatlogHook);
            if (combatManager == null) {
                return false;
            }

            Method isInCombatMethod = combatManager.getClass().getMethod("isInCombat", Player.class);
            Object inCombat = isInCombatMethod.invoke(combatManager, player);
            return Boolean.TRUE.equals(inCombat);
        } catch (ReflectiveOperationException exception) {
            logCombatHookFailure(plugin, exception);
            return false;
        }
    }

    private static void logCombatHookFailure(FairPerks plugin, ReflectiveOperationException exception) {
        if (loggedCombatHookFailure) {
            return;
        }

        // Log only once to avoid flooding console from repeated event checks.
        loggedCombatHookFailure = true;
        plugin.getLogger().warning(
                "CombatLogX hook could not be used; FairPerks will continue without combat checks. Cause: "
                        + exception.getClass().getSimpleName()
        );
    }

}
