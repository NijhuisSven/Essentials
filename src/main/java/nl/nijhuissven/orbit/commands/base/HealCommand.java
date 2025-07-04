package nl.nijhuissven.orbit.commands.base;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("heal")
@CommandPermission("orbit.heal")
public class HealCommand extends BaseCommand {

    @Default
    public void onHeal(Player player, OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (targetPlayer == null) {
            player.sendMessage("That player is not online!");
            SoundUtils.playErrorSound(player);
            return;
        }

        targetPlayer.setHealth(targetPlayer.getAttribute(Attribute.MAX_HEALTH).getValue());
        targetPlayer.setFoodLevel(20);
        targetPlayer.setSaturation(20);

        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.INFO, "You have been healed!"));
        SoundUtils.playSuccessSound(targetPlayer);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.INFO, "You have healed <green>" + targetPlayer.getName() + "<white>!"));
            SoundUtils.playSuccessSound(player);
        }
    }
}
