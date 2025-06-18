package nl.nijhuissven.essentials.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.Getter;
import nl.nijhuissven.essentials.Essentials;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.tasks.LoopCommandTask;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AutoRegister
@CommandAlias("loop")
@CommandPermission("essentials.loop")
@Description("Loop a command multiple times with a delay")
public class LoopCommand extends BaseCommand {

    @Getter
    private final Map<UUID, LoopCommandTask> loopTasks = new HashMap<>();

    @Default
    @Syntax("<amount> <tickDelay> <command>")
    @Description("Loop a command multiple times")
    public void onLoop(Player player, int amount, int tickDelay, String command) {
        if (loopTasks.containsKey(player.getUniqueId())) {
            player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "<red>You can only have one active loop at a time. Use <white>/loop stop <red>first."));
            return;
        }

        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Executing <#61bb16>" + command +
                "<white> " + amount + " times with a " + tickDelay + " tick delay."));

        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        final String finalCommand = command;

        if (tickDelay == 0) {
            for (int i = 0; i <= amount; i++) {
                player.performCommand(finalCommand.replaceAll("%current%", String.valueOf(i)));
            }
            return;
        }

        LoopCommandTask loopTask = new LoopCommandTask(this, player, finalCommand, amount);
        loopTasks.put(player.getUniqueId(), loopTask);
        loopTask.runTaskTimer(Essentials.instance(), 0L, tickDelay);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
    }

    @Subcommand("stop")
    @Description("Stop an active command loop")
    public void onLoopStop(Player player) {
        LoopCommandTask task = loopTasks.get(player.getUniqueId());

        if (task == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "<red>You don't have an active command loop."));
            return;
        }

        task.cancel();
        loopTasks.remove(player.getUniqueId());
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Command loop <#61bb16>stopped<white>."));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
    }

    @HelpCommand
    public void onHelp(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Loop Command Help:"));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "<white>/loop <amount> <tickDelay> <command> <gray>- Loop a command"));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "<white>/loop stop <gray>- Stop current command loop"));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "<gray>Use %current% in your command to get the current iteration"));
    }
}