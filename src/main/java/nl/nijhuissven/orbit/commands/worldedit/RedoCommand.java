package nl.nijhuissven.orbit.commands.worldedit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.annotations.WorldEdit;
import org.bukkit.entity.Player;

@AutoRegister
@WorldEdit
@CommandAlias("/redo|redo")
@CommandPermission("orbit.worldedit.redo")
public class RedoCommand extends BaseCommand {

    @Default
    public void onRedo(Player player) {
        Orbit.instance().worldEditManager().redo(player);
    }
}
