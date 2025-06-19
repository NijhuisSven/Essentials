package nl.nijhuissven.orbit.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.bukkit.Location;

import java.util.UUID;

@Data
@Accessors(fluent = true)
@Table(name = "homes")
@EqualsAndHashCode(callSuper = false)
public class HomeModel extends StormModel {

    @Column(name = "player_uuid")
    private UUID playerUuid;

    @Column(name = "name")
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

    public HomeModel() {
    }

    public HomeModel(UUID playerUuid, String name, Location location) {
        this.playerUuid = playerUuid;
        this.name = name;
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
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