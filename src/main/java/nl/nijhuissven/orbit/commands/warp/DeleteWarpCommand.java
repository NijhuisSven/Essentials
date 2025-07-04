package nl.nijhuissven.orbit.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
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
            SoundUtils.playErrorSound(player);
            return;
        }

        Orbit.instance().warpManager().deleteWarp(warpName);
        player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "Warp <#61bb16>" + warpName + "<white> has been deleted!"));
        SoundUtils.playSuccessSound(player);
    }
} 