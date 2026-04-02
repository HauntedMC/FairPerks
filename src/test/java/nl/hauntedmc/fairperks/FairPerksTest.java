package nl.hauntedmc.fairperks;

import com.earth2me.essentials.Essentials;
import nl.hauntedmc.fairperks.util.MessageService;
import org.junit.jupiter.api.Test;
import org.bukkit.plugin.Plugin;

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
        Plugin combatLogX = mock(Plugin.class);

        setPrivateField(plugin, "combatlogHook", combatLogX);

        assertSame(combatLogX, plugin.getCombatlogHook());
    }

    @Test
    void getMessageServiceReturnsAssignedService() throws Exception {
        FairPerks plugin = mock(FairPerks.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        MessageService messageService = mock(MessageService.class);

        setPrivateField(plugin, "messageService", messageService);

        assertSame(messageService, plugin.getMessageService());
    }

    private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = FairPerks.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
