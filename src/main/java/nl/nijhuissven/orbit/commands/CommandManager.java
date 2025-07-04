package nl.nijhuissven.orbit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import lombok.RequiredArgsConstructor;
import nl.nijhuissven.orbit.Orbit;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.annotations.Punishments;
import nl.nijhuissven.orbit.annotations.WorldEdit;
import nl.nijhuissven.orbit.utils.TimeEntry;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.util.Arrays;
import java.util.List;
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
            registerCompletions();
            registerContext();

            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> annotatedClasses = reflections.get(Scanners.SubTypes.of(Scanners.TypesAnnotated.with(AutoRegister.class)).asClass());

            int registeredCount = 0;
            for (Class<?> clazz : annotatedClasses) {
                if (BaseCommand.class.isAssignableFrom(clazz)) {
                    try {
                        if (shouldRegisterCommand(clazz)) {
                            BaseCommand command = (BaseCommand) clazz.getDeclaredConstructor().newInstance();
                            commandManager.registerCommand(command);

                            CommandAlias alias = clazz.getAnnotation(CommandAlias.class);
                            String commandName = alias != null ? alias.value() : clazz.getSimpleName();
                            
                            logger.info("Registered command: " + commandName);
                            registeredCount++;
                        }
                    } catch (Exception e) {
                        logger.warning("Failed to register command: " + clazz.getSimpleName());
                        Orbit.logger().severe(e.getMessage());
                    }
                }
            }

            logger.info("Successfully registered " + registeredCount + " commands!");
        } catch (Exception e) {
            logger.severe("Failed to register commands: " + e.getMessage());
            Orbit.logger().severe(e.getMessage());
        }
    }

    private boolean shouldRegisterCommand(Class<?> commandClass) {

        Punishments punishmentsAnnotation = commandClass.getAnnotation(Punishments.class);
        if (punishmentsAnnotation != null) {
            if (Bukkit.getPluginManager().isPluginEnabled("litebans")) {
                CommandAlias alias = commandClass.getAnnotation(CommandAlias.class);
                String commandName = alias != null ? alias.value() : commandClass.getSimpleName();
                logger.info("Skipped WorldEdit command (external WorldEdit plugin enabled): " + commandName);
                return false;
            }

            // Check if WorldEdit module is enabled in configuration
            if (!Orbit.instance().moduleConfiguration().isModuleEnabled("Punishments")) {
                CommandAlias alias = commandClass.getAnnotation(CommandAlias.class);
                String commandName = alias != null ? alias.value() : commandClass.getSimpleName();
                //logger.info("Skipped WorldEdit command (module disabled): " + commandName);
                return false;
            }
        }

        // Check if this is a WorldEdit command and if WorldEdit plugin is enabled
        WorldEdit worldEditAnnotation = commandClass.getAnnotation(WorldEdit.class);
        if (worldEditAnnotation != null) {
            if (Bukkit.getPluginManager().isPluginEnabled("WorldEdit")) {
                CommandAlias alias = commandClass.getAnnotation(CommandAlias.class);
                String commandName = alias != null ? alias.value() : commandClass.getSimpleName();
                logger.info("Skipped WorldEdit command (external WorldEdit plugin enabled): " + commandName);
                return false;
            }
            
            // Check if WorldEdit module is enabled in configuration
            if (!Orbit.instance().moduleConfiguration().isModuleEnabled("WorldEdit")) {
                CommandAlias alias = commandClass.getAnnotation(CommandAlias.class);
                String commandName = alias != null ? alias.value() : commandClass.getSimpleName();
                //logger.info("Skipped WorldEdit command (module disabled): " + commandName);
                return false;
            }
        }

        // Check for other module-specific commands based on package structure
        String packageName = commandClass.getPackageName();
        if (packageName.contains("warp") && !Orbit.instance().moduleConfiguration().isModuleEnabled("Warps")) {
            CommandAlias alias = commandClass.getAnnotation(CommandAlias.class);
            String commandName = alias != null ? alias.value() : commandClass.getSimpleName();
            //logger.info("Skipped Warps command (module disabled): " + commandName);
            return false;
        }
        
        if (packageName.contains("home") && !Orbit.instance().moduleConfiguration().isModuleEnabled("Homes")) {
            CommandAlias alias = commandClass.getAnnotation(CommandAlias.class);
            String commandName = alias != null ? alias.value() : commandClass.getSimpleName();
            //logger.info("Skipped Homes command (module disabled): " + commandName);
            return false;
        }

        return true;
    }

    private void registerContext() {
        commandManager.getCommandContexts().registerContext(TimeEntry.class, (context) -> {
            TimeEntry timeEntry = TimeEntry.from(context.popFirstArg());
            if (timeEntry == null) {
                throw new InvalidCommandArgument("Not a valid TimeEntry");
            }
            return timeEntry;
        });
    }

    private void registerCompletions() {
        // Register gamemode completions
        commandManager.getCommandCompletions().registerCompletion("gamemodes", c -> 
            Arrays.stream(GameMode.values())
                .map(GameMode::name)
                .collect(Collectors.toList())
        );

        // Register warp completion (only if Warps module is enabled)
        if (Orbit.instance().moduleConfiguration().isModuleEnabled("Warps")) {
            commandManager.getCommandCompletions().registerCompletion("warps", c ->
                Orbit.instance().warpManager().getWarpNames()
            );
        }

        // Register player completion
        commandManager.getCommandCompletions().registerCompletion("players", c -> {
            return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        });

        // Register home completion (only if Homes module is enabled)
        if (Orbit.instance().moduleConfiguration().isModuleEnabled("Homes")) {
            commandManager.getCommandCompletions().registerCompletion("homes", c -> {
                if (c.getPlayer() != null) {
                    return Orbit.instance().homeManager().getHomeNames(c.getPlayer().getUniqueId());
                }
                return List.of();
            });
        }

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
    }
} 