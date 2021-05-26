package com.customwrld.bot.util;

import com.customwrld.bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class Util {

    public static final Pattern LINK_FILTER = Pattern.compile("((https?|ftp):\\/\\/)?([a-z0-9+!*(),;?&=.-]+(:[a-z0-9+!*(),;?&=.-]+)?@)?([a-z0-9\\-\\.]*)\\.(([a-z]{2,4})|([0-9]{1,3}\\.([0-9]{1,3})\\.([0-9]{1,3})))(:[0-9]{2,5})?(\\/([a-z0-9+%-]\\.?)+)*\\/?(\\?[a-z+&$_.-][a-z0-9;:@&%=+/.-]*)?(#[a-z_.-][a-z0-9+$%_.-]*)?");
    public static final Pattern ID_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    public static String getTime(OffsetDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"));
    }

    public static String getId(String userId) {
        return ID_PATTERN.matcher(userId).replaceAll("");
    }

    public static boolean hasStaffRole(Member member) {
        Role hasRole = member.getRoles().stream()
                .filter(role -> role.getId().equals(Bot.getInstance().getConfig().getStaffRole()))
                .findFirst()
                .orElse(null);

        return hasRole != null;
    }

    public static EmbedBuilder builder(User user) {
        return new EmbedBuilder()
                .setColor(Bot.getInstance().getConfig().getBotColor())
                .setFooter(user.getAsTag(), user.getAvatarUrl())
                .setTimestamp(OffsetDateTime.now());
    }

    public static void sendEmbed(TextChannel channel, User user, String title, String description) {
        channel.sendMessage(builder(user).setTitle(title).setDescription(description).build()).queue();
    }

}
