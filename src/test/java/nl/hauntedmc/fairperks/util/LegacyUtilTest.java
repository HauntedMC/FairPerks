package nl.hauntedmc.fairperks.util;

import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LegacyUtilTest {

    @Test
    void enemyListContainsCommonHostileEntity() {
        assertTrue(LegacyUtil.ENEMY.contains(EntityType.CREEPER));
    }

    @Test
    void enemyListIsNotEmpty() {
        assertFalse(LegacyUtil.ENEMY.isEmpty());
    }
}
