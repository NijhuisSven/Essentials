package nl.nijhuissven.orbit.commands.worldedit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.annotions.WorldEdit;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@AutoRegister
@WorldEdit
@CommandAlias("/set|set")
@CommandPermission("orbit.worldedit.set")
public class SetCommand extends BaseCommand {

    @Default
    @CommandCompletion("@materials")
    public void onSet(Player player, String materialName) {
        Material material;
        try {
            material = Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT, 
                "<red>Invalid material: <white>" + materialName));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        if (!material.isBlock()) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT, 
                "<red>That's not a valid block material!"));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        Orbit.instance().worldEditManager().setBlocks(player, material);
    }
}
