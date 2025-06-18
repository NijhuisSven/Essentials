package nl.nijhuissven.essentials.commands.warpCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.essentials.Essentials;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.TeleportUtils;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("warp")
@CommandPermission("essentials.warp")
@Description("Teleport to a warp point.")
public class WarpCommand extends BaseCommand {

    @Default
    @CommandCompletion("@warps")
    public void onWarp(Player player, String warpName) {
        var warp = Essentials.instance().warpManager().getWarp(warpName);
        if (warp == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "<red>Warp <white>" + warpName + "<red> does not exist!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 0.0F);
            return;
        }

        if (warp.permission() != null && !player.hasPermission(warp.permission())) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "<red>You don't have permission to use this warp!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 0.0F);
            return;
        }

        TeleportUtils.scheduleTeleport(player, warp.toLocation(), "Teleported to warp <#61bb16>" + warpName);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
    }

    @Subcommand("list")
    @CommandPermission("essentials.warp.list")
    @Description("List all available warps.")
    public void onList(Player player) {
        var warps = Essentials.instance().warpManager().getWarpNames();
        if (warps.isEmpty()) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WARPS, "<red>No warps have been set!"));
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