package com.customwrld.bot.commands;

import com.customwrld.bot.commandapi.CommandContext;
import com.customwrld.bot.commandapi.ICommand;
import com.customwrld.bot.util.Util;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;

public class PingCommand implements ICommand {

    @Override
    public Permission permission() {
        return null;
    }

    @Override
    public String commandName() {
        return "ping";
    }

    @Override
    public String usage() {
        return this.commandName();
    }

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> Util.sendEmbed(ctx.getChannel(), ctx.getAuthor(), "**Bot Latency**", ":hourglass_flowing_sand: **Rest Ping:** " + ping + "\n:desktop: **Gateway Ping:** " + jda.getGatewayPing())
        );
    }
}
