//package com.customwrld.customwrldbot.commands;
//
//import com.customwrld.customwrldbot.Main;
//import com.customwrld.customwrldbot.util.Util;
//import com.customwrld.customwrldbot.commandapi.AbstractCommand;
//import com.customwrld.customwrldbot.commandapi.CommandEvent;
//import com.customwrld.customwrldbot.commandapi.SubCommand;
//import com.customwrld.customwrldbot.giveaway.Giveaway;
//import net.dv8tion.jda.api.Permission;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.TextChannel;
//
//public class RollCommand extends AbstractCommand {
//
//    @Override
//    public String command() {
//        return "roll";
//    }
//
//    @Override
//    public String usage() {
//        return "roll [@message]";
//    }
//
//    @Override
//    public String information() {
//        return "Redraws a giveaway and chooses a new winner.";
//    }
//
//    @Override
//    public CommandPermission permission() {
//        return Permission.ADMINISTRATOR;
//    }
//
//    @SubCommand(isDefault = true)
//    public void onWrongUsage(CommandEvent event, Member executor, TextChannel channel, String[] args) {
//        Util.sendEmbed(member.getUser(), "**Invalid Usage**", "Usage: " + Main.get().getConfig().getBotPrefix() + "roll [channelid] [messageid]", channel);
//    }
//
//    @SubCommand(moreArgs = true)
//    public void onProfileCommand(CommandEvent event, Member executor, TextChannel channel, String[] args) {
//        if(!member.hasPermission(permission())) {
//            Util.sendEmbed(member.getUser(),"**Permission Exception**", "You are currently lacking the \"Administrator\" permission.", channel);
//            return;
//        }
//
//        Giveaway giveaway = Giveaway.getById(args[0]);
//
//        if(giveaway == null) {
//            Util.sendEmbed(member.getUser(), "**Unknown Giveaway**", "That specific message is not a instance of a giveaway.", channel);
//            return;
//        }
//
//        if(!giveaway.hasEnded()) {
//            Util.sendEmbed(member.getUser(), "**Active Giveaway**", "That specific giveaway has not ended yet.", channel);
//            return;
//        }
//
//        Main.guild.getTextChannelById(giveaway.getChannelId()).retrieveMessageById(args[0]).queue(giveaway::drawWinner);
//    }
//
//}
