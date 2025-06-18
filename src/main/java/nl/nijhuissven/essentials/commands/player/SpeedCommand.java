package nl.nijhuissven.essentials.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.utils.chat.ChatUtils;
import nl.nijhuissven.essentials.utils.chat.Prefix;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("speed")
@CommandPermission("essentials.speed")
@Description("Change your movement speed.")
public class SpeedCommand extends BaseCommand {

    @Default
    public void onSpeed(Player player, float speed, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.speed.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "<red>You don't have permission to change other players' speed!"));
            return;
        }

        boolean isFlying = targetPlayer.isFlying();
        float clampedSpeed = Math.max(0.0001f, Math.min(10f, speed));
        float actualSpeed = getRealMoveSpeed(clampedSpeed, isFlying, player.hasPermission("essentials.speed.bypass"));

        if (isFlying) {
            targetPlayer.setFlySpeed(actualSpeed);
        } else {
            targetPlayer.setWalkSpeed(actualSpeed);
        }

        String speedType = isFlying ? "flying" : "walking";
        String displaySpeed = clampedSpeed == 0.0001f ? "0" : String.valueOf(clampedSpeed);

        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "Your " + speedType + " speed has been set to <#73B8E2>" +
                ChatUtils.small(displaySpeed) + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "Set <green>" + targetPlayer.getName() + "'s<white> " + speedType + " speed to <#73B8E2>" +
                    ChatUtils.small(displaySpeed) + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @CommandAlias("walkspeed")
    @Subcommand("walk")
    @CommandPermission("essentials.speed.walk")
    @Description("Set your walking speed.")
    public void onWalk(Player player, float speed, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.speed.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "<red>You don't have permission to change other players' speed!"));
            return;
        }

        float clampedSpeed = Math.max(0.0001f, Math.min(10f, speed));
        float actualSpeed = getRealMoveSpeed(clampedSpeed, false, player.hasPermission("essentials.speed.bypass"));

        targetPlayer.setWalkSpeed(actualSpeed);

        String displaySpeed = clampedSpeed == 0.0001f ? "0" : String.valueOf(clampedSpeed);

        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "Your walking speed has been set to <#73B8E2>" +
                ChatUtils.small(displaySpeed) + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "Set <green>" + targetPlayer.getName() + "'s<white> walking speed to <#73B8E2>" +
                    ChatUtils.small(displaySpeed) + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @CommandAlias("flyspeed")
    @Subcommand("fly")
    @CommandPermission("essentials.speed.fly")
    @Description("Set your flying speed.")
    public void onFly(Player player, float speed, @Optional OnlinePlayer target) {
        Player targetPlayer = target != null ? target.getPlayer() : player;

        if (target != null && !player.hasPermission("essentials.speed.others")) {
            player.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "<red>You don't have permission to change other players' speed!"));
            return;
        }

        float clampedSpeed = Math.max(0.0001f, Math.min(10f, speed));
        float actualSpeed = getRealMoveSpeed(clampedSpeed, true, player.hasPermission("essentials.speed.bypass"));

        targetPlayer.setFlySpeed(actualSpeed);

        String displaySpeed = clampedSpeed == 0.0001f ? "0" : String.valueOf(clampedSpeed);

        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "Your flying speed has been set to <#73B8E2>" +
                ChatUtils.small(displaySpeed) + "<white>!"));
        targetPlayer.playSound(targetPlayer.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);

        if (!targetPlayer.equals(player)) {
            player.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "Set <green>" + targetPlayer.getName() + "'s<white> flying speed to <#73B8E2>" +
                    ChatUtils.small(displaySpeed) + "<white>!"));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 1.0F, 1.0F);
        }
    }

    @HelpCommand
    public void onHelp(Player player) {
        player.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "Your current speeds: <#73B8E2>" +
                ChatUtils.small("Walk: " + (player.getWalkSpeed() / 0.2f) + ", Fly: " + (player.getFlySpeed() / 0.1f))));
        player.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "Usage: <white>/speed <number> [player]"));
        player.sendMessage(ChatUtils.prefixed(Prefix.SPEED, "Usage: <white>/speed <walk|fly> <number> [player]"));
    }

    private float getRealMoveSpeed(float userSpeed, boolean isFly, boolean isBypass) {
        float defaultSpeed = isFly ? 0.1f : 0.2f;
        float maxSpeed = 1f;

        if (userSpeed < 1f) {
            return defaultSpeed * userSpeed;
        } else {
            float ratio = ((userSpeed - 1) / 9) * (maxSpeed - defaultSpeed);
            return ratio + defaultSpeed;
        }
    }
}