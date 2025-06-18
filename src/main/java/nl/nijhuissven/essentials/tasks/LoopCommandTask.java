package nl.nijhuissven.essentials.tasks;

import net.kyori.adventure.bossbar.BossBar;
import nl.nijhuissven.essentials.commands.admin.LoopCommand;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LoopCommandTask extends BukkitRunnable {

    private final LoopCommand loopCommand;
    private final Player player;
    private final String command;
    private final int amount;
    private int currentAmount;
    private BossBar bossBar;

    public LoopCommandTask(LoopCommand loopCommand, Player player, String command, int amount) {
        this.loopCommand = loopCommand;
        this.player = player;
        this.command = command;
        this.amount = amount;
        this.currentAmount = 0;

        // Create boss bar with initial values
        this.bossBar = BossBar.bossBar(
                ChatUtils.color("Looping command <gray>\"" + command + "\"<white>. Progress: <gray>0<white>/<gray>" + amount + " <white>(<gray>0%<white>)"),
                0,
                BossBar.Color.WHITE,
                BossBar.Overlay.PROGRESS
        );

        player.showBossBar(bossBar);
    }

    @Override
    public void run() {
        currentAmount++;
        updateBar();

        player.performCommand(command.replaceAll("%current%", String.valueOf(currentAmount)));

        if (currentAmount >= amount) {
            cancel();
        }
    }

    @Override
    public void cancel() {
        loopCommand.getLoopTasks().remove(player.getUniqueId());
        player.hideBossBar(bossBar);
        bossBar = null;
        Bukkit.getScheduler().cancelTask(getTaskId());
        super.cancel();
    }

    private void updateBar() {
        float value = (float) currentAmount / amount;
        int percentage = (int) ((currentAmount * 100.0f) / amount);

        bossBar.progress(value);
        bossBar.name(ChatUtils.color("Looping command <gray>\"" + command + "\"<white>. Progress: <gray>" +
                currentAmount + "<white>/<gray>" + amount + " <white>(<gray>" + percentage + "%<white>)"));
    }
}