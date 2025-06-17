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

    private final DatabaseType databaseType;
    private final String mySqlHost, mySqlDatabase, mySqlUsername, mySqlPassword;
    private final int mySqlPort;

    public GlobalConfiguration() {
        File configFile = new File(Essentials.instance().getDataFolder(), "config.yml");
        Path configPath = configFile.toPath();
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .nodeStyle(NodeStyle.BLOCK)
                .indent(2)
                .build();

        try {
            CommentedConfigurationNode rootNode = loader.load();
            
            this.databaseType = DatabaseType.valueOf(rootNode.node("database", "type").getString("sqlite").toUpperCase());
            this.mySqlHost = rootNode.node("database", "mysql", "host").getString("172.18.0.1");
            this.mySqlPort = rootNode.node("database", "mysql", "port").getInt(3306);
            this.mySqlDatabase = rootNode.node("database", "mysql", "database").getString("database");
            this.mySqlUsername = rootNode.node("database", "mysql", "username").getString("username");
            this.mySqlPassword = rootNode.node("database", "mysql", "password").getString("password");

            loader.save(rootNode);
        } catch (IOException e) {
            Essentials.logger().severe("Could not load configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
} 