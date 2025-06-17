package nl.nijhuissven.essentials.commands.playerCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("playertime|ptime")
@CommandPermission("essentials.ptime")
@Description("Set your personal time independent of the server time.")
public class PlayerTimeCommand extends BaseCommand {

    @Subcommand("day")
    @Description("Set your personal time to day.")
    public void onDay(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            return;
        }

        targetPlayer.setPlayerTime(1000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#FFD700>" +
                ChatUtils.small("day") + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#FFD700>" +
                    ChatUtils.small("day") + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Subcommand("night")
    @Description("Set your personal time to night.")
    public void onNight(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            return;
        }

        targetPlayer.setPlayerTime(13000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#373737>" +
                ChatUtils.small("night") + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#373737>" +
                    ChatUtils.small("night") + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Subcommand("noon")
    @Description("Set your personal time to noon.")
    public void onNoon(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            return;
        }

        targetPlayer.setPlayerTime(6000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#FFD700>" +
                ChatUtils.small("noon") + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#FFD700>" +
                    ChatUtils.small("noon") + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Subcommand("midnight")
    @Description("Set your personal time to midnight.")
    public void onMidnight(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            return;
        }

        targetPlayer.setPlayerTime(18000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#1E1E3F>" +
                ChatUtils.small("midnight") + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#1E1E3F>" +
                    ChatUtils.small("midnight") + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Subcommand("sunrise")
    @Description("Set your personal time to sunrise.")
    public void onSunrise(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            return;
        }

        targetPlayer.setPlayerTime(23000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#FF8C33>" +
                ChatUtils.small("sunrise") + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#FF8C33>" +
                    ChatUtils.small("sunrise") + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Subcommand("sunset")
    @Description("Set your personal time to sunset.")
    public void onSunset(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            return;
        }

        targetPlayer.setPlayerTime(12000, false);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been set to <#FF5733>" +
                ChatUtils.small("sunset") + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Set <green>" + targetPlayer.getName() + "'s<white> personal time to <#FF5733>" +
                    ChatUtils.small("sunset") + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Subcommand("reset")
    @Description("Reset your personal time to match server time.")
    public void onReset(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.ptime.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>You don't have permission to change other players' time!"));
            return;
        }

        targetPlayer.resetPlayerTime();
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your personal time has been <#73B8E2>" +
                ChatUtils.small("reset") + "<white> to match the server time!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Reset <green>" + targetPlayer.getName() + "'s<white> personal time to match the server time!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Your current personal time: <#73B8E2>" +
                ChatUtils.small(String.valueOf(player.getPlayerTime()))));
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Usage: <white>/ptime <day|night|noon|midnight|sunrise|sunset|reset> [player]"));
    }
}