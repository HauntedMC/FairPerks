package nl.hauntedmc.fairperks.util;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public final class GodMacroState {

    public static final NamespacedKey GOD_MACRO_KEY = new NamespacedKey("fairperks", "godmacro");
    private static final byte ENABLED = 1;
    private static final byte DISABLED = 0;

    private GodMacroState() {
    }

    public static boolean isEnabled(Player player) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        Byte macroStatus = playerData.get(GOD_MACRO_KEY, PersistentDataType.BYTE);
        return macroStatus != null && macroStatus == ENABLED;
    }

    public static void setEnabled(Player player, boolean enabled) {
        player.getPersistentDataContainer().set(
                GOD_MACRO_KEY,
                PersistentDataType.BYTE,
                enabled ? ENABLED : DISABLED
        );
    }
}
