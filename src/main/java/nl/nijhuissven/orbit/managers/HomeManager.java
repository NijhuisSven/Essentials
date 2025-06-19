package nl.nijhuissven.orbit.managers;

import lombok.Getter;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.models.HomeModel;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class HomeManager {
    private final Map<UUID, Map<String, HomeModel>> homes = new ConcurrentHashMap<>();
    private final boolean useDatabase;
    private final YamlConfigurationLoader yamlLoader;
    File configFile = new File(Orbit.instance().getDataFolder(), "homes.yml");
    Path configPath = configFile.toPath();

    public HomeManager(boolean useDatabase) {
        this.useDatabase = useDatabase;
        this.yamlLoader = YamlConfigurationLoader.builder()
                .path(configPath)
                .build();
        loadHomes();
    }

    public void reload() {
        loadHomes();
    }

    public void loadHomes() {
        homes.clear();
        if (useDatabase) {
            loadFromDatabase();
        } else {
            loadFromYaml();
        }
    }

    private void loadFromDatabase() {
        CompletableFuture.runAsync(() -> {
            try {
                List<HomeModel> result = Orbit.instance().database().query(HomeModel.class, null, null);
                for (HomeModel home : result) {
                    homes.computeIfAbsent(home.playerUuid(), k -> new ConcurrentHashMap<>())
                         .put(home.name(), home);
                }
                Orbit.logger().info("Loaded " + result.size() + " homes from database!");
            } catch (Exception e) {
                Orbit.logger().severe("Failed to load homes from database: " + e.getMessage());
            }
        }, Orbit.instance().database().getExecutorService());
    }

    private void loadFromYaml() {
        try {
            CommentedConfigurationNode root = yamlLoader.load();
            CommentedConfigurationNode playersNode = root.node("players");

            if (!playersNode.empty()) {
                for (Map.Entry<Object, CommentedConfigurationNode> playerEntry : playersNode.childrenMap().entrySet()) {
                    String playerUuidStr = playerEntry.getKey().toString();
                    UUID playerUuid = UUID.fromString(playerUuidStr);
                    CommentedConfigurationNode playerHomesNode = playerEntry.getValue();

                    Map<String, HomeModel> playerHomes = new ConcurrentHashMap<>();
                    homes.put(playerUuid, playerHomes);

                    for (Map.Entry<Object, CommentedConfigurationNode> homeEntry : playerHomesNode.childrenMap().entrySet()) {
                        String homeName = homeEntry.getKey().toString();
                        CommentedConfigurationNode homeNode = homeEntry.getValue();

                        Location location = new Location(
                                Orbit.instance().getServer().getWorld(homeNode.node("world").getString()),
                                homeNode.node("x").getDouble(),
                                homeNode.node("y").getDouble(),
                                homeNode.node("z").getDouble(),
                                homeNode.node("yaw").getFloat(),
                                homeNode.node("pitch").getFloat()
                        );

                        playerHomes.put(homeName, new HomeModel(playerUuid, homeName, location));
                    }
                }
            }
            Orbit.logger().info("Loaded " + homes.values().stream().mapToInt(Map::size).sum() + " homes from YAML!");
        } catch (IOException e) {
            Orbit.logger().severe("Failed to load homes from YAML: " + e.getMessage());
        }
    }

    public void saveHome(UUID playerUuid, String name, Location location) {
        HomeModel home = new HomeModel(playerUuid, name, location);
        homes.computeIfAbsent(playerUuid, k -> new ConcurrentHashMap<>()).put(name, home);

        if (useDatabase) {
            CompletableFuture.runAsync(() -> {
                try {
                    Orbit.instance().database().saveModel(home);
                    Orbit.logger().info("Saved home " + name + " for player " + playerUuid + " to database!");
                } catch (Exception e) {
                    Orbit.logger().severe("Failed to save home to database: " + e.getMessage());
                }
            }, Orbit.instance().database().getExecutorService());
        } else {
            try {
                CommentedConfigurationNode root = yamlLoader.load();
                CommentedConfigurationNode playerHomesNode = root.node("players", playerUuid.toString(), name);

                playerHomesNode.node("world").set(location.getWorld().getName());
                playerHomesNode.node("x").set(location.getX());
                playerHomesNode.node("y").set(location.getY());
                playerHomesNode.node("z").set(location.getZ());
                playerHomesNode.node("yaw").set(location.getYaw());
                playerHomesNode.node("pitch").set(location.getPitch());

                yamlLoader.save(root);
                Orbit.logger().info("Saved home " + name + " for player " + playerUuid + " to YAML!");
            } catch (IOException e) {
                Orbit.logger().severe("Failed to save home to YAML: " + e.getMessage());
            }
        }
    }

    public void deleteHome(UUID playerUuid, String name) {
        Map<String, HomeModel> playerHomes = homes.get(playerUuid);
        if (playerHomes != null) {
            playerHomes.remove(name);
            if (playerHomes.isEmpty()) {
                homes.remove(playerUuid);
            }
        }

        if (useDatabase) {
            CompletableFuture.runAsync(() -> {
                try {
                    List<HomeModel> result = Orbit.instance().database().query(HomeModel.class, "player_uuid", playerUuid);
                    for (HomeModel home : result) {
                        if (home.name().equals(name)) {
                            Orbit.instance().database().deleteModel(home);
                            Orbit.logger().info("Deleted home " + name + " for player " + playerUuid + " from database!");
                            break;
                        }
                    }
                } catch (Exception e) {
                    Orbit.logger().severe("Failed to delete home from database: " + e.getMessage());
                }
            }, Orbit.instance().database().getExecutorService());
        } else {
            try {
                CommentedConfigurationNode root = yamlLoader.load();
                root.node("players", playerUuid.toString()).removeChild(name);
                yamlLoader.save(root);
                Orbit.logger().info("Deleted home " + name + " for player " + playerUuid + " from YAML!");
            } catch (IOException e) {
                Orbit.logger().severe("Failed to delete home from YAML: " + e.getMessage());
            }
        }
    }

    public HomeModel getHome(UUID playerUuid, String name) {
        Map<String, HomeModel> playerHomes = homes.get(playerUuid);
        return playerHomes != null ? playerHomes.get(name) : null;
    }

    public List<String> getHomeNames(UUID playerUuid) {
        Map<String, HomeModel> playerHomes = homes.get(playerUuid);
        return playerHomes != null ? new ArrayList<>(playerHomes.keySet()) : new ArrayList<>();
    }

    public int getHomeCount(UUID playerUuid) {
        Map<String, HomeModel> playerHomes = homes.get(playerUuid);
        return playerHomes != null ? playerHomes.size() : 0;
    }

    public boolean canCreateHome(UUID playerUuid) {
        return getHomeCount(playerUuid) < Orbit.instance().globalConfiguration().maxHomes();
    }

    public boolean canCreateHome(Player player) {
        // Check if player has permission to bypass max homes limit
        if (player.hasPermission("orbit.home.unlimited")) {
            return true;
        }
        return canCreateHome(player.getUniqueId());
    }
} 