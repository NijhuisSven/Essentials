package nl.nijhuissven.orbit.listeners;

import nl.nijhuissven.orbit.Orbit;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WorldEditListener implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        
        if (item == null || item.getType() != Material.WOODEN_AXE) {
            return;
        }
        
        if (!player.hasPermission("orbit.worldedit.wand")) {
            return;
        }
        
        // Check if WorldEditManager is enabled
        if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
            return;
        }
        
        event.setCancelled(true);
        
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            // Set position 1
            Orbit.instance().worldEditManager().setPos1(player, event.getClickedBlock().getLocation());
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // Set position 2
            Orbit.instance().worldEditManager().setPos2(player, event.getClickedBlock().getLocation());
        }
    }
} 