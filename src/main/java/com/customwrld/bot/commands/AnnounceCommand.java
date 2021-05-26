package com.customwrld.bot.commands;

import com.customwrld.bot.commandapi.CommandContext;
import com.customwrld.bot.commandapi.ICommand;
import com.customwrld.bot.util.Util;
import com.customwrld.bot.util.enums.EmbedTemplate;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class AnnounceCommand implements ICommand {

    @Override
    public Permission permission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public String commandName() {
        return "announce";
    }

    @Override
    public String usage() {
        return this.commandName() + " [title] %s% [description]";
    }

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        User author = ctx.getAuthor();
        List<String> args = ctx.getArgs();

        if(!args.contains("%s%") || args.size() < 3) {
            EmbedTemplate.WRONG_USAGE.send(channel, author, this.usage());
            return;
        }

        String[] arguments = String.join(" ", args).split("%s%");

        Util.sendEmbed(channel, author, arguments[0], arguments[1].replaceAll("\\\\n", "\n"));
        channel.sendMessage("@everyone").queue(message -> message.delete().queue());
    }
}
