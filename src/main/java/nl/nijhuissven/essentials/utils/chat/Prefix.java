package nl.nijhuissven.essentials.utils.chat;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Prefix {

    GAMEMODE("<gradient:#FA90B0:#FA7499>ɢᴀᴍᴇᴍᴏᴅᴇ<dark_gray>｜<reset>"),
    SERVER("<gradient:#FF3333:#FF3333>ѕᴇʀᴠᴇʀ<dark_gray>｜<reset>");

    private final String prefix;

    public String get() {
        return prefix;
    }

}
