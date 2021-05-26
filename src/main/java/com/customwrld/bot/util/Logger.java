package com.customwrld.bot.util;

import com.customwrld.bot.util.enums.LogType;

public class Logger {

    public static void log(LogType type, String log) {
        System.out.println(type.format(log));
    }


}
