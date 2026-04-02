package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GodMacroListenerTest {

    @Test
    void onPlayerShiftToggleGodRunsGodCommandOnSecondShiftWithinInterval() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.hasPermission("essentials.god")).thenReturn(true);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);
        when(player.getUniqueId()).thenReturn(playerId);

        TestFixtures.stubCombatState(plugin, player, false);

        FileConfiguration config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("godmacrointerval")).thenReturn(500);

        Map<NamespacedKey, Byte> dataMap = new HashMap<>();
        dataMap.put(new NamespacedKey("fairperks", "godmacro"), (byte) 1);
        PersistentDataContainer dataContainer = TestFixtures.mapBackedByteDataContainer(dataMap);
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        PlayerToggleSneakEvent event = mock(PlayerToggleSneakEvent.class);
        when(event.getPlayer()).thenReturn(player);
        when(event.isSneaking()).thenReturn(true);

        GodMacroListener listener = new GodMacroListener(plugin);
        listener.onPlayerShiftToggleGod(event);
        listener.onPlayerShiftToggleGod(event);

        verify(player, times(1)).performCommand("god");
    }

    @Test
    void onPlayerShiftToggleGodSkipsMacroWhenPlayerIsInCombat() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.hasPermission("essentials.god")).thenReturn(true);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);

        TestFixtures.stubCombatState(plugin, player, true);

        PlayerToggleSneakEvent event = mock(PlayerToggleSneakEvent.class);
        when(event.getPlayer()).thenReturn(player);

        GodMacroListener listener = new GodMacroListener(plugin);
        listener.onPlayerShiftToggleGod(event);

        verify(player, never()).performCommand("god");
    }

    @Test
    void onPlayerQuitClearsPendingMacroState() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);
        when(player.hasPermission("essentials.god")).thenReturn(true);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);

        TestFixtures.stubCombatState(plugin, player, false);

        FileConfiguration config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("godmacrointerval")).thenReturn(500);

        Map<NamespacedKey, Byte> dataMap = new HashMap<>();
        dataMap.put(new NamespacedKey("fairperks", "godmacro"), (byte) 1);
        PersistentDataContainer dataContainer = TestFixtures.mapBackedByteDataContainer(dataMap);
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        PlayerToggleSneakEvent sneakEvent = mock(PlayerToggleSneakEvent.class);
        when(sneakEvent.getPlayer()).thenReturn(player);
        when(sneakEvent.isSneaking()).thenReturn(true);

        PlayerQuitEvent quitEvent = mock(PlayerQuitEvent.class);
        when(quitEvent.getPlayer()).thenReturn(player);

        GodMacroListener listener = new GodMacroListener(plugin);
        listener.onPlayerShiftToggleGod(sneakEvent);
        listener.onPlayerQuit(quitEvent);
        listener.onPlayerShiftToggleGod(sneakEvent);

        verify(player, never()).performCommand("god");
    }
}
