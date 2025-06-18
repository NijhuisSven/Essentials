package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("fly")
@CommandPermission("orbit.fly")
@Description("Toggle flight mode for yourself or another player.")
public class FlyCommand extends BaseCommand {

    @Default
    public void onFly(Player player, @Optional OnlinePlayer target, @Optional Boolean enabled) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (enabled == null) {
            enabled = !targetPlayer.getAllowFlight();
        }

        targetPlayer.setAllowFlight(enabled);
        targetPlayer.setFlying(enabled);

        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.FLY, "Your flight mode has been <#73B8E2>" + ChatUtils.small((enabled ? "enabled" : "disabled")) + "<white>!"));

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.FLY, "You have <#73B8E2>" + ChatUtils.small((enabled ? "enabled" : "disabled") + " <green>" + "<green>" + targetPlayer.getName()) + "'s<white> flight mode!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }

    }
}
