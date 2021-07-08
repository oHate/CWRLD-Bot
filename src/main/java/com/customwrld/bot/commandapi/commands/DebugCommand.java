package com.customwrld.bot.commandapi.commands;

import com.customwrld.bot.Bot;
import com.customwrld.bot.commandapi.CommandContext;
import com.customwrld.bot.commandapi.CommandPermission;
import com.customwrld.bot.commandapi.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.interactions.ActionRow;
import net.dv8tion.jda.api.interactions.button.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class DebugCommand implements ICommand {

    // TODO: Fix permissions

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

        if(!member.getUser().getId().equals("836340297281699873")) {
            return;
        }

        this.sendVerificationMessage(ctx);
    }

    public void sendVerificationMessage(CommandContext ctx) {
        MessageEmbed body = new EmbedBuilder()
                .setColor(Bot.getBot().getConfig().getBotColor())
                .setTitle(":white_check_mark:  **DISCORD VERIFICATION**                                                     ** **")
                .setDescription("""
                        To be able to Interact with this discord server, CUSTOMWRLD
                        requires you to verify your Minecraft username.

                        > **How can I verify myself?**
                        To verify yourself simply log on to __CUSTOMWRLD.COM__ and type
                        /verify in the chatbox and you will be given a six-digit code. Type
                        the following code in this channel to verify yourself.
                        
                        > **Why do I need to do this?**
                        We want to ensure the safety of our server, and our
                        players. This also allows us to know who is who, and will
                        automatically sync your ranks to our discord server.
                        """)
                .setImage("https://media.discordapp.net/attachments/789175632340713523/827264383121883136/Customwrld.png").build();

        ctx.getChannel().sendMessage(body).queue();
    }

    public void sendSupportTicketMessage(CommandContext ctx) {
        MessageEmbed body = new EmbedBuilder()
                .setColor(Bot.getBot().getConfig().getBotColor())
                .setTitle(":tickets:  **CREATE A SUPPORT TICKET**                                                  ** **")
                .setDescription("""
                        Having any issues with the Discord or Server? Staff will get to you as
                        soon as possible, so please don't berate them with pings, thanks!

                        > Click the __**Create a Support Ticket**__ button to create a ticket.
                        """).build();

        ctx.getChannel().sendMessage(body).setActionRows(
                ActionRow.of(
                        Button.success("static:createTicket", "Create a Support Ticket")
                )
        ).queue();
    }

}
