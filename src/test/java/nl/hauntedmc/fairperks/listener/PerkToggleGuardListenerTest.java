package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;
import nl.hauntedmc.fairperks.util.MessageService;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PerkToggleGuardListenerTest {

    @Test
    void onPerkToggleCancelsGodEnableWhileInCombat() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = mock(Player.class);
        FileConfiguration config = mock(FileConfiguration.class);
        PlayerCommandPreprocessEvent event = mock(PlayerCommandPreprocessEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        TestFixtures.stubCombatState(plugin, player, true);
        MessageService messageService = TestFixtures.stubMessageService(plugin);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);
        when(event.getPlayer()).thenReturn(player);
        when(event.getMessage()).thenReturn("/god");

        PerkToggleGuardListener listener = new PerkToggleGuardListener(plugin);
        listener.onPerkToggle(event);

        verify(event).setCancelled(true);
        verify(messageService).sendActionBar(player, "actionbar.deny.perk-toggle.combat");
    }

    @Test
    void onPerkToggleCancelsFlyEnableWhenHostilesAreNearby() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = mock(Player.class);
        Entity hostile = TestFixtures.mockEntityOfType(EntityType.CREEPER);
        FileConfiguration config = mock(FileConfiguration.class);
        PlayerCommandPreprocessEvent event = mock(PlayerCommandPreprocessEvent.class);

        TestFixtures.stubCombatState(plugin, player, false);
        MessageService messageService = TestFixtures.stubMessageService(plugin);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);
        when(player.getAllowFlight()).thenReturn(false);
        when(player.getNearbyEntities(16, 16, 16)).thenReturn(List.of(hostile));
        when(event.getPlayer()).thenReturn(player);
        when(event.getMessage()).thenReturn("/fly");

        PerkToggleGuardListener listener = new PerkToggleGuardListener(plugin);
        listener.onPerkToggle(event);

        verify(event).setCancelled(true);
        verify(messageService).sendActionBar(player, "actionbar.deny.perk-toggle.hostile-nearby");
    }

    @Test
    void onPerkToggleAllowsExplicitGodDisableInCombat() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = mock(Player.class);
        FileConfiguration config = mock(FileConfiguration.class);
        PlayerCommandPreprocessEvent event = mock(PlayerCommandPreprocessEvent.class);

        TestFixtures.stubGodMode(plugin, player, true);
        TestFixtures.stubCombatState(plugin, player, true);
        MessageService messageService = TestFixtures.stubMessageService(plugin);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);
        when(event.getPlayer()).thenReturn(player);
        when(event.getMessage()).thenReturn("/god off");

        PerkToggleGuardListener listener = new PerkToggleGuardListener(plugin);
        listener.onPerkToggle(event);

        verify(event, never()).setCancelled(true);
        verify(messageService, never()).sendActionBar(player, "actionbar.deny.perk-toggle.combat");
        verify(messageService, never()).sendActionBar(player, "actionbar.deny.perk-toggle.hostile-nearby");
    }
}
