package nl.nijhuissven.orbit.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.orbit.Orbit;
import org.bukkit.Sound;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
    private String homeStorage;
    private int maxHomes;
    private boolean defaultSounds;
    private boolean defaultPrivateMessages;

    // Sound configuration
    private Sound successSound;
    private float successVolume;
    private float successPitch;
    private Sound errorSound;
    private float errorVolume;
    private float errorPitch;

    private final File configFile;
    private final YamlConfigurationLoader loader;
    private String chatFormat;
    private Map<String, String> groupFormats;

    public GlobalConfiguration() {
        this.configFile = new File(Orbit.instance().getDataFolder(), "config.yml");
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

            // Chat configuration
            this.chatFormat = rootNode.node("chat", "default-format").getString("&f{player}: &7{message}");
            loadGroupFormats(rootNode);
            
            // Warp configuration
            this.warpStorage = rootNode.node("warps", "storage").getString("yaml");
            this.teleportDelay = rootNode.node("warps", "teleport", "delay").getInt(3);
            this.cancelTeleportOnMove = rootNode.node("warps", "teleport", "cancel-on-move").getBoolean(true);

            // Home configuration
            this.homeStorage = rootNode.node("homes", "storage").getString("yaml");
            this.maxHomes = rootNode.node("homes", "max-homes").getInt(3);

            // Default player settings
            this.defaultSounds = rootNode.node("default-player-settings", "sounds").getBoolean(true);
            this.defaultPrivateMessages = rootNode.node("default-player-settings", "private-messages").getBoolean(true);

            // Sound configuration
            loadSoundConfig(rootNode);

            loader.save(rootNode);
        } catch (IOException e) {
            Orbit.logger().severe("Could not load configuration: " + e.getMessage());
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    private void loadGroupFormats(CommentedConfigurationNode rootNode) {
        this.groupFormats = new HashMap<>();
        CommentedConfigurationNode groupFormatsNode = rootNode.node("chat", "group-formats");
        
        if (groupFormatsNode.isMap()) {
            for (Map.Entry<Object, CommentedConfigurationNode> entry : groupFormatsNode.childrenMap().entrySet()) {
                String groupName = entry.getKey().toString();
                String format = entry.getValue().getString();
                this.groupFormats.put(groupName, format);
            }
        }
        
        // Add default format if not present
        if (!this.groupFormats.containsKey("default")) {
            this.groupFormats.put("default", this.chatFormat);
        }
    }

    public String getChatFormatForGroup(String groupName) {
        return groupFormats.getOrDefault(groupName, groupFormats.getOrDefault("default", chatFormat));
    }

    private void loadSoundConfig(CommentedConfigurationNode rootNode) {
        // Success sound
        String successSoundName = rootNode.node("sounds", "success", "sound").getString("BLOCK_NOTE_BLOCK_BIT");
        this.successSound = getSoundFromString(successSoundName);
        this.successVolume = (float) rootNode.node("sounds", "success", "volume").getDouble(1.0);
        this.successPitch = (float) rootNode.node("sounds", "success", "pitch").getDouble(1.0);

        // Error sound
        String errorSoundName = rootNode.node("sounds", "error", "sound").getString("BLOCK_NOTE_BLOCK_BIT");
        this.errorSound = getSoundFromString(errorSoundName);
        this.errorVolume = (float) rootNode.node("sounds", "error", "volume").getDouble(1.0);
        this.errorPitch = (float) rootNode.node("sounds", "error", "pitch").getDouble(0.0);
    }

    private Sound getSoundFromString(String soundName) {
        try {
            return Sound.valueOf(soundName.toUpperCase());
        } catch (IllegalArgumentException e) {
            Orbit.logger().warning("Invalid sound name in config: " + soundName + ". Using default sound.");
            return Sound.BLOCK_NOTE_BLOCK_BIT;
        }
    }
} 