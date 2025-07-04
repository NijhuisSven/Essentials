package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
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

        SoundUtils.playSuccessSound(targetPlayer);
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.FLY, "Your flight mode has been <#73B8E2>" + ChatUtils.small((enabled ? "enabled" : "disabled")) + "<white>!"));

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.FLY, "You have <#73B8E2>" + ChatUtils.small((enabled ? "enabled" : "disabled") + " <green>" + "<green>" + targetPlayer.getName()) + "'s<white> flight mode!"));
            SoundUtils.playSuccessSound(player);
        }

    }
}
