package com.customwrld.bot.util;

import net.dv8tion.jda.api.entities.Role;

import java.util.Comparator;

public class RoleComparator implements Comparator<Role> {

    public int compare(Role role1, Role role2) {
        return role2.getPosition() - role1.getPosition();
    }

}
