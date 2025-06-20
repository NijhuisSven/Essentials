package nl.nijhuissven.orbit.managers;

import lombok.Getter;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WorldEditManager {
    private final Map<UUID, Location> pos1 = new ConcurrentHashMap<>();
    private final Map<UUID, Location> pos2 = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> undoStack = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> redoStack = new ConcurrentHashMap<>();
    
    public void setPos1(Player player, Location location) {
        pos1.put(player.getUniqueId(), location);
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "Position 1 set to <green>" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()
        ));
        SoundUtils.playSuccessSound(player);
    }
    
    public void setPos2(Player player, Location location) {
        pos2.put(player.getUniqueId(), location);
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "Position 2 set to <green>" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()
        ));
        SoundUtils.playSuccessSound(player);
    }
    
    public boolean hasSelection(Player player) {
        return pos1.containsKey(player.getUniqueId()) && pos2.containsKey(player.getUniqueId());
    }
    
    public Location getPos1(Player player) {
        return pos1.get(player.getUniqueId());
    }
    
    public Location getPos2(Player player) {
        return pos2.get(player.getUniqueId());
    }

    public void setBlocks(Player player, Material material) {
        if (!hasSelection(player)) {
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>You need to select two positions first!"
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        Location loc1 = getPos1(player);
        Location loc2 = getPos2(player);
        
        if (!loc1.getWorld().equals(loc2.getWorld())) {
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>Both positions must be in the same world!"
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        // Store current blocks for undo
        storeUndo(player, loc1, loc2);
        
        // Calculate bounds
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        
        int totalBlocks = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
        
        // Check if operation is too large
        if (totalBlocks > 100000) { // 100k blocks limit
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>Selection too large! Maximum 100,000 blocks allowed."
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        // Process in chunks to avoid lag
        final int[] blocksChanged = {0};
        final int[] processed = {0};
        final int chunkSize = 1000;
        
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "Starting operation on <green>" + totalBlocks + "<white> blocks..."
        ));
        
        processBlocksInChunks(player, loc1.getWorld(), minX, maxX, minY, maxY, minZ, maxZ, 
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
                    chat.Prefix.WORLDEDIT,
                    "Set <green>" + blocksChanged[0] + "<white> blocks to <green>" + material.name().toLowerCase()
                ));
                SoundUtils.playSuccessSound(player);
            }
        );
    }
    
    public void replaceBlocks(Player player, Material from, Material to) {
        if (!hasSelection(player)) {
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>You need to select two positions first!"
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        Location loc1 = getPos1(player);
        Location loc2 = getPos2(player);
        
        if (!loc1.getWorld().equals(loc2.getWorld())) {
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>Both positions must be in the same world!"
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        // Store current blocks for undo
        storeUndo(player, loc1, loc2);
        
        // Calculate bounds
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        
        int totalBlocks = (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
        
        // Check if operation is too large
        if (totalBlocks > 100000) { // 100k blocks limit
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>Selection too large! Maximum 100,000 blocks allowed."
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        // Process in chunks to avoid lag
        final int[] blocksChanged = {0};
        final int[] processed = {0};
        final int chunkSize = 1000;
        
        player.sendMessage(ChatUtils.prefixed(
            Prefix.WORLDEDIT,
            "Starting replacement on <green>" + totalBlocks + "<white> blocks..."
        ));
        
        processBlocksInChunks(player, loc1.getWorld(), minX, maxX, minY, maxY, minZ, maxZ, 
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
            }
        );
    }
    
    private void storeUndo(Player player, Location loc1, Location loc2) {
        // For now, just increment the undo counter
        // In a full implementation, you'd store the actual block states
        UUID playerId = player.getUniqueId();
        undoStack.put(playerId, undoStack.getOrDefault(playerId, 0) + 1);
        // Clear redo stack when new operation is performed
        redoStack.remove(playerId);
    }
    
    public void undo(Player player) {
        UUID playerId = player.getUniqueId();
        int undoCount = undoStack.getOrDefault(playerId, 0);
        
        if (undoCount <= 0) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT,
                "<red>Nothing to undo!"
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        // For now, just decrement the counter
        // In a full implementation, you'd restore the actual block states
        undoStack.put(playerId, undoCount - 1);
        redoStack.put(playerId, redoStack.getOrDefault(playerId, 0) + 1);
        
        player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT,
            "Undid last operation. <green>" + (undoCount - 1) + "<white> operations remaining."
        ));
        SoundUtils.playSuccessSound(player);
    }
    
    public void redo(Player player) {
        UUID playerId = player.getUniqueId();
        int redoCount = redoStack.getOrDefault(playerId, 0);
        
        if (redoCount <= 0) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT, "<red>Nothing to redo!"
            ));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        // For now, just decrement the counter
        // In a full implementation, you'd restore the actual block states
        redoStack.put(playerId, redoCount - 1);
        undoStack.put(playerId, undoStack.getOrDefault(playerId, 0) + 1);
        
        player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT,
                "Redid last operation. <green>" + (redoCount - 1) + "<white> operations remaining."
        ));
        SoundUtils.playSuccessSound(player);
    }
    
    private void processBlocksInChunks(Player player, World world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ,
                                     java.util.function.Function<Block, Boolean> blockProcessor,
                                     int[] blocksChanged, int[] processed, int totalBlocks, int chunkSize,
                                     Runnable onComplete) {
        
        final int[] currentChunk = {0};
        
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    final int finalX = x;
                    final int finalY = y;
                    final int finalZ = z;
                    
                    currentChunk[0]++;
                    
                    if (currentChunk[0] % chunkSize == 0 || currentChunk[0] == totalBlocks) {
                        final int chunkEnd = currentChunk[0];
                        final int chunkStart = chunkEnd - chunkSize;
                        
                        Orbit.instance().getServer().getScheduler().runTaskLater(Orbit.instance(), () -> {
                            // Process this chunk
                            for (int i = Math.max(0, chunkStart); i < chunkEnd; i++) {
                                // Calculate coordinates from index
                                int tempX = minX + (i / ((maxY - minY + 1) * (maxZ - minZ + 1)));
                                int tempY = minY + ((i % ((maxY - minY + 1) * (maxZ - minZ + 1))) / (maxZ - minZ + 1));
                                int tempZ = minZ + (i % (maxZ - minZ + 1));
                                
                                if (tempX <= maxX && tempY <= maxY && tempZ <= maxZ) {
                                    Block block = world.getBlockAt(tempX, tempY, tempZ);
                                    if (blockProcessor.apply(block)) {
                                        blocksChanged[0]++;
                                    }
                                    processed[0]++;
                                }
                            }
                            
                            // Progress message
                            if (chunkEnd % (chunkSize * 5) == 0 || chunkEnd == totalBlocks) {
                                player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT,
                                    "Progress: <green>" + chunkEnd + "<white>/<green>" + totalBlocks + "<white> blocks processed"
                                ));
                            }
                            
                            // Complete
                            if (chunkEnd == totalBlocks) {
                                onComplete.run();
                            }
                        }, (chunkEnd / chunkSize) * 2L); // Spread out over time
                    }
                }
            }
        }
    }
} 