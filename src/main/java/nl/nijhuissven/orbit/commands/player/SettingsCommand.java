package nl.nijhuissven.orbit.commands.player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.entity.Player;

@AutoRegister
@CommandAlias("settings|setting|preferences|prefs")
@CommandPermission("orbit.settings")
@Description("Manage your player settings.")
public class SettingsCommand extends BaseCommand {

    @Default
    public void onDefault(Player player) {
        showSettings(player, player);
    }

    @Subcommand("view")
    @Description("View your current settings.")
    public void onView(Player player) {
        showSettings(player, player);
    }

    @Subcommand("view")
    @CommandPermission("orbit.settings.others")
    @Description("View another player's settings.")
    public void onViewOther(Player player, OnlinePlayer target) {
        showSettings(player, target.getPlayer());
    }

    @Subcommand("sounds")
    @Description("Toggle sound effects on/off.")
    public void onSounds(Player player) {
        boolean currentStatus = Orbit.instance().playerManager().isSoundsEnabled(player.getUniqueId());
        Orbit.instance().playerManager().setSoundsEnabled(player.getUniqueId(), !currentStatus);

        String status = !currentStatus ? "enabled" : "disabled";
        player.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Sound effects have been <#73B8E2>" +
                ChatUtils.small(status) + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("sounds")
    @CommandPermission("orbit.settings.others")
    @Description("Toggle sound effects for another player.")
    public void onSoundsOther(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();
        boolean currentStatus = Orbit.instance().playerManager().isSoundsEnabled(targetPlayer.getUniqueId());
        Orbit.instance().playerManager().setSoundsEnabled(targetPlayer.getUniqueId(), !currentStatus);

        String status = !currentStatus ? "enabled" : "disabled";
        player.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Sound effects have been <#73B8E2>" +
                ChatUtils.small(status) + "<white> for <green>" + targetPlayer.getName() + "<white>!"));
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Your sound effects have been <#73B8E2>" +
                ChatUtils.small(status) + "<white> by <green>" + player.getName() + "<white>!"));

        SoundUtils.playSuccessSound(player);
        SoundUtils.playSuccessSound(targetPlayer);
    }

    @Subcommand("messages|msg")
    @Description("Toggle private messages on/off.")
    public void onMessages(Player player) {
        boolean currentStatus = Orbit.instance().playerManager().isPrivateMessagesEnabled(player.getUniqueId());
        Orbit.instance().playerManager().setPrivateMessagesEnabled(player.getUniqueId(), !currentStatus);

        String status = !currentStatus ? "enabled" : "disabled";
        player.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Private messages have been <#73B8E2>" +
                ChatUtils.small(status) + "<white>!"));
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("messages|msg")
    @CommandPermission("orbit.settings.others")
    @Description("Toggle private messages for another player.")
    public void onMessagesOther(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();
        boolean currentStatus = Orbit.instance().playerManager().isPrivateMessagesEnabled(targetPlayer.getUniqueId());
        Orbit.instance().playerManager().setPrivateMessagesEnabled(targetPlayer.getUniqueId(), !currentStatus);

        String status = !currentStatus ? "enabled" : "disabled";
        player.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Private messages have been <#73B8E2>" +
                ChatUtils.small(status) + "<white> for <green>" + targetPlayer.getName() + "<white>!"));
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Your private messages have been <#73B8E2>" +
                ChatUtils.small(status) + "<white> by <green>" + player.getName() + "<white>!"));

        SoundUtils.playSuccessSound(player);
        SoundUtils.playSuccessSound(targetPlayer);
    }

    @Subcommand("reset")
    @Description("Reset your settings to default values.")
    public void onReset(Player player) {
        Orbit.instance().playerManager().setSoundsEnabled(player.getUniqueId(), 
            Orbit.instance().globalConfiguration().defaultSounds());
        Orbit.instance().playerManager().setPrivateMessagesEnabled(player.getUniqueId(), 
            Orbit.instance().globalConfiguration().defaultPrivateMessages());

        player.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Your settings have been reset to default values!"));
        SoundUtils.playSuccessSound(player);
    }

    @Subcommand("reset")
    @CommandPermission("orbit.settings.others")
    @Description("Reset another player's settings to default values.")
    public void onResetOther(Player player, OnlinePlayer target) {
        Player targetPlayer = target.getPlayer();
        Orbit.instance().playerManager().setSoundsEnabled(targetPlayer.getUniqueId(), 
            Orbit.instance().globalConfiguration().defaultSounds());
        Orbit.instance().playerManager().setPrivateMessagesEnabled(targetPlayer.getUniqueId(), 
            Orbit.instance().globalConfiguration().defaultPrivateMessages());

        player.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Settings for <green>" + targetPlayer.getName() + 
            "<white> have been reset to default values!"));
        targetPlayer.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Your settings have been reset to default values by <green>" + 
            player.getName() + "<white>!"));

        SoundUtils.playSuccessSound(player);
        SoundUtils.playSuccessSound(targetPlayer);
    }

    private void showSettings(Player viewer, Player target) {
        boolean isOwnSettings = viewer.equals(target);
        String playerName = isOwnSettings ? "Your" : target.getName() + "'s";
        
        viewer.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, playerName + " current settings:"));
        viewer.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "• Sound Effects: <#73B8E2>" + 
            ChatUtils.small(Orbit.instance().playerManager().isSoundsEnabled(target.getUniqueId()) ? "enabled" : "disabled")));
        viewer.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "• Private Messages: <#73B8E2>" + 
            ChatUtils.small(Orbit.instance().playerManager().isPrivateMessagesEnabled(target.getUniqueId()) ? "enabled" : "disabled")));
        
        if (isOwnSettings) {
            viewer.sendMessage(ChatUtils.prefixed(Prefix.SETTINGS, "Use <white>/settings sounds<white> or <white>/settings messages<white> to toggle settings!"));
        }
    }
} 