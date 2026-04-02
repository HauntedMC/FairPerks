package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MessageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void loadUsesConfiguredLanguageFileWhenItExists() throws IOException {
        writeFile(
                tempDir.resolve("messages.yml"),
                "command:\n  godmacro:\n    enabled: \"<yellow>enabled default</yellow>\"\n"
        );
        writeFile(
                tempDir.resolve("messages_NL.yml"),
                "command:\n  godmacro:\n    enabled: \"<yellow>ingeschakeld nl</yellow>\"\n"
        );

        FairPerks plugin = mock(FairPerks.class);
        FileConfiguration config = mock(FileConfiguration.class);
        Logger logger = Logger.getLogger("MessageServiceTest");

        when(plugin.getDataFolder()).thenReturn(tempDir.toFile());
        when(plugin.getConfig()).thenReturn(config);
        when(plugin.getLogger()).thenReturn(logger);
        when(config.getString("language", "default")).thenReturn("NL");

        MessageService messageService = new MessageService(plugin);
        messageService.load();

        String message = PlainTextComponentSerializer.plainText().serialize(
                messageService.component("command.godmacro.enabled")
        );

        assertEquals("ingeschakeld nl", message);
    }

    @Test
    void loadFallsBackToDefaultWhenLanguageFileDoesNotExist() throws IOException {
        writeFile(
                tempDir.resolve("messages.yml"),
                "command:\n  godmacro:\n    enabled: \"<yellow>enabled default</yellow>\"\n"
        );

        FairPerks plugin = mock(FairPerks.class);
        FileConfiguration config = mock(FileConfiguration.class);
        Logger logger = Logger.getLogger("MessageServiceTest");

        when(plugin.getDataFolder()).thenReturn(tempDir.toFile());
        when(plugin.getConfig()).thenReturn(config);
        when(plugin.getLogger()).thenReturn(logger);
        when(config.getString("language", "default")).thenReturn("DE");

        MessageService messageService = new MessageService(plugin);
        messageService.load();

        String message = PlainTextComponentSerializer.plainText().serialize(
                messageService.component("command.godmacro.enabled")
        );

        assertEquals("enabled default", message);
    }

    private static void writeFile(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }
}
