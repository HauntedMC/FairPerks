package nl.hauntedmc.fairperks;

import com.earth2me.essentials.Essentials;
import com.github.sirblobman.combatlogx.api.ICombatLogX;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

class FairPerksTest {

    @Test
    void getEssentialsHookReturnsAssignedHook() throws Exception {
        FairPerks plugin = mock(FairPerks.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        Essentials essentials = mock(Essentials.class);

        setPrivateField(plugin, "essentialsHook", essentials);

        assertSame(essentials, plugin.getEssentialsHook());
    }

    @Test
    void getCombatlogHookReturnsAssignedHook() throws Exception {
        FairPerks plugin = mock(FairPerks.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        ICombatLogX combatLogX = mock(ICombatLogX.class);

        setPrivateField(plugin, "combatlogHook", combatLogX);

        assertSame(combatLogX, plugin.getCombatlogHook());
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = FairPerks.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
