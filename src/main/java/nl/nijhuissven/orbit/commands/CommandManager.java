package nl.nijhuissven.orbit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import lombok.RequiredArgsConstructor;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotions.AutoRegister;
import nl.nijhuissven.orbit.annotions.WorldEdit;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
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
                Orbit.instance().warpManager().getWarpNames()
            );

            // Register player completion
            commandManager.getCommandCompletions().registerCompletion("players", c -> {
                return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
            });

            // Register home completion
            commandManager.getCommandCompletions().registerCompletion("homes", c -> {
                if (c.getPlayer() != null) {
                    return Orbit.instance().homeManager().getHomeNames(c.getPlayer().getUniqueId());
                }
                return Arrays.asList();
            });

            // Register entity types completion
            commandManager.getCommandCompletions().registerCompletion("entitytypes", c -> {
                return Arrays.stream(EntityType.values())
                    .map(EntityType::name)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            });

            // Register worlds completion
            commandManager.getCommandCompletions().registerCompletion("worlds", c -> {
                return Bukkit.getWorlds().stream()
                    .map(World::getName)
                    .collect(Collectors.toList());
            });

            // Register materials completion
            commandManager.getCommandCompletions().registerCompletion("materials", c -> {
                return Arrays.stream(Material.values())
                    .filter(Material::isBlock)
                    .map(Material::name)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            });

            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> annotatedClasses = reflections.get(Scanners.SubTypes.of(Scanners.TypesAnnotated.with(AutoRegister.class)).asClass());

            int registeredCount = 0;
            for (Class<?> clazz : annotatedClasses) {
                if (BaseCommand.class.isAssignableFrom(clazz)) {
                    try {
                        // Check if this is a WorldEdit command and if WorldEdit plugin is enabled
                        WorldEdit worldEditAnnotation = clazz.getAnnotation(WorldEdit.class);
                        if (worldEditAnnotation != null && Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
                            CommandAlias alias = clazz.getAnnotation(CommandAlias.class);
                            String commandName = alias != null ? alias.value() : clazz.getSimpleName();
                            logger.info("Skipped WorldEdit command (WorldEdit plugin enabled): " + commandName);
                            continue;
                        }
                        
                        BaseCommand command = (BaseCommand) clazz.getDeclaredConstructor().newInstance();
                        commandManager.registerCommand(command);

                        CommandAlias alias = clazz.getAnnotation(CommandAlias.class);
                        String commandName = alias != null ? alias.value() : clazz.getSimpleName();
                        
                        logger.info("Registered Aikar command: " + commandName);
                        registeredCount++;
                    } catch (Exception e) {
                        logger.warning("Failed to register Aikar command: " + clazz.getSimpleName());
                        Orbit.logger().severe(e.getMessage());
                    }
                }
            }

            logger.info("Successfully registered " + registeredCount + " Aikar commands!");
        } catch (Exception e) {
            logger.severe("Failed to register commands: " + e.getMessage());
            Orbit.logger().severe(e.getMessage());
        }
    }
} 