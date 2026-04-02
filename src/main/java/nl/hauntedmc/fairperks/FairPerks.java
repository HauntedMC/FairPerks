package nl.hauntedmc.fairperks;


import nl.hauntedmc.fairperks.command.GodMacroCommand;
import nl.hauntedmc.fairperks.listener.*;
import nl.hauntedmc.fairperks.util.MessageService;
import nl.hauntedmc.fairperks.util.YamlSyncUtil;

import com.earth2me.essentials.Essentials;

import org.bukkit.command.PluginCommand;
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
        if (this.getConfig().getBoolean("enabled.anchor")) {
            this.getServer().getPluginManager().registerEvents(new AnchorInteractListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.bed")) {
            this.getServer().getPluginManager().registerEvents(new BedInteractListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.blockignite")) {
            this.getServer().getPluginManager().registerEvents(new BlockIgniteListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.spawnermobs")) {
            this.getServer().getPluginManager().registerEvents(new CreatureSpawnListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.creeperignite")) {
            this.getServer().getPluginManager().registerEvents(new CreeperIgniteListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.endcrystal")) {
            this.getServer().getPluginManager().registerEvents(new EndCrystalInteractListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.godmacro")) {
            this.getServer().getPluginManager().registerEvents(new GodMacroListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.lava")) {
            this.getServer().getPluginManager().registerEvents(new LavaPlaceListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.melee")) {
            this.getServer().getPluginManager().registerEvents(new MeleeListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.projectile")) {
            this.getServer().getPluginManager().registerEvents(new ProjectileListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.target")) {
            this.getServer().getPluginManager().registerEvents(new TargetListener(this), this);
        }
        if (this.getConfig().getBoolean("enabled.tntignite")) {
            this.getServer().getPluginManager().registerEvents(new TntIgniteListener(this), this);
        }
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

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }
}
