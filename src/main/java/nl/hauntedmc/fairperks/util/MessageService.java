package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Loads localized message templates and resolves runtime components.
 * <p>
 * Resolution order is active language file first, then bundled defaults.
 */
public class MessageService {

    private static final String DEFAULT_MESSAGES_FILE = "messages.yml";

    private final FairPerks plugin;
    private final MiniMessage miniMessage;
    private final Set<String> missingKeysLogged;

    private FileConfiguration defaultMessages;
    private FileConfiguration activeMessages;

    public MessageService(FairPerks plugin) {
        this.plugin = plugin;
        this.miniMessage = MiniMessage.miniMessage();
        this.missingKeysLogged = new HashSet<>();
    }

    public void load() {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            plugin.getLogger().warning("Could not create plugin data folder: " + dataFolder.getAbsolutePath());
        }

        YamlSyncUtil.syncWithBundledDefaults(plugin, DEFAULT_MESSAGES_FILE, new File(dataFolder, DEFAULT_MESSAGES_FILE));
        this.defaultMessages = loadMessagesFile(DEFAULT_MESSAGES_FILE);

        String configuredLanguage = plugin.getConfig().getString("language", "default");
        String activeFileName = resolveActiveMessagesFile(configuredLanguage);
        this.activeMessages = loadMessagesFile(activeFileName);

        if (!DEFAULT_MESSAGES_FILE.equals(activeFileName)) {
            plugin.getLogger().info("Loaded message language from " + activeFileName + ".");
        }
    }

    public void sendActionBar(Player player, String key, TagResolver... resolvers) {
        player.sendActionBar(component(key, resolvers));
    }

    public void sendMessage(Player player, String key, TagResolver... resolvers) {
        player.sendMessage(component(key, resolvers));
    }

    public Component component(String key, TagResolver... resolvers) {
        String template = resolveTemplate(key);
        return miniMessage.deserialize(template, resolvers);
    }

    /**
     * Looks up a message key with fallback to defaults and one-time missing-key logging.
     */
    private String resolveTemplate(String key) {
        String activeTemplate = activeMessages != null ? activeMessages.getString(key) : null;
        if (activeTemplate != null) {
            return activeTemplate;
        }

        String defaultTemplate = defaultMessages != null ? defaultMessages.getString(key) : null;
        if (defaultTemplate != null) {
            return defaultTemplate;
        }

        if (missingKeysLogged.add(key)) {
            plugin.getLogger().warning("Missing message key: " + key);
        }
        return "<red>Missing message key: " + key + "</red>";
    }

    private String resolveActiveMessagesFile(String configuredLanguage) {
        if (configuredLanguage == null) {
            return DEFAULT_MESSAGES_FILE;
        }

        String normalizedLanguage = configuredLanguage.trim();
        if (normalizedLanguage.isEmpty() || normalizedLanguage.equalsIgnoreCase("default")) {
            return DEFAULT_MESSAGES_FILE;
        }

        String candidate = "messages_" + normalizedLanguage.toUpperCase(Locale.ROOT) + ".yml";
        syncBundledLanguageFile(candidate);
        File candidateFile = new File(plugin.getDataFolder(), candidate);
        if (candidateFile.exists()) {
            return candidate;
        }

        plugin.getLogger().warning("Language file " + candidate + " not found; falling back to " + DEFAULT_MESSAGES_FILE + ".");
        return DEFAULT_MESSAGES_FILE;
    }

    private FileConfiguration loadMessagesFile(String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }

    private void syncBundledLanguageFile(String resourcePath) {
        YamlSyncUtil.syncWithBundledDefaults(plugin, resourcePath, new File(plugin.getDataFolder(), resourcePath));
    }
}
