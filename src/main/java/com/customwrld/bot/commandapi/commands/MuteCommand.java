//package com.customwrld.bot.commandapi.commands;
//
//import com.customwrld.bot.Bot;
//import com.customwrld.bot.commandapi.CommandContext;
//import com.customwrld.bot.commandapi.CommandPermission;
//import com.customwrld.bot.commandapi.ICommand;
//import com.customwrld.bot.profile.Profile;
//import com.customwrld.bot.profile.punishment.Punishment;
//import com.customwrld.bot.profile.punishment.PunishmentType;
//import com.customwrld.bot.util.timer.timers.MuteTimer;
//import com.customwrld.bot.util.Duration;
//import com.customwrld.bot.util.Util;
//import com.customwrld.bot.util.enums.EmbedTemplate;
//import net.dv8tion.jda.api.JDA;
//import net.dv8tion.jda.api.Permission;
//import net.dv8tion.jda.api.entities.Guild;
//import net.dv8tion.jda.api.entities.Role;
//import net.dv8tion.jda.api.entities.TextChannel;
//import net.dv8tion.jda.api.entities.User;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class MuteCommand implements ICommand {
//
//    @Override
//    public CommandPermission permission() {
//        return new CommandPermission(CommandPermission.Type.ROLE, Util.STAFF_ROLE);
//    }
//
//    @Override
//    public String commandName() {
//        return "mute";
//    }
//
//    @Override
//    public String usage() {
//        return this.commandName() + " [member] [duration] [reason]";
//    }
//
//    @Override
//    public void handle(CommandContext ctx) {
//        List<String> args = ctx.getArgs();
//        JDA jda = ctx.getJDA();
//        Guild guild = ctx.getGuild();
//        TextChannel channel = ctx.getChannel();
//        Role role = guild.getRoleById(Bot.getBot() .getConfig().getMutedRole());
//        User author = ctx.getAuthor();
//        String userId = Util.getId(args.get(0));
//
//        if(role == null) {
//            channel.sendMessage("<@836340297281699873>").queue();
//            Util.sendEmbed(channel, author, "**Command Exception**", "MuteCommand.java | Role is NULL.");
//            return;
//        }
//
//        jda.retrieveUserById(userId).queue(user -> guild.retrieveMember(user).queue(member -> {
//            Duration duration = Duration.fromString(args.get(1));
//
//            if (duration.getValue() == -1) {
//                EmbedTemplate.DURATION_EXCEPTION.send(channel, author);
//                return;
//            }
//
//            List<String> reasonList = new ArrayList<>(args);
//            reasonList.remove(0);
//            reasonList.remove(0);
//
//            String reason = String.join(" ", reasonList);
//
//            Profile profile = new Profile(userId);
//
//            Punishment activeMute = profile.getActivePunishmentByType(PunishmentType.MUTE);
//
//            if (activeMute != null) {
//                Util.sendEmbed(channel, author, "**Active Punishment**", "That user currently has an active Mute.");
//                return;
//            }
//
//            Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.MUTE, System.currentTimeMillis(), reason, duration.getValue());
//
//            punishment.setAddedBy(author.getId());
//            profile.getPunishments().add(punishment);
//            profile.save();
//
//            guild.retrieveMember(jda.getSelfUser()).queue(selfMember -> {
//                if (selfMember.canInteract(member)) {
//                    guild.addRoleToMember(member, role).queue();
//                    if(!punishment.isPermanent()) {
//                        new MuteTimer(profile, punishment).start();
//                    }
//                } else {
//                    Util.sendEmbed(channel, member.getUser(), "**Permissions Exception**", "You do not have permission to mute this user.");
//                }
//            });
//        }), (error) -> EmbedTemplate.USER_EXCEPTION.send(channel, author));
//    }
//}
