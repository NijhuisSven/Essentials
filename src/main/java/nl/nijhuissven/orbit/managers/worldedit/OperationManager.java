package nl.nijhuissven.orbit.managers.worldedit;

import nl.nijhuissven.orbit.managers.worldedit.handlers.BlockProcessor;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class OperationManager {

    private static final int CHUNK_SIZE = 1000;
    private static final int MAX_BLOCKS = 100000;

    private final SelectionManager selectionManager;
    private final UndoRedoManager undoRedoManager;
    private final BossBarManager bossBarManager;
    private final VisualizationManager visualizationManager;

    public OperationManager(SelectionManager selectionManager, UndoRedoManager undoRedoManager, 
                          BossBarManager bossBarManager, VisualizationManager visualizationManager) {
        this.selectionManager = selectionManager;
        this.undoRedoManager = undoRedoManager;
        this.bossBarManager = bossBarManager;
        this.visualizationManager = visualizationManager;
    }

    public void setBlocks(Player player, Material material) {
        if (!validateOperationStart(player)) {
            return;
        }

        Location pos1 = selectionManager.getPos1(player);
        Location pos2 = selectionManager.getPos2(player);
        
        if (!validateSelection(player, pos1, pos2)) {
            return;
        }

        int totalBlocks = BlockProcessor.calculateTotalBlocks(pos1, pos2);
        
        if (!validateBlockLimit(player, totalBlocks)) {
            return;
        }

        executeSetOperation(player, pos1, pos2, material, totalBlocks);
    }

    public void replaceBlocks(Player player, Material fromMaterial, Material toMaterial) {
        if (!validateOperationStart(player)) {
            return;
        }

        Location pos1 = selectionManager.getPos1(player);
        Location pos2 = selectionManager.getPos2(player);
        
        if (!validateSelection(player, pos1, pos2)) {
            return;
        }

        int totalBlocks = BlockProcessor.calculateTotalBlocks(pos1, pos2);
        
        if (!validateBlockLimit(player, totalBlocks)) {
            return;
        }

        executeReplaceOperation(player, pos1, pos2, fromMaterial, toMaterial, totalBlocks);
    }

    private boolean validateOperationStart(Player player) {
        if (bossBarManager.isOperationRunning(player)) {
            notifyOperationAlreadyRunning(player);
            return false;
        }
        return true;
    }

    private boolean validateSelection(Player player, Location pos1, Location pos2) {
        return BlockProcessor.validateSelection(player, pos1, pos2);
    }

    private boolean validateBlockLimit(Player player, int totalBlocks) {
        return BlockProcessor.validateBlockLimit(player, totalBlocks);
    }

    private void executeSetOperation(Player player, Location pos1, Location pos2, Material material, int totalBlocks) {
        undoRedoManager.storeUndo(player, pos1, pos2);
        bossBarManager.createSetBlocksBossBar(player, material, totalBlocks);
        
        // Calculate operation bounds
        int[] bounds = BlockProcessor.calculateBounds(pos1, pos2);
        
        OperationContext context = new OperationContext(player, totalBlocks);
        
        BlockProcessor.processBlocksInChunks(
            player, pos1.getWorld(), bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5],
            block -> processSetBlock(block, material),
            context.blocksChanged, context.processed, totalBlocks, CHUNK_SIZE,
            () -> completeSetOperation(player, material, context.blocksChanged[0]),
            bossBarManager
        );
    }

    private void executeReplaceOperation(Player player, Location pos1, Location pos2, 
                                       Material fromMaterial, Material toMaterial, int totalBlocks) {
        // Prepare for operation
        undoRedoManager.storeUndo(player, pos1, pos2);
        bossBarManager.createReplaceBlocksBossBar(player, fromMaterial, toMaterial, totalBlocks);
        
        // Calculate operation bounds
        int[] bounds = BlockProcessor.calculateBounds(pos1, pos2);
        
        OperationContext context = new OperationContext(player, totalBlocks);
        
        BlockProcessor.processBlocksInChunks(
            player, pos1.getWorld(), bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5],
            block -> processReplaceBlock(block, fromMaterial, toMaterial),
            context.blocksChanged, context.processed, totalBlocks, CHUNK_SIZE,
            () -> completeReplaceOperation(player, fromMaterial, toMaterial, context.blocksChanged[0]),
            bossBarManager
        );
    }

    private boolean processSetBlock(org.bukkit.block.Block block, Material material) {
        if (block.getType() != material) {
            block.setType(material);
            return true;
        }
        return false;
    }

    private boolean processReplaceBlock(org.bukkit.block.Block block, Material fromMaterial, Material toMaterial) {
        if (block.getType() == fromMaterial) {
            block.setType(toMaterial);
            return true;
        }
        return false;
    }

    private void completeSetOperation(Player player, Material material, int blocksChanged) {
        notifyOperationComplete(player, "Set", blocksChanged, material.name().toLowerCase());
        bossBarManager.cleanupBossBar(player);
    }

    private void completeReplaceOperation(Player player, Material fromMaterial, Material toMaterial, int blocksChanged) {
        String message = String.format("Replaced %d blocks from %s to %s", 
                                     blocksChanged, fromMaterial.name().toLowerCase(), toMaterial.name().toLowerCase());
        notifyOperationComplete(player, message, blocksChanged, null);
        bossBarManager.cleanupBossBar(player);
    }

    private void notifyOperationAlreadyRunning(Player player) {
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "<red>An operation is already running! Please wait for it to complete or cancel it first."
        ));
        SoundUtils.playErrorSound(player);
    }

    private void notifyOperationComplete(Player player, String operationType, int blocksChanged, String materialName) {
        String message;
        if (materialName != null) {
            message = String.format("%s <green>%d<white> blocks to <green>%s", operationType, blocksChanged, materialName);
        } else {
            message = String.format("%s <green>%d<white> blocks", operationType, blocksChanged);
        }
        
        player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT, message));
        SoundUtils.playSuccessSound(player);
    }

    private static class OperationContext {
        final int[] blocksChanged = {0};
        final int[] processed = {0};
        
        OperationContext(Player player, int totalBlocks) {
        }
    }
} 