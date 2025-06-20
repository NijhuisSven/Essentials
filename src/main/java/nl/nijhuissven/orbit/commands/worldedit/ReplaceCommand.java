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
@CommandAlias("/replace|replace")
@CommandPermission("orbit.worldedit.replace")
public class ReplaceCommand extends BaseCommand {

    @Default
    @CommandCompletion("@materials @materials")
    public void onReplace(Player player, String fromMaterial, String toMaterial) {
        Material from;
        Material to;
        
        try {
            from = Material.valueOf(fromMaterial.toUpperCase());
            to = Material.valueOf(toMaterial.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT, 
                "<red>Invalid material!"));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        if (!from.isBlock() || !to.isBlock()) {
            player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT, 
                "<red>Both materials must be valid blocks!"));
            SoundUtils.playErrorSound(player);
            return;
        }
        
        Orbit.instance().worldEditManager().replaceBlocks(player, from, to);
    }
}
