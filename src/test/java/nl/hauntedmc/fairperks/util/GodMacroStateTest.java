package nl.hauntedmc.fairperks.util;

import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GodMacroStateTest {

    @Test
    void isEnabledMigratesLegacyStringValueToByte() {
        Player player = mock(Player.class);
        PersistentDataContainer dataContainer = mock(PersistentDataContainer.class);
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);
        when(dataContainer.get(eq(GodMacroState.GOD_MACRO_KEY), eq(PersistentDataType.BYTE)))
                .thenThrow(new IllegalArgumentException("wrong tag type"));
        when(dataContainer.get(eq(GodMacroState.GOD_MACRO_KEY), eq(PersistentDataType.STRING)))
                .thenReturn("true");

        assertTrue(GodMacroState.isEnabled(player));

        verify(dataContainer).remove(GodMacroState.GOD_MACRO_KEY);
        verify(dataContainer).set(GodMacroState.GOD_MACRO_KEY, PersistentDataType.BYTE, (byte) 1);
    }

    @Test
    void isEnabledUsesByteValueWhenPresent() {
        Player player = mock(Player.class);
        PersistentDataContainer dataContainer = mock(PersistentDataContainer.class);
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);
        when(dataContainer.get(eq(GodMacroState.GOD_MACRO_KEY), eq(PersistentDataType.BYTE))).thenReturn((byte) 1);

        assertTrue(GodMacroState.isEnabled(player));

        verify(dataContainer, never()).get(eq(GodMacroState.GOD_MACRO_KEY), eq(PersistentDataType.STRING));
    }

    @Test
    void setEnabledWritesByteValue() {
        Player player = mock(Player.class);
        PersistentDataContainer dataContainer = mock(PersistentDataContainer.class);
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        GodMacroState.setEnabled(player, false);

        verify(dataContainer).remove(GodMacroState.GOD_MACRO_KEY);
        verify(dataContainer).set(GodMacroState.GOD_MACRO_KEY, PersistentDataType.BYTE, (byte) 0);
    }
}
