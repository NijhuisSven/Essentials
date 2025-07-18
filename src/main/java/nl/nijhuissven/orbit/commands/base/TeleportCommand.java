package nl.nijhuissven.orbit.commands.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("teleport|tp")
@CommandPermission("orbit.teleport")
@Description("Teleport yourself or others to a player or location.")
public class TeleportCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onTeleport(Player player, OnlinePlayer target) {
        player.teleport(target.getPlayer());
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "You have teleported to <green>" + target.getPlayer().getName() + "<white>."));
        SoundUtils.playSuccessSound(player);
    }

    @Default
    @CommandCompletion("@players @players")
    public void onTeleportPlayers(Player player, OnlinePlayer targetOne, OnlinePlayer targetTwo) {
        Player playerOne = targetOne.getPlayer();
        Player playerTwo = targetTwo.getPlayer();

        playerOne.teleport(playerTwo);

        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "Teleported <green>" + playerOne.getName() +
                "<white> to <green>" + playerTwo.getName() + "<white>."));
        playerOne.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "You were teleported to <green>" +
                playerTwo.getName() + "<white> by <green>" + player.getName() + "<white>."));

        SoundUtils.playSuccessSound(player);
        SoundUtils.playSuccessSound(playerOne);
    }

    @Default
    public void onTeleportCoords(Player player, double x, double y, double z) {
        Location location = new Location(player.getWorld(), x, y, z);
        player.teleport(location);

        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "You have teleported to <#61bb16>" +
                String.format("%.1f, %.1f, %.1f", x, y, z) + "<white>."));
        SoundUtils.playSuccessSound(player);
    }

    @Default
    @CommandCompletion("@players")
    public void onTeleportPlayerCoords(Player player, OnlinePlayer target, double x, double y, double z) {
        Player targetPlayer = target.getPlayer();
        Location location = new Location(targetPlayer.getWorld(), x, y, z);
        targetPlayer.teleport(location);

        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "Teleported <green>" + targetPlayer.getName() +
                "<white> to <#61bb16>" + String.format("%.1f, %.1f, %.1f", x, y, z) + "<white>."));
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "You were teleported to <#61bb16>" +
                String.format("%.1f, %.1f, %.1f", x, y, z) + "<white> by <green>" + player.getName() + "<white>."));

        SoundUtils.playSuccessSound(player);
        SoundUtils.playSuccessSound(targetPlayer);
    }

    @CommandAlias("teleporthere|tphere|tph|s")
    @CommandPermission("orbit.teleport.here")
    @Description("Teleport a player to you.")
    @CommandCompletion("@players")
    public void onTeleportHere(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();
        targetPlayer.teleport(player);

        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "You have teleported <green>" +
                targetPlayer.getName() + "<white> to your location."));
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "You were teleported to <green>" +
                player.getName() + "<white>."));

        SoundUtils.playSuccessSound(player);
        SoundUtils.playSuccessSound(targetPlayer);
    }

    @HelpCommand
    public void onHelp(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "Teleport Command Help:"));
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "<white>/tp <player> <gray>- Teleport to a player"));
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "<white>/tp <player1> <player2> <gray>- Teleport player1 to player2"));
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "<white>/tp <x> <y> <z> <gray>- Teleport to coordinates"));
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "<white>/tp <player> <x> <y> <z> <gray>- Teleport player to coordinates"));
        player.sendMessage(ChatUtils.prefixed(Prefix.TELEPORT, "<white>/tph <player> <gray>- Teleport player to you"));
    }
}