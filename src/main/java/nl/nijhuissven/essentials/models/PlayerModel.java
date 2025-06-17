package nl.nijhuissven.essentials.models;

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
@Table(name = "players")
@EqualsAndHashCode(callSuper = false)
public class PlayerModel extends StormModel {

    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @Column(name = "name")
    private String name;

    @Column(name = "ignore_messages")
    private Boolean ignoreMessages;
    public PlayerModel() {
    }

    public PlayerModel(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.ignoreMessages = false;

    }
} 