package com.customwrld.bot.commandapi;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class CommandPermission {

    public static boolean hasRole(Member member, Role role) {
        return member.getRoles().contains(role);
    }

    public static boolean hasPermission(Member member, Permission... permissions) {
        for(Permission permission : permissions) {
            if(member.hasPermission(permission)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasUser(Member member, String... userIds) {
        for(String id : userIds) {
            if(id.equals(member.getId())) {
                return true;
            }
        }

        return false;
    }

}
