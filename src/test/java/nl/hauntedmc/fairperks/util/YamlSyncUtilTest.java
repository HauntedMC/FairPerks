package nl.hauntedmc.fairperks.util;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class YamlSyncUtilTest {

    @Test
    void mergeWithDefaultsAddsMissingRemovesObsoleteAndPreservesExistingValues() {
        YamlConfiguration current = new YamlConfiguration();
        current.set("enabled.bed", false);
        current.set("enabled.obsolete", true);
        current.set("old", 1);
        current.set("blockignite", List.of("FLINT_AND_STEEL"));

        YamlConfiguration defaults = new YamlConfiguration();
        defaults.set("enabled.bed", true);
        defaults.set("enabled.lava", true);
        defaults.set("ignite_entityrange", 5);
        defaults.set("blockignite", List.of("FIREBALL"));

        YamlConfiguration merged = YamlSyncUtil.mergeWithDefaults(current, defaults);

        assertFalse(merged.getBoolean("enabled.bed"));
        assertTrue(merged.getBoolean("enabled.lava"));
        assertEquals(5, merged.getInt("ignite_entityrange"));
        assertEquals(List.of("FLINT_AND_STEEL"), merged.getStringList("blockignite"));
        assertFalse(merged.contains("enabled.obsolete"));
        assertFalse(merged.contains("old"));
    }
}
