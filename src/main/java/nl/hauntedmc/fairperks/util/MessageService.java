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

public class MessageService {

    private static final String DEFAULT_MESSAGES_FILE = "messages.yml";
    private static final String BUILTIN_DUTCH_MESSAGES_FILE = "messages_NL.yml";

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
            plugin.getLogger().warning("Kon plugin data map niet aanmaken: " + dataFolder.getAbsolutePath());
        }

        saveBundledResourceIfMissing(DEFAULT_MESSAGES_FILE);
        saveBundledResourceIfMissing(BUILTIN_DUTCH_MESSAGES_FILE);

        this.defaultMessages = loadMessagesFile(DEFAULT_MESSAGES_FILE);

        String configuredLanguage = plugin.getConfig().getString("language", "default");
        String activeFileName = resolveActiveMessagesFile(configuredLanguage);
        this.activeMessages = loadMessagesFile(activeFileName);

        if (!DEFAULT_MESSAGES_FILE.equals(activeFileName)) {
            plugin.getLogger().info("Berichtentaal geladen uit " + activeFileName + ".");
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
            plugin.getLogger().warning("Ontbrekende message key: " + key);
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
        File candidateFile = new File(plugin.getDataFolder(), candidate);
        if (candidateFile.exists()) {
            return candidate;
        }

        plugin.getLogger().warning("Taalbestand " + candidate + " niet gevonden, fallback naar " + DEFAULT_MESSAGES_FILE + ".");
        return DEFAULT_MESSAGES_FILE;
    }

    private FileConfiguration loadMessagesFile(String fileName) {
        return YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), fileName));
    }

    private void saveBundledResourceIfMissing(String resourcePath) {
        File target = new File(plugin.getDataFolder(), resourcePath);
        if (target.exists()) {
            return;
        }

        if (plugin.getResource(resourcePath) == null) {
            return;
        }

        plugin.saveResource(resourcePath, false);
    }
}
