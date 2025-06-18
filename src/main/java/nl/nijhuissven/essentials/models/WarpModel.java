package nl.nijhuissven.essentials.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.bukkit.Location;

@Data
@Accessors(fluent = true)
@Table(name = "warps")
@EqualsAndHashCode(callSuper = false)
public class WarpModel extends StormModel {

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "world")
    private String world;

    @Column(name = "x")
    private Double x;

    @Column(name = "y")
    private Double y;

    @Column(name = "z")
    private Double z;

    @Column(name = "yaw")
    private Float yaw;

    @Column(name = "pitch")
    private Float pitch;

    @Column(name = "permission")
    private String permission;

    public WarpModel() {
    }

    public WarpModel(String name, Location location, String permission) {
        this.name = name;
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.permission = permission;
    }

    public Location toLocation() {
        return new Location(
            org.bukkit.Bukkit.getWorld(world),
            x,
            y,
            z,
            yaw,
            pitch
        );
    }
} 