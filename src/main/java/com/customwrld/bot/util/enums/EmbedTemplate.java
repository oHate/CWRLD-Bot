package com.customwrld.bot.util.enums;

import com.customwrld.bot.Bot;
import com.customwrld.bot.util.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.text.MessageFormat;

@Getter
@AllArgsConstructor
public enum EmbedTemplate {
    WRONG_USAGE("**Invalid Command Usage**", "Correct Usage: " + Bot.getBot().getConfig().getBotPrefix() + "{0}"),
    PERMISSION_EXCEPTION("**Permission Exception**", "You do not have permission to use this command."),
    TICKET_EXCEPTION("**Ticket Exception**", "You cannot create a new ticket."),
    TICKET_CREATED("**Ticket Created**", "You have successfully created a new ticket!\n\n__[Click Here to visit your Ticket!](https://discord.com/channels/{0}/{1}/{2})__"),
    BUTTON_EXCEPTION("**Permission Exception**", "You do not have permission to use this button."),
    BUTTON_EXPIRED("**Expired Button**", "The button you have triggered has expired."),
    USER_EXCEPTION("**User Exception**", "The provided user does not exist."),
    PUNISHMENT_EXCEPTION("**Punishment Exception**", "The provided punishment does not exist."),
    MEMBER_EXCEPTION("**Invalid Member**", "The provided member is not in the guild."),
    CHANNEL_EXCEPTION("**Invalid Channel**", "The provided channel is not a valid channel."),
    DURATION_EXCEPTION("**Duration Exception**", "The provided duration is not valid.");

    private final String title;
    private final String description;

    public void send(TextChannel channel, User user, Object... objects) {
        Util.sendEmbed(channel, user, title, new MessageFormat(description).format(objects));
    }

    public MessageEmbed getEmbed(User user, Object... objects) {
        return Util.builder(user)
                .setTitle(title)
                .setDescription(new MessageFormat(description).format(objects)).build();
    }

}
