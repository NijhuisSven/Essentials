package nl.nijhuissven.orbit.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        final Player player = event.getPlayer();
        final String message = PlainTextComponentSerializer.plainText().serialize(event.message());
        
        String format = getChatFormatForPlayer(player);
        
        String formattedMessage = format
                .replace("{player}", player.getName())
                .replace("{message}", message)
                .replace("{world}", player.getWorld().getName());
                
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            final CachedMetaData metaData = LuckPermsProvider.get().getPlayerAdapter(Player.class).getMetaData(player);
            final String group = metaData.getPrimaryGroup();

            formattedMessage = formattedMessage
                    .replace("{prefix}", metaData.getPrefix() != null ? metaData.getPrefix() : "")
                    .replace("{suffix}", metaData.getSuffix() != null ? metaData.getSuffix() : "")
                    .replace("{group}", group != null ? group : "");
        }

        event.setCancelled(true);
        String finalFormattedMessage = formattedMessage;
        Bukkit.getOnlinePlayers().forEach(loopplayer -> {
            loopplayer.sendMessage(ChatUtils.color(finalFormattedMessage));
        });
    }
    
    private String getChatFormatForPlayer(Player player) {
        if (Bukkit.getPluginManager().isPluginEnabled("LuckPerms")) {
            try {
                final CachedMetaData metaData = LuckPermsProvider.get().getPlayerAdapter(Player.class).getMetaData(player);
                final String group = metaData.getPrimaryGroup();
                
                // Get group-specific format
                String groupFormat = Orbit.instance().globalConfiguration().getChatFormatForGroup(group);
                if (groupFormat != null) {
                    return groupFormat;
                }
            } catch (Exception e) {
                // Fallback to default format if LuckPerms fails
            }
        }
        
        // Return default format if no group-specific format is found
        return Orbit.instance().globalConfiguration().chatFormat();
    }
}