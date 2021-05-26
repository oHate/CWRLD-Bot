package com.customwrld.bot.listeners;

import com.customwrld.bot.Bot;
import com.customwrld.bot.config.Config;
import com.customwrld.bot.tickets.Ticket;
import com.customwrld.bot.tickets.TicketProcedure;
import com.customwrld.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class ReactionListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        Bot bot = Bot.getInstance();
        User user = event.getUser();
        User self = event.getJDA().getSelfUser();
        Member member = event.getMember();
        String userId = user.getId();
        String messageId = event.getMessageId();
        Config config = bot.getConfig();
        Guild guild = bot.getGuild();
        TicketProcedure procedure = TicketProcedure.getById(userId);

        if (procedure != null) {
            if (messageId.equals(procedure.getMessageId())) {
                String emoji = event.getReactionEmote().getEmoji();

                switch (emoji) {
                    case "U+2702": // Scissors
                        procedure.finish();
                        procedure.getTicket().delete();
                        event.getChannel().delete().queue();
                        break;
                    case "U+1F6AB": // No Entry Sign
                        procedure.finish();
                        event.getChannel().retrieveMessageById(messageId).queue(message -> message.delete().queue());
                        break;
                }
            }
        }

        for (Map.Entry<String, Ticket> ent : Ticket.getActiveTickets().entrySet()) {
            Ticket ticket = ent.getValue();
            if (messageId.equals(ticket.getReactionMessageId()) && user != self) {
                if (event.getReactionEmote().getAsCodepoints().equals("U+1F512")) { // Lock
                    if (TicketProcedure.canStartProcedure(userId, messageId)) {
                        event.getChannel().sendMessage(new EmbedBuilder()
                                .addField("**Ticket Menu**", ":scissors: Delete Ticket\n:no_entry_sign: Cancel", false)
                                .setColor(config.getBotColor())
                                .setTimestamp(OffsetDateTime.now())
                                .setFooter(user.getAsTag(), user.getAvatarUrl()).build()).queue(message -> {
                            message.addReaction("U+2702").queue(); // Scissors
                            message.addReaction("U+1F6AB").queue(); // No Entry Sign
                            new TicketProcedure(userId, message.getId(), ticket);
                        });
                    }
                }
                event.getReaction().removeReaction(user).queue();
            }
        }

        if (messageId.equals(config.getTicketMessage())) {
            if (event.getReactionEmote().getAsCodepoints().equals("U+1F3AB")) { // Ticket
                if (!self.getId().equals(userId)) {
                    if (Ticket.canCreateTicket(userId)) {
                        guild.createTextChannel("ticket-" + user.getName(), guild.getCategoryById(config.getTicketCategory()))
                                .addMemberPermissionOverride(member.getIdLong(), allow, deny)
                                .queue(channel -> {
                                    channel.getManager().sync().queue();
                                    PermissionOverride permissionOverride = channel.getPermissionOverride(member);
                                    permissionOverride.getManager().grant(allow).queue();
                                    permissionOverride.getManager().deny(deny).queue();

                                    channel.sendMessage("> <@" + userId + ">").queue();
                                    channel.sendMessage(Util.builder(user)
                                            .addField("**Ticket has been created!**", "Support will be with your shortly!\nTo close this ticket react with :lock:", false)
                                            .build())
                                            .queue(message -> {
                                        message.addReaction("U+1F512").queue(); // Lock
                                        Ticket ticket = new Ticket(userId, channel.getId(), message.getId());
                                        ticket.save();
                                        Ticket.getActiveTickets().put(ticket.getDiscordId(), ticket);
                                    });
                                });
                    }
                    event.getReaction().removeReaction(user).queue();
                }
            }
        }
    }

    private final Collection<Permission> allow = Arrays.asList(Permission.CREATE_INSTANT_INVITE, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_HISTORY);
    private final Collection<Permission> deny = Arrays.asList(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_WEBHOOKS, Permission.MESSAGE_MANAGE, Permission.MESSAGE_TTS, Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EXT_EMOJI);


}
