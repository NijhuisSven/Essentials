package nl.nijhuissven.essentials;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import lombok.experimental.Accessors;
import nl.nijhuissven.essentials.commands.gamemodeCommands;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@Getter
@Accessors(fluent = true)
public final class Essentials extends JavaPlugin {

    @Getter
    private static Logger logger;

    @Override
    public void onEnable() {
        Essentials.logger = getLogger();

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");

        commandManager.registerCommand(new gamemodeCommands());

        Essentials.logger.info("[Essentials] Enabling Essentials plugin...");
    }

    @Override
    public void onDisable() {
        Essentials.logger.info("[Essentials] Disabling Essentials plugin...");
    }
}
