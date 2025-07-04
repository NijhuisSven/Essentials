package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.models.HomeModel;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.TeleportUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("home")
@CommandPermission("orbit.home")
@Description("Manage your homes.")
public class HomeCommand extends BaseCommand {

    @Default
    @CommandPermission("orbit.home.list")
    @Description("List all your homes.")
    public void onHome(Player player) {
        var homes = Orbit.instance().homeManager().getHomeNames(player.getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>You don't have any homes set!"));
            return;
        }

        player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "Your homes:"));
        StringBuilder sb = new StringBuilder();
        for (String home : homes) {
            sb.append("<click:run_command:'/home ").append(home).append("'><#61bb16>").append(home).append("</click><white>, ");
        }
        if (!sb.isEmpty()) sb.setLength(sb.length() - 9);
        player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, sb.toString()));
    }

    @Default
    @CommandCompletion("@homes")
    @Description("Teleport to a home.")
    public void onHomeTeleport(Player player, String homeName) {
        HomeModel home = Orbit.instance().homeManager().getHome(player.getUniqueId(), homeName);
        if (home == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>Home <white>" + homeName + "<red> does not exist!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 0.0F);
            return;
        }

        TeleportUtils.scheduleTeleport(player, home.toLocation(), "Teleported to home <#61bb16>" + homeName);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
    }

    @Default
    @CommandCompletion("@players @homes")
    @CommandPermission("orbit.home.visit")
    @Description("Teleport to another player's home.")
    public void onHomeTeleportOthers(Player player, String targetPlayer, String homeName) {
        Player target = Bukkit.getPlayer(targetPlayer);
        if (target == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>Player <white>" + targetPlayer + "<red> is not online!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        HomeModel home = Orbit.instance().homeManager().getHome(target.getUniqueId(), homeName);
        if (home == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>Home <white>" + homeName + "<red> does not exist for <white>" + targetPlayer + "<red>!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        TeleportUtils.scheduleTeleport(player, home.toLocation(), "Teleported to <white>" + targetPlayer + "'s<white> home <#61bb16>" + homeName);
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("list")
    @CommandPermission("orbit.home.list")
    @Description("List all your homes.")
    public void onList(Player player) {
        var homes = Orbit.instance().homeManager().getHomeNames(player.getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>You don't have any homes set!"));
            return;
        }

        player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "Your homes:"));
        StringBuilder sb = new StringBuilder();
        for (String home : homes) {
            sb.append("<click:run_command:'/home ").append(home).append("'><#61bb16>").append(home).append("</click><white>, ");
        }
        if (!sb.isEmpty()) sb.setLength(sb.length() - 9);
        player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, sb.toString()));
    }

    @Subcommand("list")
    @CommandPermission("orbit.home.list.others")
    @Description("List all homes of a player.")
    public void onListOthers(Player player, String targetPlayer) {
        Player target = Bukkit.getPlayer(targetPlayer);
        if (target == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>Player <white>" + targetPlayer + "<red> is not online!"));
            return;
        }

        var homes = Orbit.instance().homeManager().getHomeNames(target.getUniqueId());
        if (homes.isEmpty()) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<white>" + targetPlayer + "<red> doesn't have any homes!"));
            return;
        }

        player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<white>" + targetPlayer + "'s<white> homes:"));
        StringBuilder sb = new StringBuilder();
        for (String home : homes) {
            sb.append("<click:run_command:'/home ").append(targetPlayer).append(" ").append(home).append("'><#61bb16>").append(home).append("</click><white>, ");
        }
        if (!sb.isEmpty()) sb.setLength(sb.length() - 9);
        player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, sb.toString()));
    }
} 