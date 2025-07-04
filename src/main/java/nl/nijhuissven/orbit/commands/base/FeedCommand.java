package nl.nijhuissven.orbit.commands.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("feed")
@CommandPermission("orbit.feed")
public class FeedCommand extends BaseCommand {

    @Default
    public void onFeedSelf(Player player) {
        player.setFoodLevel(20);
        player.setSaturation(20);

        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "You have been fed!"));
        SoundUtils.playSuccessSound(player);
    }

    @Default
    public void onFeed(Player player, OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (targetPlayer == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "This player is not online!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        targetPlayer.setFoodLevel(20);
        targetPlayer.setSaturation(20);

        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.INFO, "You have been fed!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "You have fed <green>" + targetPlayer.getName() + "<white>!"));
            SoundUtils.playSuccessSound(player);
        }
    }

}
