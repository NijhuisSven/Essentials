package nl.nijhuissven.orbit.utils;

import lombok.Getter;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import nl.nijhuissven.orbit.tasks.TeleportCountdownTask;

@Getter
public class TeleportUtils {
    private static final Map<UUID, BukkitTask> teleportTasks = new HashMap<>();
    private static final Map<UUID, Location> teleportLocations = new HashMap<>();

    public static void scheduleTeleport(Player player, Location location, String successMessage) {
        int delay = Orbit.instance().globalConfiguration().teleportDelay();
        boolean cancelOnMove = Orbit.instance().globalConfiguration().cancelTeleportOnMove();

        cancelTeleport(player);

        if (delay <= 0) {
            player.teleport(location);
            player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, successMessage));
            return;
        }

        teleportLocations.put(player.getUniqueId(), player.getLocation().clone());
        // player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "<gray>Teleporting in <#61bb16>" + delay + "<gray> seconds. Don't move!"));

        if (player.hasPermission("orbit.teleport.bypass")) {
            player.teleport(location);
            player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, successMessage));
            teleportLocations.remove(player.getUniqueId());
            return;
        }

        BukkitTask bukkitTask = new TeleportCountdownTask(
            player,
            location,
            successMessage,
            delay,
            cancelOnMove,
            teleportLocations,
            () -> cancelTeleport(player)
        ).runTaskTimer(Orbit.instance(), 0L, 20L);
        teleportTasks.put(player.getUniqueId(), bukkitTask);
    }

    public static void cancelTeleport(Player player) {
        UUID playerId = player.getUniqueId();
        if (teleportTasks.containsKey(playerId)) {
            teleportTasks.get(playerId).cancel();
            teleportTasks.remove(playerId);
            teleportLocations.remove(playerId);
        }
    }

    public static boolean hasMoved(Player player) {
        UUID playerId = player.getUniqueId();
        if (!teleportLocations.containsKey(playerId)) {
            return false;
        }

        Location initial = teleportLocations.get(playerId);
        Location current = player.getLocation();
        
        return initial.getBlockX() != current.getBlockX() ||
               initial.getBlockY() != current.getBlockY() ||
               initial.getBlockZ() != current.getBlockZ();
    }
} 