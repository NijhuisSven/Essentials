package nl.nijhuissven.orbit.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@AutoRegister
@CommandAlias("broadcast|bc")
@CommandPermission("orbit.broadcast")
public class BroadcastCommand extends BaseCommand {

    @Default
    public void onBroadcast(CommandSender sender, String message) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("");
            player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, message));
            player.sendMessage("");
            SoundUtils.playSuccessSound(player);
        });
    }

}
