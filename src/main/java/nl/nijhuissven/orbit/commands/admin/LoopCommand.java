package nl.nijhuissven.orbit.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lombok.Getter;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.tasks.LoopCommandTask;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AutoRegister
@CommandAlias("loop")
@CommandPermission("orbit.loop")
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
            SoundUtils.playErrorSound(player);
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
        loopTask.runTaskTimer(Orbit.instance(), 0L, tickDelay);
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("stop")
    @Description("Stop an active command loop")
    public void onLoopStop(Player player) {
        LoopCommandTask task = loopTasks.get(player.getUniqueId());

        if (task == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "<red>You don't have an active command loop."));
            SoundUtils.playErrorSound(player);
            return;
        }

        task.cancel();
        loopTasks.remove(player.getUniqueId());
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Command loop <#61bb16>stopped<white>."));
        SoundUtils.playSuccessSound(player);
    }

    @HelpCommand
    public void onHelp(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "Loop Command Help:"));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "<white>/loop <amount> <tickDelay> <command> <gray>- Loop a command"));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "<white>/loop stop <gray>- Stop current command loop"));
        player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "<gray>Use %current% in your command to get the current iteration"));
    }
}