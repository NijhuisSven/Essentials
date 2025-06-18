package nl.nijhuissven.essentials.commands.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("top")
@CommandPermission("essentials.top")
@Description("Teleport to the highest block at your current position.")
public class TopCommand extends BaseCommand {

    @Default
    public void onTop(Player player) {
        Location location = player.getLocation();
        World world = location.getWorld();

        Location highestLocation = world.getHighestBlockAt(location).getLocation();

        if (highestLocation.getY() <= -64) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "Cannot find a safe location to teleport to."));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 0.5F);
            return;
        }

        highestLocation.setY(highestLocation.getY() + 1.1);
        highestLocation.setX(location.getX());
        highestLocation.setZ(location.getZ());

        player.teleport(highestLocation);
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "You have been teleported to the highest position."));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0f, 1.0f);
    }
}