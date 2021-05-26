package com.customwrld.bot.util.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
@AllArgsConstructor
public enum LogType {

    READY("[\u001B[33mREADY\u001B[0m] {0}"),
    COMMAND("[\u001B[95mCOMMAND\u001B[0m] {0}"),
    INFO("[\u001B[36mINFO\u001B[0m] {0}"),
    WARNING("[\u001B[91mWARNING\u001B[0m] {0}"),
    ERROR("\u001B[7;31m[ERROR] {0}\u001B[0m");

    private final String string;

    public String format(Object... objects) {
        return new MessageFormat(string).format(objects);
    }

}
