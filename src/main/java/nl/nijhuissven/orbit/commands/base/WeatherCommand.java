package nl.nijhuissven.orbit.commands.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.World;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("weather|w")
@CommandPermission("orbit.weather")
@Description("Change the weather in the current world or a specific world.")
public class WeatherCommand extends BaseCommand {

    @CommandAlias("sun|clear")
    @Subcommand("sun|clear")
    @Description("Set the weather to clear/sunny.")
    public void onSun(Player player, @Optional Integer duration) {
        World world = player.getWorld();
        world.setStorm(false);
        world.setThundering(false);

        if (duration != null) {
            world.setWeatherDuration(duration * 20);
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Weather has been set to <#FFD700>" +
                    ChatUtils.small("sunny") + "<white> for <#FFD700>" + ChatUtils.small(duration + " seconds") + "<white>!"));
        } else {
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Weather has been set to <#FFD700>" +
                    ChatUtils.small("sunny") + "<white>!"));
        }

        SoundUtils.playSuccessSound(player);
    }

    @CommandAlias("rain|storm")
    @Subcommand("rain|storm")
    @Description("Set the weather to rainy/stormy.")
    public void onRain(Player player, @Optional Integer duration) {
        World world = player.getWorld();
        world.setStorm(true);
        world.setThundering(false);

        if (duration != null) {
            world.setWeatherDuration(duration * 20); // Convert to ticks
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Weather has been set to <#73B8E2>" +
                    ChatUtils.small("rainy") + "<white> for <#73B8E2>" + ChatUtils.small(duration + " seconds") + "<white>!"));
        } else {
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Weather has been set to <#73B8E2>" +
                    ChatUtils.small("rainy") + "<white>!"));
        }

        SoundUtils.playSuccessSound(player);
    }

    @CommandAlias("thunder")
    @Subcommand("thunder")
    @Description("Set the weather to thundering.")
    public void onThunder(Player player, @Optional Integer duration) {
        World world = player.getWorld();
        world.setStorm(true);
        world.setThundering(true);

        if (duration != null) {
            world.setThunderDuration(duration * 20); // Convert to ticks
            world.setWeatherDuration(duration * 20);
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Weather has been set to <#FFB700>" +
                    ChatUtils.small("thunder") + "<white> for <#FFB700>" + ChatUtils.small(duration + " seconds") + "<white>!"));
        } else {
            player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Weather has been set to <#FFB700>" +
                    ChatUtils.small("thunder") + "<white>!"));
        }

        SoundUtils.playSuccessSound(player);
    }

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.WEATHER, "Usage: <white>/weather <sun|rain|thunder> [duration]"));
    }
}