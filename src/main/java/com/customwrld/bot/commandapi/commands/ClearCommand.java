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
//import java.time.OffsetDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//
//public class ClearCommand implements ICommand {
//
//    @Override
//    public CommandPermission permission() {
//        return new CommandPermission(CommandPermission.Type.PERMISSION, Permission.ADMINISTRATOR);
//    }
//
//    @Override
//    public String commandName() {
//        return "clear";
//    }
//
//    @Override
//    public String usage() {
//        return this.commandName() + " [amount]";
//    }
//
//    @Override
//    public void handle(CommandContext ctx) {
//        TextChannel channel = ctx.getChannel();
//        User author = ctx.getAuthor();
//        List<String> args = ctx.getArgs();
//
//        if (args.size() < 1) {
//            EmbedTemplate.WRONG_USAGE.send(channel, author, this.usage());
//        } else {
//            try {
//                int amount = Integer.parseInt(args.get(0));
//                if (amount > 100 || amount < 2) {
//                    Util.sendEmbed(channel, author, "**Number Exception**", "You must provide a number that is within 2-100.");
//                    return;
//                }
//
//                ctx.getMessage().delete().queue((messageDeleteSuccess) -> channel.getHistory().retrievePast(amount).queue(messages -> {
//                    messages.removeIf(m -> m.getTimeCreated().isBefore(OffsetDateTime.now().minus(2, ChronoUnit.WEEKS)));
//
//                    if (messages.isEmpty()) {
//                        Util.sendEmbed(channel, author, "**Clear Completed**", "No messages were deleted.");
//                        return;
//                    }
//
//                    channel.deleteMessages(messages).queue((historyDeleteSuccess) -> Util.sendEmbed(channel, author, "**Clear Completed**", messages.size() + " messages have been deleted."));
//                }));
//            } catch (NumberFormatException e) {
//                Util.sendEmbed(channel, author, "**Number Exception**", "You have provided an invalid number.");
//            }
//        }
//    }
//}
