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
@CommandAlias("clear|ci")
@CommandPermission("orbit.clear")
public class ClearCommand extends BaseCommand {

    @Default
    public void onClearSelf(Player player) {
        player.getInventory().clear();
        SoundUtils.playSuccessSound(player);
        player.sendMessage(ChatUtils.prefixed(Prefix.INVENTORY, "Your inventory has been cleared!"));
    }

    @Default
    public void onClear(Player player, OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        targetPlayer.getInventory().clear();

        SoundUtils.playSuccessSound(targetPlayer);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.INVENTORY, "Your inventory has been cleared!"));

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.INVENTORY, "You have cleared <green>" + targetPlayer.getName() + "'s<white> inventory!"));
            SoundUtils.playSuccessSound(player);
        }
    }
}
