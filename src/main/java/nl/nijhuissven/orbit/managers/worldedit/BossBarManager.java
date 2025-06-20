package nl.nijhuissven.orbit.managers.worldedit;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarManager {
    private final Map<UUID, BossBar> activeBossBars = new ConcurrentHashMap<>();
    private final Map<UUID, String> staticTexts = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> totalBlocks = new ConcurrentHashMap<>();
    
    public BossBar createSetBlocksBossBar(Player player, Material material, int total) {
        // Cancel any existing operation first
        if (hasActiveBossBar(player)) {
            cancelOperation(player);
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<yellow>Previous operation cancelled due to new operation starting."
            ));
        }
        
        String staticText = "Setting blocks to <gray>" + material.name().toLowerCase() + "<white>. Progress: <gray>";
        BossBar bossBar = BossBar.bossBar(
            ChatUtils.color(staticText + "0<white>/<gray>" + total + " <white>(<gray>0%<white>)"),
            0,
            BossBar.Color.GREEN,
            BossBar.Overlay.PROGRESS
        );
        player.showBossBar(bossBar);
        activeBossBars.put(player.getUniqueId(), bossBar);
        staticTexts.put(player.getUniqueId(), staticText);
        totalBlocks.put(player.getUniqueId(), total);
        return bossBar;
    }
    
    public BossBar createReplaceBlocksBossBar(Player player, Material from, Material to, int total) {
        // Cancel any existing operation first
        if (hasActiveBossBar(player)) {
            cancelOperation(player);
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<yellow>Previous operation cancelled due to new operation starting."
            ));
        }
        
        String staticText = "Replacing <gray>" + from.name().toLowerCase() + "<white> with <gray>" + to.name().toLowerCase() + "<white>. Progress: <gray>";
        BossBar bossBar = BossBar.bossBar(
            ChatUtils.color(staticText + "0<white>/<gray>" + total + " <white>(<gray>0%<white>)"),
            0,
            BossBar.Color.BLUE,
            BossBar.Overlay.PROGRESS
        );
        player.showBossBar(bossBar);
        activeBossBars.put(player.getUniqueId(), bossBar);
        staticTexts.put(player.getUniqueId(), staticText);
        totalBlocks.put(player.getUniqueId(), total);
        return bossBar;
    }
    
    public void updateBossBarProgress(Player player, int current, int total) {
        BossBar bossBar = activeBossBars.get(player.getUniqueId());
        String staticText = staticTexts.get(player.getUniqueId());
        
        if (bossBar != null && staticText != null) {
            float progress = (float) current / total;
            int percentage = (int) ((current * 100.0f) / total);
            
            // Build the complete text with updated progress
            String completeText = staticText + current + "<white>/<gray>" + total + " <white>(<gray>" + percentage + "%<white>)";
            
            // Update the boss bar name
            bossBar.name(ChatUtils.color(completeText));
            bossBar.progress(progress);
        }
    }
    
    public void cleanupBossBar(Player player) {
        BossBar bossBar = activeBossBars.remove(player.getUniqueId());
        staticTexts.remove(player.getUniqueId());
        totalBlocks.remove(player.getUniqueId());
        if (bossBar != null) {
            player.hideBossBar(bossBar);
        }
    }
    
    public void cancelOperation(Player player) {
        BossBar bossBar = activeBossBars.remove(player.getUniqueId());
        staticTexts.remove(player.getUniqueId());
        totalBlocks.remove(player.getUniqueId());
        if (bossBar != null) {
            player.hideBossBar(bossBar);
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<yellow>WorldEdit operation cancelled."
            ));
        }
    }
    
    public int getActiveBossBarCount() {
        return activeBossBars.size();
    }
    
    public boolean hasActiveBossBar(Player player) {
        return activeBossBars.containsKey(player.getUniqueId());
    }
    
    public boolean isOperationRunning(Player player) {
        return hasActiveBossBar(player);
    }
} 