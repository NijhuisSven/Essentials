package nl.nijhuissven.orbit.commands.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import nl.nijhuissven.orbit.annotations.AutoRegister;
import nl.nijhuissven.orbit.utils.SoundUtils;
import nl.nijhuissven.orbit.utils.chat.ChatUtils;
import nl.nijhuissven.orbit.utils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.*;

@AutoRegister
@CommandAlias("killall")
@CommandPermission("orbit.killall")
@Description("Kill entities with various options")
public class KillAllCommand extends BaseCommand {

    @Default
    @Description("Kill all entities in current world")
    public void onKillAll(Player player) {
        // Kill all mobs in current world
        int killed = killEntitiesInWorld(player.getWorld(), null, -1);
        player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "Killed <#BE080F>" + killed + "<white> entities in <#BE080F>" + player.getWorld().getName()));
        SoundUtils.playSuccessSound(player);
    }

    @Default
    @CommandCompletion("@entitytypes")
    @Description("Kill specific entity type")
    public void onKillAllType(Player player, String entityType) {
        EntityType type = getEntityType(entityType);
        if (type == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "<red>Invalid entity type: <white>" + entityType));
            SoundUtils.playErrorSound(player);
            return;
        }

        int killed = killEntitiesInWorld(player.getWorld(), type, -1);
        player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "Killed <#BE080F>" + killed + "<white> " + type.name().toLowerCase() + " in <#BE080F>" + player.getWorld().getName()));
        SoundUtils.playSuccessSound(player);
    }

    @Default
    @CommandCompletion("@entitytypes @worlds")
    @Description("Kill specific entity type in specific world")
    public void onKillAllTypeWorld(Player player, String entityType, String worldName) {
        EntityType type = getEntityType(entityType);
        if (type == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "<red>Invalid entity type: <white>" + entityType));
            SoundUtils.playErrorSound(player);
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "<red>World not found: <white>" + worldName));
            SoundUtils.playErrorSound(player);
            return;
        }

        int killed = killEntitiesInWorld(world, type, -1);
        player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "Killed <#BE080F>" + killed + "<white> " + type.name().toLowerCase() + " in <#BE080F>" + world.getName()));
        SoundUtils.playSuccessSound(player);
    }

    @Default
    @CommandCompletion("@entitytypes @range:1:1000")
    @Description("Kill specific entity type within radius")
    public void onKillAllTypeRadius(Player player, String entityType, int radius) {
        EntityType type = getEntityType(entityType);
        if (type == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "<red>Invalid entity type: <white>" + entityType));
            SoundUtils.playErrorSound(player);
            return;
        }

        if (radius <= 0) {
            player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "<red>Radius must be positive!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        int killed = killEntitiesInRadius(player, type, radius);
        player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "Killed <#BE080F>" + killed + "<white> " + type.name().toLowerCase() + " within <#BE080F>" + radius + "<white> blocks"));
        SoundUtils.playSuccessSound(player);
    }

    @Default
    @CommandCompletion("@entitytypes @range:1:1000 @worlds")
    @Description("Kill specific entity type in specific world within radius")
    public void onKillAllTypeRadiusWorld(Player player, String entityType, int radius, String worldName) {
        EntityType type = getEntityType(entityType);
        if (type == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "<red>Invalid entity type: <white>" + entityType));
            SoundUtils.playErrorSound(player);
            return;
        }

        if (radius <= 0) {
            player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "<red>Radius must be positive!"));
            SoundUtils.playErrorSound(player);
            return;
        }

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "<red>World not found: <white>" + worldName));
            SoundUtils.playErrorSound(player);
            return;
        }

        int killed = killEntitiesInRadiusWorld(player, type, radius, world);
        player.sendMessage(ChatUtils.prefixed(Prefix.KILL, "Killed <#BE080F>" + killed + "<white> " + type.name().toLowerCase() + " within <#BE080F>" + radius + "<white> blocks in <#BE080F>" + world.getName()));
        SoundUtils.playSuccessSound(player);
    }

    private int killEntitiesInWorld(World world, EntityType type, int radius) {
        int killed = 0;
        for (Entity entity : world.getEntities()) {
            if (shouldKillEntity(entity, type)) {
                entity.remove();
                killed++;
            }
        }
        return killed;
    }

    private int killEntitiesInRadius(Player player, EntityType type, int radius) {
        int killed = 0;
        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), radius, radius, radius)) {
            if (shouldKillEntity(entity, type)) {
                entity.remove();
                killed++;
            }
        }
        return killed;
    }

    private int killEntitiesInRadiusWorld(Player player, EntityType type, int radius, World world) {
        int killed = 0;
        for (Entity entity : world.getNearbyEntities(player.getLocation(), radius, radius, radius)) {
            if (shouldKillEntity(entity, type)) {
                entity.remove();
                killed++;
            }
        }
        return killed;
    }

    private boolean shouldKillEntity(Entity entity, EntityType type) {
        // Don't kill players
        if (entity instanceof Player) {
            return false;
        }

        // If no specific type specified, kill all mobs
        if (type == null) {
            return entity instanceof LivingEntity;
        }

        // Kill specific entity type
        return entity.getType() == type;
    }

    private EntityType getEntityType(String name) {
        try {
            return EntityType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
} 