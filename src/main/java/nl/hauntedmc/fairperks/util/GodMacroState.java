package nl.hauntedmc.fairperks.util;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Persistent storage wrapper for per-player god-macro enable state.
 */
public final class GodMacroState {

    public static final NamespacedKey GOD_MACRO_KEY = new NamespacedKey("fairperks", "godmacro");
    private static final byte ENABLED = 1;
    private static final byte DISABLED = 0;

    private GodMacroState() {
    }

    /**
     * Reads the god-macro flag with backwards compatibility.
     * <p>
     * Current format is {@link PersistentDataType#BYTE}. Older plugin versions stored
     * the same key as {@link PersistentDataType#STRING}. When a legacy string is found,
     * this method transparently migrates it to byte format.
     * <p>
     * For players without any stored value (for example brand-new players), this returns
     * {@code false}.
     */
    public static boolean isEnabled(Player player) {
        PersistentDataContainer playerData = player.getPersistentDataContainer();
        Byte macroStatus = readByte(playerData);
        if (macroStatus != null) {
            return macroStatus == ENABLED;
        }

        String legacyStatus = readLegacyString(playerData);
        if (legacyStatus == null) {
            return false;
        }

        boolean enabled = Boolean.parseBoolean(legacyStatus);
        writeByte(playerData, enabled ? ENABLED : DISABLED);
        return enabled;
    }

    public static void setEnabled(Player player, boolean enabled) {
        writeByte(player.getPersistentDataContainer(), enabled ? ENABLED : DISABLED);
    }

    private static Byte readByte(PersistentDataContainer playerData) {
        try {
            return playerData.get(GOD_MACRO_KEY, PersistentDataType.BYTE);
        } catch (IllegalArgumentException ignored) {
            // Key exists but with another data type (legacy/corrupted data).
            return null;
        }
    }

    private static String readLegacyString(PersistentDataContainer playerData) {
        try {
            return playerData.get(GOD_MACRO_KEY, PersistentDataType.STRING);
        } catch (IllegalArgumentException ignored) {
            // Key exists but is not string (for example already migrated to byte).
            return null;
        }
    }

    private static void writeByte(PersistentDataContainer playerData, byte value) {
        // Remove first to ensure a clean overwrite even when previous type differed.
        playerData.remove(GOD_MACRO_KEY);
        playerData.set(GOD_MACRO_KEY, PersistentDataType.BYTE, value);
    }
}
