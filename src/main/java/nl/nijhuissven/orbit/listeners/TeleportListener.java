package nl.nijhuissven.orbit.listeners;

import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.utils.TeleportUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleportListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!Orbit.instance().globalConfiguration().cancelTeleportOnMove()) {
            return;
        }

        // Check if the player has moved from their initial position
        if (event.getFrom().getBlockX() != event.getTo().getBlockX() ||
            event.getFrom().getBlockY() != event.getTo().getBlockY() ||
            event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {

            if (TeleportUtils.hasMoved(event.getPlayer())) {
                TeleportUtils.cancelTeleport(event.getPlayer());
                event.getPlayer().sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "<red>Teleport cancelled because you moved!"));
            }
        }
    }
} 