package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import net.ess3.api.IUser;
import net.ess3.api.events.GodStatusChangeEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * Prevents heal/refill abuse from Essentials god toggles.
 * <p>
 * Essentials sets health/food to full when god is enabled. This listener keeps the
 * player's pre-toggle health and hunger, then reapplies those values one tick later.
 */
public class GodRestoreFixListener implements Listener {

    private static final int MAX_FOOD_LEVEL = 20;

    private final FairPerks plugin;

    public GodRestoreFixListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGodToggle(GodStatusChangeEvent event) {
        if (!event.getValue()) {
            return;
        }

        Player player = resolveAffectedPlayer(event.getAffected());
        if (player == null) {
            return;
        }

        double previousHealth = player.getHealth();
        int previousFoodLevel = player.getFoodLevel();

        plugin.getServer().getScheduler().runTask(
                plugin,
                () -> reapplyPreToggleStats(player, previousHealth, previousFoodLevel)
        );
    }

    private void reapplyPreToggleStats(Player player, double previousHealth, int previousFoodLevel) {
        if (!player.isOnline() || player.isDead()) {
            return;
        }

        double currentHealth = player.getHealth();
        double targetHealth = Math.min(previousHealth, currentHealth);
        if (targetHealth > 0.0D) {
            player.setHealth(Math.min(targetHealth, player.getMaxHealth()));
        }

        int currentFoodLevel = player.getFoodLevel();
        int targetFoodLevel = Math.min(previousFoodLevel, currentFoodLevel);
        player.setFoodLevel(clampFoodLevel(targetFoodLevel));
    }

    private int clampFoodLevel(int value) {
        if (value < 0) {
            return 0;
        }
        return Math.min(value, MAX_FOOD_LEVEL);
    }

    private Player resolveAffectedPlayer(IUser affectedUser) {
        if (affectedUser == null) {
            return null;
        }
        return affectedUser.getBase();
    }
}
