package com.customwrld.bot.commandapi.commands;

import com.customwrld.bot.Bot;
import com.customwrld.bot.commandapi.CommandContext;
import com.customwrld.bot.commandapi.CommandPermission;
import com.customwrld.bot.commandapi.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

public class DebugCommand implements ICommand {

    // TODO: Fix permissions

    @Override
    public CommandPermission permission() {
        return null;
    }

    @Override
    public String commandName() {
        return "debug";
    }

    @Override
    public String usage() {
        return this.commandName() + " [args]";
    }

    @Override
    public void handle(CommandContext ctx) {
        Member member = ctx.getMember();
        TextChannel channel = ctx.getChannel();

        if(!member.getUser().getId().equals("836340297281699873")) {
            return;
        }

        sendEmbed(ctx);
    }

    public void sendMessage(CommandContext ctx) {
        ctx.getChannel().sendMessage(String.join(" ", ctx.getArgs())).queue();
    }

    public void sendEmbed(CommandContext ctx) {
        ctx.getChannel().sendMessage(new EmbedBuilder().setDescription(String.join(" ", ctx.getArgs())).build()).queue();
    }

    public void genSupportTicketMessage(CommandContext ctx) {
        MessageEmbed header = new EmbedBuilder().setColor(Bot.getInstance().getConfig().getBotColor()).setImage("https://media.discordapp.net/attachments/789175632340713523/827264383121883136/Customwrld.png").build();

        MessageEmbed body = new EmbedBuilder()
                .setColor(Bot.getInstance().getConfig().getBotColor())
                .setTitle(":tickets:  **CREATE A SUPPORT TICKET**                                                  ** **")
                .setDescription("""
                        Having any issues with the Discord or Server? Staff will get to you as
                        soon as possible, so please don't berate them with pings, thanks!

                        > Click the __**Create a Support Ticket**__ button to create a ticket.
                        """).build();

        ctx.getChannel().sendMessage(header).queue((success) -> ctx.getChannel().sendMessage(body).setActionRows(
                ActionRow.of(
                        Button.success("static:createTicket", "Create a Support Ticket")
                )
        ).queue());
    }

}
