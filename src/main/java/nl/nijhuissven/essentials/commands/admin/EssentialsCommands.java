package nl.nijhuissven.essentials.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import lombok.SneakyThrows;
import nl.nijhuissven.essentials.Essentials;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.FormatDate;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.entity.Player;

import java.nio.file.Files;

@AutoRegister
@CommandAlias("essentials|ess")
@CommandPermission("essentials.admin")
public class EssentialsCommands extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Essentials Admin Commands:"));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "/ess reload - Reload the configuration files."));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "/ess version - Show the Essentials version."));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "/ess about - Information about Essentials."));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "/ess pluginstats - Show plugin statistics."));
    }
    @Subcommand("reload")
    public void onReload(Player player) {
        Essentials.instance().globalConfiguration().reload();
        Essentials.instance().warpManager().reload();

        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Config files has been reloaded"));
        Essentials.logger().info("Config files has been reloaded by " + player.getName());
    }

    @Subcommand("version")
    public void onVersion(Player player) {
        String version = Essentials.instance().getPluginMeta().getVersion();
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Essentials version: <#61bb16>" + version));
    }

    @Subcommand("about")
    public void onAbout(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Essentials is a powerful plugin for managing your server."));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "For more information, visit our website or contact the developers."));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Thank you for using Essentials!"));
    }


    @SneakyThrows
    @Subcommand("pluginstats")
    public void onPluginStats(Player player) {
        // File statistics
        final long START_TIME = System.currentTimeMillis();

        long configFileSize = Files.size(Essentials.instance().globalConfiguration().configFile().toPath());
        String configSizeDisplay;
        if (configFileSize >= 1024) {
            configSizeDisplay = (configFileSize / 1024) + " KB";
        } else {
            configSizeDisplay = configFileSize + " bytes";
        }

        // Memory statistics
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long pluginMemoryEstimate = estimatePluginMemoryUsage();
        double memoryPercentage = (double) pluginMemoryEstimate / totalMemory * 100;

        // Plugin uptime
        long uptime = System.currentTimeMillis() - START_TIME;
        String uptimeStr = FormatDate.formatUptime(uptime);

        // Send statistics to player
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Essentials Plugin Statistics:"));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Version: <#61bb16>" + Essentials.instance().getPluginMeta().getVersion()));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Uptime: <#61bb16>" + uptimeStr));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Config Size: <#61bb16>" + configSizeDisplay));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Total Warps: <#61bb16>" + Essentials.instance().warpManager().getWarps().size()));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Memory Usage: <#61bb16>" +
                String.format("%.2f", memoryPercentage) + "% <gray>(" +
                (pluginMemoryEstimate / (1024 * 1024)) + " MB / " +
                (totalMemory / (1024 * 1024)) + " MB)"));
    }

    private long estimatePluginMemoryUsage() {
        long baseMemory = 5 * 1024 * 1024;

        int warpsCount = Essentials.instance().warpManager().getWarps().size();
        long warpMemory = (long) warpsCount * 2 * 1024;

        long configMemory;
        try {
            configMemory = Files.size(Essentials.instance().globalConfiguration().configFile().toPath());
        } catch (Exception e) {
            configMemory = 50 * 1024;
        }

        return baseMemory + warpMemory + configMemory;
    }
}
