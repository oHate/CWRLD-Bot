package com.customwrld.bot.commandapi.button;

import com.customwrld.bot.Bot;
import com.customwrld.bot.util.config.Config;
import com.customwrld.bot.modules.Ticket;
import com.customwrld.bot.util.Util;
import com.customwrld.bot.util.enums.EmbedTemplate;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.ActionRow;
import net.dv8tion.jda.api.interactions.Component;
import net.dv8tion.jda.api.interactions.button.Button;

import java.util.*;

public class ButtonHandler extends ListenerAdapter {

    public static Map<String, Handler> buttonMap = new HashMap<>();
    private final Collection<Permission> allow = Arrays.asList(Permission.CREATE_INSTANT_INVITE, Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_HISTORY);
    private final Collection<Permission> deny = Arrays.asList(Permission.MANAGE_CHANNEL, Permission.MANAGE_PERMISSIONS, Permission.MANAGE_WEBHOOKS, Permission.MESSAGE_MANAGE, Permission.MESSAGE_TTS, Permission.MESSAGE_MENTION_EVERYONE, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_EXT_EMOJI);

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        Member member = event.getMember();
        Message message = event.getMessage();
        String[] components = event.getComponentId().split(":");

        if (message == null || member == null) {
            event.reply("> An error has occurred.").setEphemeral(true).queue();
            return;
        }

        if (components[0].equals("static")) {
            switch (components[1]) {
                case ("createTicket") -> createTicket(event);
                case ("deleteTicket") -> event.getTextChannel().delete().queue();
            }
        } else {
            Handler buttonHandler = buttonMap.get(event.getComponentId());

            if (buttonHandler != null) {
                buttonHandler.run(components[1], event);
            } else {
                disableButtons(event);
            }
        }
    }

    public void disableButtons(ButtonClickEvent event) {
        Message message = event.getMessage();
        if (message == null) {
            throw new IllegalStateException("Cannot update button for ephemeral messages! Discord does not provide enough information to perform the update.");
        } else {
            List<ActionRow> components = message.getActionRows();

            Iterator<ActionRow> rows = components.iterator();

            while (rows.hasNext()) {
                List<Component> row = rows.next().getComponents();
                ListIterator<Component> it = row.listIterator();

                while (it.hasNext()) {
                    Component component = it.next();
                    Button newButton = ((Button) component).asDisabled();

                    it.set(newButton);

                    if (row.isEmpty()) {
                        rows.remove();
                    }
                }
            }

            if (event.isAcknowledged()) {
                event.getHook().editMessageComponentsById(message.getId(), components).queue();
            } else {
                event.editComponents(components).queue();
            }
        }
    }

    private void createTicket(ButtonClickEvent event) {
        Bot bot = Bot.getBot();
        Config config = bot.getConfig();
        Guild guild = event.getGuild();
        Member member = event.getMember();
        User user = event.getUser();

        if (guild == null || member == null) {
            return;
        }

        if (Ticket.canCreateTicket(user.getId())) {
            guild.createTextChannel("ticket-" + user.getName(), guild.getCategoryById(config.getTicketCategory()))
                    .addMemberPermissionOverride(user.getIdLong(), allow, deny)
                    .queue(channel -> {
                        channel.getManager().sync().queue();
                        PermissionOverride permissionOverride = channel.getPermissionOverride(member);

                        if (permissionOverride == null) {
                            return;
                        }

                        permissionOverride.getManager().grant(allow).queue();
                        permissionOverride.getManager().deny(deny).queue();

                        channel.sendMessage("> <@" + user.getId() + ">").queue();
                        channel.sendMessage(Util.builder(user)
                                .addField("**Ticket has been created!**", "Support will be with your shortly!\n\n> Click the __**Delete Ticket**__ button to delete the ticket.", false)
                                .build())
                                .setActionRows(ActionRow.of(Button.danger("static:deleteTicket", "Delete Ticket")))
                                .queue(message -> {
                                    Ticket ticket = new Ticket(user.getId(), channel.getId());
                                    ticket.save();
                                    Ticket.getActiveTickets().put(ticket.getDiscordId(), ticket);
                                    event.replyEmbeds(EmbedTemplate.TICKET_CREATED.getEmbed(member.getUser(), guild.getId(), channel.getId(), message.getId())).setEphemeral(true).queue();
                                });
                    });
        } else {
            event.replyEmbeds(EmbedTemplate.TICKET_EXCEPTION.getEmbed(member.getUser())).setEphemeral(true).queue();
        }
    }

}
