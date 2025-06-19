package nl.nijhuissven.orbit.commands.warp;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.TeleportUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("warp")
@CommandPermission("orbit.warp")
@Description("Teleport to a warp point.")
public class WarpCommand extends BaseCommand {

    @Default
    @CommandCompletion("@warps")
    public void onWarp(Player player, String warpName) {
        var warp = Orbit.instance().warpManager().getWarp(warpName);
        if (warp == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "<red>Warp <white>" + warpName + "<red> does not exist!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        if (warp.permission() != null && !player.hasPermission(warp.permission())) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "<red>You don't have permission to use this warp!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        TeleportUtils.scheduleTeleport(player, warp.toLocation(), "Teleported to warp <#61bb16>" + warpName);
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("list")
    @CommandPermission("orbit.warp.list")
    @Description("List all available warps.")
    public void onList(Player player) {
        var warps = Orbit.instance().warpManager().getWarpNames();
        if (warps.isEmpty()) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "<red>No warps have been set!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "Available warps:"));
        StringBuilder sb = new StringBuilder();
        for (String warp : warps) {
            sb.append("<click:run_command:'/warp ").append(warp).append("'><#61bb16>").append(warp).append("</click><white>, ");
        }
        if (!sb.isEmpty()) sb.setLength(sb.length() - 9);
        player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, sb.toString()));
    }
} 