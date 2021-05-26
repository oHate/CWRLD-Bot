package com.customwrld.bot.commands;

import com.customwrld.bot.Bot;
import com.customwrld.bot.commandapi.CommandContext;
import com.customwrld.bot.commandapi.ICommand;
import com.customwrld.bot.timers.GiveawayTimer;
import com.customwrld.bot.util.Argument;
import com.customwrld.bot.util.Duration;
import com.customwrld.bot.util.TimeUtil;
import com.customwrld.bot.util.Util;
import com.customwrld.bot.giveaway.Giveaway;
import com.customwrld.bot.util.enums.EmbedTemplate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.*;

public class GiveawayCommand implements ICommand {

    @Override
    public Permission permission() {
        return Permission.ADMINISTRATOR;
    }

    @Override
    public String commandName() {
        return "giveaway";
    }

    @Override
    public String usage() {
        return this.commandName() + " [channel] [time] [prize]";
    }

    @Override
    public void handle(CommandContext ctx) {
        TextChannel channel = ctx.getChannel();
        User author = ctx.getAuthor();
        List<String> args = ctx.getArgs();

        if(args.size() < 3) {
            EmbedTemplate.WRONG_USAGE.send(ctx.getChannel(), author, this.usage());
            return;
        }

        String id = args.get(0);

        if(Argument.CHANNEL_MENTION.matcher(id).matches()) {
            TextChannel guildChannel = ctx.getGuild().getTextChannelById(Util.getId(id));

            if(guildChannel != null) {
                long duration = Duration.fromString(args.get(1)).getValue();

                if(duration != -1 && duration != Integer.MAX_VALUE) {
                    long ends = System.currentTimeMillis() + duration;

                    List<String> argsCopy = new ArrayList<>(args);
                    argsCopy.remove(0);
                    argsCopy.remove(0);

                    String prize = String.join(" ", argsCopy);

                    guildChannel.sendMessage(new EmbedBuilder()
                            .setColor(Bot.getInstance().getConfig().getBotColor())
                            .setTitle(prize)
                            .setDescription("React with :tada: to enter!")
                            .addField("Time Remaining", TimeUtil.millisToRoundedTime(duration), false)
                            .setFooter("Ends at")
                            .setTimestamp(new Date(ends).toInstant())
                            .build()).queue(message -> {
                        message.addReaction("\uD83C\uDF89").queue();
                        Giveaway giveaway = new Giveaway(ends, guildChannel.getId(), message.getId(), prize);
                        giveaway.save();
                        new GiveawayTimer(giveaway).start();
                    });

                    channel.sendMessage(Util.builder(author)
                            .setTitle("**Giveaway Started**")
                            .addField("**Channel**", args.get(0), false)
                            .addField("**Duration**", TimeUtil.millisToRoundedTime(duration), false)
                            .addField("**Prize**", prize, false)
                            .build()).queue();

                } else {
                    EmbedTemplate.DURATION_EXCEPTION.send(channel, author);
                }
            } else {
                EmbedTemplate.MEMBER_EXCEPTION.send(channel, author);
            }
        }
    }
}
