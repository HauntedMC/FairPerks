package nl.hauntedmc.fairperks.util;

import nl.hauntedmc.fairperks.FairPerks;

import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class SpawnerUtil {

    public static boolean isSpawnermob(Entity damagedEntity) {
        return damagedEntity.getMetadata("spawnermob").stream().anyMatch(MetadataValue::asBoolean);
    }

    public static void setSpawnermob(Entity spawnerMob, MetadataValue metadataValue) {
        spawnerMob.setMetadata("spawnermob", metadataValue);
    }

    @NotNull
    public static MetadataValue createCustomMetadataValue(FairPerks plugin) {
        return new MetadataValue() {
            @Override
            public Object value() {
                return null;
            }

            @Override
            public int asInt() {
                return 0;
            }

            @Override
            public float asFloat() {
                return 0;
            }

            @Override
            public double asDouble() {
                return 0;
            }

            @Override
            public long asLong() {
                return 0;
            }

            @Override
            public short asShort() {
                return 0;
            }

            @Override
            public byte asByte() {
                return 0;
            }

            @Override
            public boolean asBoolean() {
                return true;
            }

            @Override
            public @NotNull String asString() {
                return "";
            }

            @Override
            public Plugin getOwningPlugin() {
                return plugin;
            }

            @Override
            public void invalidate() {
            }
        };
    }

}
