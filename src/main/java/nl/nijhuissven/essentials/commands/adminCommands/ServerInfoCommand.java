package nl.nijhuissven.essentials.commands.adminCommands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.FormatDate;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("serverinfo")
@CommandPermission("essentials.serverinfo")
public class ServerInfoCommand extends BaseCommand {

    private static final long START_TIME = System.currentTimeMillis();

    @Default
    public void onInfo(Player player) {
        long uptime = System.currentTimeMillis() - START_TIME;
        String uptimeStr = FormatDate.formatUptime(uptime);
        int ram = (int) (Runtime.getRuntime().totalMemory() / (1024 * 1024));
        int memory = (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024));

        int online = Bukkit.getOnlinePlayers().size();
        int max = Bukkit.getMaxPlayers();

        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Uptime: <#61bb16>" + uptimeStr));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Players: <#61bb16>" + online + "<gray>/<#61bb16>" + max));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Memory: <#61bb16>" + ram + "<gray>/<#61bb16>" + memory + " MB"));
    }

}
