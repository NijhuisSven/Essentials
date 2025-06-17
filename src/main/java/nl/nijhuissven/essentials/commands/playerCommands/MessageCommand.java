package nl.nijhuissven.essentials.commands.playerCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.kyori.adventure.text.Component;
import nl.nijhuissven.essentials.Essentials;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("msg|m|t|tell|w|whisper")
@CommandPermission("essentials.msg")
@Description("Send a private message to another player.")
public class MessageCommand extends BaseCommand {

    @Default
    public void onMessage(Player player, OnlinePlayer target, String message) {
        Player targetPlayer = target.getPlayer();

        // Check if target has messages disabled
        if (Essentials.instance().playerManager().getPlayer(targetPlayer).ignoreMessages()) {
            player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "<red>This player has messages disabled!"));
            return;
        }

        // Send messages
        Component senderMessage = ChatUtils.prefixed(Prefix.MESSAGES, "To <green>" + targetPlayer.getName() + "<white>: " + message);
        Component targetMessage = ChatUtils.prefixed(Prefix.MESSAGES, "From <green>" + player.getName() + "<white>: " + message);

        player.sendMessage(senderMessage);
        targetPlayer.sendMessage(targetMessage);

        // Play sound
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
    }
}