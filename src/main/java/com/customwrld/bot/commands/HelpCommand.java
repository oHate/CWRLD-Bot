//package me.ohate.railedbot.commands;
//
//import me.ohate.railedbot.Main;
//import me.ohate.railedbot.commandapi.AbstractHelpCommand;
//import me.ohate.railedbot.commandapi.CommandEvent;
//import me.ohate.railedbot.commandapi.ICommand;
//import me.ohate.railedbot.util.Util;
//import net.dv8tion.jda.api.EmbedBuilder;
//import net.dv8tion.jda.api.Permission;
//
//import java.util.Map;
//import java.util.Set;
//
//public class HelpCommand extends AbstractHelpCommand {
//
//    @Override
//    public String command() {
//        return "help";
//    }
//
//    @Override
//    public String usage() {
//        return "help [command]";
//    }
//
//    @Override
//    public String information() {
//        return "Displays helpful information about each command.";
//    }
//
//    @Override
//    public Permission permission() {
//        return null;
//    }
//
//    @Override
//    public void provideGeneralHelp(CommandEvent event, String prefix, Map<String, ICommand> commands) {
//        event.getChannel().sendMessage(new EmbedBuilder()
//                .setTitle("**Discord Bot Commands**")
//                .setColor(Main.get().getConfig().getBotColor())
//                .setDescription("`announce` : Sends an announcement to the current channel.\n" +
//                        "`embed` : Sends a embed to the discord.\n" +
//                        "`ban` : Permanently or temporarily bans a user from the guild.\n" +
//                        "`kick` : Kicks a user from the guild.\n" +
//                        "`mute` : Permanently or temporarily mutes a user.\n" +
//                        "`guildinfo` : Shows information about the current guild.\n" +
//                        "`help` : Displays this information.\n" +
//                        "`profile` : Displays a users guild profile.\n" +
//                        "`userinfo` : Displays information about the user.").build()).queue();
//    }
//
//    @Override
//    public void provideSpecificHelp(CommandEvent event, String prefix, ICommand command, Set<String> labels) {
//        for (Map.Entry<String, ICommand> ent : Main.get().getSettings().commands.entrySet()) {
//            if (ent.getValue() == command) {
//                String commandName = ent.getKey();
//                switch (commandName.toLowerCase()) {
//                    case "announce":
//                        Util.sendEmbed(event.getAuthor(), "**Announce Command**", "**Permission:** Administrator Permission\n**Usage:** " + Main.get().getSettings().getPrefix() + "announce -title [title] -message [message]\n**Example:** " + Main.get().getSettings().getPrefix() + "announce -title Announcement -message Content", event.getChannel());
//                        break;
//                    case "embed":
//                        Util.sendEmbed(event.getAuthor(), "**Embed Command**", "**Permission:** Administrator Permission\n**Usage:** " + Main.get().getSettings().getPrefix() + "embed -title [title] -message [message]\n**Example:** " + Main.get().getSettings().getPrefix() + "embed -title Embed -message Content", event.getChannel());
//                        break;
//                    case "ban":
//                        Util.sendEmbed(event.getAuthor(), "**Ban Command**", "**Permission:** Ban Permission\n**Usage:** " + Main.get().getSettings().getPrefix() + "ban [@user] [perm/1y1m1d1h1s] [reason]\n**Example:** " + Main.get().getSettings().getPrefix() + "ban @oHate#0001 perm Staff Discretion", event.getChannel());
//                        break;
//                }
//            }
//        }
//    }
//}
