package com.customwrld.bot.commandapi;

import com.customwrld.bot.util.enums.EmbedTemplate;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

@Getter
public class CommandPermission {

    public CommandPermission.Type type;
    public Object[] object;

    public CommandPermission(CommandPermission.Type type, Object... object) {
        this.type = type;
        this.object = object;
    }

    public boolean handlePermission(Member member, TextChannel channel) {
        switch (this.type) {
            case PERMISSION -> {
                Permission[] permissions = (Permission[]) this.object;

                for(Permission permission : permissions) {
                    if(member.hasPermission(permission)) {
                        return true;
                    }
                }
            }

            case ROLE -> {
                Role[] roles = (Role[]) this.object;

                for(Role role : roles) {
                    if(member.getRoles().contains(role)) {
                        return true;
                    }
                }
            }

            case USERID -> {
                String[] userIds = (String[]) this.object;
                String memberId = member.getId();

                for(String id : userIds) {
                    if(id.equals(memberId)) {
                        return true;
                    }
                }
            }
        }

        EmbedTemplate.PERMISSION_EXCEPTION.send(channel, member.getUser());
        return false;
    }

    public enum Type {
        PERMISSION,
        ROLE,
        USERID;

    }

}
