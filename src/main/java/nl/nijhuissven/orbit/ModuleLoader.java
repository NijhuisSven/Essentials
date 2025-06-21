package nl.nijhuissven.orbit;

import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.orbit.config.GlobalConfiguration;
import nl.nijhuissven.orbit.config.ModuleConfiguration;
import nl.nijhuissven.orbit.listeners.*;
import nl.nijhuissven.orbit.managers.HomeManager;
import nl.nijhuissven.orbit.managers.WarpManager;
import nl.nijhuissven.orbit.managers.worldedit.WorldEditManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Accessors(fluent = true)
public class ModuleLoader {
    
    private final JavaPlugin plugin;
    private final GlobalConfiguration globalConfiguration;
    private final ModuleConfiguration moduleConfiguration;
    
    private WarpManager warpManager;
    private HomeManager homeManager;
    private WorldEditManager worldEditManager;
    
    public ModuleLoader(JavaPlugin plugin, GlobalConfiguration globalConfiguration, ModuleConfiguration moduleConfiguration) {
        this.plugin = plugin;
        this.globalConfiguration = globalConfiguration;
        this.moduleConfiguration = moduleConfiguration;
    }
    
    public void loadModules() {
        loadWarpsModule();
        loadHomesModule();
        loadWorldEditModule();
        loadChatFormatModule();
        registerCoreListeners();
    }

    private void loadWarpsModule() {
        if (moduleConfiguration.isModuleEnabled("Warps")) {
            this.warpManager = new WarpManager(globalConfiguration.warpStorage().equalsIgnoreCase("database"));
            Orbit.logger().info("Warps module enabled");
        } else {
            Orbit.logger().info("Warps module disabled");
        }
    }
    
    private void loadHomesModule() {
        if (moduleConfiguration.isModuleEnabled("Homes")) {
            this.homeManager = new HomeManager(globalConfiguration.homeStorage().equalsIgnoreCase("database"));
            Orbit.logger().info("Homes module enabled");
        } else {
            Orbit.logger().info("Homes module disabled");
        }
    }
    
    private void loadWorldEditModule() {
        if (moduleConfiguration.isModuleEnabled("WorldEdit")) {
            if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
                Orbit.logger().info("External WorldEdit plugin found. Disabling built-in WorldEdit module.");
            } else {
                this.worldEditManager = new WorldEditManager();
                plugin.getServer().getPluginManager().registerEvents(new WorldEditListener(), plugin);
                plugin.getServer().getPluginManager().registerEvents(new WandListener(this.worldEditManager.getVisualizationManager()), plugin);
                Orbit.logger().info("WorldEdit module enabled");
            }
        } else {
            Orbit.logger().info("WorldEdit module disabled");
        }
    }
    
    private void loadChatFormatModule() {
        if (moduleConfiguration.isModuleEnabled("ChatFormat")) {
            plugin.getServer().getPluginManager().registerEvents(new ChatListener(), plugin);
            Orbit.logger().info("ChatFormat module enabled");
        } else {
            Orbit.logger().info("ChatFormat module disabled");
        }
    }
    
    private void registerCoreListeners() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerListener(Orbit.instance().playerManager()), plugin);
        plugin.getServer().getPluginManager().registerEvents(new TeleportListener(), plugin);
    }
    
    public void cleanup() {
        if (worldEditManager != null) {
            plugin.getServer().getOnlinePlayers().forEach(player -> worldEditManager.cleanup(player));
        }
    }
} 