//package com.customwrld.bot.commandapi.commands;
//
//import com.customwrld.bot.commandapi.button.ButtonHandler;
//import com.customwrld.bot.commandapi.CommandContext;
//import com.customwrld.bot.commandapi.CommandPermission;
//import com.customwrld.bot.commandapi.ICommand;
//import com.customwrld.bot.util.Util;
//import com.customwrld.bot.util.enums.EmbedTemplate;
//import net.dv8tion.jda.api.entities.Member;
//import net.dv8tion.jda.api.entities.TextChannel;
//import net.dv8tion.jda.api.entities.User;
//
//import java.util.Arrays;
//
//public class NetworkCommand implements ICommand {
//
//    // TODO: Fix permissions
//
//    @Override
//    public CommandPermission permission() {
//        return null;
//    }
//
//    @Override
//    public String commandName() {
//        return "network";
//    }
//
//    @Override
//    public String usage() {
//        return this.commandName() + " [args]";
//    }
//
//    @Override
//    public void handle(CommandContext ctx) {
//        String[] args = ctx.getArgs();
//        User author = ctx.getAuthor();
//        Member member = ctx.getMember();
//        TextChannel channel = ctx.getChannel();
//
//        if(args.length < 1) {
//            EmbedTemplate.WRONG_USAGE.send(channel, author, this.usage());
//            return;
//        }
//
//        // !network executecmd op oHate test
//
//        switch (args[0]) {
//            case ("executecmd"): {
//
//                if(args.length < 3) {
//                    EmbedTemplate.WRONG_USAGE.send(channel, author, this.usage());
//                    return;
//                }
//
//                String command = args[1].toLowerCase();
//
//                for(String blocked : Arrays.asList("op")) {
//                    if(blocked.toLowerCase().equals(blocked)) {
//                        channel.sendMessage(Util.builder(author)
//                                .setTitle("**Disabled Command**")
//                                .setDescription("The \"" + blocked.toUpperCase() + "\" command is disabled using the \"executecmd\" argument.")
//                                .build())
//                                .queue();
//                        return;
//                    }
//                }
//
//                String commandArgs = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
//                System.out.println(commandArgs);
//            }
//        }
//
//        if(!member.getUser().getId().equals("836340297281699873")) {
//            return;
//        }
////
////        Button confirm = Button.success(Util.generateId(member), "Confirm");
////        Button cancel = Button.danger(Util.generateId(member), "Cancel");
////
////        ctx.getChannel().sendMessage(Util.builder(member.getUser())
////                .setTitle("**Confirmation**")
////                .build())
////                .setActionRows(ActionRow.of(confirm, cancel))
////                .queue();
////
////        ButtonHandler.buttonMap.put(confirm.getId(), (authorId, event) -> {
////            Message message = event.getMessage();
////
////            if(message == null) {
////                event.reply("> An error has occurred.").setEphemeral(true).queue();
////                return;
////            }
////
////            message.delete().queue();
////
////            event.reply("").setEphemeral(true).queue();
////        });
////
////        ButtonHandler.buttonMap.put(cancel.getId(), (authorId, event) -> {
////            Message message = event.getMessage();
////
////            if(message == null) {
////                event.reply("> An error has occurred.").setEphemeral(true).queue();
////                return;
////            }
////
////            message.getActionRows().forEach(row -> row.getButtons().forEach(button -> event.updateButton(button.asDisabled()).queue()));
////        });
//    }
//}
