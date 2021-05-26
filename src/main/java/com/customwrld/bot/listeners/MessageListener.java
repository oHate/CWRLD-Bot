package com.customwrld.bot.listeners;

import com.customwrld.bot.Bot;
import com.customwrld.bot.config.Config;
import com.customwrld.bot.util.Util;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    // TODO: Add filtered words

    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent event) {
        Bot bot = Bot.getInstance();
        Message message = event.getMessage();
        Member member = event.getMember();

        if (member != null) {
            if (Bot.getInstance().getConfig().getFilteredChannels().contains(event.getChannel().getId())) {
                if (isLink(message)) {
                    if (!Util.hasStaffRole(member)) {
                        message.delete().queue();
                        event.getChannel().sendMessage("> <@" + member.getUser().getId() + ">").queue();
                        Util.sendEmbed(event.getChannel(), member.getUser(), "**Filtered Link**", "Links are not allowed in this channel.");
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        Bot bot = Bot.getInstance();
        Message message = event.getMessage();
        User user = event.getAuthor();
        Member member = event.getMember();
        Config config = bot.getConfig();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        if (member != null) {
            if (config.getFilteredChannels().contains(event.getChannel().getId())) {
                if (isLink(message)) {
                    if (!Util.hasStaffRole(member)) {
                        message.delete().queue();
                        event.getChannel().sendMessage("> <@" + member.getUser().getId() + ">").queue();
                        Util.sendEmbed(event.getChannel(), member.getUser(), "**Filtered Link**", "Links are not allowed in this channel.");
                        return;
                    }
                }
            }

            String raw = event.getMessage().getContentRaw();

            if (raw.startsWith(config.getBotPrefix())) {
                bot.getCommandManager().handle(event);
            }
        }
    }

    public boolean isLink(Message message) {
        return Util.LINK_FILTER.matcher(message.getContentRaw()).find();
    }

}
