package nl.nijhuissven.orbit.commands.moderation;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.annotations.Punishments;
import nl.nijhuissven.orbit.utils.TimeEntry;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@AutoRegister
@Punishments
@CommandAlias("ban")
@CommandPermission("orbit.ban")
public class BanCommand extends BaseCommand {

    @Default
    public void onBan(Player player, OfflinePlayer target, @Optional TimeEntry duration, @Optional String reason) {
        Orbit.logger().info("player: " + player);
        Orbit.logger().info("target: " + target);
        Orbit.logger().info("TimeEntry: " + duration.getReadableTime());
        Orbit.logger().info("Reason: " + reason);
    }

}