package nl.nijhuissven.orbit;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.orbit.commands.CommandManager;
import nl.nijhuissven.orbit.config.GlobalConfiguration;
import nl.nijhuissven.orbit.config.ModuleConfiguration;
import nl.nijhuissven.orbit.database.Database;
import nl.nijhuissven.orbit.managers.HomeManager;
import nl.nijhuissven.orbit.managers.PlayerManager;
import nl.nijhuissven.orbit.managers.WarpManager;
import nl.nijhuissven.orbit.managers.worldedit.WorldEditManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
@Accessors(fluent = true)
public final class Orbit extends JavaPlugin {

    @Getter
    private static Logger logger;
    private static Orbit instance;
    
    private GlobalConfiguration globalConfiguration;
    private ModuleConfiguration moduleConfiguration;
    private ModuleLoader moduleLoader;
    private Database database;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        Orbit.logger = getLogger();
        Orbit.instance = this;

        // Initialize configurations
        this.globalConfiguration = new GlobalConfiguration();
        this.moduleConfiguration = new ModuleConfiguration();
        
        // Initialize database
        this.database = new Database(globalConfiguration, getDataFolder());

        // Initialize core managers (always loaded)
        this.playerManager = new PlayerManager();

        // Load modules
        this.moduleLoader = new ModuleLoader(this, globalConfiguration, moduleConfiguration);
        this.moduleLoader.loadModules();

        // Initialize command manager
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        CommandManager cmdManager = new CommandManager(commandManager, logger, getClass().getPackage().getName());
        cmdManager.registerCommands();

        // Load all players
        playerManager.loadAllPlayers();

        Orbit.logger.info("Enabling Orbit...");
    }

    @Override
    public void onDisable() {
        // Save all online players
        getServer().getOnlinePlayers().forEach(player -> playerManager.savePlayer(player));
        
        // Clean up modules
        if (moduleLoader != null) {
            moduleLoader.cleanup();
        }
        
        // Close database connection
        if (database != null) {
            database.disconnect();
        }
        
        Orbit.logger.info("Disabling Orbit...");
    }

    public static Orbit instance() {
        return instance;
    }
    
    // Delegate methods to ModuleLoader for backward compatibility
    public WarpManager warpManager() {
        return moduleLoader != null ? moduleLoader.warpManager() : null;
    }
    
    public HomeManager homeManager() {
        return moduleLoader != null ? moduleLoader.homeManager() : null;
    }
    
    public WorldEditManager worldEditManager() {
        return moduleLoader != null ? moduleLoader.worldEditManager() : null;
    }
}
