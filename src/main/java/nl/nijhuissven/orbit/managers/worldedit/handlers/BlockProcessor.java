package nl.nijhuissven.orbit.managers.worldedit.handlers;

import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.managers.worldedit.BossBarManager;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.function.Function;

public class BlockProcessor {
    
    public static void processBlocksInChunks(Player player, World world, int minX, int maxX, int minY, int maxY, int minZ, int maxZ,
                                           Function<Block, Boolean> blockProcessor,
                                           int[] blocksChanged, int[] processed, int totalBlocks, int chunkSize,
                                           Runnable onComplete, BossBarManager bossBarManager) {
        
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
                            for (int i = Math.max(0, chunkStart); i < chunkEnd; i++) {
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
                            
                            if (bossBarManager.hasActiveBossBar(player)) {
                                bossBarManager.updateProgress(player, chunkEnd, totalBlocks);
                            }
                            
                            if (chunkEnd == totalBlocks) {
                                onComplete.run();
                            }
                        }, (chunkEnd / chunkSize) * 2L);
                    }
                }
            }
        }
    }
    
    public static boolean validateSelection(Player player, Location loc1, Location loc2) {
        if (loc1 == null || loc2 == null) {
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>You need to select two positions first!"
            ));
            SoundUtils.playErrorSound(player);
            return false;
        }
        
        if (!loc1.getWorld().equals(loc2.getWorld())) {
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>Both positions must be in the same world!"
            ));
            SoundUtils.playErrorSound(player);
            return false;
        }
        
        return true;
    }
    
    public static boolean validateBlockLimit(Player player, int totalBlocks) {
        if (totalBlocks > 100000) { // 100k blocks limit
            player.sendMessage(ChatUtils.prefixed(
                Prefix.WORLDEDIT,
                "<red>Selection too large! Maximum 100,000 blocks allowed."
            ));
            SoundUtils.playErrorSound(player);
            return false;
        }
        return true;
    }
    
    public static int calculateTotalBlocks(Location loc1, Location loc2) {
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        
        return (maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1);
    }
    
    public static int[] calculateBounds(Location loc1, Location loc2) {
        int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
        int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
        int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
        int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
        int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
        int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
        
        return new int[]{minX, maxX, minY, maxY, minZ, maxZ};
    }
} 