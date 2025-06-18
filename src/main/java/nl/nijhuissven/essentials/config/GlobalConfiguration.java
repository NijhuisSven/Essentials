package nl.nijhuissven.essentials.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.essentials.Essentials;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Getter
@Accessors(fluent = true)
public class GlobalConfiguration {
    private DatabaseType databaseType;
    private String mySqlHost;
    private int mySqlPort;
    private String mySqlDatabase;
    private String mySqlUsername;
    private String mySqlPassword;
    private String warpStorage;
    private int teleportDelay;
    private boolean cancelTeleportOnMove;

    private final File configFile;
    private final YamlConfigurationLoader loader;

    public GlobalConfiguration() {
        this.configFile = new File(Essentials.instance().getDataFolder(), "config.yml");
        Path configPath = configFile.toPath();
        this.loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();

        loadConfig();
    }

    public void reload() {
        loadConfig();
    }

    private void loadConfig() {
        try {
            CommentedConfigurationNode rootNode = loader.load();

            // Database configuration
            this.databaseType = DatabaseType.valueOf(rootNode.node("database", "type").getString("sqlite").toUpperCase());
            this.mySqlHost = rootNode.node("database", "mysql", "host").getString("172.18.0.1");
            this.mySqlPort = rootNode.node("database", "mysql", "port").getInt(3306);
            this.mySqlDatabase = rootNode.node("database", "mysql", "database").getString("database");
            this.mySqlUsername = rootNode.node("database", "mysql", "username").getString("username");
            this.mySqlPassword = rootNode.node("database", "mysql", "password").getString("password");

            // Warp configuration
            this.warpStorage = rootNode.node("warps", "storage").getString("yaml");
            this.teleportDelay = rootNode.node("warps", "teleport", "delay").getInt(3);
            this.cancelTeleportOnMove = rootNode.node("warps", "teleport", "cancel-on-move").getBoolean(true);

            loader.save(rootNode);
        } catch (IOException e) {
            Essentials.logger().severe("Could not load configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
} 