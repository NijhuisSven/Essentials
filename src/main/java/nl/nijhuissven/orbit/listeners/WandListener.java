package nl.nijhuissven.orbit.listeners;

import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.managers.worldedit.VisualizationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class WandListener implements Listener {

    private final VisualizationManager visualizationManager;

    public WandListener(VisualizationManager visualizationManager) {
        this.visualizationManager = visualizationManager;
    }

    private void scheduleUpdate(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                visualizationManager.updateVisibility(player);
            }
        }.runTask(Orbit.instance());
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        scheduleUpdate(event.getPlayer());
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        scheduleUpdate(event.getPlayer());
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        scheduleUpdate(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            scheduleUpdate((Player) event.getWhoClicked());
        }
    }
} 