package nl.hauntedmc.fairperks.listener;

import nl.hauntedmc.fairperks.FairPerks;

import net.ess3.api.IUser;
import net.ess3.api.events.GodStatusChangeEvent;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GodRestoreFixListenerTest {

    @Test
    void onGodToggleReappliesPreToggleHealthAndHunger() {
        FairPerks plugin = mock(FairPerks.class);
        Server server = mock(Server.class);
        BukkitScheduler scheduler = mock(BukkitScheduler.class);
        Player player = mock(Player.class);
        IUser affectedUser = mock(IUser.class);
        GodStatusChangeEvent event = mock(GodStatusChangeEvent.class);

        when(plugin.getServer()).thenReturn(server);
        when(server.getScheduler()).thenReturn(scheduler);
        when(event.getValue()).thenReturn(true);
        when(event.getAffected()).thenReturn(affectedUser);
        when(affectedUser.getBase()).thenReturn(player);
        when(player.isOnline()).thenReturn(true);
        when(player.isDead()).thenReturn(false);
        when(player.getHealth()).thenReturn(5.0D, 20.0D);
        when(player.getMaxHealth()).thenReturn(20.0D);
        when(player.getFoodLevel()).thenReturn(8, 20);

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(1);
            task.run();
            return mock(BukkitTask.class);
        }).when(scheduler).runTask(eq(plugin), any(Runnable.class));

        GodRestoreFixListener listener = new GodRestoreFixListener(plugin);
        listener.onGodToggle(event);

        verify(player).setHealth(5.0D);
        verify(player).setFoodLevel(8);
    }

    @Test
    void onGodToggleDoesNotHealWhenHealthDroppedAfterEnable() {
        FairPerks plugin = mock(FairPerks.class);
        Server server = mock(Server.class);
        BukkitScheduler scheduler = mock(BukkitScheduler.class);
        Player player = mock(Player.class);
        IUser affectedUser = mock(IUser.class);
        GodStatusChangeEvent event = mock(GodStatusChangeEvent.class);

        when(plugin.getServer()).thenReturn(server);
        when(server.getScheduler()).thenReturn(scheduler);
        when(event.getValue()).thenReturn(true);
        when(event.getAffected()).thenReturn(affectedUser);
        when(affectedUser.getBase()).thenReturn(player);
        when(player.isOnline()).thenReturn(true);
        when(player.isDead()).thenReturn(false);
        when(player.getHealth()).thenReturn(5.0D, 3.0D);
        when(player.getMaxHealth()).thenReturn(20.0D);
        when(player.getFoodLevel()).thenReturn(8, 6);

        doAnswer(invocation -> {
            Runnable task = invocation.getArgument(1);
            task.run();
            return mock(BukkitTask.class);
        }).when(scheduler).runTask(eq(plugin), any(Runnable.class));

        GodRestoreFixListener listener = new GodRestoreFixListener(plugin);
        listener.onGodToggle(event);

        verify(player).setHealth(3.0D);
        verify(player).setFoodLevel(6);
    }

    @Test
    void onGodToggleIgnoresDisableChanges() {
        FairPerks plugin = mock(FairPerks.class);
        GodStatusChangeEvent event = mock(GodStatusChangeEvent.class);

        when(event.getValue()).thenReturn(false);

        GodRestoreFixListener listener = new GodRestoreFixListener(plugin);
        listener.onGodToggle(event);

        verify(event, never()).getAffected();
        verify(plugin, never()).getServer();
    }
}
