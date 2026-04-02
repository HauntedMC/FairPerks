package nl.hauntedmc.fairperks.command;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.util.GodMacroState;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GodMacroCommand implements CommandExecutor {

    private final FairPerks plugin;

    public GodMacroCommand(FairPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return true;
        }

        if (!player.hasPermission("fairperks.godmacro")) {
            return true;
        }

        boolean enabled = !GodMacroState.isEnabled(player);
        GodMacroState.setEnabled(player, enabled);
        plugin.getMessageService().sendMessage(
                player,
                enabled ? "command.godmacro.enabled" : "command.godmacro.disabled"
        );

        return true;
    }
}
