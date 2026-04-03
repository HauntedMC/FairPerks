package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;
import nl.hauntedmc.fairperks.util.MessageService;

import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GodMacroListenerTest {

    private static PersistentDataContainer godMacroEnabledContainer() {
        Map<NamespacedKey, Byte> dataMap = new HashMap<>();
        dataMap.put(new NamespacedKey("fairperks", "godmacro"), (byte) 1);
        return TestFixtures.mapBackedByteDataContainer(dataMap);
    }

    @Test
    void onPlayerShiftToggleGodRunsGodCommandOnSecondShiftWithinInterval() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.hasPermission("essentials.god")).thenReturn(true);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);
        when(player.getUniqueId()).thenReturn(playerId);
        when(player.getNearbyEntities(16, 16, 16)).thenReturn(java.util.List.of());

        TestFixtures.stubCombatState(plugin, player, false);
        PersistentDataContainer dataContainer = godMacroEnabledContainer();
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        FileConfiguration config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getBoolean("enabled.perktoggleguard", true)).thenReturn(true);
        when(config.getInt("godmacrointerval")).thenReturn(500);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);

        PlayerToggleSneakEvent event = mock(PlayerToggleSneakEvent.class);
        when(event.getPlayer()).thenReturn(player);
        when(event.isSneaking()).thenReturn(true);

        GodMacroListener listener = new GodMacroListener(plugin);
        listener.onPlayerShiftToggleGod(event);
        listener.onPlayerShiftToggleGod(event);

        verify(player, times(1)).performCommand("god");
    }

    @Test
    void onPlayerShiftToggleGodBlocksEnablingMacroWhenPlayerIsInCombat() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);
        when(player.hasPermission("essentials.god")).thenReturn(true);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);

        TestFixtures.stubCombatState(plugin, player, true);
        MessageService messageService = TestFixtures.stubMessageService(plugin);
        PersistentDataContainer dataContainer = godMacroEnabledContainer();
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        FileConfiguration config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getBoolean("enabled.perktoggleguard", true)).thenReturn(true);
        when(config.getInt("godmacrointerval")).thenReturn(500);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);

        PlayerToggleSneakEvent sneakEvent = mock(PlayerToggleSneakEvent.class);
        when(sneakEvent.getPlayer()).thenReturn(player);
        when(sneakEvent.isSneaking()).thenReturn(true);

        GodMacroListener listener = new GodMacroListener(plugin);
        listener.onPlayerShiftToggleGod(sneakEvent);
        listener.onPlayerShiftToggleGod(sneakEvent);

        verify(player, never()).performCommand("god");
        verify(messageService).sendActionBar(player, "actionbar.deny.perk-toggle.combat");
    }

    @Test
    void onPlayerShiftToggleGodAllowsDisablingMacroInCombat() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);
        when(player.hasPermission("essentials.god")).thenReturn(true);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);

        TestFixtures.stubGodMode(plugin, player, true);
        TestFixtures.stubCombatState(plugin, player, true);
        PersistentDataContainer dataContainer = godMacroEnabledContainer();
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        FileConfiguration config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getBoolean("enabled.perktoggleguard", true)).thenReturn(true);
        when(config.getInt("godmacrointerval")).thenReturn(500);

        PlayerToggleSneakEvent sneakEvent = mock(PlayerToggleSneakEvent.class);
        when(sneakEvent.getPlayer()).thenReturn(player);
        when(sneakEvent.isSneaking()).thenReturn(true);

        GodMacroListener listener = new GodMacroListener(plugin);
        listener.onPlayerShiftToggleGod(sneakEvent);
        listener.onPlayerShiftToggleGod(sneakEvent);

        verify(player, times(1)).performCommand("god");
    }

    @Test
    void onPlayerShiftToggleGodUsesDefaultIntervalWhenConfigIsNonPositive() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.hasPermission("essentials.god")).thenReturn(true);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);
        when(player.getUniqueId()).thenReturn(playerId);
        when(player.getNearbyEntities(16, 16, 16)).thenReturn(java.util.List.of());

        TestFixtures.stubCombatState(plugin, player, false);
        PersistentDataContainer dataContainer = godMacroEnabledContainer();
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        FileConfiguration config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getBoolean("enabled.perktoggleguard", true)).thenReturn(true);
        when(config.getInt("godmacrointerval")).thenReturn(0);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);

        PlayerToggleSneakEvent event = mock(PlayerToggleSneakEvent.class);
        when(event.getPlayer()).thenReturn(player);
        when(event.isSneaking()).thenReturn(true);

        GodMacroListener listener = new GodMacroListener(plugin);
        listener.onPlayerShiftToggleGod(event);
        listener.onPlayerShiftToggleGod(event);

        verify(player, times(1)).performCommand("god");
    }

    @Test
    void onPlayerShiftToggleGodSkipsGuardWhenPerkToggleGuardIsDisabled() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.hasPermission("essentials.god")).thenReturn(true);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);
        when(player.getUniqueId()).thenReturn(playerId);

        TestFixtures.stubCombatState(plugin, player, true);
        PersistentDataContainer dataContainer = godMacroEnabledContainer();
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        Entity hostile = mock(Entity.class);
        when(hostile.getType()).thenReturn(EntityType.ZOMBIE);
        when(player.getNearbyEntities(anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(java.util.List.of(hostile));

        FileConfiguration config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getBoolean("enabled.perktoggleguard", true)).thenReturn(false);
        when(config.getInt("godmacrointerval")).thenReturn(500);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);

        PlayerToggleSneakEvent event = mock(PlayerToggleSneakEvent.class);
        when(event.getPlayer()).thenReturn(player);
        when(event.isSneaking()).thenReturn(true);

        GodMacroListener listener = new GodMacroListener(plugin);
        listener.onPlayerShiftToggleGod(event);
        listener.onPlayerShiftToggleGod(event);

        verify(player, times(1)).performCommand("god");
        verify(player, never()).getNearbyEntities(anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    void onPlayerQuitClearsPendingMacroState() {
        FairPerks plugin = mock(FairPerks.class);

        Player player = mock(Player.class);
        UUID playerId = UUID.randomUUID();
        when(player.getUniqueId()).thenReturn(playerId);
        when(player.hasPermission("essentials.god")).thenReturn(true);
        when(player.hasPermission("fairperks.godmacro")).thenReturn(true);
        when(player.getNearbyEntities(16, 16, 16)).thenReturn(java.util.List.of());

        TestFixtures.stubCombatState(plugin, player, false);
        PersistentDataContainer dataContainer = godMacroEnabledContainer();
        when(player.getPersistentDataContainer()).thenReturn(dataContainer);

        FileConfiguration config = mock(FileConfiguration.class);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getBoolean("enabled.perktoggleguard", true)).thenReturn(true);
        when(config.getInt("godmacrointerval")).thenReturn(500);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);

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
