package nl.nijhuissven.orbit.managers.worldedit;

import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SelectionManager {

    private final Map<UUID, Location> pos1 = new ConcurrentHashMap<>();
    private final Map<UUID, Location> pos2 = new ConcurrentHashMap<>();

    public void setPos1(Player player, Location location) {
        pos1.put(player.getUniqueId(), location);
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "Position 1 set to <green>" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()
        ));
        SoundUtils.playSuccessSound(player);
    }

    public void setPos2(Player player, Location location) {
        pos2.put(player.getUniqueId(), location);
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "Position 2 set to <green>" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()
        ));
        SoundUtils.playSuccessSound(player);
    }

    public boolean hasSelection(Player player) {
        return pos1.containsKey(player.getUniqueId()) && pos2.containsKey(player.getUniqueId());
    }

    public Location getPos1(Player player) {
        return pos1.get(player.getUniqueId());
    }

    public Location getPos2(Player player) {
        return pos2.get(player.getUniqueId());
    }

    public void clearSelection(Player player) {
        pos1.remove(player.getUniqueId());
        pos2.remove(player.getUniqueId());
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "<yellow>Selection cleared."
        ));
        SoundUtils.playSuccessSound(player);
    }
} 