package com.customwrld.bot.util.timer.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimerType {

    COUNTDOWN(0),
    STOPWATCH(1);

    private final int ordinal;

    public static TimerType fromString(String id) {
        return TimerType.valueOf(id.toUpperCase());
    }
}