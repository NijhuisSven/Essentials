package nl.nijhuissven.orbit.managers.worldedit;

import lombok.Getter;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@Getter
public class WorldEditManager {
    private final SelectionManager selectionManager;
    private final UndoRedoManager undoRedoManager;
    private final BossBarManager bossBarManager;
    private final OperationManager operationManager;
    private final SelectionVisualizer selectionVisualizer;
    private final VisualizationManager visualizationManager;

    public WorldEditManager() {
        this.selectionManager = new SelectionManager();
        this.selectionVisualizer = new SelectionVisualizer();
        this.undoRedoManager = new UndoRedoManager();
        this.bossBarManager = new BossBarManager();
        this.visualizationManager = new VisualizationManager(this.selectionManager, this.selectionVisualizer);
        this.operationManager = new OperationManager(this.selectionManager, this.undoRedoManager, this.bossBarManager);
    }

    // Selection methods
    public void setPos1(Player player, Location location) {
        selectionManager.setPos1(player, location);
        visualizationManager.startTracking(player);
    }
    
    public void setPos2(Player player, Location location) {
        selectionManager.setPos2(player, location);
        visualizationManager.startTracking(player);
    }
    
    public boolean hasSelection(Player player) {
        return selectionManager.hasSelection(player);
    }
    
    public Location getPos1(Player player) {
        return selectionManager.getPos1(player);
    }
    
    public Location getPos2(Player player) {
        return selectionManager.getPos2(player);
    }
    
    public void clearSelection(Player player) {
        selectionManager.clearSelection(player);
        visualizationManager.stopTracking(player);
    }

    // Undo/Redo methods
    public void undo(Player player) {
        undoRedoManager.undo(player);
    }
    
    public void redo(Player player) {
        undoRedoManager.redo(player);
    }
    
    public int getUndoCount(Player player) {
        return undoRedoManager.getUndoCount(player);
    }
    
    public int getRedoCount(Player player) {
        return undoRedoManager.getRedoCount(player);
    }
    
    // Block operations
    public void setBlocks(Player player, Material material) {
        operationManager.setBlocks(player, material);
    }
    
    public void replaceBlocks(Player player, Material from, Material to) {
        operationManager.replaceBlocks(player, from, to);
    }
    
    // Boss bar management
    public void cleanupBossBar(Player player) {
        bossBarManager.cleanupBossBar(player);
    }
    
    public void cleanup(Player player) {
        cleanupBossBar(player);
        visualizationManager.stopTracking(player);
    }
    
    public void cancelOperation(Player player) {
        if (!bossBarManager.isOperationRunning(player)) {
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>No operation is currently running!"
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        bossBarManager.cancelOperation(player);
        SoundUtils.playSuccessSound(player);
    }
    
    public boolean isOperationRunning(Player player) {
        return bossBarManager.isOperationRunning(player);
    }
    
    public int getActiveBossBarCount() {
        return bossBarManager.getActiveBossBarCount();
    }
    
    public boolean hasActiveBossBar(Player player) {
        return bossBarManager.hasActiveBossBar(player);
    }
} 