package nl.nijhuissven.essentials.commands.playerCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("playerweather|pweather")
@CommandPermission("essentials.pweather")
@Description("Set your personal weather independent of the server weather.")
public class PlayerWeatherCommand extends BaseCommand {

    @Subcommand("sun|clear")
    @Description("Set your personal weather to clear/sunny.")
    public void onSun(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.pweather.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "<red>You don't have permission to change other players' weather!"));
            return;
        }

        targetPlayer.setPlayerWeather(WeatherType.CLEAR);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Your personal weather has been set to <#FFD700>" +
                ChatUtils.small("sunny") + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Set <green>" + targetPlayer.getName() + "'s<white> personal weather to <#FFD700>" +
                    ChatUtils.small("sunny") + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Subcommand("rain|storm")
    @Description("Set your personal weather to rainy/stormy.")
    public void onRain(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.pweather.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "<red>You don't have permission to change other players' weather!"));
            return;
        }

        targetPlayer.setPlayerWeather(WeatherType.DOWNFALL);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Your personal weather has been set to <#73B8E2>" +
                ChatUtils.small("rainy") + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Set <green>" + targetPlayer.getName() + "'s<white> personal weather to <#73B8E2>" +
                    ChatUtils.small("rainy") + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Subcommand("reset")
    @Description("Reset your personal weather to match server weather.")
    public void onReset(Player player, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.pweather.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "<red>You don't have permission to change other players' weather!"));
            return;
        }

        targetPlayer.resetPlayerWeather();
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Your personal weather has been <#73B8E2>" +
                ChatUtils.small("reset") + "<white> to match the server weather!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Reset <green>" + targetPlayer.getName() + "'s<white> personal weather to match the server weather!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Your current personal weather: <#73B8E2>" +
                ChatUtils.small(player.getPlayerWeather() == null ? "default" : player.getPlayerWeather().toString().toLowerCase())));
        player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Usage: <white>/pweather <sun|rain|reset> [player]"));
    }
}