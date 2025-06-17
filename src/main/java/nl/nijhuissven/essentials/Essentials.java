package nl.nijhuissven.essentials;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.essentials.commands.CommandManager;
import nl.nijhuissven.essentials.config.GlobalConfiguration;
import nl.nijhuissven.essentials.database.Database;
import nl.nijhuissven.essentials.listeners.PlayerListener;
import nl.nijhuissven.essentials.managers.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
@Accessors(fluent = true)
public final class Essentials extends JavaPlugin {

    @Getter
    private static Logger logger;
    private static Essentials instance;
    private GlobalConfiguration globalConfiguration;
    private Database database;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        Essentials.logger = getLogger();
        Essentials.instance = this;

        // Initialize configuration
        this.globalConfiguration = new GlobalConfiguration();
        
        // Initialize database
        this.database = new Database(globalConfiguration, getDataFolder());

        // Initialize player manager
        this.playerManager = new PlayerManager();

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(playerManager), this);

        // Initialize command manager
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        CommandManager cmdManager = new CommandManager(commandManager, logger, getClass().getPackage().getName());
        cmdManager.registerCommands();

        // Load all players
        playerManager.loadAllPlayers();

        Essentials.logger.info("Enabling Essentials plugin...");
    }

    @Override
    public void onDisable() {
        // Save all online players
        getServer().getOnlinePlayers().forEach(player -> playerManager.savePlayer(player));
        
        // Close database connection
        if (database != null) {
            database.disconnect();
        }
        
        Essentials.logger.info("Disabling Essentials plugin...");
    }

    public static Essentials instance() {
        return instance;
    }
}
