package com.customwrld.bot.listeners;

import com.customwrld.bot.Bot;
import com.customwrld.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Bot bot = Bot.getInstance();
        User user = event.getUser();
        TextChannel channel = bot.getGuild().getTextChannelById(bot.getConfig().getWelcomeChannel());

        EmbedBuilder builder = Util.builder(event.getUser())
                .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                .setThumbnail(user.getAvatarUrl())
                .setTitle("**Welcome to the CUSTOMWRLD Discord**")
                .setDescription(":video_game: ・ **Server IP:** CUSTOMWRLD.COM\n:gem: ・ **Store Link:** https://store.customwrld.com/\n:bird: ・ **Twitter:** https://www.twitter.com/customwrldcom\n:link: ・ **Discord Invite:** https://discord.gg/QYMmkdXBjf");
        channel.sendMessage("> <@" + user.getId() + ">").queue();
        channel.sendMessage(builder.build()).queue();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        Guild guild = event.getGuild();
        if (guild.equals(Bot.getInstance().getGuild())) {
            guild.leave().queue();
        }
    }

}
