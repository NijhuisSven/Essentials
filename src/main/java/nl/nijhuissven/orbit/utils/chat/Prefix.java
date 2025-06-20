package nl.nijhuissven.orbit.utils.chat;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Prefix {

    GAMEMODE("<gradient:#FA90B0:#FA7499>⛏ ɢᴀᴍᴇᴍᴏᴅᴇ<dark_gray>｜<reset>"),
    FLY("<gradient:#38d9d6:#73B8E2>✈ ꜰʟɪɢʜᴛ<dark_gray>｜<reset>"),
    WEATHER("<gradient:#FEE440:#FFA500>☀ ᴡᴇᴀᴛʜᴇʀ<dark_gray>｜<reset>"),
    TIME("<gradient:#4A90E2:#1A237E>⌛ ᴛɪᴍᴇ<dark_gray>｜<reset>"),
    ENDERCHEST("<gradient:#C32CB3:#7C006F>🧪 ᴇɴᴅᴇʀᴄʜᴇsᴛ<dark_gray>｜<reset>"),
    SPEED("<gradient:#83c9d1:#376F75>⚡ sᴘᴇᴇᴅ<dark_gray>｜<reset>"),
    MESSAGES("<gradient:#FF9B54:#FF7754>✉ ᴍᴇssᴀɢᴇs<dark_gray>｜<reset>"),
    SETTINGS("<gradient:#9C27B0:#673AB7>🪓 sᴇᴛᴛɪɴɢs<dark_gray>｜<reset>"),
    INFO("<gradient:#61bb16:#33640A>ℹ ɪɴғᴏ<dark_gray>｜<reset>"),
    WARPS("<gradient:#F160AF:#99145C>⚗ ᴡᴀʀᴘs<dark_gray>｜<reset>"),
    HOMES("<gradient:#4CAF50:#2E7D32>🏠 ʜᴏᴍᴇs<dark_gray>｜<reset>"),
    TELEPORT("<gradient:#B8039B:#640254><bold>🌀</bold> ᴛᴇʟᴇᴘᴏʀᴛ<dark_gray>｜<reset>"),
    KILL("<gradient:#BE080F:#8F0E13>🗡 ᴋɪʟʟ<dark_gray>｜<reset>"),
    INVENTORY("<gradient:#FF6F00:#FF3D00>📦 ɪɴᴠᴇɴᴛᴏʀʏ<dark_gray>｜<reset>"),
    SERVER("<gradient:#00A8A8:#006969>☁ ѕᴇʀᴠᴇʀ<dark_gray>｜<reset>"),
    WORLDEDIT("<gradient:#4CAF50:#2E7D32>⚒ ᴡᴏʀʟᴅᴇᴅɪᴛ<dark_gray>｜<reset>");

    private final String prefix;

    public String get() {
        return prefix;
    }

}
