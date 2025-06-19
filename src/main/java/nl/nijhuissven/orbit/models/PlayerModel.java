package nl.nijhuissven.orbit.models;

import com.craftmend.storm.api.StormModel;
import com.craftmend.storm.api.markers.Column;
import com.craftmend.storm.api.markers.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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

    @Column(name = "sounds_enabled")
    private Boolean soundsEnabled;

    @Column(name = "private_messages_enabled")
    private Boolean privateMessagesEnabled;

    public PlayerModel() {
    }

    public PlayerModel(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.soundsEnabled = true;
        this.privateMessagesEnabled = true;
    }

    public PlayerModel(UUID uuid, String name, boolean soundsEnabled, boolean privateMessagesEnabled) {
        this.uuid = uuid;
        this.name = name;
        this.soundsEnabled = soundsEnabled;
        this.privateMessagesEnabled = privateMessagesEnabled;
    }
} 