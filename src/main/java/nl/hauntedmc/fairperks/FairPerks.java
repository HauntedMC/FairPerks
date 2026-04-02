package nl.hauntedmc.fairperks;


import nl.hauntedmc.fairperks.command.GodMacroCommand;
import nl.hauntedmc.fairperks.listener.*;
import nl.hauntedmc.fairperks.util.MessageService;

import com.earth2me.essentials.Essentials;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;


public class FairPerks extends JavaPlugin {

    private Essentials essentialsHook;
    private Plugin combatlogHook;
    private MessageService messageService;

    @Override
    public void onEnable() {
        this.getLogger().info("FairPerks wordt geladen.");
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
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private void initializeMessageService() {
        this.messageService = new MessageService(this);
        this.messageService.load();
    }

    private void registerPluginHooks() {
        Plugin essentialsPlugin = getServer().getPluginManager().getPlugin("Essentials");
        this.essentialsHook = essentialsPlugin instanceof Essentials ? (Essentials) essentialsPlugin : null;

        if (this.essentialsHook == null) {
            getLogger().warning("Essentials is niet geinstalleerd op deze server.\n" +
                    "FairPerks schakelt zichzelf nu uit.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.combatlogHook = getServer().getPluginManager().getPlugin("CombatLogX");

        if (this.combatlogHook == null) {
            getLogger().warning("CombatLogX is niet geinstalleerd op deze server.\n" +
                    "FairPerks gaat verder zonder combat checks voor godmacro.");
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
                getLogger().warning("Commando 'godmacro' ontbreekt in plugin.yml; godmacro wordt niet geregistreerd.");
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
