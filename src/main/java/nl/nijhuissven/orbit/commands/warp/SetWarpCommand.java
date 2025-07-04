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
@CommandAlias("setwarp")
@CommandPermission("orbit.setwarp")
@Description("Set a warp point at your current location.")
public class SetWarpCommand extends BaseCommand {

    @Default
    public void onSetWarp(Player player, String warpName) {
        Orbit.instance().warpManager().saveWarp(warpName, player.getLocation(), "orbit.warp." + warpName);
        player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "Warp <#61bb16>" + warpName + "<white> has been set!"));
        SoundUtils.playSuccessSound(player);
    }
} 