package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("delhome|deletehome")
@CommandPermission("orbit.delhome")
@Description("Delete a home.")
public class DeleteHomeCommand extends BaseCommand {

    @Default
    @CommandCompletion("@homes")
    public void onDeleteHome(Player player, String homeName) {
        if (Orbit.instance().homeManager().getHome(player.getUniqueId(), homeName) == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>Home <white>" + homeName + "<red> does not exist!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        Orbit.instance().homeManager().deleteHome(player.getUniqueId(), homeName);
        player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "Home <#61bb16>" + homeName + "<white> has been deleted!"));
        SoundUtils.playSuccessSound(player);
    }

    @Default
    @CommandCompletion("@players @homes")
    @CommandPermission("orbit.delhome.others")
    public void onDeleteHomeOthers(Player player, String targetPlayer, String homeName) {
        Player target = Bukkit.getPlayer(targetPlayer);
        if (target == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>Player <white>" + targetPlayer + "<red> is not online!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        if (Orbit.instance().homeManager().getHome(target.getUniqueId(), homeName) == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>Home <white>" + homeName + "<red> does not exist for <white>" + targetPlayer + "<red>!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        Orbit.instance().homeManager().deleteHome(target.getUniqueId(), homeName);
        player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "Deleted <white>" + targetPlayer + "'s<white> home <#61bb16>" + homeName));
        SoundUtils.playSuccessSound(player);
    }
} 