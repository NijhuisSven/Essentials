package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("msgtoggle|togglemsg|togglemessages")
@CommandPermission("orbit.msgtoggle")
@Description("Toggle private messages on/off.")
public class MessageToggleCommand extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        boolean messagesEnabled = Orbit.instance().playerManager().isPrivateMessagesEnabled(player.getUniqueId());
        player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Private messages are currently <#73B8E2>" +
                ChatUtils.small(messagesEnabled ? "enabled" : "disabled") + "<white>!"));
        player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Usage: <white>/msgtoggle [player]"));
    }

    @Default
    public void onToggle(Player player) {
        boolean currentStatus = Orbit.instance().playerManager().isPrivateMessagesEnabled(player.getUniqueId());
        Orbit.instance().playerManager().setPrivateMessagesEnabled(player.getUniqueId(), !currentStatus);

        String status = !currentStatus ? "enabled" : "disabled";
        player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Private messages have been <#73B8E2>" +
                ChatUtils.small(status) + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("other")
    @CommandPermission("orbit.msgtoggle.others")
    @Description("Toggle private messages for another player.")
    public void onToggleOther(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();
        boolean currentStatus = Orbit.instance().playerManager().isPrivateMessagesEnabled(targetPlayer.getUniqueId());
        Orbit.instance().playerManager().setPrivateMessagesEnabled(targetPlayer.getUniqueId(), !currentStatus);

        String status = !currentStatus ? "enabled" : "disabled";
        player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Private messages have been <#73B8E2>" +
                ChatUtils.small(status) + "<white> for <green>" + targetPlayer.getName() + "<white>!"));
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Your private messages have been <#73B8E2>" +
                ChatUtils.small(status) + "<white> by <green>" + player.getName() + "<white>!"));

        SoundUtils.playSuccessSound(player);
        SoundUtils.playSuccessSound(targetPlayer);
    }
}