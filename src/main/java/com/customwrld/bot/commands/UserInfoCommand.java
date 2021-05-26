package com.customwrld.bot.commands;

import com.customwrld.bot.commandapi.CommandContext;
import com.customwrld.bot.commandapi.ICommand;
import com.customwrld.bot.util.Argument;
import com.customwrld.bot.util.RoleComparator;
import com.customwrld.bot.util.Util;
import com.customwrld.bot.util.enums.EmbedTemplate;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.util.ArrayList;
import java.util.List;

public class UserInfoCommand implements ICommand {

    @Override
    public Permission permission() {
        return null;
    }

    @Override
    public String commandName() {
        return "userinfo";
    }

    @Override
    public String usage() {
        return commandName() + " [user]";
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        if(args.size() < 1) {
            sendUserInfo(ctx, ctx.getMember());
        } else {
            String id = args.get(0);

            if(Argument.MEMBER_MENTION.matcher(id).matches()) {
                ctx.getJDA().retrieveUserById(Util.getId(id))
                        .queue((user) -> ctx.getGuild().retrieveMember(user).queue(
                                (member) -> sendUserInfo(ctx, member),
                                (error) -> EmbedTemplate.MEMBER_EXCEPTION.send(ctx.getChannel(), ctx.getAuthor())),
                                (error) -> EmbedTemplate.USER_EXCEPTION.send(ctx.getChannel(), ctx.getAuthor())
                        );
            } else {
                EmbedTemplate.WRONG_USAGE.send(ctx.getChannel(), ctx.getAuthor(), this.usage());
            }
        }
    }

    public void sendUserInfo(CommandContext ctx, Member member) {
        User user = member.getUser();
        List<Role> memberRoles = new ArrayList<>(member.getRoles());

        memberRoles.sort(new RoleComparator());

        StringBuilder roleBuilder = new StringBuilder();

        for (Role role : memberRoles) {
            roleBuilder.append("<@&").append(role.getId()).append("> ");
        }

        EmbedBuilder builder = Util.builder(ctx.getAuthor())
                .setAuthor(user.getName(), null, user.getAvatarUrl())
                .setThumbnail(user.getAvatarUrl())
                .addField("**Username**", user.getAsTag(), true)
                .addField("**User ID**", user.getId(), true)
                .addField("**Account Created**", Util.getTime(user.getTimeCreated()), true)
                .addField("**Joined**", Util.getTime(member.getTimeJoined()), true)
                .addField("**Roles**", roleBuilder.toString(), true);
        ctx.getChannel().sendMessage(builder.build()).queue();
    }

}
