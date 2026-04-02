package nl.hauntedmc.fairperks.util;

import org.bukkit.entity.EntityType;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Central hostile-entity catalogue shared across listeners.
 * <p>
 * Keeping this list in one place ensures all restrictions use the same definition
 * of "enemy", including server-specific entity types available on this runtime.
 */
public final class LegacyUtil {

    public static final Set<EntityType> ENEMY = Collections.unmodifiableSet(EnumSet.of(
            EntityType.BLAZE,
            EntityType.BOGGED,
            EntityType.BREEZE,
            EntityType.CAVE_SPIDER,
            EntityType.CREAKING,
            EntityType.CREEPER,
            EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.ENDER_DRAGON,
            EntityType.EVOKER,
            EntityType.GHAST,
            EntityType.GIANT,
            EntityType.GUARDIAN,
            EntityType.HOGLIN,
            EntityType.HUSK,
            EntityType.ILLUSIONER,
            EntityType.MAGMA_CUBE,
            EntityType.NAUTILUS,
            EntityType.PARCHED,
            EntityType.PHANTOM,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER,
            EntityType.RAVAGER,
            EntityType.SHULKER,
            EntityType.SILVERFISH,
            EntityType.SKELETON,
            EntityType.SKELETON_HORSE,
            EntityType.SLIME,
            EntityType.SPIDER,
            EntityType.STRAY,
            EntityType.VEX,
            EntityType.VINDICATOR,
            EntityType.WARDEN,
            EntityType.WITCH,
            EntityType.WITHER,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_NAUTILUS,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIFIED_PIGLIN
    ));

    private LegacyUtil() {
    }

    /**
     * Returns whether the provided entity type is considered hostile for FairPerks.
     */
    public static boolean isEnemy(EntityType entityType) {
        return ENEMY.contains(entityType);
    }
}
