package nl.nijhuissven.orbit.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import lombok.SneakyThrows;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.config.ModuleConfiguration;
import nl.nijhuissven.orbit.utils.FormatDate;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.entity.Player;

import java.nio.file.Files;
import java.util.Map;

@AutoRegister
@CommandAlias("orbit")
@CommandPermission("orbit.admin")
public class OrbitCommands extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Orbit Admin Commands:"));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "/orbit reload - Reload the configuration files."));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "/orbit version - Show the Orbit version."));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "/orbit about - Information about Orbit."));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "/orbit pluginstats - Show plugin statistics."));
    }

    @Subcommand("reload")
    public void onReload(Player player) {
        Orbit.instance().globalConfiguration().reload();
        Orbit.instance().moduleConfiguration().reload();
        if (Orbit.instance().moduleConfiguration().isModuleEnabled("Warps")) {
            Orbit.instance().warpManager().reload();
        }

        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Config files has been reloaded"));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "If you have changed the modules.yml file. Please restart the server."));
        SoundUtils.playSuccessSound(player);
        Orbit.logger().info("Config files has been reloaded by " + player.getName());
    }

    @Subcommand("version")
    public void onVersion(Player player) {
        String version = Orbit.instance().getPluginMeta().getVersion();
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Orbit version: <#61bb16>" + version));
    }

    @Subcommand("about")
    public void onAbout(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Orbit is a powerful plugin for managing your server."));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "For more information, visit our <click:open_url:'https://github.com/NijhuisSven/Orbit'><u>github page</u></click>"));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Thank you for using Orbit!"));
    }

    @Subcommand("modules")
    public void onModules(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Modules:"));

        Map<String, ModuleConfiguration.ModuleInfo> modules = Orbit.instance().moduleConfiguration().getAllModules();

        for (ModuleConfiguration.ModuleInfo module : modules.values()) {
            player.sendMessage(ChatUtils.prefixed(Prefix.SERVER,
                    " " + module.name() + " " + " <#006969>" +
                            module.version() + " <dark_gray>| " +
                            (module.enabled() ? "<green>Enabled" : "<red>Disabled")));
        }
    }
    
    @SneakyThrows
    @Subcommand("pluginstats")
    public void onPluginStats(Player player) {
        // File statistics
        final long START_TIME = System.currentTimeMillis();

        long configFileSize = Files.size(Orbit.instance().globalConfiguration().configFile().toPath());
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
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Orbit Plugin Statistics:"));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Version: <#61bb16>" + Orbit.instance().getPluginMeta().getVersion()));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Uptime: <#61bb16>" + uptimeStr));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Config Size: <#61bb16>" + configSizeDisplay));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Total Warps: <#61bb16>" + Orbit.instance().warpManager().getWarps().size()));
        player.sendMessage(ChatUtils.prefixed(Prefix.SERVER, "Memory Usage: <#61bb16>" +
                String.format("%.2f", memoryPercentage) + "% <gray>(" +
                (pluginMemoryEstimate / (1024 * 1024)) + " MB / " +
                (totalMemory / (1024 * 1024)) + " MB)"));
    }

    private long estimatePluginMemoryUsage() {
        long baseMemory = 5 * 1024 * 1024;

        int warpsCount = Orbit.instance().warpManager().getWarps().size();
        long warpMemory = (long) warpsCount * 2 * 1024;

        long configMemory;
        try {
            configMemory = Files.size(Orbit.instance().globalConfiguration().configFile().toPath());
        } catch (Exception e) {
            configMemory = 50 * 1024;
        }

        return baseMemory + warpMemory + configMemory;
    }
}
