package nl.nijhuissven.orbit.managers.worldedit.handlers;

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

    // Configuration constants
    private static final float EDGE_THICKNESS = 0.05f;
    private static final Material WIREFRAME_MATERIAL = Material.LIME_STAINED_GLASS;
    private static final int BRIGHTNESS_LEVEL = 15;

    // Player data storage
    private final Map<UUID, List<BlockDisplay>> playerVisualizations = new ConcurrentHashMap<>();
    private final Map<UUID, SelectionData> playerSelections = new ConcurrentHashMap<>();

    private record SelectionData(Location pos1, Location pos2, Color color) {
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            
            SelectionData other = (SelectionData) obj;
            boolean sameOrder = Objects.equals(pos1, other.pos1) && 
                              Objects.equals(pos2, other.pos2) && 
                              Objects.equals(color, other.color);
            boolean reverseOrder = Objects.equals(pos1, other.pos2) && 
                                 Objects.equals(pos2, other.pos1) && 
                                 Objects.equals(color, other.color);
            
            return sameOrder || reverseOrder;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(pos1) + Objects.hashCode(pos2) + Objects.hashCode(color);
        }
    }

    public void displaySelection(Player player, Location pos1, Location pos2, Color color) {
        if (!isValidSelection(pos1, pos2)) {
            hideSelection(player);
            return;
        }

        UUID playerId = player.getUniqueId();
        SelectionData newSelection = new SelectionData(pos1, pos2, color);

        if (hasUnchangedSelection(playerId, newSelection)) {
            return;
        }

        clearPlayerVisualization(player);
        List<BlockDisplay> wireframe = createPlayerSpecificWireframe(player, pos1, pos2, color);
        
        playerSelections.put(playerId, newSelection);
        playerVisualizations.put(playerId, wireframe);
    }

    public void displaySelection(Player player, Location pos1, Location pos2) {
        displaySelection(player, pos1, pos2, Color.LIME);
    }

    public void hideSelection(Player player) {
        UUID playerId = player.getUniqueId();
        playerSelections.remove(playerId);
        
        List<BlockDisplay> entities = playerVisualizations.remove(playerId);
        if (entities != null) {
            entities.forEach(this::removeEntity);
        }
    }

    private List<BlockDisplay> createPlayerSpecificWireframe(Player player, Location pos1, Location pos2, Color color) {
        World world = pos1.getWorld();
        CuboidBounds bounds = calculateBounds(pos1, pos2);
        Location[] corners = calculateCornerPositions(world, bounds);
        
        List<BlockDisplay> wireframe = new ArrayList<>();
        
        // Add all 12 edges of the cuboid
        addHorizontalEdges(wireframe, corners, color);
        addVerticalEdges(wireframe, corners, color);
                
        return wireframe;
    }

    private BlockDisplay createEdge(Location start, Location end, Color color) {
        World world = start.getWorld();
        float length = (float) start.distance(end);
        
        EdgeTransform transform = calculateEdgeTransform(start, end, length);
        
        return world.spawn(start, BlockDisplay.class, display -> {
            display.setBlock(WIREFRAME_MATERIAL.createBlockData());
            display.setBrightness(new Display.Brightness(BRIGHTNESS_LEVEL, BRIGHTNESS_LEVEL));
            display.setGlowColorOverride(color);
            display.setGlowing(true);
            display.setTransformation(transform.toTransformation());
            
            display.setVisibleByDefault(true);
        });
    }

    private CuboidBounds calculateBounds(Location pos1, Location pos2) {
        return new CuboidBounds(
            Math.min(pos1.getBlockX(), pos2.getBlockX()),
            Math.max(pos1.getBlockX(), pos2.getBlockX()) + 1.0,
            Math.min(pos1.getBlockY(), pos2.getBlockY()),
            Math.max(pos1.getBlockY(), pos2.getBlockY()) + 1.0,
            Math.min(pos1.getBlockZ(), pos2.getBlockZ()),
            Math.max(pos1.getBlockZ(), pos2.getBlockZ()) + 1.0
        );
    }

    private Location[] calculateCornerPositions(World world, CuboidBounds bounds) {
        return new Location[]{
            new Location(world, bounds.minX, bounds.minY, bounds.minZ),
            new Location(world, bounds.maxX, bounds.minY, bounds.minZ),
            new Location(world, bounds.minX, bounds.maxY, bounds.minZ),
            new Location(world, bounds.maxX, bounds.maxY, bounds.minZ),
            new Location(world, bounds.minX, bounds.minY, bounds.maxZ),
            new Location(world, bounds.maxX, bounds.minY, bounds.maxZ),
            new Location(world, bounds.minX, bounds.maxY, bounds.maxZ),
            new Location(world, bounds.maxX, bounds.maxY, bounds.maxZ)
        };
    }

    private void addHorizontalEdges(List<BlockDisplay> wireframe, Location[] corners, Color color) {
        // Bottom face edges
        wireframe.add(createEdge(corners[0], corners[1], color)); // X-axis
        wireframe.add(createEdge(corners[0], corners[4], color)); // Z-axis
        wireframe.add(createEdge(corners[1], corners[5], color)); // Z-axis
        wireframe.add(createEdge(corners[4], corners[5], color)); // X-axis
        
        // Top face edges
        wireframe.add(createEdge(corners[2], corners[3], color)); // X-axis
        wireframe.add(createEdge(corners[2], corners[6], color)); // Z-axis
        wireframe.add(createEdge(corners[3], corners[7], color)); // Z-axis
        wireframe.add(createEdge(corners[6], corners[7], color)); // X-axis
    }


    private void addVerticalEdges(List<BlockDisplay> wireframe, Location[] corners, Color color) {
        wireframe.add(createEdge(corners[0], corners[2], color));
        wireframe.add(createEdge(corners[1], corners[3], color));
        wireframe.add(createEdge(corners[4], corners[6], color));
        wireframe.add(createEdge(corners[5], corners[7], color));
    }

    private EdgeTransform calculateEdgeTransform(Location start, Location end, float length) {
        Vector3f scale = new Vector3f(EDGE_THICKNESS);
        Vector3f translation = new Vector3f();

        if (start.getX() != end.getX()) {
            // X-axis edge
            scale.x = length;
            translation.set(0, -EDGE_THICKNESS / 2, -EDGE_THICKNESS / 2);
        } else if (start.getY() != end.getY()) {
            // Y-axis edge
            scale.y = length;
            translation.set(-EDGE_THICKNESS / 2, 0, -EDGE_THICKNESS / 2);
        } else {
            // Z-axis edge
            scale.z = length;
            translation.set(-EDGE_THICKNESS / 2, -EDGE_THICKNESS / 2, 0);
        }

        return new EdgeTransform(translation, scale);
    }

    private boolean isValidSelection(Location pos1, Location pos2) {
        return pos1 != null && pos2 != null && pos1.getWorld().equals(pos2.getWorld());
    }

    private boolean hasUnchangedSelection(UUID playerId, SelectionData newSelection) {
        return playerVisualizations.containsKey(playerId) && 
               newSelection.equals(playerSelections.get(playerId));
    }

    private void clearPlayerVisualization(Player player) {
        List<BlockDisplay> entities = playerVisualizations.remove(player.getUniqueId());
        if (entities != null) {
            entities.forEach(this::removeEntity);
        }
    }

    private void removeEntity(BlockDisplay entity) {
        if (entity.isValid()) {
            entity.remove();
        }
    }

    private record CuboidBounds(double minX, double maxX, double minY, double maxY, double minZ, double maxZ) {}

    private record EdgeTransform(Vector3f translation, Vector3f scale) {
        public Transformation toTransformation() {
            return new Transformation(translation, new Quaternionf(), scale, new Quaternionf());
        }
    }
} 