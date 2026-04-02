package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;

import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CombatUtilTest {

    @Test
    void isInCombatReturnsCombatManagerState() {
        FairPerks plugin = mock(FairPerks.class);
        ICombatLogX combatLogX = mock(ICombatLogX.class);
        ICombatManager combatManager = mock(ICombatManager.class);
        Player player = mock(Player.class);

        when(plugin.getCombatlogHook()).thenReturn(combatLogX);
        when(combatLogX.getCombatManager()).thenReturn(combatManager);
        when(combatManager.isInCombat(player)).thenReturn(true);

        assertTrue(CombatUtil.isInCombat(player, plugin));
    }
}
