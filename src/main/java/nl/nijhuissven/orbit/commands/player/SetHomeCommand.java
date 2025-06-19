package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("sethome")
@CommandPermission("orbit.sethome")
@Description("Set a home at your current location.")
public class SetHomeCommand extends BaseCommand {

    @Default
    public void onSetHome(Player player, String homeName) {
        if (!Orbit.instance().homeManager().canCreateHome(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "<red>You have reached the maximum number of homes (" + Orbit.instance().globalConfiguration().maxHomes() + ")!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        Orbit.instance().homeManager().saveHome(player.getUniqueId(), homeName, player.getLocation());
        player.sendMessage(ChatUtils.prefixed(Prefix.HOMES, "Home <#61bb16>" + homeName + "<white> has been set!"));
        SoundUtils.playSuccessSound(player);
    }
} 