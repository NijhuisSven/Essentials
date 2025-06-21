package nl.nijhuissven.orbit.managers.worldedit;

import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.managers.worldedit.handlers.SelectionVisualizer;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class VisualizationManager {

    private static final Material WAND_ITEM = Material.WOODEN_AXE;
    private static final int UPDATE_INTERVAL_TICKS = 10;

    private final SelectionManager selectionManager;
    private final SelectionVisualizer visualizer;
    private final BossBarManager bossBarManager;

    private final Map<UUID, BukkitTask> activeTrackingTasks = new ConcurrentHashMap<>();

    public enum OperationType {
        SET(Color.ORANGE, "Set Operation"),
        REPLACE(Color.BLUE, "Replace Operation"),
        NONE(Color.LIME, "No Operation");

        private final Color color;
        private final String displayName;

        OperationType(Color color, String displayName) {
            this.color = color;
            this.displayName = displayName;
        }

        public Color getColor() {
            return color;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public VisualizationManager(SelectionManager selectionManager, SelectionVisualizer visualizer, BossBarManager bossBarManager) {
        this.selectionManager = selectionManager;
        this.visualizer = visualizer;
        this.bossBarManager = bossBarManager;
    }

    public void startTracking(Player player) {
        UUID playerId = player.getUniqueId();
        
        if (activeTrackingTasks.containsKey(playerId)) {
            return;
        }

        BukkitTask trackingTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isPlayerValid(player)) {
                    stopTracking(player);
                    return;
                }
                updateVisualization(player);
            }
        }.runTaskTimer(Orbit.instance(), 0L, UPDATE_INTERVAL_TICKS);

        activeTrackingTasks.put(playerId, trackingTask);
    }

    public void stopTracking(Player player) {
        UUID playerId = player.getUniqueId();
        
        BukkitTask task = activeTrackingTasks.remove(playerId);
        if (task != null) {
            task.cancel();
        }
        
        visualizer.hideSelection(player);
    }

    public void updateVisualization(Player player) {
        if (!selectionManager.hasSelection(player)) {
            visualizer.hideSelection(player);
            return;
        }

        if (!isHoldingWand(player)) {
            visualizer.hideSelection(player);
            return;
        }

        // Show visualization with appropriate color
        Location pos1 = selectionManager.getPos1(player);
        Location pos2 = selectionManager.getPos2(player);
        OperationType currentOperation = getCurrentOperation(player);
        
        visualizer.displaySelection(player, pos1, pos2, currentOperation.getColor());
    }

    public void setCurrentOperation(Player player, OperationType operationType) {
    }

    public void clearCurrentOperation(Player player) {

    }

    private boolean isPlayerValid(Player player) {
        return player.isOnline() && selectionManager.hasSelection(player);
    }

    private boolean isHoldingWand(Player player) {
        ItemStack heldItem = player.getInventory().getItemInMainHand();
        return heldItem.getType() == WAND_ITEM;
    }

    private OperationType getCurrentOperation(Player player) {
        OperationType operationType = bossBarManager.getCurrentOperation(player);
        return operationType != null ? operationType : OperationType.NONE;
    }
} 