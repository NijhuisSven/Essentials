package nl.nijhuissven.essentials.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("gamemode|gm")
@CommandPermission("essentials.gamemode")
public class gamemodeCommands extends BaseCommand {


    @Default
    public void onGamemode(Player player, GameMode gameMode, OnlinePlayer target) {
        if (gameMode == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.GAMEMODE, "<red>You have to specify a gamemode!"));
        }

        Player targetPlayer = target != null ? target.getPlayer() : player;

        targetPlayer.setGameMode(gameMode);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.GAMEMODE, "Your gamemode has been set to <green>" + ChatUtils.small(gameMode.name()) + "<white>!"));

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.GAMEMODE, "You have set <green>" + targetPlayer.getName() + "'s<white> gamemode to <green>" + ChatUtils.small(gameMode.name()) + "<white>!"));
        }
    }
}
