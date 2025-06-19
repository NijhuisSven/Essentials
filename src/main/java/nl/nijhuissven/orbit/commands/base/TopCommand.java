package nl.nijhuissven.orbit.commands.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("top")
@CommandPermission("orbit.top")
@Description("Teleport to the highest block at your current position.")
public class TopCommand extends BaseCommand {

    @Default
    public void onTop(Player player) {
        Location location = player.getLocation();
        World world = location.getWorld();

        Location highestLocation = world.getHighestBlockAt(location).getLocation();

        if (highestLocation.getY() <= -64) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "Cannot find a safe location to teleport to."));
            SoundUtils.playErrorSound(player);
            return;
        }

        highestLocation.setY(highestLocation.getY() + 1.1);
        highestLocation.setX(location.getX());
        highestLocation.setZ(location.getZ());

        player.teleport(highestLocation);
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "You have been teleported to the highest position."));
        SoundUtils.playSuccessSound(player);
    }
}