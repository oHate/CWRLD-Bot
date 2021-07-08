//package com.customwrld.bot.commandapi.commands;
//
//import com.customwrld.bot.commandapi.CommandContext;
//import com.customwrld.bot.commandapi.CommandPermission;
//import com.customwrld.bot.commandapi.ICommand;
//import com.customwrld.bot.util.Table;
//import com.customwrld.bot.util.Util;
//import com.customwrld.bot.util.enums.EmbedTemplate;
//import com.customwrld.commonlib.modules.profile.Profile;
//import com.customwrld.commonlib.modules.punishment.Punishment;
//import com.customwrld.commonlib.modules.rank.Rank;
//import com.customwrld.commonlib.util.TimeUtil;
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.Permission;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.TextChannel;
//import net.dv8tion.jda.api.entities.User;
//
//import java.util.Date;
//
//public class LookupCommand implements ICommand {
//
//    @Override
//    public CommandPermission permission() {
//        return null; // new CommandPermission(CommandPermission.Type.PERMISSION, Permission.ADMINISTRATOR);
//    }
//
//    @Override
//    public String commandName() {
//        return "lookup";
//    }
//
//    @Override
//    public String usage() {
//        return this.commandName() + " [id]";
//    }
//
//    @Override
//    public void handle(CommandContext ctx) {
//        String[] args = ctx.getArgs();
//        Member member = ctx.getMember();
//        User author = ctx.getAuthor();
//        TextChannel channel = ctx.getChannel();
//
//        if(!member.getUser().getId().equals("836340297281699873")) {
//            return;
//        }
//
//        if (args.length < 1) {
//            EmbedTemplate.WRONG_USAGE.send(channel, author, this.usage());
//            return;
//        }
//
//        String punishmentId = args[0];
//
//        Punishment punishment = Punishment.getPunishment(punishmentId.toUpperCase());
//
//        if (punishment == null) {
//            System.out.println("Punishment was NULL");
//            EmbedTemplate.PUNISHMENT_EXCEPTION.send(channel, author);
//            return;
//        }
//
//        Profile profile = Profile.getProfile(null, punishment.getPlayerUUID());
//
//        if (profile == null) {
//            System.out.println("Profile was NULL");
//            // ERROR
//            return;
//        }
//
//        Rank rank = null; // Rank.getRank(profile.api().getActiveGrant().getRankUUID());
//
//        if (rank == null) {
//            System.out.println("Rank was NULL");
//            // ERROR
//            return;
//        }
//
////        User user = null;
//
////        if(profile.getDiscordId() != null) {
////            ctx.getJDA().retrieveUserById(profile.getDiscordId()).queue(retrievedUser -> {
////
////            });
////        }
//
//        Profile adderProfile = null;
//
//        if (punishment.getAddedByUUID() != null) {
//            adderProfile = Profile.getProfile(null, punishment.getAddedByUUID());
//        }
//
//        Profile removerProfile = null;
//
//        if (punishment.isRemoved() && punishment.getRemovedByUUID() != null) {
//            removerProfile = Profile.getProfile(null, punishment.getAddedByUUID());
//        }
//
//        EmbedBuilder builder = Util.builder(author)
//                .setTitle("Player Punishment Information")
//                .setDescription("__**Player Profile Information**__\n```" +
//                        Table.of(new String[][]{
//                                new String[]{"Player UUID", String.valueOf(profile.getId())},
//                                new String[]{"Player Name", profile.getName()},
//                                new String[]{"Rank", rank.getName()},
//                                new String[]{"Discord ID", "Not Linked"}, // TODO
//                                new String[]{"Discord Name", "Not Linked"}, // TODO
//                                new String[]{"First Login", TimeUtil.dateToString(new Date(profile.getFirstSeen()), true)},
//                        })
//                        + "```\n__**Punishment Information**__\n```" +
//                        Table.of(new String[][]{
//                                new String[]{"Punishment ID", punishment.getId()},
//                                new String[]{"Punishment Type", punishment.getType().getReadable()},
//                                new String[]{"Punishment Duration", punishment.api().getDurationText()},
//                                new String[]{"Punishment Removed", String.valueOf(punishment.isRemoved())},
//                        })
//                        +
//                        Table.of(new String[][]{
//                                new String[]{"Staff UUID", (adderProfile != null ? String.valueOf(adderProfile.getId()) : "N/A")},
//                                new String[]{"Staff Name", (adderProfile != null ? adderProfile.getName() : "Console")},
//                                new String[]{"Added Reason", punishment.getAddedReason()},
//                                new String[]{"Added At", TimeUtil.dateToString(new Date(punishment.getAddedAt()), true)},
//                        }) +
//                        (punishment.isRemoved() ?
//                                 Table.of(new String[][]{
//                                        new String[]{"Remover UUID", (removerProfile != null ? String.valueOf(removerProfile.getId()) : "N/A")},
//                                        new String[]{"Remover Name", (removerProfile != null ? removerProfile.getName() : "Console")},
//                                        new String[]{"Removed Reason", punishment.getAddedReason()},
//                                        new String[]{"Removed At", TimeUtil.dateToString(new Date(punishment.getRemovedAt()), true)},
//                                }) : "")
//                        + "```"
//                );
//
//        channel.sendMessage(builder.build()).queue();
//    }
//
//}
