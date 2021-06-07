package com.customwrld.bot.commandapi.commands;

import com.customwrld.bot.commandapi.CommandContext;
import com.customwrld.bot.commandapi.CommandPermission;
import com.customwrld.bot.commandapi.ICommand;
import com.customwrld.bot.util.Util;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {

    @Override
    public CommandPermission permission() {
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
                (ping) -> Util.sendEmbed(
                        ctx.getChannel(),
                        ctx.getAuthor(),
                        "**Bot Latency**",
                        ":hourglass_flowing_sand: ｜ **Rest Ping:** " + ping + "ms \n:desktop: ｜ **Gateway Ping:** " + jda.getGatewayPing() + "ms \n:signal_strength: ｜ **Atom Response Time:** 1ms"
                )
        );
    }
}
