package nl.nijhuissven.orbit.tasks;

import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class TeleportCountdownTask extends BukkitRunnable {
    private final Player player;
    private final Location location;
    private final String successMessage;
    private int secondsLeft;
    private final boolean cancelOnMove;
    private final Map<UUID, Location> teleportLocations;
    private final Runnable cancelTeleport;

    public TeleportCountdownTask(Player player, Location location, String successMessage, int delay, boolean cancelOnMove, Map<UUID, Location> teleportLocations, Runnable cancelTeleport) {
        this.player = player;
        this.location = location;
        this.successMessage = successMessage;
        this.secondsLeft = delay;
        this.cancelOnMove = cancelOnMove;
        this.teleportLocations = teleportLocations;
        this.cancelTeleport = cancelTeleport;
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            cancelTeleport.run();
            return;
        }
        if (cancelOnMove && hasMoved()) {
            cancelTeleport.run();
            player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "<red>Teleport cancelled because you moved!"));
            return;
        }
        if (secondsLeft <= 0) {
            player.teleport(location);
            player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, successMessage));
            cancelTeleport.run();
            teleportLocations.remove(player.getUniqueId());
            return;
        }
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "<gray>Teleporting in <#61bb16>" + secondsLeft + "<gray> seconds..."));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        secondsLeft--;
    }

    private boolean hasMoved() {
        Location initial = teleportLocations.get(player.getUniqueId());
        if (initial == null) return false;
        Location current = player.getLocation();
        return initial.getBlockX() != current.getBlockX() ||
               initial.getBlockY() != current.getBlockY() ||
               initial.getBlockZ() != current.getBlockZ();
    }
} 