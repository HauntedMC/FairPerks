package nl.hauntedmc.fairperks.command;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class GodMacroCommand implements CommandExecutor {

    private final FairPerks plugin;
    private final String toggleMessage = ChatColor.YELLOW + "De god macro (dubbelshift) is nu %s.";

    public GodMacroCommand(FairPerks plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (player.hasPermission("fairperks.godmacro")) {
                NamespacedKey key = new NamespacedKey(this.plugin, "godmacro");
                PersistentDataContainer playerMeta = player.getPersistentDataContainer();

                if (playerMeta.has(key, PersistentDataType.STRING)) {
                    String macroStatus = playerMeta.get(key, PersistentDataType.STRING);

                    if (macroStatus != null && macroStatus.equals("true")) {
                        playerMeta.set(key, PersistentDataType.STRING, "false");
                        player.sendMessage(String.format(toggleMessage,  "uitgeschakeld"));
                    } else {
                        playerMeta.set(key, PersistentDataType.STRING, "true");
                        player.sendMessage(String.format(toggleMessage,  "ingeschakeld"));
                    }
                }
            }
        }
        return true;
    }
}
