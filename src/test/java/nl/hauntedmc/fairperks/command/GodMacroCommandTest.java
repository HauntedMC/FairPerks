package nl.hauntedmc.fairperks.command;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GodMacroCommandTest {

    @Test
    void onCommandEnablesMacroWhenNoStoredValueExists() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);

        Map<NamespacedKey, String> dataMap = new HashMap<>();
        PersistentDataContainer dataContainer = TestFixtures.mapBackedStringDataContainer(dataMap);
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        GodMacroCommand godMacroCommand = new GodMacroCommand(plugin);
        boolean result = godMacroCommand.onCommand(
                player,
                mock(Command.class),
                "godmacro",
                new String[0]
        );

        assertTrue(result);
        assertEquals("true", dataMap.values().iterator().next());
        verify(player).sendMessage(org.mockito.ArgumentMatchers.contains("ingeschakeld"));
    }

    @Test
    void onCommandDisablesMacroWhenStoredValueIsTrue() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);

        Map<NamespacedKey, String> dataMap = new HashMap<>();
        NamespacedKey key = new NamespacedKey("fairperks", "godmacro");
        dataMap.put(key, "true");

        PersistentDataContainer dataContainer = TestFixtures.mapBackedStringDataContainer(dataMap);
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        GodMacroCommand godMacroCommand = new GodMacroCommand(plugin);
        boolean result = godMacroCommand.onCommand(
                player,
                mock(Command.class),
                "godmacro",
                new String[0]
        );

        assertTrue(result);
        assertEquals("false", dataMap.get(key));
        verify(player).sendMessage(org.mockito.ArgumentMatchers.contains("uitgeschakeld"));
    }

    @Test
    void onCommandReturnsTrueForNonPlayerSender() {
        FairPerks plugin = mock(FairPerks.class);
        GodMacroCommand godMacroCommand = new GodMacroCommand(plugin);

        CommandSender sender = mock(CommandSender.class);
        boolean result = godMacroCommand.onCommand(
                sender,
                mock(Command.class),
                "godmacro",
                new String[0]
        );

        assertTrue(result);
    }
}
