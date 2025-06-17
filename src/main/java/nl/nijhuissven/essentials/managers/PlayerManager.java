package nl.nijhuissven.essentials.managers;

import lombok.Getter;
import nl.nijhuissven.essentials.Essentials;
import nl.nijhuissven.essentials.models.PlayerModel;
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
                List<PlayerModel> result = Essentials.instance().database().query(PlayerModel.class, "uuid", player.getUniqueId());
                PlayerModel model = result.isEmpty() ? new PlayerModel(player.getUniqueId(), player.getName()) : result.get(0);
                players.put(player.getUniqueId(), model);
                Essentials.instance().logger().info("Loaded data for player: " + player.getName());
            } catch (Exception e) {
                Essentials.instance().logger().severe("Couldn't load player data for " + player.getName() + ": " + e.getMessage());
                players.put(player.getUniqueId(), new PlayerModel(player.getUniqueId(), player.getName()));
            }
        }, Essentials.instance().database().getExecutorService());
    }

    public void unloadPlayer(Player player) {
        players.remove(player.getUniqueId());
    }

    public void savePlayer(Player player) {
        PlayerModel model = getPlayer(player);
        if (model != null) {
            CompletableFuture.runAsync(() -> {
                try {
                    Essentials.instance().database().saveModel(model);
                    Essentials.instance().logger().info("Saved data for player: " + player.getName());
                } catch (Exception e) {
                    Essentials.instance().logger().severe("Failed to save player data for " + player.getName() + ": " + e.getMessage());
                }
            }, Essentials.instance().database().getExecutorService());
        }
    }

    public void loadAllPlayers() {
        CompletableFuture.runAsync(() -> {
            try {
                players.clear();
                List<PlayerModel> allPlayers = Essentials.instance().database().query(PlayerModel.class, null, null);
                for (PlayerModel model : allPlayers) {
                    players.put(model.uuid(), model);
                }
                Essentials.instance().logger().info("Loaded " + players.size() + " players!");
            } catch (Exception e) {
                Essentials.instance().logger().severe("Couldn't load player models: " + e.getMessage());
            }
        }, Essentials.instance().database().getExecutorService());
    }
} 