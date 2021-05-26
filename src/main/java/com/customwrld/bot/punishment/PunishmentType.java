package com.customwrld.bot.punishment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PunishmentType {

    BAN("Ban", "banned", "unbanned", true),
    MUTE("Mute", "muted", "unmuted", false),
    KICK("Kick", "kicked", null, false);

    private final String readable;
    private final String context;
    private final String undoContext;
    private final boolean ban;

}
