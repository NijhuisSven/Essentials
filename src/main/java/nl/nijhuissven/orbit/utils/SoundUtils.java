package nl.nijhuissven.orbit.utils;

import nl.nijhuissven.orbit.Orbit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

    public static void playSound(Player player, Sound sound, float volume, float pitch) {
        if (Orbit.instance().playerManager().isSoundsEnabled(player.getUniqueId())) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static void playSuccessSound(Player player) {
        var config = Orbit.instance().globalConfiguration();
        playSound(player, config.successSound(), config.successVolume(), config.successPitch());
    }

    public static void playErrorSound(Player player) {
        var config = Orbit.instance().globalConfiguration();
        playSound(player, config.errorSound(), config.errorVolume(), config.errorPitch());
    }
} 