package nl.nijhuissven.orbit.managers;

import lombok.Getter;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.models.WarpModel;
import org.bukkit.Location;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WarpManager {
    private final Map<String, WarpModel> warps = new ConcurrentHashMap<>();
    private final boolean useDatabase;
    private final YamlConfigurationLoader yamlLoader;
    File configFile = new File(Orbit.instance().getDataFolder(), "warps.yml");
    Path configPath = configFile.toPath();

    public WarpManager(boolean useDatabase) {
        this.useDatabase = useDatabase;
        this.yamlLoader = YamlConfigurationLoader.builder()
                .path(configPath)
                .build();
        loadWarps();
    }

    public void reload() {
        loadWarps();
    }

    public void loadWarps() {
        warps.clear();
        if (useDatabase) {
            loadFromDatabase();
        } else {
            loadFromYaml();
        }
    }

    private void loadFromDatabase() {
        CompletableFuture.runAsync(() -> {
            try {
                List<WarpModel> result = Orbit.instance().database().query(WarpModel.class, null, null);
                for (WarpModel warp : result) {
                    warps.put(warp.name(), warp);
                }
                Orbit.logger().info("Loaded " + warps.size() + " warps from database!");
            } catch (Exception e) {
                Orbit.logger().severe("Failed to load warps from database: " + e.getMessage());
            }
        }, Orbit.instance().database().getExecutorService());
    }

    private void loadFromYaml() {
        try {
            CommentedConfigurationNode root = yamlLoader.load();
            CommentedConfigurationNode warpsNode = root.node("warps");
            CommentedConfigurationNode permissionsNode = root.node("permissions");

            if (!warpsNode.empty()) {
                for (Map.Entry<Object, CommentedConfigurationNode> entry : warpsNode.childrenMap().entrySet()) {
                    String name = entry.getKey().toString();
                    CommentedConfigurationNode warpNode = entry.getValue();

                    Location location = new Location(
                            Orbit.instance().getServer().getWorld(warpNode.node("world").getString()),
                            warpNode.node("x").getDouble(),
                            warpNode.node("y").getDouble(),
                            warpNode.node("z").getDouble(),
                            warpNode.node("yaw").getFloat(),
                            warpNode.node("pitch").getFloat()
                    );

                    String permission = permissionsNode.node(name).getString();
                    warps.put(name, new WarpModel(name, location, permission));
                }
            }
            Orbit.logger().info("Loaded " + warps.size() + " warps from YAML!");
        } catch (IOException e) {
            Orbit.logger().severe("Failed to load warps from YAML: " + e.getMessage());
        }
    }

    public void saveWarp(String name, Location location, String permission) {
        WarpModel warp = new WarpModel(name, location, permission);
        warps.put(name, warp);

        if (useDatabase) {
            CompletableFuture.runAsync(() -> {
                try {
                    Orbit.instance().database().saveModel(warp);
                    Orbit.logger().info("Saved warp " + name + " to database!");
                } catch (Exception e) {
                    Orbit.logger().severe("Failed to save warp to database: " + e.getMessage());
                }
            }, Orbit.instance().database().getExecutorService());
        } else {
            try {
                CommentedConfigurationNode root = yamlLoader.load();
                CommentedConfigurationNode warpsNode = root.node("warps", name);
                CommentedConfigurationNode permissionsNode = root.node("permissions");

                warpsNode.node("world").set(location.getWorld().getName());
                warpsNode.node("x").set(location.getX());
                warpsNode.node("y").set(location.getY());
                warpsNode.node("z").set(location.getZ());
                warpsNode.node("yaw").set(location.getYaw());
                warpsNode.node("pitch").set(location.getPitch());

                if (permission != null) {
                    permissionsNode.node(name).set(permission);
                }

                yamlLoader.save(root);
                Orbit.logger().info("Saved warp " + name + " to YAML!");
            } catch (IOException e) {
                Orbit.logger().severe("Failed to save warp to YAML: " + e.getMessage());
            }
        }
    }

    public void deleteWarp(String name) {
        warps.remove(name);

        if (useDatabase) {
            CompletableFuture.runAsync(() -> {
                try {
                    List<WarpModel> result = Orbit.instance().database().query(WarpModel.class, "name", name);
                    if (!result.isEmpty()) {
                        Orbit.instance().database().deleteModel(result.getFirst());
                        Orbit.logger().info("Deleted warp " + name + " from database!");
                    }
                } catch (Exception e) {
                    Orbit.logger().severe("Failed to delete warp from database: " + e.getMessage());
                }
            }, Orbit.instance().database().getExecutorService());
        } else {
            try {
                CommentedConfigurationNode root = yamlLoader.load();
                root.node("warps").removeChild(name);
                root.node("permissions").removeChild(name);
                yamlLoader.save(root);
                Orbit.logger().info("Deleted warp " + name + " from YAML!");
            } catch (IOException e) {
                Orbit.logger().severe("Failed to delete warp from YAML: " + e.getMessage());
            }
        }
    }

    public WarpModel getWarp(String name) {
        return warps.get(name);
    }

    public List<String> getWarpNames() {
        return new ArrayList<>(warps.keySet());
    }
} 