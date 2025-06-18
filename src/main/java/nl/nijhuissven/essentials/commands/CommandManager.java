package nl.nijhuissven.essentials.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import lombok.RequiredArgsConstructor;
import nl.nijhuissven.essentials.Essentials;
import nl.nijhuissven.essentials.annotions.AutoRegister;
import nl.nijhuissven.essentials.managers.WarpManager;
import org.bukkit.GameMode;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CommandManager {
    private final PaperCommandManager commandManager;
    private final Logger logger;
    private final String basePackage;

    public void registerCommands() {
        try {
            // Register gamemode completions
            commandManager.getCommandCompletions().registerCompletion("gamemodes", c -> 
                Arrays.stream(GameMode.values())
                    .map(GameMode::name)
                    .collect(Collectors.toList())
            );

            // Register warp completion
            commandManager.getCommandCompletions().registerCompletion("warps", c ->
                Essentials.instance().warpManager().getWarpNames()
            );

            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> annotatedClasses = reflections.get(Scanners.SubTypes.of(Scanners.TypesAnnotated.with(AutoRegister.class)).asClass());

            int registeredCount = 0;
            for (Class<?> clazz : annotatedClasses) {
                if (BaseCommand.class.isAssignableFrom(clazz)) {
                    try {
                        BaseCommand command = (BaseCommand) clazz.getDeclaredConstructor().newInstance();
                        commandManager.registerCommand(command);

                        CommandAlias alias = clazz.getAnnotation(CommandAlias.class);
                        String commandName = alias != null ? alias.value() : clazz.getSimpleName();
                        
                        logger.info("Registered Aikar command: " + commandName);
                        registeredCount++;
                    } catch (Exception e) {
                        logger.warning("Failed to register Aikar command: " + clazz.getSimpleName());
                        Essentials.logger().severe(e.getMessage());
                    }
                }
            }

            logger.info("Successfully registered " + registeredCount + " Aikar commands!");
        } catch (Exception e) {
            logger.severe("Failed to register commands: " + e.getMessage());
            Essentials.logger().severe(e.getMessage());
        }
    }
} 