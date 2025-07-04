package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import net.kyori.adventure.text.Component;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("msg|m|t|tell|w|whisper")
@CommandPermission("orbit.msg")
@Description("Send a private message to another player.")
public class MessageCommand extends BaseCommand {

    @Default
    public void onMessage(Player player, OnlinePlayer target, String message) {
        Player targetPlayer = target.getPlayer();

        // Check if target has messages disabled
        if (!Orbit.instance().playerManager().isPrivateMessagesEnabled(targetPlayer.getUniqueId())) {
            player.sendMessage(ChatUtils.prefixed(Prefix.MESSAGES, "<red>This player has messages disabled!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        // Send messages
        Component senderMessage = ChatUtils.prefixed(Prefix.MESSAGES, "To <green>" + targetPlayer.getName() + "<white>: " + message);
        Component targetMessage = ChatUtils.prefixed(Prefix.MESSAGES, "From <green>" + player.getName() + "<white>: " + message);

        player.sendMessage(senderMessage);
        targetPlayer.sendMessage(targetMessage);

        // Play sound
        SoundUtils.playSuccessSound(player);
        SoundUtils.playSuccessSound(targetPlayer);
    }
}