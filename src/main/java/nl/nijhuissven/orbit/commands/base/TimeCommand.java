package nl.nijhuissven.orbit.commands.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("time|t")
@CommandPermission("orbit.time")
@Description("Change the time in the current world.")
public class TimeCommand extends BaseCommand {

    @Subcommand("day")
    @CommandAlias("day")
    @Description("Set the time to day.")
    public void onDay(Player player) {
        World world = player.getWorld();
        world.setTime(1000);
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Time has been set to <#FFD700>" +
                ChatUtils.small("day") + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("night")
    @CommandAlias("night")
    @Description("Set the time to night.")
    public void onNight(Player player) {
        World world = player.getWorld();
        world.setTime(13000);
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Time has been set to <#373737>" +
                ChatUtils.small("night") + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @CommandAlias("noon")
    @Description("Set the time to noon.")
    public void onNoon(Player player) {
        World world = player.getWorld();
        world.setTime(6000);
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Time has been set to <#FFD700>" +
                ChatUtils.small("noon") + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @CommandAlias("midnight")
    @Description("Set the time to midnight.")
    public void onMidnight(Player player) {
        World world = player.getWorld();
        world.setTime(18000);
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Time has been set to <#1E1E3F>" +
                ChatUtils.small("midnight") + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @CommandAlias("sunrise")
    @Description("Set the time to sunrise.")
    public void onSunrise(Player player) {
        World world = player.getWorld();
        world.setTime(23000);
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Time has been set to <#FF8C33>" +
                ChatUtils.small("sunrise") + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @CommandAlias("sunset")
    @Description("Set the time to sunset.")
    public void onSunset(Player player) {
        World world = player.getWorld();
        world.setTime(12000);
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Time has been set to <#FF5733>" +
                ChatUtils.small("sunset") + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("set")
    @Description("Set the time to a specific value (0-24000).")
    public void onSet(Player player, int time) {
        if (time < 0 || time > 24000) {
            player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "<red>Time must be between 0 and 24000!"));
            return;
        }

        World world = player.getWorld();
        world.setTime(time);
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Time has been set to <#73B8E2>" +
                ChatUtils.small(String.valueOf(time)) + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("add")
    @Description("Add to the current time.")
    public void onAdd(Player player, int time) {
        World world = player.getWorld();
        long currentTime = world.getTime();
        world.setTime(currentTime + time);
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Added <#73B8E2>" +
                ChatUtils.small(String.valueOf(time)) + "<white> to the current time!"));
        SoundUtils.playSuccessSound(player);
    }

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.TIME, "Usage: <white>/time <day|night|noon|midnight|sunrise|sunset|set|add> [time]"));
    }
}