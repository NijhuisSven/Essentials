package nl.nijhuissven.orbit.managers;

import lombok.Getter;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.models.PlayerModel;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerManager {
    private final Map<UUID, PlayerModel> players = new ConcurrentHashMap<>();

    public PlayerModel getPlayer(Player player) {
        return players.get(player.getUniqueId());
    }

    public PlayerModel getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public void loadPlayer(Player player) {
        CompletableFuture.runAsync(() -> {
            try {
                List<PlayerModel> result = Orbit.instance().database().query(PlayerModel.class, "uuid", player.getUniqueId());
                PlayerModel model;
                if (result.isEmpty()) {
                    // Create new player with default settings from config
                    model = new PlayerModel(
                        player.getUniqueId(), 
                        player.getName(),
                        Orbit.instance().globalConfiguration().defaultSounds(),
                        Orbit.instance().globalConfiguration().defaultPrivateMessages()
                    );
                } else {
                    model = result.getFirst();
                    // Update settings if they're null (for existing players)
                    if (model.soundsEnabled() == null) {
                        model.soundsEnabled(Orbit.instance().globalConfiguration().defaultSounds());
                    }
                    if (model.privateMessagesEnabled() == null) {
                        model.privateMessagesEnabled(Orbit.instance().globalConfiguration().defaultPrivateMessages());
                    }
                }
                players.put(player.getUniqueId(), model);
                Orbit.logger().info("Loaded data for player: " + player.getName());
            } catch (Exception e) {
                Orbit.logger().severe("Couldn't load player data for " + player.getName() + ": " + e.getMessage());
                players.put(player.getUniqueId(), new PlayerModel(
                    player.getUniqueId(), 
                    player.getName(),
                    Orbit.instance().globalConfiguration().defaultSounds(),
                    Orbit.instance().globalConfiguration().defaultPrivateMessages()
                ));
            }
        }, Orbit.instance().database().getExecutorService());
    }

    public void unloadPlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public void savePlayer(Player player) {
        PlayerModel model = getPlayer(player);
        if (model != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    Orbit.instance().database().saveModel(model);
                    Orbit.logger().info("Saved data for player: " + player.getName());
                } catch (Exception e) {
                    Orbit.logger().severe("Failed to save player data for " + player.getName() + ": " + e.getMessage());
                }
            }, Orbit.instance().database().getExecutorService());
        }
    }

    public void loadAllPlayers() {
        CompletableFuture.runAsync(() -> {
            try {
                players.clear();
                List<PlayerModel> allPlayers = Orbit.instance().database().query(PlayerModel.class, null, null);
                for (PlayerModel model : allPlayers) {
                    // Update settings if they're null (for existing players)
                    if (model.soundsEnabled() == null) {
                        model.soundsEnabled(Orbit.instance().globalConfiguration().defaultSounds());
                    }
                    if (model.privateMessagesEnabled() == null) {
                        model.privateMessagesEnabled(Orbit.instance().globalConfiguration().defaultPrivateMessages());
                    }
                    players.put(model.uuid(), model);
                }
                Orbit.logger().info("Loaded " + players.size() + " players!");
            } catch (Exception e) {
                Orbit.logger().severe("Couldn't load player models: " + e.getMessage());
            }
        }, Orbit.instance().database().getExecutorService());
    }

    // Player settings methods
    public boolean isSoundsEnabled(UUID playerUuid) {
        PlayerModel model = getPlayer(playerUuid);
        return model != null && model.soundsEnabled() != null ? model.soundsEnabled() : Orbit.instance().globalConfiguration().defaultSounds();
    }

    public boolean isPrivateMessagesEnabled(UUID playerUuid) {
        PlayerModel model = getPlayer(playerUuid);
        return model != null && model.privateMessagesEnabled() != null ? model.privateMessagesEnabled() : Orbit.instance().globalConfiguration().defaultPrivateMessages();
    }

    public void setSoundsEnabled(UUID playerUuid, boolean enabled) {
        PlayerModel model = getPlayer(playerUuid);
        if (model != null) {
            model.soundsEnabled(enabled);
            // Save immediately
            CompletableFuture.runAsync(() -> {
                try {
                    Orbit.instance().database().saveModel(model);
                } catch (Exception e) {
                    Orbit.logger().severe("Failed to save sounds setting for player " + playerUuid + ": " + e.getMessage());
                }
            }, Orbit.instance().database().getExecutorService());
        }
    }

    public void setPrivateMessagesEnabled(UUID playerUuid, boolean enabled) {
        PlayerModel model = getPlayer(playerUuid);
        if (model != null) {
            model.privateMessagesEnabled(enabled);
            // Save immediately
            CompletableFuture.runAsync(() -> {
                try {
                    Orbit.instance().database().saveModel(model);
                } catch (Exception e) {
                    Orbit.logger().severe("Failed to save private messages setting for player " + playerUuid + ": " + e.getMessage());
                }
            }, Orbit.instance().database().getExecutorService());
        }
    }
} 