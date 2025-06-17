package nl.nijhuissven.essentials.commands.playerCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AutoRegister
@CommandAlias("enderchest|ec")
@CommandPermission("essentials.enderchest")
public class EnderChestCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onEnderChest(@NotNull Player player, @Optional OfflinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (targetPlayer == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.ENDERCHEST, "<red>That player is not online!"));
            return;
        }

        player.openInventory(targetPlayer.getEnderChest());

        player.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        player.sendMessage(ChatUtils.prefixed(Prefix.ENDERCHEST, "You have opened your ender chest!"));

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.ENDERCHEST, "You have opened <green>" + targetPlayer.getName() + "'s<white> ender chest!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }

    }

}
