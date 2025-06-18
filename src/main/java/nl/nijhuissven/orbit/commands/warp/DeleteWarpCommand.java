package nl.nijhuissven.orbit.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("delwarp|deletewarp")
@CommandPermission("orbit.delwarp")
@Description("Delete a warp point.")
public class DeleteWarpCommand extends BaseCommand {

    @Default
    @CommandCompletion("@warps")
    public void onDeleteWarp(Player player, String warpName) {
        if (Orbit.instance().warpManager().getWarp(warpName) == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "<red>Warp <white>" + warpName + "<red> does not exist!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 0.0F);
            return;
        }

        Orbit.instance().warpManager().deleteWarp(warpName);
        player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "Warp <#61bb16>" + warpName + "<white> has been deleted!"));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
    }
} 