package nl.nijhuissven.orbit.managers.worldedit;

import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class OperationManager {
    private final SelectionManager selectionManager;
    private final UndoRedoManager undoRedoManager;
    private final BossBarManager bossBarManager;

    public OperationManager(SelectionManager selectionManager, UndoRedoManager undoRedoManager, BossBarManager bossBarManager) {
        this.selectionManager = selectionManager;
        this.undoRedoManager = undoRedoManager;
        this.bossBarManager = bossBarManager;
    }
    
    public void setBlocks(Player player, Material material) {
        // Check if an operation is already running
        if (bossBarManager.isOperationRunning(player)) {
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>An operation is already running! Please wait for it to complete or cancel it first."
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        Location loc1 = selectionManager.getPos1(player);
        Location loc2 = selectionManager.getPos2(player);
        
        if (!BlockProcessor.validateSelection(player, loc1, loc2)) {
            return;
        }
        
        int totalBlocks = BlockProcessor.calculateTotalBlocks(loc1, loc2);
        
        if (!BlockProcessor.validateBlockLimit(player, totalBlocks)) {
            return;
        }
        
        // Store current blocks for undo
        undoRedoManager.storeUndo(player, loc1, loc2);
        
        // Create and show boss bar
        bossBarManager.createSetBlocksBossBar(player, material, totalBlocks);
        
        // Calculate bounds
        int[] bounds = BlockProcessor.calculateBounds(loc1, loc2);
        
        // Process in chunks to avoid lag
        final int[] blocksChanged = {0};
        final int[] processed = {0};
        final int chunkSize = 1000;
        
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "Starting operation on <green>" + totalBlocks + "<white> blocks..."
        ));
        
        BlockProcessor.processBlocksInChunks(player, loc1.getWorld(), bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], 
            (block) -> {
                if (block.getType() != material) {
                    block.setType(material);
                    return true;
                }
                return false;
            }, 
            blocksChanged, processed, totalBlocks, chunkSize,
            () -> {
                player.sendMessage(ChatUtils.prefixed(
                    Prefix.WORLDEDIT,
                    "Set <green>" + blocksChanged[0] + "<white> blocks to <green>" + material.name().toLowerCase()
                ));
                SoundUtils.playSuccessSound(player);
                // Hide boss bar
                bossBarManager.cleanupBossBar(player);
            },
            bossBarManager
        );
    }
    
    public void replaceBlocks(Player player, Material from, Material to) {
        // Check if an operation is already running
        if (bossBarManager.isOperationRunning(player)) {
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>An operation is already running! Please wait for it to complete or cancel it first."
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        Location loc1 = selectionManager.getPos1(player);
        Location loc2 = selectionManager.getPos2(player);
        
        if (!BlockProcessor.validateSelection(player, loc1, loc2)) {
            return;
        }
        
        int totalBlocks = BlockProcessor.calculateTotalBlocks(loc1, loc2);
        
        if (!BlockProcessor.validateBlockLimit(player, totalBlocks)) {
            return;
        }
        
        // Store current blocks for undo
        undoRedoManager.storeUndo(player, loc1, loc2);
        
        // Create and show boss bar
        bossBarManager.createReplaceBlocksBossBar(player, from, to, totalBlocks);
        
        // Calculate bounds
        int[] bounds = BlockProcessor.calculateBounds(loc1, loc2);
        
        // Process in chunks to avoid lag
        final int[] blocksChanged = {0};
        final int[] processed = {0};
        final int chunkSize = 1000;
        
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "Starting replacement on <green>" + totalBlocks + "<white> blocks..."
        ));
        
        BlockProcessor.processBlocksInChunks(player, loc1.getWorld(), bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5], 
            (block) -> {
                if (block.getType() == from) {
                    block.setType(to);
                    return true;
                }
                return false;
            }, 
            blocksChanged, processed, totalBlocks, chunkSize,
            () -> {
                player.sendMessage(ChatUtils.prefixed(
                    Prefix.WORLDEDIT,
                    "Replaced <green>" + blocksChanged[0] + "<white> blocks from <green>" + from.name().toLowerCase() + 
                    "<white> to <green>" + to.name().toLowerCase()
                ));
                SoundUtils.playSuccessSound(player);
                // Hide boss bar
                bossBarManager.cleanupBossBar(player);
            },
            bossBarManager
        );
    }
} 