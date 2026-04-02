package nl.hauntedmc.fairperks.command;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class GodMacroCommand implements CommandExecutor {

    private static final NamespacedKey GOD_MACRO_KEY = new NamespacedKey("fairperks", "godmacro");
    private final FairPerks plugin;

    public GodMacroCommand(FairPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (player.hasPermission("fairperks.godmacro")) {
                PersistentDataContainer playerMeta = player.getPersistentDataContainer();

                if (playerMeta.has(GOD_MACRO_KEY, PersistentDataType.STRING)) {
                    String macroStatus = playerMeta.get(GOD_MACRO_KEY, PersistentDataType.STRING);

                    if (macroStatus != null && macroStatus.equals("true")) {
                        playerMeta.set(GOD_MACRO_KEY, PersistentDataType.STRING, "false");
                        plugin.getMessageService().sendMessage(player, "command.godmacro.disabled");
                    } else {
                        playerMeta.set(GOD_MACRO_KEY, PersistentDataType.STRING, "true");
                        plugin.getMessageService().sendMessage(player, "command.godmacro.enabled");
                    }
                } else {
                    playerMeta.set(GOD_MACRO_KEY, PersistentDataType.STRING, "true");
                    plugin.getMessageService().sendMessage(player, "command.godmacro.enabled");
                }
            }
        }
        return true;
    }
}
