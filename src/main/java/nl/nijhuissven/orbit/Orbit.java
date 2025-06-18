package nl.nijhuissven.orbit;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.orbit.commands.CommandManager;
import nl.nijhuissven.orbit.config.GlobalConfiguration;
import nl.nijhuissven.orbit.database.Database;
import nl.nijhuissven.orbit.listeners.PlayerListener;
import nl.nijhuissven.orbit.listeners.TeleportListener;
import nl.nijhuissven.orbit.managers.PlayerManager;
import nl.nijhuissven.orbit.managers.WarpManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
@Accessors(fluent = true)
public final class Orbit extends JavaPlugin {

    @Getter
    private static Logger logger;
    private static Orbit instance;
    private GlobalConfiguration globalConfiguration;
    private Database database;
    private PlayerManager playerManager;
    private WarpManager warpManager;

    @Override
    public void onEnable() {
        Orbit.logger = getLogger();
        Orbit.instance = this;

        // Initialize configuration
        this.globalConfiguration = new GlobalConfiguration();
        
        // Initialize database
        this.database = new Database(globalConfiguration, getDataFolder());

        // Initialize managers
        this.playerManager = new PlayerManager();
        this.warpManager = new WarpManager(globalConfiguration.warpStorage().equalsIgnoreCase("database"));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(playerManager), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(), this);

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
        
        // Close database connection
        if (database != null) {
            database.disconnect();
        }
        
        Orbit.logger.info("Disabling Orbit...");
    }

    public static Orbit instance() {
        return instance;
    }
}
