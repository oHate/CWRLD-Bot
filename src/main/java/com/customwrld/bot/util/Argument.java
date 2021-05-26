package com.customwrld.bot.util;

import java.util.regex.Pattern;

public class Argument {

    public static final Pattern MEMBER_MENTION = Pattern.compile("<@!?\\d+>");
    public static final Pattern ROLE_MENTION = Pattern.compile("<&\\d+>");
    public static final Pattern CHANNEL_MENTION = Pattern.compile("<#\\d+>");

}
