package nl.hauntedmc.fairperks.testutil;

import nl.hauntedmc.fairperks.FairPerks;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class TestFixtures {

    private TestFixtures() {
    }

    public static Player mockPlayerWithSpigot(boolean flying) {
        Player player = mock(Player.class);
        Player.Spigot spigot = mock(Player.Spigot.class);

        when(player.isFlying()).thenReturn(flying);
        when(player.spigot()).thenReturn(spigot);

        return player;
    }

    public static void stubGodMode(FairPerks plugin, Player player, boolean godMode) {
        Essentials essentials = mock(Essentials.class);
        User user = mock(User.class);

        when(plugin.getEssentialsHook()).thenReturn(essentials);
        when(essentials.getUser(player)).thenReturn(user);
        when(user.isGodModeEnabled()).thenReturn(godMode);
    }

    public static void stubCombatState(FairPerks plugin, Player player, boolean inCombat) {
        ICombatLogX combatLogX = mock(ICombatLogX.class);
        ICombatManager combatManager = mock(ICombatManager.class);

        when(plugin.getCombatlogHook()).thenReturn(combatLogX);
        when(combatLogX.getCombatManager()).thenReturn(combatManager);
        when(combatManager.isInCombat(player)).thenReturn(inCombat);
    }

    public static void stubMainHandMaterial(Player player, Material material) {
        PlayerInventory inventory = mock(PlayerInventory.class);
        ItemStack mainHand = mock(ItemStack.class);
        ItemStack offHand = mock(ItemStack.class);

        when(player.getInventory()).thenReturn(inventory);
        when(inventory.getItemInMainHand()).thenReturn(mainHand);
        when(inventory.getItemInOffHand()).thenReturn(offHand);
        when(mainHand.getType()).thenReturn(material);
        when(offHand.getType()).thenReturn(Material.AIR);
    }

    public static Entity mockEntityOfType(EntityType entityType) {
        Entity entity = mock(Entity.class);
        when(entity.getType()).thenReturn(entityType);
        return entity;
    }

    public static PersistentDataContainer mapBackedStringDataContainer(Map<NamespacedKey, String> map) {
        PersistentDataContainer dataContainer = mock(PersistentDataContainer.class);

        when(dataContainer.has(any(NamespacedKey.class), eq(PersistentDataType.STRING)))
                .thenAnswer(invocation -> map.containsKey(invocation.getArgument(0)));
        when(dataContainer.get(any(NamespacedKey.class), eq(PersistentDataType.STRING)))
                .thenAnswer(invocation -> map.get(invocation.getArgument(0)));
        doAnswer(invocation -> {
            NamespacedKey key = invocation.getArgument(0);
            String value = invocation.getArgument(2);
            map.put(key, value);
            return null;
        }).when(dataContainer).set(any(NamespacedKey.class), eq(PersistentDataType.STRING), anyString());

        return dataContainer;
    }
}
