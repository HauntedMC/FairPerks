package nl.hauntedmc.fairperks.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class YamlSyncUtil {

    private YamlSyncUtil() {
    }

    public static void syncWithBundledDefaults(JavaPlugin plugin, String resourcePath, File targetFile) {
        ensureParentDirectory(plugin, targetFile);
        saveBundledResourceIfMissing(plugin, resourcePath, targetFile);

        FileConfiguration bundledDefaults = loadBundledConfiguration(plugin, resourcePath);
        if (bundledDefaults == null || !targetFile.exists()) {
            return;
        }

        FileConfiguration current = YamlConfiguration.loadConfiguration(targetFile);
        YamlConfiguration merged = mergeWithDefaults(current, bundledDefaults);
        saveYaml(plugin, targetFile, merged);
    }

    static YamlConfiguration mergeWithDefaults(FileConfiguration current, FileConfiguration defaults) {
        YamlConfiguration merged = new YamlConfiguration();
        for (String key : defaults.getKeys(true)) {
            if (defaults.isConfigurationSection(key)) {
                continue;
            }

            Object value = current.contains(key) ? current.get(key) : defaults.get(key);
            merged.set(key, value);
        }
        return merged;
    }

    private static void ensureParentDirectory(JavaPlugin plugin, File targetFile) {
        File parent = targetFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            plugin.getLogger().warning("Could not create directory: " + parent.getAbsolutePath());
        }
    }

    private static void saveBundledResourceIfMissing(JavaPlugin plugin, String resourcePath, File targetFile) {
        if (targetFile.exists() || plugin.getResource(resourcePath) == null) {
            return;
        }

        plugin.saveResource(resourcePath, false);
    }

    private static FileConfiguration loadBundledConfiguration(JavaPlugin plugin, String resourcePath) {
        InputStream inputStream = plugin.getResource(resourcePath);
        if (inputStream == null) {
            return null;
        }

        try (InputStream stream = inputStream;
             InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            return YamlConfiguration.loadConfiguration(reader);
        } catch (IOException exception) {
            plugin.getLogger().warning("Could not read bundled resource " + resourcePath + ": " + exception.getMessage());
            return null;
        }
    }

    private static void saveYaml(JavaPlugin plugin, File targetFile, YamlConfiguration yaml) {
        try {
            yaml.save(targetFile);
        } catch (IOException exception) {
            plugin.getLogger().warning("Could not save " + targetFile.getName() + ": " + exception.getMessage());
        }
    }
}
