package nl.nijhuissven.orbit.commands.worldedit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.annotions.WorldEdit;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@AutoRegister
@WorldEdit
@CommandAlias("/wand|wand")
@CommandPermission("orbit.worldedit.wand")
public class WandCommand extends BaseCommand {

    @Default
    public void onWand(Player player) {
        ItemStack wand = new ItemStack(Material.WOODEN_AXE);
        player.getInventory().addItem(wand);
        
        player.sendMessage(ChatUtils.prefixed(Prefix.WORLDEDIT, 
            "You received a WorldEdit wand! <gray>Left-click to set position 1, right-click to set position 2."));
        SoundUtils.playSuccessSound(player);
    }
}
