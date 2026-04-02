package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;
import nl.hauntedmc.fairperks.testutil.TestFixtures;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CombatUtilTest {

    @Test
    void isInCombatReturnsFalseWhenCombatLogIsNotInstalled() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = mock(Player.class);

        when(plugin.getCombatlogHook()).thenReturn(null);

        assertFalse(CombatUtil.isInCombat(player, plugin));
    }

    @Test
    void isInCombatReturnsFalseWhenCombatLogPluginIsDisabled() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = mock(Player.class);
        Plugin combatHook = mock(Plugin.class);

        when(plugin.getCombatlogHook()).thenReturn(combatHook);
        when(combatHook.isEnabled()).thenReturn(false);

        assertFalse(CombatUtil.isInCombat(player, plugin));
    }

    @Test
    void isInCombatReturnsCombatManagerStateWhenHookExists() {
        FairPerks plugin = mock(FairPerks.class);
        Player player = mock(Player.class);

        TestFixtures.stubCombatState(plugin, player, true);

        assertTrue(CombatUtil.isInCombat(player, plugin));
    }
}
