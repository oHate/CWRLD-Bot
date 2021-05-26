package com.customwrld.bot.commands;

import com.customwrld.bot.commandapi.CommandContext;
import com.customwrld.bot.commandapi.ICommand;
import com.customwrld.bot.util.Util;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

public class GuildInfoCommand implements ICommand {

    @Override
    public Permission permission() {
        return null;
    }

    @Override
    public String commandName() {
        return "guildinfo";
    }

    @Override
    public String usage() {
        return this.commandName();
    }

    @Override
    public void handle(CommandContext ctx) {
        Guild guild = ctx.getGuild();
        User author = ctx.getAuthor();

        if(guild.getOwner() == null) {
            Util.sendEmbed(ctx.getChannel(), author, "**Guild Exception**", "The owner of the guild was marked as NULL.");
            return;
        }

        EmbedBuilder builder = Util.builder(author)
                .setAuthor(guild.getName(), null, guild.getIconUrl())
                .setThumbnail(guild.getIconUrl())
                .addField("**Owner**", guild.getOwner().getUser().getAsTag(), true)
                .addField("**Owner ID**", guild.getOwner().getUser().getId(), true)
                .addField("**Members**", String.valueOf(guild.getMemberCount()), true)
                .addField("**Region**", guild.getRegionRaw(), true)
                .addField("**Categories**", String.valueOf(guild.getCategories().size()), true)
                .addField("**Text Channels**", String.valueOf(guild.getTextChannels().size()), true)
                .addField("**Voice Channels**", String.valueOf(guild.getVoiceChannels().size()), true)
                .addField("**Roles**", String.valueOf(guild.getRoles().size()), true)
                .addField("**Server Created**", Util.getTime(guild.getTimeCreated()), true);
        ctx.getChannel().sendMessage(builder.build()).queue();
    }

}
