package nl.hauntedmc.fairperks;

import nl.hauntedmc.fairperks.command.GodMacroCommand;
import nl.hauntedmc.fairperks.listener.AnchorInteractListener;
import nl.hauntedmc.fairperks.listener.BedInteractListener;
import nl.hauntedmc.fairperks.listener.BlockIgniteListener;
import nl.hauntedmc.fairperks.listener.CreeperIgniteListener;
import nl.hauntedmc.fairperks.listener.CreatureSpawnListener;
import nl.hauntedmc.fairperks.listener.EndCrystalInteractListener;
import nl.hauntedmc.fairperks.listener.GodMacroListener;
import nl.hauntedmc.fairperks.listener.LavaPlaceListener;
import nl.hauntedmc.fairperks.listener.MeleeListener;
import nl.hauntedmc.fairperks.listener.PerkToggleGuardListener;
import nl.hauntedmc.fairperks.listener.ProjectileListener;
import nl.hauntedmc.fairperks.listener.PvpDamageListener;
import nl.hauntedmc.fairperks.listener.TargetListener;
import nl.hauntedmc.fairperks.listener.TntIgniteListener;
import nl.hauntedmc.fairperks.listener.TntPrimeListener;
import nl.hauntedmc.fairperks.util.MessageService;
import nl.hauntedmc.fairperks.util.YamlSyncUtil;

import com.earth2me.essentials.Essentials;

import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class FairPerks extends JavaPlugin {

    private Essentials essentialsHook;
    private Plugin combatlogHook;
    private MessageService messageService;

    @Override
    public void onEnable() {
        this.getLogger().info("FairPerks is starting.");
        initializeConfig();
        initializeMessageService();
        registerPluginHooks();
        if (!isEnabled()) {
            return;
        }
        registerListeners();
        registerCommands();
    }

    public Essentials getEssentialsHook() {
        return essentialsHook;
    }

    public Plugin getCombatlogHook() {
        return combatlogHook;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    private void initializeConfig() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            getLogger().warning("Could not create plugin data folder: " + dataFolder.getAbsolutePath());
        }

        YamlSyncUtil.syncWithBundledDefaults(this, "config.yml", new File(dataFolder, "config.yml"));
        reloadConfig();
    }

    private void initializeMessageService() {
        this.messageService = new MessageService(this);
        this.messageService.load();
    }

    private void registerPluginHooks() {
        Plugin essentialsPlugin = getServer().getPluginManager().getPlugin("Essentials");
        this.essentialsHook = essentialsPlugin instanceof Essentials ? (Essentials) essentialsPlugin : null;

        if (this.essentialsHook == null) {
            getLogger().warning("Essentials is not installed on this server.\n" +
                    "FairPerks will now disable itself.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.combatlogHook = getServer().getPluginManager().getPlugin("CombatLogX");

        if (this.combatlogHook == null) {
            getLogger().warning("CombatLogX is not installed on this server.\n" +
                    "FairPerks will continue without combat checks for godmacro.");
        }
    }

    private void registerListeners() {
        registerListenerIfEnabled("enabled.anchor", new AnchorInteractListener(this));
        registerListenerIfEnabled("enabled.bed", new BedInteractListener(this));
        registerListenerIfEnabled("enabled.blockignite", new BlockIgniteListener(this));
        registerListenerIfEnabled("enabled.spawnermobs", new CreatureSpawnListener());
        registerListenerIfEnabled("enabled.creeperignite", new CreeperIgniteListener(this));
        registerListenerIfEnabled("enabled.endcrystal", new EndCrystalInteractListener(this));
        registerListenerIfEnabled("enabled.godmacro", new GodMacroListener(this));
        registerListenerIfEnabled("enabled.lava", new LavaPlaceListener(this));
        registerListenerIfEnabled("enabled.melee", new MeleeListener(this));
        registerListenerIfEnabled("enabled.perktoggleguard", new PerkToggleGuardListener(this));
        registerListenerIfEnabled("enabled.pvp", new PvpDamageListener(this));
        registerListenerIfEnabled("enabled.projectile", new ProjectileListener(this));
        registerListenerIfEnabled("enabled.target", new TargetListener(this));
        registerListenerIfEnabled("enabled.tntignite", new TntIgniteListener(this));
        registerListenerIfEnabled("enabled.tntprime", new TntPrimeListener(this));
    }

    private void registerCommands() {
        if (this.getConfig().getBoolean("enabled.godmacro")) {
            PluginCommand godMacroCommand = this.getCommand("godmacro");
            if (godMacroCommand == null) {
                getLogger().warning("Command 'godmacro' is missing in plugin.yml; godmacro will not be registered.");
                return;
            }
            godMacroCommand.setExecutor(new GodMacroCommand(this));
        }
    }

    private void registerListenerIfEnabled(String configPath, Listener listener) {
        if (this.getConfig().getBoolean(configPath)) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }
}
