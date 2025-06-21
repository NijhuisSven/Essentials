package nl.nijhuissven.orbit.managers.worldedit;

import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UndoRedoManager {
    private final Map<UUID, Integer> undoStack = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> redoStack = new ConcurrentHashMap<>();
    
    public void storeUndo(Player player, Location loc1, Location loc2) {
        UUID playerId = player.getUniqueId();
        undoStack.put(playerId, undoStack.getOrDefault(playerId, 0) + 1);

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
        
        redoStack.put(playerId, redoCount - 1);
        undoStack.put(playerId, undoStack.getOrDefault(playerId, 0) + 1);
        
        player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT,
                "Redid last operation. <green>" + (redoCount - 1) + "<white> operations remaining."
        ));
        SoundUtils.playSuccessSound(player);
    }
    
    public int getUndoCount(Player player) {
        return undoStack.getOrDefault(player.getUniqueId(), 0);
    }
    
    public int getRedoCount(Player player) {
        return redoStack.getOrDefault(player.getUniqueId(), 0);
    }
} 