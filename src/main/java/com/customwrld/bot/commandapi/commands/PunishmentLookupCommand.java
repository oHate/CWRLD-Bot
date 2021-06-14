package com.customwrld.bot.commandapi.commands;

import com.customwrld.bot.commandapi.CommandContext;
import com.customwrld.bot.commandapi.CommandPermission;
import com.customwrld.bot.commandapi.ICommand;
import com.customwrld.bot.flame.Punishment;
import com.customwrld.bot.util.Table;
import com.customwrld.bot.util.TimeUtil;
import com.customwrld.bot.util.Util;
import com.customwrld.bot.util.enums.EmbedTemplate;
import com.sun.management.OperatingSystemMXBean;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;

public class PunishmentLookupCommand implements ICommand {

    @Override
    public CommandPermission permission() {
        return new CommandPermission(CommandPermission.Type.PERMISSION, Permission.ADMINISTRATOR);
    }

    @Override
    public String commandName() {
        return "punishmentlookup";
    }

    @Override
    public String usage() {
        return this.commandName() + " [id]";
    }

    @Override
    public void handle(CommandContext ctx) {
        String[] args = ctx.getArgs();
        User author = ctx.getAuthor();
        Member member = ctx.getMember();
        TextChannel channel = ctx.getChannel();

        if(args.length < 1) {
            EmbedTemplate.WRONG_USAGE.send(channel, author, this.usage());
            return;
        }

        String punishmentId = args[0];

        Punishment punishment = Punishment.getById(punishmentId);

        if(punishment == null) {
            EmbedTemplate.PUNISHMENT_EXCEPTION.send(channel, author);
            return;
        }

//        Profile;

//        String profileTable = Table.of(new String[][]{
//                new String[]{"Player UUID", }, // TODO: CACHE RESPONSE TIME SO IT CAN BE USED IN PING COMMAND
//                new String[]{"Online Players", players + "/1000"},
//                new String[]{"Uptime", TimeUtil.millisToRoundedTime(payload.getUptime())},
//                new String[]{"CPU Usage", new DecimalFormat("#0.00").format(payload.getUsedCpu() * 100) + "% of 100%"}, // TODO: DEBUG CPU IT SOMETIMES DISPLAYS 0
//                new String[]{"RAM Usage", Util.readableBytes(payload.getUsedMemory()) + " of " + Util.readableBytes(ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class).getTotalMemorySize());

        EmbedBuilder builder = Util.builder(author)
                .setTitle("Player Punishment Information")
                .setDescription("__**Player Profile Information**__");
    }

}
