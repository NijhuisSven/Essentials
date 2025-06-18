package nl.nijhuissven.essentials.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.essentials.Essentials;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("msgtoggle|togglemsg|togglemessages")
@CommandPermission("essentials.msgtoggle")
@Description("Toggle private messages on/off.")
public class MessageToggleCommand extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Private messages are currently <#73B8E2>" +
                ChatUtils.small(Essentials.instance().playerManager().getPlayer(player).ignoreMessages() ? "disabled" : "enabled") + "<white>!"));
        player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Usage: <white>/msgtoggle [player]"));
    }

    @Default
    public void onToggle(Player player) {
        Essentials.instance().playerManager().getPlayer(player).ignoreMessages(!Essentials.instance().playerManager().getPlayer(player).ignoreMessages());
        try {
            Essentials.instance().playerManager().savePlayer(player);
        } catch (Exception e) {
            player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "<red>Failed to save your preferences!"));
            return;
        }

        String status = Essentials.instance().playerManager().getPlayer(player).ignoreMessages() ? "disabled" : "enabled";
        player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Private messages have been <#73B8E2>" +
                ChatUtils.small(status) + "<white>!"));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
    }

    @Subcommand("other")
    @CommandPermission("essentials.msgtoggle.others")
    @Description("Toggle private messages for another player.")
    public void onToggleOther(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();
        Essentials.instance().playerManager().getPlayer(targetPlayer).ignoreMessages(!Essentials.instance().playerManager().getPlayer(targetPlayer).ignoreMessages());
        try {
            Essentials.instance().playerManager().savePlayer(targetPlayer);
        } catch (Exception e) {
            player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "<red>Failed to save player preferences!"));
            return;
        }

        String status = Essentials.instance().playerManager().getPlayer(targetPlayer).ignoreMessages() ? "disabled" : "enabled";
        player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Private messages have been <#73B8E2>" +
                ChatUtils.small(status) + "<white> for <green>" + targetPlayer.getName() + "<white>!"));
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "Your private messages have been <#73B8E2>" +
                ChatUtils.small(status) + "<white> by <green>" + player.getName() + "<white>!"));

        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
    }
}