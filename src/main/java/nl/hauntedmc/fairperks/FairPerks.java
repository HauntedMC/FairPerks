package nl.hauntedmc.fairperks;


import nl.hauntedmc.fairperks.command.GodMacroCommand;
import nl.hauntedmc.fairperks.listener.*;

import com.github.sirblobman.combatlogx.api.ICombatLogX;

import com.earth2me.essentials.Essentials;

import org.bukkit.plugin.java.JavaPlugin;


public class FairPerks extends JavaPlugin {

    private Essentials essentialsHook;
    private ICombatLogX combatlogHook;

    @Override
    public void onEnable() {
        this.getLogger().info("FairPerks wordt geladen.");
        initializeConfig();
        registerPluginHooks();
        registerListeners();
        registerCommands();
    }

    public Essentials getEssentialsHook() {
        return essentialsHook;
    }

    public ICombatLogX getCombatlogHook() {
        return combatlogHook;
    }

    private void initializeConfig() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    private void registerPluginHooks() {
        this.essentialsHook = (Essentials) getServer().getPluginManager().getPlugin("Essentials");

        if (this.essentialsHook == null) {
            getLogger().warning("Essentials is niet geinstalleerd op deze server.\n" +
                    "FairPerks schakelt zichzelf nu uit.");
            getServer().getPluginManager().disablePlugin(this);
        }

        this.combatlogHook = (ICombatLogX) getServer().getPluginManager().getPlugin("CombatLogX");

        if (this.combatlogHook == null) {
            getLogger().warning("CombatLogX is niet geinstalleerd op deze server.\n" +
                    "FairPerks schakelt zichzelf nu uit.");
            getServer().getPluginManager().disablePlugin(this);
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
            this.getCommand("godmacro").setExecutor(new GodMacroCommand(this));
        }
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }
}
