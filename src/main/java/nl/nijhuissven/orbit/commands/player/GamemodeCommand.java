package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("gamemode|gm")
@CommandPermission("orbit.gamemode")
@Description("Change your or another player's gamemode.")
public class GamemodeCommand extends BaseCommand {

    @Default
    @CommandCompletion("@gamemodes @players")
    public void onGamemode(Player player, String input, @Optional OnlinePlayer target) {
        GameMode gameMode = parseMode(input);
        if (gameMode == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.GAMEMODE, "<red>Invalid gamemode! Use: creative, survival, adventure, spectator"));
            return;
        }

        Player targetPlayer = target != null ? target.getPlayer() : player;

        targetPlayer.setGameMode(gameMode);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.GAMEMODE, "Your gamemode has been set to <#FA7499>" + ChatUtils.small(modeName(gameMode)) + "<white>!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.GAMEMODE, "You have set <green>" + targetPlayer.getName() + "'s<white> gamemode to <#FA7499>" + ChatUtils.small(modeName(gameMode)) + "<white>!"));
            SoundUtils.playSuccessSound(player);
        }
    }

    //daan plugge
    private String modeName(GameMode mode) {
        return switch (mode) {
            case CREATIVE -> "Creative";
            case SURVIVAL -> "Survival";
            case ADVENTURE -> "Adventure";
            case SPECTATOR -> "Spectator";
        };
    }
    //daan plugge
    private GameMode parseMode(String input) {
        return switch (input.toUpperCase()) {
            case "CREATIVE", "CREA", "C", "1" -> GameMode.CREATIVE;
            case "SURVIVAL", "SURV", "S", "0" -> GameMode.SURVIVAL;
            case "ADVENTURE", "ADVEN", "A", "2" -> GameMode.ADVENTURE;
            case "SPECTATOR", "SPEC", "SP", "3" -> GameMode.SPECTATOR;
            default -> null;
        };
    }
}
