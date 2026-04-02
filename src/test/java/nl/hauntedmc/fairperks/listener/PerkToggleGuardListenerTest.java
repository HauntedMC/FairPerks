package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;
import nl.hauntedmc.fairperks.util.MessageService;

import net.ess3.api.IUser;
import net.ess3.api.events.FlyStatusChangeEvent;
import net.ess3.api.events.GodStatusChangeEvent;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PerkToggleGuardListenerTest {

    @Test
    void onGodToggleCancelsEnableWhileInCombat() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = mock(Player.class);
        IUser affectedUser = mock(IUser.class);
        FileConfiguration config = mock(FileConfiguration.class);
        GodStatusChangeEvent event = mock(GodStatusChangeEvent.class);

        TestFixtures.stubGodMode(plugin, player, false);
        TestFixtures.stubCombatState(plugin, player, true);
        MessageService messageService = TestFixtures.stubMessageService(plugin);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);
        when(event.getValue()).thenReturn(true);
        when(event.getAffected()).thenReturn(affectedUser);
        when(affectedUser.getBase()).thenReturn(player);

        PerkToggleGuardListener listener = new PerkToggleGuardListener(plugin);
        listener.onGodToggle(event);

        verify(event).setCancelled(true);
        verify(messageService).sendActionBar(player, "actionbar.deny.perk-toggle.combat");
    }

    @Test
    void onFlyToggleCancelsEnableWhenHostilesAreNearby() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = mock(Player.class);
        IUser affectedUser = mock(IUser.class);
        Entity hostile = TestFixtures.mockEntityOfType(EntityType.CREEPER);
        FileConfiguration config = mock(FileConfiguration.class);
        FlyStatusChangeEvent event = mock(FlyStatusChangeEvent.class);

        TestFixtures.stubCombatState(plugin, player, false);
        MessageService messageService = TestFixtures.stubMessageService(plugin);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);
        when(player.getNearbyEntities(16, 16, 16)).thenReturn(List.of(hostile));
        when(event.getValue()).thenReturn(true);
        when(event.getAffected()).thenReturn(affectedUser);
        when(affectedUser.getBase()).thenReturn(player);

        PerkToggleGuardListener listener = new PerkToggleGuardListener(plugin);
        listener.onFlyToggle(event);

        verify(event).setCancelled(true);
        verify(messageService).sendActionBar(player, "actionbar.deny.perk-toggle.hostile-nearby");
    }

    @Test
    void onGodToggleAllowsDisableInCombat() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = mock(Player.class);
        IUser affectedUser = mock(IUser.class);
        FileConfiguration config = mock(FileConfiguration.class);
        GodStatusChangeEvent event = mock(GodStatusChangeEvent.class);

        TestFixtures.stubGodMode(plugin, player, true);
        TestFixtures.stubCombatState(plugin, player, true);
        MessageService messageService = TestFixtures.stubMessageService(plugin);
        when(plugin.getConfig()).thenReturn(config);
        when(config.getInt("perktoggle_entityrange")).thenReturn(16);
        when(event.getValue()).thenReturn(false);
        when(event.getAffected()).thenReturn(affectedUser);
        when(affectedUser.getBase()).thenReturn(player);

        PerkToggleGuardListener listener = new PerkToggleGuardListener(plugin);
        listener.onGodToggle(event);

        verify(event, never()).setCancelled(true);
        verify(messageService, never()).sendActionBar(player, "actionbar.deny.perk-toggle.combat");
        verify(messageService, never()).sendActionBar(player, "actionbar.deny.perk-toggle.hostile-nearby");
    }
}
