package nl.nijhuissven.orbit.managers.worldedit;

import net.kyori.adventure.bossbar.BossBar;
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
    private final Map<UUID, String> progressTexts = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> totalBlockCounts = new ConcurrentHashMap<>();
    private final Map<UUID, VisualizationManager.OperationType> activeOperations = new ConcurrentHashMap<>();

    private VisualizationManager visualizationManager;

    public BossBarManager(VisualizationManager visualizationManager) {
        this.visualizationManager = visualizationManager;
    }

    public void setVisualizationManager(VisualizationManager visualizationManager) {
        this.visualizationManager = visualizationManager;
    }

    public BossBar createSetBlocksBossBar(Player player, Material material, int totalBlocks) {
        cancelExistingOperation(player);
        
        String progressText = buildSetProgressText(material);
        BossBar bossBar = createBossBar(player, progressText, totalBlocks, BossBar.Color.GREEN);
        
        storeOperationData(player, bossBar, progressText, totalBlocks, VisualizationManager.OperationType.SET);
        
        return bossBar;
    }

    public BossBar createReplaceBlocksBossBar(Player player, Material fromMaterial, Material toMaterial, int totalBlocks) {
        cancelExistingOperation(player);
        
        String progressText = buildReplaceProgressText(fromMaterial, toMaterial);
        BossBar bossBar = createBossBar(player, progressText, totalBlocks, BossBar.Color.BLUE);
        
        storeOperationData(player, bossBar, progressText, totalBlocks, VisualizationManager.OperationType.REPLACE);
        
        return bossBar;
    }

    public void updateProgress(Player player, int currentBlocks, int totalBlocks) {
        BossBar bossBar = activeBossBars.get(player.getUniqueId());
        String progressText = progressTexts.get(player.getUniqueId());
        
        if (bossBar == null || progressText == null) {
            return;
        }

        float progress = calculateProgress(currentBlocks, totalBlocks);
        int percentage = calculatePercentage(currentBlocks, totalBlocks);
        String updatedText = buildProgressText(progressText, currentBlocks, totalBlocks, percentage);
        
        updateBossBar(bossBar, updatedText, progress);
        updateVisualizationColor(player);
    }

    public void cleanupBossBar(Player player) {
        UUID playerId = player.getUniqueId();
        
        BossBar bossBar = activeBossBars.remove(playerId);
        progressTexts.remove(playerId);
        totalBlockCounts.remove(playerId);
        activeOperations.remove(playerId);
        
        if (bossBar != null) {
            player.hideBossBar(bossBar);
        }
        
        clearVisualizationColor(player);
    }

    public void cancelOperation(Player player) {
        UUID playerId = player.getUniqueId();
        
        BossBar bossBar = activeBossBars.remove(playerId);
        progressTexts.remove(playerId);
        totalBlockCounts.remove(playerId);
        activeOperations.remove(playerId);
        
        if (bossBar != null) {
            player.hideBossBar(bossBar);
            notifyOperationCancelled(player);
        }
        
        clearVisualizationColor(player);
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

    public VisualizationManager.OperationType getCurrentOperation(Player player) {
        return activeOperations.get(player.getUniqueId());
    }

    private void cancelExistingOperation(Player player) {
        if (hasActiveBossBar(player)) {
            cancelOperation(player);
            notifyPreviousOperationCancelled(player);
        }
    }

    private BossBar createBossBar(Player player, String progressText, int totalBlocks, BossBar.Color color) {
        String initialText = buildProgressText(progressText, 0, totalBlocks, 0);
        BossBar bossBar = BossBar.bossBar(
            ChatUtils.color(initialText),
            0.0f,
            color,
            BossBar.Overlay.PROGRESS
        );
        
        player.showBossBar(bossBar);
        return bossBar;
    }

    private void storeOperationData(Player player, BossBar bossBar, String progressText, 
                                  int totalBlocks, VisualizationManager.OperationType operationType) {
        UUID playerId = player.getUniqueId();
        
        activeBossBars.put(playerId, bossBar);
        progressTexts.put(playerId, progressText);
        totalBlockCounts.put(playerId, totalBlocks);
        activeOperations.put(playerId, operationType);
    }

    private String buildSetProgressText(Material material) {
        return "Setting blocks to <gray>" + material.name().toLowerCase() + "<white>. Progress: <gray>";
    }

    private String buildReplaceProgressText(Material fromMaterial, Material toMaterial) {
        return "Replacing <gray>" + fromMaterial.name().toLowerCase() + 
               "<white> with <gray>" + toMaterial.name().toLowerCase() + "<white>. Progress: <gray>";
    }

    private String buildProgressText(String baseText, int current, int total, int percentage) {
        return baseText + current + "<white>/<gray>" + total + " <white>(<gray>" + percentage + "%<white>)";
    }

    private float calculateProgress(int current, int total) {
        return (float) current / total;
    }

    private int calculatePercentage(int current, int total) {
        return (int) ((current * 100.0f) / total);
    }

    private void updateBossBar(BossBar bossBar, String text, float progress) {
        bossBar.name(ChatUtils.color(text));
        bossBar.progress(progress);
    }

    private void updateVisualizationColor(Player player) {
        if (visualizationManager != null) {
            VisualizationManager.OperationType operationType = activeOperations.get(player.getUniqueId());
            if (operationType != null) {
                visualizationManager.setCurrentOperation(player, operationType);
            }
        }
    }

    private void clearVisualizationColor(Player player) {
        if (visualizationManager != null) {
            visualizationManager.clearCurrentOperation(player);
        }
    }

    private void notifyOperationCancelled(Player player) {
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "<yellow>WorldEdit operation cancelled."
        ));
    }

    private void notifyPreviousOperationCancelled(Player player) {
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "<yellow>Previous operation cancelled due to new operation starting."
        ));
    }
} 