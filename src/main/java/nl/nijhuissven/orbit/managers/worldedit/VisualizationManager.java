package nl.nijhuissven.orbit.managers.worldedit;

import nl.nijhuissven.orbit.Orbit;
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

    private final SelectionManager selectionManager;
    private final SelectionVisualizer selectionVisualizer;
    private final Map<UUID, BukkitTask> runningTasks = new ConcurrentHashMap<>();
    private static final Material WAND_MATERIAL = Material.WOODEN_AXE;

    public VisualizationManager(SelectionManager selectionManager, SelectionVisualizer selectionVisualizer) {
        this.selectionManager = selectionManager;
        this.selectionVisualizer = selectionVisualizer;
    }

    public void startTracking(Player player) {
        if (runningTasks.containsKey(player.getUniqueId())) return;

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !selectionManager.hasSelection(player)) {
                    stopTracking(player);
                    return;
                }
                updateVisibility(player);
            }
        }.runTaskTimer(Orbit.instance(), 0L, 10L);
        runningTasks.put(player.getUniqueId(), task);
    }

    public void stopTracking(Player player) {
        BukkitTask task = runningTasks.remove(player.getUniqueId());
        if (task != null) task.cancel();
        selectionVisualizer.hideSelection(player);
    }

    public void updateVisibility(Player player) {
        if (!selectionManager.hasSelection(player)) {
            selectionVisualizer.hideSelection(player);
            return;
        }

        ItemStack heldItem = player.getInventory().getItemInMainHand();
        if (heldItem.getType() == WAND_MATERIAL) {
            Location pos1 = selectionManager.getPos1(player);
            Location pos2 = selectionManager.getPos2(player);
            selectionVisualizer.showSelection(player, pos1, pos2);
        } else {
            selectionVisualizer.hideSelection(player);
        }
    }
} 