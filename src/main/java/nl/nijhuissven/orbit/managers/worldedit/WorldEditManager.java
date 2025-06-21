package nl.nijhuissven.orbit.managers.worldedit;

import lombok.Getter;
import nl.nijhuissven.orbit.managers.worldedit.handlers.SelectionVisualizer;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Main manager for WorldEdit functionality.
 * Coordinates all WorldEdit operations and provides a unified interface.
 */
@Getter
public class WorldEditManager {

    // Core managers
    private final SelectionManager selectionManager;
    private final UndoRedoManager undoRedoManager;
    private final BossBarManager bossBarManager;
    private final OperationManager operationManager;
    private final SelectionVisualizer selectionVisualizer;
    private final VisualizationManager visualizationManager;

    public WorldEditManager() {
        // Initialize managers in dependency order
        this.selectionManager = new SelectionManager();
        this.selectionVisualizer = new SelectionVisualizer();
        this.undoRedoManager = new UndoRedoManager();
        
        // Create with temporary null reference to handle circular dependency
        this.bossBarManager = new BossBarManager(null);
        this.visualizationManager = new VisualizationManager(
            this.selectionManager, 
            this.selectionVisualizer, 
            this.bossBarManager
        );
        
        // Set the visualization manager reference
        this.bossBarManager.setVisualizationManager(this.visualizationManager);
        
        // Create operation manager with all dependencies
        this.operationManager = new OperationManager(
            this.selectionManager, 
            this.undoRedoManager, 
            this.bossBarManager, 
            this.visualizationManager
        );
    }

    // ==================== SELECTION MANAGEMENT ====================

    /**
     * Sets the first position of a player's selection.
     */
    public void setPos1(Player player, Location location) {
        selectionManager.setPos1(player, location);
        visualizationManager.startTracking(player);
    }
    
    /**
     * Sets the second position of a player's selection.
     */
    public void setPos2(Player player, Location location) {
        selectionManager.setPos2(player, location);
        visualizationManager.startTracking(player);
    }
    
    /**
     * Checks if a player has a complete selection.
     */
    public boolean hasSelection(Player player) {
        return selectionManager.hasSelection(player);
    }
    
    /**
     * Gets the first position of a player's selection.
     */
    public Location getPos1(Player player) {
        return selectionManager.getPos1(player);
    }
    
    /**
     * Gets the second position of a player's selection.
     */
    public Location getPos2(Player player) {
        return selectionManager.getPos2(player);
    }
    
    /**
     * Clears a player's selection.
     */
    public void clearSelection(Player player) {
        selectionManager.clearSelection(player);
        visualizationManager.stopTracking(player);
    }

    // ==================== UNDO/REDO MANAGEMENT ====================

    /**
     * Undoes the last operation for a player.
     */
    public void undo(Player player) {
        undoRedoManager.undo(player);
    }
    
    /**
     * Redoes the last undone operation for a player.
     */
    public void redo(Player player) {
        undoRedoManager.redo(player);
    }
    
    /**
     * Gets the number of operations that can be undone.
     */
    public int getUndoCount(Player player) {
        return undoRedoManager.getUndoCount(player);
    }
    
    /**
     * Gets the number of operations that can be redone.
     */
    public int getRedoCount(Player player) {
        return undoRedoManager.getRedoCount(player);
    }
    
    // ==================== BLOCK OPERATIONS ====================

    /**
     * Sets all blocks in the selection to the specified material.
     */
    public void setBlocks(Player player, Material material) {
        operationManager.setBlocks(player, material);
    }
    
    /**
     * Replaces all blocks of the specified material with another material.
     */
    public void replaceBlocks(Player player, Material from, Material to) {
        operationManager.replaceBlocks(player, from, to);
    }
    
    // ==================== OPERATION MANAGEMENT ====================

    /**
     * Cancels a player's current operation.
     */
    public void cancelOperation(Player player) {
        if (!bossBarManager.isOperationRunning(player)) {
            notifyNoOperationRunning(player);
            return;
        }
        
        bossBarManager.cancelOperation(player);
        SoundUtils.playSuccessSound(player);
    }
    
    /**
     * Checks if a player has an operation running.
     */
    public boolean isOperationRunning(Player player) {
        return bossBarManager.isOperationRunning(player);
    }
    
    /**
     * Gets the number of active boss bars across all players.
     */
    public int getActiveBossBarCount() {
        return bossBarManager.getActiveBossBarCount();
    }
    
    /**
     * Checks if a player has an active boss bar.
     */
    public boolean hasActiveBossBar(Player player) {
        return bossBarManager.hasActiveBossBar(player);
    }

    // ==================== CLEANUP ====================

    /**
     * Cleans up all resources for a player.
     */
    public void cleanup(Player player) {
        bossBarManager.cleanupBossBar(player);
        visualizationManager.stopTracking(player);
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Notifies a player that no operation is currently running.
     */
    private void notifyNoOperationRunning(Player player) {
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "<red>No operation is currently running!"
        ));
        SoundUtils.playErrorSound(player);
    }
} 