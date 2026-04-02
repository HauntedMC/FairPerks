package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.PerkToggleGuardUtil;
import nl.hauntedmc.fairperks.util.PlayerRestrictionUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Intercepts direct toggle commands for perk controls (currently god/fly) and
 * blocks only enable actions when fairness guards fail.
 * <p>
 * Disable actions are always allowed so players can instantly drop the perk state.
 */
public class PerkToggleGuardListener implements Listener {

    private static final Set<String> GUARDED_COMMANDS = Set.of("god", "fly");
    private static final Set<String> ENABLE_ARGS = Set.of("on", "enable", "enabled", "true", "1", "yes", "y");
    private static final Set<String> DISABLE_ARGS = Set.of("off", "disable", "disabled", "false", "0", "no", "n");

    private final FairPerks plugin;

    public PerkToggleGuardListener(FairPerks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPerkToggle(PlayerCommandPreprocessEvent event) {
        ParsedCommand parsedCommand = parseCommand(event.getMessage());
        if (parsedCommand == null || !GUARDED_COMMANDS.contains(parsedCommand.command())) {
            return;
        }

        Player player = event.getPlayer();
        boolean currentlyEnabled = currentState(parsedCommand.command(), player);
        ToggleAction toggleAction = determineToggleAction(parsedCommand.arguments(), currentlyEnabled);
        if (toggleAction != ToggleAction.ENABLE) {
            return;
        }

        if (!PerkToggleGuardUtil.canEnablePerk(player, this.plugin)) {
            event.setCancelled(true);
        }
    }

    /**
     * Resolves whether a bare command (for example "/god") means enable or disable
     * by checking the player's current perk state.
     */
    private boolean currentState(String command, Player player) {
        if ("god".equals(command)) {
            return PlayerRestrictionUtil.isInGodMode(player, this.plugin);
        }

        return player.getAllowFlight();
    }

    /**
     * Interprets explicit toggle arguments from common Essentials variants.
     */
    private ToggleAction determineToggleAction(List<String> args, boolean currentlyEnabled) {
        if (args.isEmpty()) {
            return currentlyEnabled ? ToggleAction.DISABLE : ToggleAction.ENABLE;
        }

        String firstArg = args.getFirst();
        if (ENABLE_ARGS.contains(firstArg)) {
            return ToggleAction.ENABLE;
        }

        if (DISABLE_ARGS.contains(firstArg)) {
            return ToggleAction.DISABLE;
        }

        if ("toggle".equals(firstArg)) {
            return currentlyEnabled ? ToggleAction.DISABLE : ToggleAction.ENABLE;
        }

        return ToggleAction.UNKNOWN;
    }

    /**
     * Parses a raw chat command into normalized command + lowercase arguments.
     */
    private ParsedCommand parseCommand(String rawMessage) {
        if (rawMessage == null || rawMessage.length() <= 1 || rawMessage.charAt(0) != '/') {
            return null;
        }

        String trimmed = rawMessage.substring(1).trim();
        if (trimmed.isEmpty()) {
            return null;
        }

        String[] segments = trimmed.split("\\s+");
        if (segments.length == 0) {
            return null;
        }

        String command = normalizeCommand(segments[0]);
        List<String> arguments = Arrays.stream(segments)
                .skip(1)
                .map(argument -> argument.toLowerCase(Locale.ROOT))
                .toList();

        return new ParsedCommand(command, arguments);
    }

    /**
     * Removes optional namespace prefixes (for example "essentials:god" -> "god")
     * so aliases and namespaced forms are treated consistently.
     */
    private String normalizeCommand(String rawCommand) {
        String normalized = rawCommand.toLowerCase(Locale.ROOT);
        int namespaceSeparatorIndex = normalized.indexOf(':');
        if (namespaceSeparatorIndex == -1) {
            return normalized;
        }

        return normalized.substring(namespaceSeparatorIndex + 1);
    }

    private record ParsedCommand(String command, List<String> arguments) {
    }

    private enum ToggleAction {
        ENABLE,
        DISABLE,
        UNKNOWN
    }
}
