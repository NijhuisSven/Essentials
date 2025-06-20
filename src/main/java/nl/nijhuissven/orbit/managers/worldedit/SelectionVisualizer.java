package nl.nijhuissven.orbit.managers.worldedit;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SelectionVisualizer {

    private record Selection(Location pos1, Location pos2) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Selection that = (Selection) o;
            return (Objects.equals(pos1, that.pos1) && Objects.equals(pos2, that.pos2)) ||
                   (Objects.equals(pos1, that.pos2) && Objects.equals(pos2, that.pos1));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(pos1) + Objects.hashCode(pos2);
        }
    }

    private final Map<UUID, List<BlockDisplay>> activeVisualizations = new ConcurrentHashMap<>();
    private final Map<UUID, Selection> activeSelections = new ConcurrentHashMap<>();
    private static final float LINE_THICKNESS = 0.05f;
    private static final Material VISUALIZATION_MATERIAL = Material.LIME_STAINED_GLASS;

    public void showSelection(Player player, Location pos1, Location pos2) {
        if (pos1 == null || pos2 == null || !pos1.getWorld().equals(pos2.getWorld())) {
            hideSelection(player);
            return;
        }

        UUID playerUUID = player.getUniqueId();
        Selection newSelection = new Selection(pos1, pos2);

        if (activeVisualizations.containsKey(playerUUID) && newSelection.equals(activeSelections.get(playerUUID))) {
            return;
        }

        hideSelection(player);

        List<BlockDisplay> displayEntities = new ArrayList<>();
        World world = pos1.getWorld();

        double minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        double minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        double minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        double maxX = Math.max(pos1.getBlockX(), pos2.getBlockX()) + 1.0;
        double maxY = Math.max(pos1.getBlockY(), pos2.getBlockY()) + 1.0;
        double maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ()) + 1.0;

        Location[] corners = {
            new Location(world, minX, minY, minZ), new Location(world, maxX, minY, minZ),
            new Location(world, minX, maxY, minZ), new Location(world, maxX, maxY, minZ),
            new Location(world, minX, minY, maxZ), new Location(world, maxX, minY, maxZ),
            new Location(world, minX, maxY, maxZ), new Location(world, maxX, maxY, maxZ)
        };
        
        displayEntities.add(createEdge(corners[0], corners[1]));
        displayEntities.add(createEdge(corners[2], corners[3]));
        displayEntities.add(createEdge(corners[4], corners[5]));
        displayEntities.add(createEdge(corners[6], corners[7]));
        
        displayEntities.add(createEdge(corners[0], corners[2]));
        displayEntities.add(createEdge(corners[1], corners[3]));
        displayEntities.add(createEdge(corners[4], corners[6]));
        displayEntities.add(createEdge(corners[5], corners[7]));
        
        displayEntities.add(createEdge(corners[0], corners[4]));
        displayEntities.add(createEdge(corners[1], corners[5]));
        displayEntities.add(createEdge(corners[2], corners[6]));
        displayEntities.add(createEdge(corners[3], corners[7]));

        activeSelections.put(playerUUID, newSelection);
        activeVisualizations.put(playerUUID, displayEntities);
    }

    public void hideSelection(Player player) {
        activeSelections.remove(player.getUniqueId());
        List<BlockDisplay> entities = activeVisualizations.remove(player.getUniqueId());
        if (entities != null) {
            entities.forEach(entity -> {
                if (entity.isValid()) entity.remove();
            });
        }
    }

    private BlockDisplay createEdge(Location p1, Location p2) {
        World world = p1.getWorld();
        float length = (float) p1.distance(p2);
        
        Vector3f scale = new Vector3f(LINE_THICKNESS);
        Vector3f translation = new Vector3f();

        if (p1.getX() != p2.getX()) {
            scale.x = length;
            translation.set(0, -LINE_THICKNESS / 2, -LINE_THICKNESS / 2);
        } else if (p1.getY() != p2.getY()) {
            scale.y = length;
            translation.set(-LINE_THICKNESS / 2, 0, -LINE_THICKNESS / 2);
        } else {
            scale.z = length;
            translation.set(-LINE_THICKNESS / 2, -LINE_THICKNESS / 2, 0);
        }

        return world.spawn(p1, BlockDisplay.class, (display) -> {
            display.setBlock(VISUALIZATION_MATERIAL.createBlockData());
            display.setBrightness(new Display.Brightness(15, 15));
            display.setGlowColorOverride(Color.LIME);
            display.setGlowing(true);
            display.setTransformation(new Transformation(translation, new Quaternionf(), scale, new Quaternionf()));
        });
    }
} 