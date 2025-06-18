package nl.nijhuissven.orbit.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

@AutoRegister
@CommandAlias("lagg|gc")
@CommandPermission("orbit.lagg")
public class LaggCommand extends BaseCommand {

    @Default
    public void onLagg(Player player) {
        Runtime runtime = Runtime.getRuntime();
        double tps = Bukkit.getTPS()[0];

        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Current TPS: " + colorTps(tps)));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Maximum Memory: <#61bb16>" + (runtime.maxMemory() / 1024 / 1024) + " MB"));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Allocated Memory: <#61bb16>" + (runtime.totalMemory() / 1024 / 1024) + " MB"));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Free Memory: <#61bb16>" + (runtime.freeMemory() / 1024 / 1024) + " MB"));

        player.sendMessage(Component.empty());

        for (World world : Bukkit.getWorlds()) {
            String environment = getEnvironmentType(world);

            int tileEntities = 0;
            for (@NotNull Chunk loadedChunk : world.getLoadedChunks()) {
                tileEntities += loadedChunk.getTileEntities().length;
            }

            player.sendMessage(ChatUtils.prefixed(Prefix.INFO,
                    "<white>World <#61bb16>" + world.getName() + " <white>(" + environment + "): " +
                            "<#61bb16>" + world.getLoadedChunks().length + " <white>chunks, " +
                            "<#61bb16>" + world.getEntities().size() + " <white>entities, " +
                            "<#61bb16>" + tileEntities + " <white>tiles."));
        }
    }

    private String colorTps(double tps) {
        if (tps >= 18.0) {
            return "<green>" + (int) tps;
        } else if (tps >= 15.0) {
            return "<yellow>" + (int) tps;
        } else {
            return "<red>" + (int) tps;
        }
    }

    private String getEnvironmentType(World world) {
        return switch (world.getEnvironment()) {
            case NETHER -> "Nether";
            case THE_END -> "The End";
            case NORMAL -> "Normal";
            default -> "Custom";
        };
    }
}