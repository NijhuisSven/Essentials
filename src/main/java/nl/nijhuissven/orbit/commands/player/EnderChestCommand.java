package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AutoRegister
@CommandAlias("enderchest|ec")
@CommandPermission("orbit.enderchest")
public class EnderChestCommand extends BaseCommand {

    @Default
    @CommandCompletion("@players")
    public void onEnderChest(@NotNull Player player, @Optional OfflinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (targetPlayer == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.ENDERCHEST, "<red>That player is not found!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        player.openInventory(targetPlayer.getEnderChest());

        SoundUtils.playSuccessSound(player);
        player.sendMessage(ChatUtils.prefixed(Prefix.ENDERCHEST, "You have opened your ender chest!"));

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.ENDERCHEST, "You have opened <green>" + targetPlayer.getName() + "'s<white> ender chest!"));
            SoundUtils.playSuccessSound(player);
        }

    }

}
