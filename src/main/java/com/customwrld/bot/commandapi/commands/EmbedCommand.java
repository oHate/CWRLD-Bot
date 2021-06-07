//package com.customwrld.bot.commandapi.commands;
//
//import com.customwrld.bot.commandapi.CommandContext;
//import com.customwrld.bot.commandapi.CommandPermission;
//import com.customwrld.bot.commandapi.ICommand;
//import com.customwrld.bot.util.Util;
//import com.customwrld.bot.util.enums.EmbedTemplate;
//import net.dv8tion.jda.api.Permission;
//import net.dv8tion.jda.api.entities.TextChannel;
//import net.dv8tion.jda.api.entities.User;
//
//import java.util.List;
//
//public class EmbedCommand implements ICommand {
//
//    @Override
//    public CommandPermission permission() {
//        return new CommandPermission(CommandPermission.Type.PERMISSION, Permission.ADMINISTRATOR);
//    }
//
//    @Override
//    public String commandName() {
//        return "embed";
//    }
//
//    @Override
//    public String usage() {
//        return this.commandName() + " [title] %s% [description]";
//    }
//
//    @Override
//    public void handle(CommandContext ctx) {
//        TextChannel channel = ctx.getChannel();
//        User author = ctx.getAuthor();
//        List<String> args = ctx.getArgs();
//
//        if(!args.contains("%s%") || args.size() < 3) {
//            EmbedTemplate.WRONG_USAGE.send(channel, author, this.usage());
//            return;
//        }
//
//        String[] arguments = String.join(" ", args).split("%s%");
//
//        Util.sendEmbed(channel, author, arguments[0], arguments[1].replaceAll("\\\\n", "\n"));
//    }
//}
