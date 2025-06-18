package nl.nijhuissven.essentials.commands.warpCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.essentials.Essentials;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("setwarp")
@CommandPermission("essentials.setwarp")
@Description("Set a warp point at your current location.")
public class SetWarpCommand extends BaseCommand {

    @Default
    public void onSetWarp(Player player, String warpName) {
        Essentials.instance().warpManager().saveWarp(warpName, player.getLocation(), "essentials.warp." + warpName);
        player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "Warp <#61bb16>" + warpName + "<white> has been set!"));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
    }
} 