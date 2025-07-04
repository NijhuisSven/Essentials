package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("playertime|ptime")
@CommandPermission("orbit.ptime")
@Description("Set your personal time independent of the server time.")
public class PlayerTimeCommand extends BaseCommand {

    @Subcommand("day")
    @Description("Set your personal time to day.")
    public void onDay(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("orbit.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        targetPlayer.setPlayerTime(1000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#FFD700>" +
                ChatUtils.small("day") + "<white>!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#FFD700>" +
                    ChatUtils.small("day") + "<white>!"));
            SoundUtils.playSuccessSound(player);
        }
    }

    @Subcommand("night")
    @Description("Set your personal time to night.")
    public void onNight(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("orbit.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        targetPlayer.setPlayerTime(13000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#373737>" +
                ChatUtils.small("night") + "<white>!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#373737>" +
                    ChatUtils.small("night") + "<white>!"));
            SoundUtils.playSuccessSound(player);
        }
    }

    @Subcommand("noon")
    @Description("Set your personal time to noon.")
    public void onNoon(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("orbit.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        targetPlayer.setPlayerTime(6000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#FFD700>" +
                ChatUtils.small("noon") + "<white>!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#FFD700>" +
                    ChatUtils.small("noon") + "<white>!"));
            SoundUtils.playSuccessSound(player);
        }
    }

    @Subcommand("midnight")
    @Description("Set your personal time to midnight.")
    public void onMidnight(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("orbit.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        targetPlayer.setPlayerTime(18000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#1E1E3F>" +
                ChatUtils.small("midnight") + "<white>!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#1E1E3F>" +
                    ChatUtils.small("midnight") + "<white>!"));
            SoundUtils.playSuccessSound(player);
        }
    }

    @Subcommand("sunrise")
    @Description("Set your personal time to sunrise.")
    public void onSunrise(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("orbit.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        targetPlayer.setPlayerTime(23000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#FF8C33>" +
                ChatUtils.small("sunrise") + "<white>!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#FF8C33>" +
                    ChatUtils.small("sunrise") + "<white>!"));
            SoundUtils.playSuccessSound(player);
        }
    }

    @Subcommand("sunset")
    @Description("Set your personal time to sunset.")
    public void onSunset(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("orbit.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        targetPlayer.setPlayerTime(12000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#FF5733>" +
                ChatUtils.small("sunset") + "<white>!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#FF5733>" +
                    ChatUtils.small("sunset") + "<white>!"));
            SoundUtils.playSuccessSound(player);
        }
    }

    @Subcommand("reset")
    @Description("Reset your personal time to match server time.")
    public void onReset(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("orbit.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        targetPlayer.resetPlayerTime();
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been <#73B8E2>" +
                ChatUtils.small("reset") + "<white> to match the server time!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Reset <green>" + targetPlayer.getName() + "'s<white> personal time to match the server time!"));
            SoundUtils.playSuccessSound(player);
        }
    }

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your current personal time: <#73B8E2>" +
                ChatUtils.small(String.valueOf(player.getPlayerTime()))));
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Usage: <white>/ptime <day|night|noon|midnight|sunrise|sunset|reset> [player]"));
    }
}