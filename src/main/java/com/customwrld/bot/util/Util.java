package com.customwrld.bot.util;

import com.customwrld.bot.Bot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

public class Util {

    public static final Pattern LINK_FILTER = Pattern.compile("((https?|ftp):\\/\\/)?([a-z0-9+!*(),;?&=.-]+(:[a-z0-9+!*(),;?&=.-]+)?@)?([a-z0-9\\-\\.]*)\\.(([a-z]{2,4})|([0-9]{1,3}\\.([0-9]{1,3})\\.([0-9]{1,3})))(:[0-9]{2,5})?(\\/([a-z0-9+%-]\\.?)+)*\\/?(\\?[a-z+&$_.-][a-z0-9;:@&%=+/.-]*)?(#[a-z_.-][a-z0-9+$%_.-]*)?");
    public static final Pattern ID_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

    public static String[] OWNERS = new String[]{"280750430643027968", "227172957695377409", "486609898147151882", "836340297281699873"};
    public static Role OWNER_ROLE;
    public static Role ADMIN_ROLE;
    public static Role MOD_ROLE;
    public static Role HELPER_ROLE;
    public static Role STAFF_ROLE;

    public static void initialize(Guild guild) {
        OWNER_ROLE = guild.getRoleById("826982155909201950");
        ADMIN_ROLE = guild.getRoleById("826982163270467585");
        MOD_ROLE = guild.getRoleById("826982164163461130");
        HELPER_ROLE = guild.getRoleById("826982164901920778");
        STAFF_ROLE = guild.getRoleById("826982162900975666");
    }

    public static String getTime(OffsetDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy"));
    }

    public static String getId(String userId) {
        return ID_PATTERN.matcher(userId).replaceAll("");
    }

    public static boolean hasStaffRole(Member member) {
        Role hasRole = member.getRoles().stream()
                .filter(role -> role.getId().equals(Bot.getBot().getConfig().getStaffRole()))
                .findFirst()
                .orElse(null);

        return hasRole != null;
    }

    public static String generateId(Member member) {
        return UUID.randomUUID() + ":" + member.getId();
    }

    public static EmbedBuilder builder(User user) {
        return new EmbedBuilder()
                .setColor(Bot.getBot().getConfig().getBotColor())
                .setFooter(user.getAsTag(), user.getAvatarUrl())
                .setTimestamp(OffsetDateTime.now());
    }

    public static void sendEmbed(TextChannel channel, User user, String title, String description) {
        channel.sendMessage(builder(user).setTitle(title).setDescription(description).build()).queue();
    }

    public static String readableBytes(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %cB", value / 1024.0, ci.current());
    }

}
